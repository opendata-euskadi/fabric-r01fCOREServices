package r01f.cloud.firebase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class PushMessageRequest {
//////////////////////////////////////////////////////////////
// MEMBERS
//////////////////////////////////////////////////////////////
	@Getter private final RegistredDevicesTopic _topic;
    @Getter private final RegistredDeviceToken _token;
	@Getter private final String _title;
    @Getter private final String _message;
////////////////////////////////////////////////////////////
// OTHER CONSTRUCTORS
///////////////////////////////////////////////////////////
    public PushMessageRequest(final RegistredDevicesTopic topic,
    		                  final String title,
    		                  final String message) {
    	this(topic,null,title,message );
    }
////////////////////////////////////////////////////////////
//	OTHER METHODS
///////////////////////////////////////////////////////////
    public boolean hasToken() {
    	return _token != null;
    }

}
