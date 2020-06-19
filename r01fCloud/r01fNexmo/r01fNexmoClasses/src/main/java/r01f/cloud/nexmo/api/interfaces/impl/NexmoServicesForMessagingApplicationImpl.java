package r01f.cloud.nexmo.api.interfaces.impl;

import com.nexmo.client.NexmoClient;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.nexmo.NexmoAPI.NexmoAPIData;
import r01f.cloud.nexmo.api.interfaces.NexmoServicesForMessagingApplication;
import r01f.cloud.nexmo.model.NexmoIDS.MessageUUID;
import r01f.cloud.nexmo.model.outbound.NexmoOutboundMessage;
import r01f.httpclient.HttpRequestHeader;
import r01f.httpclient.HttpResponse;
import r01f.mime.MimeTypes;
import r01f.objectstreamer.Marshaller;
import r01f.services.client.servicesproxy.rest.DelegateForRawREST;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.util.types.Strings;

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
	public void send(final Phone toPhone,  final String text) {
		/*NexmoOutboundMessage outboudMesagge = new NexmoOutboundMessage();
		Message message = new Message();
		TextMessageContent _content = new TextMessageContent(text);
		message.setContent(_content);
		send(outboudMesagge);*/
		throw new UnsupportedOperationException(" Not implemented. Use : public MessageUUID send( final NexmoOutboundMessage out ) ");
		
	}
	@Override
	public MessageUUID send( final NexmoOutboundMessage out ) {		
	   return _doSend(out);	
	}
////////////////////////////////////////////////////
// PRIVATE METHODS
////////////////////////////////////////////////////
	private  MessageUUID _doSend( final NexmoOutboundMessage out ) {		
		// do the http call
		Url restResourceUrl = _apiData.getRestResouceURIForMessagingApplicationImpl();
		log.warn(" Create REST Resource Url {}", restResourceUrl);
	    //0. Marshall.
		String entityAsStringJson = _marshaller.forWriting().toJson(out);
		//1. Generare JWT based on Nexmo Client.
		String jwtAsString = _nexmoClient.generateJwt();			 
	    log.warn(" \n \n Generated JWT  {} \n" , jwtAsString);
		//2. POST.
		HttpResponse httpResponse = DelegateForRawREST.POST(restResourceUrl, 									                        // REST Resource URI
				                                            MimeTypes.APPLICATION_JSON,                                                 // Mime Type JSON
										  					new HttpRequestHeader("Authorization","Bearer " + jwtAsString),     						    // JWT bearer
										  					entityAsStringJson,                              						    // Posted JSON as String
										  					new HttpRequestHeader("accept",MimeTypes.APPLICATION_JSON.asString()));    // Accept Header, to obtain CRUDResult based response ( otherwise model object will be returned)
	
	 
	    //Parse Result
		 if (httpResponse.isSuccess()) {
			// Parse 200 OK result JSON to MessageUUID //{"message_uuid":"e670a362-568a-4895-a93e-76ffb78c21f1"}
			 String jsonAsString = httpResponse.loadAsString();//StringPersistenceUtils.load(response.getEntity().getContent());
			 log.warn( "json response {}", jsonAsString);
			 MessageUUID uuid = _marshaller.forReading()
			                                 .fromJson(jsonAsString, MessageUUID.class);
			 log.warn(" post message uuid {}", uuid.asString());
			 return uuid;
		 } else {			
			throw new IllegalStateException( Strings.customized(" error posting  outbound message {}",httpResponse.loadAsString()));
		 }		
	}	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE METHODS	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

}
