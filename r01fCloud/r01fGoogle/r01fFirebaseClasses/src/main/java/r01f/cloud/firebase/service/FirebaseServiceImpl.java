package r01f.cloud.firebase.service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.firebase.model.FirebasePushMessageRequest;
import r01f.cloud.firebase.model.FirebasePushMessageResponse;
import r01f.httpclient.HttpClientProxySettings;
import r01f.service.ServiceCanBeDisabled;

@Slf4j
public class FirebaseServiceImpl
  implements FirebaseService,
			 ServiceCanBeDisabled {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final FirebaseAPIData _apiData;
	@SuppressWarnings("unused")
	private final HttpClientProxySettings _proxySettings;	// TODO enable api with proxy
	private boolean _disabled;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public FirebaseServiceImpl(final FirebaseConfig config) {
		this(config.getApiData(),
			 config.getProxySettings());
	}
	public FirebaseServiceImpl(final FirebaseAPIData apiData) {
		this(apiData,
			 null);		// proxy settings
	}
	public FirebaseServiceImpl(final FirebaseAPIData apiData,
							   final HttpClientProxySettings proxySettings) {
		_apiData = apiData;
		_proxySettings = proxySettings;
		FirebaseOptions	options = new FirebaseOptions.Builder()
												  .setCredentials(_apiData.getGoogleCredentials())
												  .build();
		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
}
/////////////////////////////////////////////////////////////////////////////////////////
//ServiceCanBeDisabled
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isEnabled() {
		return !_disabled;
	}
	@Override
	public boolean isDisabled() {
		return _disabled;
	}
	@Override
	public void setEnabled() {
		_disabled = false;
	}
	@Override
	public void setDisabled() {
		_disabled = true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	API DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public static class FirebaseAPIData {
		@Getter private final GoogleCredentials _googleCredentials;  //GoogleCredentials.fromStream(stream)
	}
/////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////
	@Override
	public FirebasePushMessageResponse push(final FirebasePushMessageRequest pushMessageRequest) {
		log.warn(".. push {}",
							pushMessageRequest.debugInfo());
		Message message = _buidMessage(pushMessageRequest);
		FirebasePushMessageResponse response  = null;
		if (pushMessageRequest.isAsyncRequest()) {
			response = _pushMessageAsync(message);
		} else {
			response = _pushMessage(message);
		}		
		return response;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static FirebasePushMessageResponse _pushMessage(final Message message) {
	   try {
		   return new FirebasePushMessageResponse(FirebaseMessaging.getInstance().send(message));
	   } catch (final  FirebaseMessagingException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e.getLocalizedMessage());
	   }
	}
	private static FirebasePushMessageResponse _pushMessageAsync(final Message message) {
	   try {
		   return new FirebasePushMessageResponse(FirebaseMessaging.getInstance().sendAsync(message).get());
	   } catch (final  InterruptedException | ExecutionException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e.getLocalizedMessage());
	   } 
	}
	
	/**
	 * Builds APN Config.
	 * @param topic
	 * @return
	 */
	private static ApnsConfig _buildApnsConfig() {
		return ApnsConfig.builder()
  						  .setAps(Aps.builder()
  									 // .setCategory(topic.asString())
  									 // .setThreadId(topic.asString()
  								   .build())
  						  .build();
	}
	/**
	 * Builds the Android-specific options that can be included in a Message.Instances of this class are thread-safe and immutable
	 * @param topic
	 * @return
	 */
	private static AndroidConfig _buildAndroidConfig(final FirebasePushMessageRequest pushMessageRequest) {
	  	return AndroidConfig.builder()
						  .setTtl(Duration.ofMinutes(2).toMillis())
						  .setCollapseKey(pushMessageRequest.getCollapseKey())
						  .setPriority(AndroidConfig.Priority.HIGH)
						  .setNotification(AndroidNotification.builder()
															  .setSound(pushMessageRequest.hasCustomNotificationSound() ? pushMessageRequest.getNotificationSound() : "default")
															  .setChannelId(pushMessageRequest.hasChannelId() ? pushMessageRequest.getChannelId() : null)
															  .setDefaultVibrateTimings(true)															  
															  //.setClickAction("MAINACTIVITY")
															  .build())
						  
						  .build();
	}
  	private static Message _buidMessage(final FirebasePushMessageRequest pushMessageRequest) {
  		Builder baseMessage  = Message.builder()
							  		  .setApnsConfig(_buildApnsConfig())
							  		  .setAndroidConfig(_buildAndroidConfig(pushMessageRequest))
							  		  .setNotification(Notification.builder()
							  				 					 		.setBody(pushMessageRequest.getBody())
								  				 						.setTitle(pushMessageRequest.getTitle())								  				 						
																	.build());
		if (pushMessageRequest.hasToken()) {
			baseMessage.setToken(pushMessageRequest.getToken().asString());
		} else {
			baseMessage.setTopic(pushMessageRequest.getTopic().asString());
		}
		if (pushMessageRequest.hasDataItems()) {
			pushMessageRequest.getDataItems()
						   	  .forEach(i-> {
							   				baseMessage.putData(i.getId().asString(), i.getValue());
						   			  });
  		}
  		return baseMessage.build();
  	}
}
