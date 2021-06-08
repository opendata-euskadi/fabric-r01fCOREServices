package r01f.core.services.mail.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierResponseError;
import r01f.core.services.notifier.NotifierResponseErrorType;
import r01f.core.services.notifier.NotifierResponseErrorTypes;
import r01f.core.services.notifier.NotifierResponseOK;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.patterns.Factory;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Slf4j
@Singleton
@Accessors(prefix="_")
public class JavaMailSenderNotifierService
  implements NotifierServiceForEMail {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final JavaMailSender _springJavaMailSender;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public JavaMailSenderNotifierService(final JavaMailSender mailSender) {
		_springJavaMailSender = mailSender;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse<EMailRFC822Address> notify(final EMailRFC822Address from, final EMailRFC822Address to,
													   final Factory<MimeMessage> messageToBeDeliveredFactory) {
		Collection<NotifierResponse<EMailRFC822Address>> res = this.notifyAll(from,Lists.newArrayList(to),
																			  messageToBeDeliveredFactory);
		return Iterables.getFirst(res,
								  null);
	}
	@Override
	public Collection<NotifierResponse<EMailRFC822Address>> notifyAll(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,
																	  final Factory<MimeMessage> messageToBeDeliveredFactory) {
		NotifierTaskOID taskOid = NotifierTaskOID.supply();
		Collection<NotifierResponse<EMailRFC822Address>> response = null;
		try {
			// [1] - Create the message
			MimeMessage mimeMsg = messageToBeDeliveredFactory.create();

			// [2] - Ensure the from & to are set at the mime message
			if (mimeMsg.getFrom() != null) mimeMsg.setFrom(from.asRFC822Address());

			if (CollectionUtils.isNullOrEmpty(mimeMsg.getRecipients(RecipientType.TO))
			 && CollectionUtils.isNullOrEmpty(mimeMsg.getRecipients(RecipientType.CC))
			 && CollectionUtils.isNullOrEmpty(mimeMsg.getRecipients(RecipientType.BCC))) {
				mimeMsg.setRecipients(RecipientType.BCC,
									  EMailRFC822Address.multipleAsRFC822Address(to));
			}

			// [2] - Send an email to every destination
			log.info("[{}] > Sending EMail notifications to {} recipients",
					 JavaMailSenderNotifierService.class.getSimpleName(),
					 to.size());
	        _springJavaMailSender.send(mimeMsg);

	        // [3] - Build the response
	        response = _buildResponseOK(taskOid,
	        						    to);	// success
		} catch (final Throwable th) {
			log.error("Error while notifying using email: {}",
					  th.getMessage(),th);
			response = _buildResponseError(taskOid,
									        to,
									        NotifierResponseErrorTypes.UNKNOWN,
									        th.getLocalizedMessage())	;// failed, if want some more specific error catch exceptions..
		}
		return response;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//  BUILD RESULTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static Collection<NotifierResponse<EMailRFC822Address>> _buildResponseOK(final NotifierTaskOID taskOid,
																				     final Collection<EMailRFC822Address> to) {
		return FluentIterable.from(to)
					.transform(new Function<EMailRFC822Address,NotifierResponse<EMailRFC822Address>>() {
										@Override
										public NotifierResponse<EMailRFC822Address> apply(final EMailRFC822Address addr) {
											return new NotifierResponseOK<EMailRFC822Address>(taskOid,
																							  addr,
																							  NotifierType.EMAIL);
										}
							   })
					.toList();
	}
	private static Collection<NotifierResponse<EMailRFC822Address>> _buildResponseError(final NotifierTaskOID taskOid,
		     																		    final Collection<EMailRFC822Address> to, final NotifierResponseErrorType errorType, final String errorDetail) {
		return FluentIterable.from(to)
			.transform(new Function<EMailRFC822Address,NotifierResponse<EMailRFC822Address>>() {
								@Override
								public NotifierResponse<EMailRFC822Address> apply(final EMailRFC822Address addr) {
										return new NotifierResponseError<EMailRFC822Address>(taskOid,
															                                 addr,
															                                  NotifierType.EMAIL,errorType,errorDetail);
										}
								})
				.toList();
	}
}
