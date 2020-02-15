package r01f.cloud.firebase.service;

import r01f.cloud.firebase.model.FirebasePushMessageResponse;
import r01f.cloud.firebase.model.FirebasePushMessageRequest;

public interface FirebaseService {	//interface

	FirebasePushMessageResponse push(final FirebasePushMessageRequest pushMessageRequest);
}
