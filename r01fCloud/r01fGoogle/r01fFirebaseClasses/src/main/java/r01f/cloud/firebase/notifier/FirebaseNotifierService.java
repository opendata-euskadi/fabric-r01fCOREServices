package r01f.cloud.firebase.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;
import r01f.cloud.firebase.model.PushMessageRequest;
import r01f.cloud.firebase.model.PushMessageResponse;
import r01f.cloud.firebase.service.FirebaseServiceImpl;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.core.services.notifier.NotifierPushMessageSubcriber;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierServiceForPushMessage;
import r01f.core.services.notifier.NotifierTaskOID;
import r01f.guids.CommonOIDs.AppCode;
import r01f.patterns.Factory;

@Slf4j
@Singleton
public class FirebaseNotifierService
  implements NotifierServiceForPushMessage<RegistredDevicesTopic,RegistredDeviceToken> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final FirebaseServiceImpl _firebaseService;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public FirebaseNotifierService(final FirebaseServiceImpl firebaseService) {
		_firebaseService = firebaseService;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// [to do ] OwnedContactMean... cannot be expose a Phone, so
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>> notify(final AppCode from,
																											  final NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken> to,
																											  final Factory<NotifierPushMessage> messageToBeDeliveredFactory) {
		@SuppressWarnings("unchecked")
		Collection<NotifierResponse<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>>> res = this.notifyAll(from,Lists.newArrayList(to),
				 																													  messageToBeDeliveredFactory);
		return Iterables.getFirst(res,null);
	}
	@SuppressWarnings("unused")
	@Override
	public Collection<NotifierResponse<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>>> notifyAll(final AppCode from,
			                                                                                                                 final Collection<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>> to,
			                                                                                                                 final Factory<NotifierPushMessage> messageToBeDeliveredFactory) {
		Collection<NotifierResponse<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>>> responses
						= Lists.newArrayListWithExpectedSize(to.size());

		// [1] - Create the pushm message
		NotifierPushMessage pushMessage = messageToBeDeliveredFactory.create();

		// [2] - push to every destination
		log.info("[{}] > push to {} subscribers",
												  FirebaseNotifierService.class.getSimpleName(),
												   to.size());
		for (final NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken> subscriber : to) {
			try {


				PushMessageRequest pushMessageRequest = new PushMessageRequest(subscriber.getTopic(),
						                                                       subscriber.getToken(),
						                                                       pushMessage.getText(),
						                                                       pushMessage.getTitle(),null);


				PushMessageResponse response = _firebaseService.push(pushMessageRequest);

				responses.add(new NotifierResponseImpl<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>>(NotifierTaskOID.supply(),
																																  subscriber,
																																  true));	// success
			} catch (final Throwable nitex) {

				responses.add(new NotifierResponseImpl<NotifierPushMessageSubcriber<RegistredDevicesTopic, RegistredDeviceToken>>(NotifierTaskOID.supply(),
					                                                                                                         	  subscriber,
														  	                                                                      false));	// failed
			}
		}
	    // [3] - Build the response
		return responses;
	}
}
