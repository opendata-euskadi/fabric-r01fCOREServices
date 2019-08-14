package r01f.core.services.mail;

import java.util.Collection;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.core.services.mail.EMailMimeMessages;
import r01f.core.services.mail.model.EMailMessage;
import r01f.core.services.mail.model.EMailMessageAttachment;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class JavaMailSenders {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static JavaMailSendersSendStep using(final JavaMailSender javaMailSender) {
		return new JavaMailSenders() { /* nothing */ }
					.new JavaMailSendersSendStep(javaMailSender);
	}
	@RequiredArgsConstructor
	public class JavaMailSendersSendStep {
		private final JavaMailSender _javaMailSender;

		public void send(final EMailMessage emailMsg,
						 final EMailMessageAttachment... attachs) throws MessagingException {
			this.send(emailMsg,
					  CollectionUtils.hasData(attachs) ? Lists.newArrayList(attachs) : null);
		}
		public void send(final EMailMessage emailMsg,
						 final Collection<EMailMessageAttachment> attachs) throws MessagingException {
			if (CollectionUtils.hasData(attachs)) {
				// MimeMessage
				MimeMessage mimeMessage = EMailMimeMessages.createMimeMessageFor(emailMsg)
															 .withDefaultCharset()
															 .usingDefaultSession()
															 .withAttachments(attachs)
															 .build();
				_javaMailSender.send(mimeMessage);
			} else {
				// Simple message
				SimpleMailMessage simpleMessage = JavaMailSenders.springSimpleMailMessageFrom(emailMsg);
				_javaMailSender.send(simpleMessage);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	static SimpleMailMessage springSimpleMailMessageFrom(final EMailMessage emailMsg) {
		SimpleMailMessage outEMailMsg = new SimpleMailMessage();
		outEMailMsg.setFrom(emailMsg.getFrom().asRFC822Address());
		if (CollectionUtils.hasData(emailMsg.getTo())) outEMailMsg.setTo(FluentIterable.from(emailMsg.getTo())
																				.transform(EMailRFC822Address.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		if (CollectionUtils.hasData(emailMsg.getCc())) outEMailMsg.setCc(FluentIterable.from(emailMsg.getCc())
																				.transform(EMailRFC822Address.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		if (CollectionUtils.hasData(emailMsg.getBcc())) outEMailMsg.setCc(FluentIterable.from(emailMsg.getBcc())
																				.transform(EMailRFC822Address.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		outEMailMsg.setSubject(emailMsg.getSubject());
		if (Strings.isNOTNullOrEmpty(emailMsg.getHtmlBody())) {
			outEMailMsg.setText(emailMsg.getHtmlBody());
		} else if (Strings.isNOTNullOrEmpty(emailMsg.getPlainTextBody())) {
			outEMailMsg.setText(emailMsg.getPlainTextBody());
		}
		return outEMailMsg;
	}
	static EMailMessage eMailMessageFrom(final SimpleMailMessage springSimpleMailMessage) {
		EMailRFC822Address from = EMailRFC822Address.fromRFC822AddressString(springSimpleMailMessage.getFrom());

		Collection<EMailRFC822Address> to = CollectionUtils.hasData(springSimpleMailMessage.getTo())
													? FluentIterable.from(springSimpleMailMessage.getTo())
																	.transform(EMailRFC822Address.FROM_STRING_TRANSFORM)
																	.toList()
													: null;
		Collection<EMailRFC822Address> cc = CollectionUtils.hasData(springSimpleMailMessage.getCc())
													? FluentIterable.from(springSimpleMailMessage.getCc())
																	.transform(EMailRFC822Address.FROM_STRING_TRANSFORM)
																	.toList()
													: null;
		Collection<EMailRFC822Address> bcc = CollectionUtils.hasData(springSimpleMailMessage.getBcc())
													? FluentIterable.from(springSimpleMailMessage.getBcc())
																	.transform(EMailRFC822Address.FROM_STRING_TRANSFORM)
																	.toList()
													: null;
		String subject = springSimpleMailMessage.getSubject();
		String body = springSimpleMailMessage.getText();

		return new EMailMessage(from,to,cc,bcc,
								subject,
								null,body);		// assumed html body
	}
}
