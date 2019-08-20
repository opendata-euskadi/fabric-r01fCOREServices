package r01f.services.latinia.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierTaskOID;
import r01f.model.latinia.LatiniaRequestMessage;
import r01f.model.latinia.LatiniaResponse;
import r01f.model.latinia.LatiniaResponseMessage;
import r01f.model.latinia.LatiniaResponsePhone;
import r01f.patterns.Factory;
import r01f.services.latinia.LatiniaService;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;

@Slf4j
@Singleton
public class LatiniaNotifierServices
  implements NotifierServiceForSMS {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final LatiniaService _latiniaService;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public LatiniaNotifierServices(final LatiniaService latiniaService) {
		_latiniaService = latiniaService;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse<Phone> notify(final OwnedContactMean<Phone> from, final Phone to,
										  final Factory<String> messageToBeDeliveredFactory) {
		Collection<NotifierResponse<Phone>> res = this.notifyAll(from,Lists.newArrayList(to),
																 messageToBeDeliveredFactory);
		return Iterables.getFirst(res,
								  null);
	}
	@Override
	public Collection<NotifierResponse<Phone>> notifyAll(final OwnedContactMean<Phone> from,final Collection<Phone> to,
														 final Factory<String> messageToBeDeliveredFactory) {
		Collection<NotifierResponse<Phone>> responses = Lists.newArrayListWithExpectedSize(to.size());

		// [1] - Create the message
		String smsMsg = messageToBeDeliveredFactory.create();

		// [2] - Send an email to every destination
		log.info("[{}] > Sending SMS notifications to {} recipients",
				 LatiniaNotifierServices.class.getSimpleName(),
				 to.size());
		for (Phone phone : to) {
			try {
				// BEWARE! 	Do NOT set timestamp because the message MUST immediately delivered,
				//			(timestamp should be used when delivering deferred messages).
				LatiniaRequestMessage latiniaMsg = new LatiniaRequestMessage();
				latiniaMsg.setAcknowledge("S"); // Telephone company must send acknowledge to latinia service,
												// this service does NOT received this confirmation directly
				latiniaMsg.setMessageContent(smsMsg);
				latiniaMsg.setReceiverNumbers(phone.asStringWithoutCountryCode());

				// Send Message to Latinia service
				log.info("\t- sending SMS to {}",
						 phone.asStringWithoutCountryCode());
				LatiniaResponse resp = _latiniaService.sendNotification(latiniaMsg);

				responses.add(_buildResponseFor(resp));	// success
			} catch (Throwable th) {
				log.error("Error while notifying using [latinia] to send an sms to {}: {}",
						  phone,
						  th.getMessage(),th);
				responses.add(new NotifierResponseImpl<Phone>(null,		// no task id
													   		  phone,
													   		  true));	// success
			}
		}
	    // [3] - Build the response
		return responses;
	}
	private static NotifierResponseImpl<Phone> _buildResponseFor(final LatiniaResponse resp) {
		// <RESPUESTA>
		// 	    <MENSAJE NUM="1">
		// 	    	<TELEFONO NUM="659000001">
		// 	    		<RESULTADO>OK</RESULTADO>
		// 	    		<IDENTIFICADOR>UGsiZ7E1naZX/Uey32A1hFUq</IDENTIFICADOR>
		// 	    	</TELEFONO>
		// 	    	<TELEFONO NUM="666000001">
		// 	    		<RESULTADO>OK</RESULTADO>
		// 	    		<IDENTIFICADOR>UGsiZ7E2efSshUey32A1mU7o</IDENTIFICADOR>
		// 	    	</TELEFONO>
		// 	    	<TELEFONO NUM="600123456">
		// 	    		<RESULTADO>ERROR</RESULTADO>
		// 	    		<CODIGO_ERROR>301</CODIGO_ERROR>
		// 	    		<MENSAJE_ERROR>El mensaje ha expirado</MENSAJE_ERROR>
		// 	    	</TELEFONO>
		// 	    </MENSAJE>
		//      <MENSAJE NUM="2">
		//      ...........
		//      </MENSAJE>
		// </RESPUESTA>
		LatiniaResponseMessage msgResp = Iterables.getFirst(resp.getLatiniaResponses(),
														    null);
		LatiniaResponsePhone msgPhoneResp = Iterables.getFirst(msgResp.getResponsePhoneResults(),
															   null);
		NotifierTaskOID taskId = NotifierTaskOID.forId(msgPhoneResp.getMessageId());
		boolean ok = msgPhoneResp.getResult().equals("OK");
		return new NotifierResponseImpl<Phone>(taskId,
				   		  					   Phone.of(msgPhoneResp.getReceiverNumber()),
				   		  					   ok);
	}
}
