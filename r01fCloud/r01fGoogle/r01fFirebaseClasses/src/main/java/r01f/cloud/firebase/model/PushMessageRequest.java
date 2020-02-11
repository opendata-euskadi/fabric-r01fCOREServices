package r01f.cloud.firebase.model;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;
import r01f.debug.Debuggable;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class PushMessageRequest
	implements Debuggable {
//////////////////////////////////////////////////////////////
// MEMBERS
//////////////////////////////////////////////////////////////
	@Getter private final RegistredDevicesTopic _topic;
    @Getter private final RegistredDeviceToken _token;
	@Getter private final String _title;
    @Getter private final String _body;
    @Getter private final Collection<PushMessageDataItem> _dataItems;
////////////////////////////////////////////////////////////
// OTHER CONSTRUCTORS
///////////////////////////////////////////////////////////
    public PushMessageRequest(final RegistredDevicesTopic topic,
    		                  final String title,
    		                  final String body,
    		                  final PushMessageDataItem... dataItems) {
    	this(topic,null,title,body,Lists.newArrayList(dataItems) );
    }
    public PushMessageRequest(final RegistredDevicesTopic topic,
						      final String title,
						      final String body) {
    	this(topic,null,title,body,null );
    }
    public PushMessageRequest(final RegistredDeviceToken token,
				              final String title,
				              final String body,
				              final PushMessageDataItem... dataItems) {
    	this(null,token,title,body,Lists.newArrayList(dataItems) );
	}
	public PushMessageRequest(final RegistredDeviceToken token,
						      final String title,
						      final String body) {
		this(null,token,title,body,null );
	}
////////////////////////////////////////////////////////////
//	OTHER METHODS
///////////////////////////////////////////////////////////
    public boolean hasToken() {
    	return _token != null;
    }
    public boolean hasDataItems() {
    	return CollectionUtils.hasData(_dataItems);
    }
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
			for (PushMessageDataItem it : _dataItems ) {
				dbg.append("\t").append( it.getId() + ": " + it.getValue());
			}
		}
		return dbg;
	}
}
