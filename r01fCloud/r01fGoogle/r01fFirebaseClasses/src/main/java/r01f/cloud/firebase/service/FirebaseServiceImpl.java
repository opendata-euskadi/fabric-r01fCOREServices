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
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;
import r01f.cloud.firebase.model.PushMessageRequest;
import r01f.cloud.firebase.model.PushMessageResponse;
import r01f.httpclient.HttpClientProxySettings;
import r01f.service.ServiceCanBeDisabled;


public class FirebaseServiceImpl
		implements FirebaseService,
		            ServiceCanBeDisabled {
////////////////////////////////////
	private final FirebaseAPIData _apiData;
	@SuppressWarnings("unused")
	private final HttpClientProxySettings _proxySettings;	// TODO enable api with proxy
	private boolean _disabled;
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public FirebaseServiceImpl(final FirebaseConfig config) {
		this(config.getApiData(),
		     config.getProxySettings());
	}
	public FirebaseServiceImpl(final FirebaseAPIData apiData) {
		this(apiData,null);	// proxy settings
	}
	public FirebaseServiceImpl(final FirebaseAPIData apiData,
							    final HttpClientProxySettings proxySettings) {
		_apiData = apiData;
		_proxySettings = proxySettings;
		 FirebaseOptions	options = new FirebaseOptions.Builder()
				 										  .setCredentials(_apiData.getGoogleCredentials()).build();
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
//API DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public static class FirebaseAPIData {
		@Getter private final GoogleCredentials _googleCredentials;  //GoogleCredentials.fromStream(stream)
	}
/////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////
	@Override
	public PushMessageResponse push(final PushMessageRequest pushMessageRequest) {
		return _pushMessage(_buidMessage(pushMessageRequest));
	}
/////////////////////////////////////////////////////////////////////////////
// PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////
  private static PushMessageResponse _pushMessage(final Message message){
       try {
		return new PushMessageResponse(FirebaseMessaging.getInstance().sendAsync(message).get());
	} catch (final InterruptedException | ExecutionException e) {
		throw new RuntimeException(e.getLocalizedMessage());
	}
  }
  /**
   * Builds APN Config.
   * @param topic
   * @return
   */
  private static ApnsConfig _buildApnsConfig(final RegistredDevicesTopic topic) {
      return ApnsConfig.builder()
			              .setAps(Aps.builder()
			            		      .setCategory(topic.asString())
			                          .setThreadId(topic.asString()).build())
			              .build();
  }
  /**
   * Builds the Android-specific options that can be included in a Message.Instances of this class are thread-safe and immutable
   * @param topic
   * @return
   */
  private static AndroidConfig _buildAndroidConfig(final RegistredDevicesTopic topic) {
      return AndroidConfig.builder()
			              .setTtl(Duration.ofMinutes(2).toMillis())
			              .setCollapseKey(topic.getId())
			              .setPriority(AndroidConfig.Priority.HIGH)
			              .setNotification(AndroidNotification.builder()
			            		                              .setSound("default")
			                                                  .setColor("#FFFF00")
			                                                  .setTag(topic.getId()).build())
			              .build();
  }

  private static Message _buidMessage(final PushMessageRequest pushMesageRequest) {
	  Builder baseMessage  = Message.builder()
							  		 .setApnsConfig(_buildApnsConfig(pushMesageRequest.getTopic()))
							  		 .setAndroidConfig(_buildAndroidConfig(pushMesageRequest.getTopic()))
							  		 .setNotification(Notification.builder()
							  				 					 		.setBody(pushMesageRequest.getMessage())
								  				 						.setTitle(pushMesageRequest.getTitle())
					                                                .build());


	  if (pushMesageRequest.hasToken()) {
		  baseMessage.setToken(pushMesageRequest.getToken().asString());
	  } else {
		  baseMessage.setTopic(pushMesageRequest.getTopic().asString());
	  }
	  return baseMessage.build();
  }

}
