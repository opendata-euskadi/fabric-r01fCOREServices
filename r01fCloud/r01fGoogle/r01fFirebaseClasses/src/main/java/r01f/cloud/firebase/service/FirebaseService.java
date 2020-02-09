package r01f.cloud.firebase.service;

import r01f.cloud.firebase.model.PushMessageResponse;
import r01f.cloud.firebase.model.PushMessageRequest;

public interface FirebaseService {
	//interface

	PushMessageResponse push(final PushMessageRequest pushMessageRequest);
}
