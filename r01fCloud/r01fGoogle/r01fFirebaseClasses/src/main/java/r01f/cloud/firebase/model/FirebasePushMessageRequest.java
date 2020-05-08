package r01f.cloud.firebase.model;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.cloud.firebase.model.FirebaseIds.FirebaseRegisteredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.FirebaseRegisteredDevicesTopic;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.debug.Debuggable;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class FirebasePushMessageRequest
     extends NotifierPushMessage
  implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final FirebaseRegisteredDevicesTopic _topic;
	@Getter private final FirebaseRegisteredDeviceToken _token;
	@Getter private final String _title;
	@Getter private final String _body;
	@Getter private final String _notificationSound;
	@Getter private final Collection<FirebasePushMessageDataItem> _dataItems;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public FirebasePushMessageRequest(final FirebaseRegisteredDevicesTopic topic,
									  final String title,
									  final String body,
									  final String notificationSound,
									  final FirebasePushMessageDataItem... dataItems) {
		this(topic,
			 null,			// token
			 title, body, notificationSound,
			 Lists.newArrayList(dataItems));
	}
	public FirebasePushMessageRequest(final FirebaseRegisteredDeviceToken token,
							  		  final String title,
							  		  final String body,
							  		  final String notificationSound,
							  		  final FirebasePushMessageDataItem... dataItems) {
		this(null,			// topic
			 token,
			 title, body, notificationSound,
			 Lists.newArrayList(dataItems));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasToken() {
		return _token != null;
	}
	public boolean hasDataItems() {
		return CollectionUtils.hasData(_dataItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUGGABLE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder dbg = new StringBuilder();
		if (_title != null) {
			dbg.append("\n Title: ")
			   .append(_title);
		}
		if (_body != null) {
			dbg.append("\n body: ")
			   .append(_body);
		}
		if (hasCustomNotificationSound()) {
			dbg.append("\n sound: ")
			   .append(_notificationSound);
		}
		if (_token != null) {
			dbg.append("\n token: ")
			   .append(_token);
		}
		if (_topic != null) {
			dbg.append("\n topic: ")
			   .append(_topic);
		}
		if (CollectionUtils.hasData(_dataItems)) {
			dbg.append("\n data items: ");
			for (FirebasePushMessageDataItem it : _dataItems ) {
				dbg.append("\t").append( it.getId() + ": " + it.getValue());
			}
		}
		return dbg;
	}

	public boolean hasCustomNotificationSound() {
		return Strings.isNOTNullOrEmpty(_notificationSound);
	}
}
