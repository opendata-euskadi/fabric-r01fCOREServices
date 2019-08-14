package r01f.cloud.twilio.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Call;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.twilio.TwilioService;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierServicesForVoicePhoneCall;
import r01f.core.services.notifier.NotifierTaskOID;
import r01f.patterns.Factory;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.types.url.Url;

@Slf4j
@Singleton
public class TwilioNotifierServices
  implements NotifierServicesForVoicePhoneCall {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final TwilioService _twilioService;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public TwilioNotifierServices(final TwilioService twilioService) {
		_twilioService = twilioService;
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
				 TwilioNotifierServices.class.getSimpleName(),
				 to.size());
		for (Phone phone : to) {
			try {
				Phone intPhone = Phone.of(phone.asStringEnsuringCountryCode("+34"));	// ensure the phone contains +34
				log.info("\t- making voice call to {}",intPhone);
				Url twmlUrl = Url.from("https://dl.dropboxusercontent.com/u/1264561/testTwilioTWML.xml");
				Call twCall = _twilioService.makeCall(intPhone,
													  twmlUrl);
				responses.add(new NotifierResponseImpl<Phone>(NotifierTaskOID.supply(),
														  	  phone,
														  	  true));	// success
			} catch (TwilioRestException restEx) {
				log.error("Error while making a voice call to {} using twilio: {}",
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
