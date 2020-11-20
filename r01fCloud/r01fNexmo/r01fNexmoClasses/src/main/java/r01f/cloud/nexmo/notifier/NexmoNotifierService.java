package r01f.cloud.nexmo.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.nexmo.client.voice.CallEvent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.nexmo.api.interfaces.NexmoServicesForVoice;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierServiceForVoicePhoneCall;
import r01f.patterns.Factory;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.types.url.Url;

@Slf4j
@Singleton
public class NexmoNotifierService
  implements NotifierServiceForVoicePhoneCall {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final NexmoServicesForVoice  _nexmoServiceForVoice;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public NexmoNotifierService(final NexmoServicesForVoice nexmoServiceForVoice) {
		_nexmoServiceForVoice = nexmoServiceForVoice;
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
		String voiceMsg = messageToBeDeliveredFactory.create();

		// [2] - Send an email to every destination
		log.info("[{}] > Sending voice notifications to {} recipients",
				 NexmoNotifierService.class.getSimpleName(),
				 to.size());
		for (Phone phone : to) {
			try {
				Phone intPhone = Phone.of(phone.asStringEnsuringCountryCode("+34"));	// ensure the phone contains +34
				log.info("\t- makingo voice call to {}",intPhone);
				Url twmlUrl = Url.from("https://dl.dropboxusercontent.com/u/1264561/testTwilioTWML.xml");
				CallEvent twCall = _nexmoServiceForVoice.makeCall(intPhone,
													             twmlUrl);
				responses.add(new NotifierResponseImpl<Phone>(NotifierTaskOID.supply(),
														  	  phone,
														  	  true));	// success
			} catch (Throwable restEx) {
				log.error("Error while making a voice call to {} using NEXMO: {}",
						  phone,
						  restEx.getMessage(),restEx);
				responses.add(new NotifierResponseImpl<Phone>(NotifierTaskOID.supply(),
														  	  phone,
														  	  false));	// failed
			}
		}
	    // [3] - Build the response
		return responses;
	}
}
