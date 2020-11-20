package r01f.cloud.firebase.service;

import r01f.cloud.firebase.model.FirebasePushMessageRequest;
import r01f.cloud.firebase.model.FirebasePushMessageResponse;

public interface FirebaseService {	//interface

	FirebasePushMessageResponse push(final FirebasePushMessageRequest pushMessageRequest);
}
