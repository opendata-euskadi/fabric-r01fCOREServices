package r01f.cloud.firebase.notifier;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.firebase.model.FirebaseIds.FirebasePushMessageDataItemID;
import r01f.cloud.firebase.model.FirebaseIds.FirebaseRegisteredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.FirebaseRegisteredDevicesTopic;
import r01f.cloud.firebase.model.FirebasePushMessageDataItem;
import r01f.cloud.firebase.model.FirebasePushMessageRequest;
import r01f.cloud.firebase.model.FirebasePushMessageResponse;
import r01f.cloud.firebase.service.FirebaseServiceImpl;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.core.services.notifier.NotifierPushMessageSubscriber;
import r01f.core.services.notifier.NotifierResponse;
import r01f.core.services.notifier.NotifierServiceForPushMessage;
import r01f.guids.CommonOIDs.AppCode;
import r01f.patterns.Factory;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
@Singleton
public class FirebaseNotifierService
  implements NotifierServiceForPushMessage {
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
	public NotifierResponse<NotifierPushMessageSubscriber> notify(final AppCode from,
																 final NotifierPushMessageSubscriber to,
														         final Factory<NotifierPushMessage> messageToBeDeliveredFactory) {

		Collection<NotifierResponse<NotifierPushMessageSubscriber>> res = this.notifyAll(from,
																						 Lists.newArrayList(to),
				 																	     messageToBeDeliveredFactory);
		return Iterables.getFirst(res,null);
	}

	@Override
	public Collection<NotifierResponse<NotifierPushMessageSubscriber>> notifyAll(final AppCode from,
			                                                                     final Collection<NotifierPushMessageSubscriber> to,
			                                                                     final Factory<NotifierPushMessage> messageToBeDeliveredFactory) {
		Collection<NotifierResponse<NotifierPushMessageSubscriber>> responses
						= Lists.newArrayListWithExpectedSize(to.size());

		// [1] - Create the push message
		NotifierPushMessage pushMessage = messageToBeDeliveredFactory.create();

		//[2] key value data items.
		Collection<FirebasePushMessageDataItem> pushMessageDataItems = Lists.newArrayList();
		if ( CollectionUtils.hasData(pushMessage.getKeyValueData())) {
			 for (final String key : pushMessage.getKeyValueData().keySet() ) {
				 pushMessageDataItems.add(new FirebasePushMessageDataItem(FirebasePushMessageDataItemID.of(key),pushMessage.getKeyValueData().get(key)));
			 }
		}

		// [3] - push to every destination
		log.info("[{}] > push to {} subscribers",
												  FirebaseNotifierService.class.getSimpleName(),
												   to.size());
		for (final NotifierPushMessageSubscriber subscriber : to) {
			try {
				FirebaseRegisteredDevicesTopic topic = subscriber.getTopic() != null ?
						                                                                FirebaseRegisteredDevicesTopic.of(subscriber.getTopic().asString()) : null ;
				FirebaseRegisteredDeviceToken token  = subscriber.getToken() != null ?
					                                                                 	FirebaseRegisteredDeviceToken.of(subscriber.getToken().asString()) : null ;
				FirebasePushMessageRequest pushMessageRequest = new FirebasePushMessageRequest(topic,
						                                                                       token,
						                                                                       pushMessage.getTitle(),
						                                                                       pushMessage.getBody(),
						                                                                       pushMessage.getNotificationSound(),
						                                                                       pushMessage.getCollapseKey(),
						                                                                       pushMessage.getChannelId(),
						                                                                       pushMessageDataItems);

				FirebasePushMessageResponse response = _firebaseService.push(pushMessageRequest);

				log.warn(" response : {}",
						                 response.getMessage());

				responses.add(new NotifierResponseImpl<NotifierPushMessageSubscriber>(NotifierTaskOID.supply(),
																					  subscriber,
																				      true));	// success
			} catch (final Throwable nitex) {

				responses.add(new NotifierResponseImpl<NotifierPushMessageSubscriber>(NotifierTaskOID.supply(),
					                                                                  subscriber,
														  	                          false));	// failed
			}
		}
	    // [3] - Build the response
		return responses;
	}
}
