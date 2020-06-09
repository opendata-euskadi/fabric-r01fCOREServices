package r01f.cloud.nexmo.api.interfaces.impl;

import com.nexmo.client.NexmoClient;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.nexmo.NexmoAPI.NexmoAPIData;
import r01f.cloud.nexmo.api.interfaces.NexmoServicesForMessagingApplication;
import r01f.cloud.nexmo.model.Message;
import r01f.cloud.nexmo.model.MessageContents.TextMessageContent;
import r01f.cloud.nexmo.model.outbound.NexmoOutboundMessage;
import r01f.httpclient.HttpRequestHeader;
import r01f.httpclient.HttpResponse;
import r01f.mime.MimeTypes;
import r01f.objectstreamer.Marshaller;
import r01f.services.client.servicesproxy.rest.DelegateForRawREST;
import r01f.types.contact.Phone;
import r01f.types.url.Url;

@Slf4j
public class NexmoServicesForMessagingApplicationImpl 
			extends NexmoServicesBaseImpl 
		implements NexmoServicesForMessagingApplication {
////////////////////////////////////////////////////
// CONSTRUCTOR
////////////////////////////////////////////////////	
	public NexmoServicesForMessagingApplicationImpl(final NexmoAPIData apiData,
			                                        final NexmoClient nexmoClient,
			                                        final Marshaller marshaller) {
		super(apiData,nexmoClient,marshaller);	
	}
	
	public void send(final Phone toPhone,
                     final String text) {
		NexmoOutboundMessage outboudMesagge = new NexmoOutboundMessage();
		Message message = new Message();
		TextMessageContent _content = new TextMessageContent(text);
		message.setContent(_content);
		send(outboudMesagge);
	}
	

	@Override
	public void send(final NexmoOutboundMessage out) {		
		// do the http call
		Url restResourceUrl = _apiData.getRestResouceURIForMessagingApplicationImpl();
		log.warn(" Create REST Resource Url {}", restResourceUrl);
	
		String entityAsStringJson = _marshaller.forWriting().toJson(out);
		log.warn(" Post Entity :\n {}", entityAsStringJson);
		
		String jwtAsString = _nexmoClient.generateJwt();
		
		HttpResponse httpResponse = DelegateForRawREST.POST(restResourceUrl, 									                        // REST Resource URI
				                                            MimeTypes.APPLICATION_JSON,                                                 // Mime Type JSON
										  					new HttpRequestHeader("bearer",jwtAsString),     						    // JWT bearer
										  					entityAsStringJson,                              						    // Posted JSON as String
										  					new HttpRequestHeader("accept",MimeTypes.APPLICATION_JSON.asString()));    // Accept Header, to obtain CRUDResult based response ( otherwise model object will be returned)
	
		// log & return
		_logResponse(restResourceUrl,httpResponse);		
		
		// To do . Parse 200 OK result JSON to MessageUUID //{"message_uuid":"e670a362-568a-4895-a93e-76ffb78c21f1"}
		
	}	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE METHODS	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	private void _logResponse(Url restResourceUrl, HttpResponse httpResponse) {
		 log.warn(" Response :\n\n{}",httpResponse.loadAsString());		
	}
}
