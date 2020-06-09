package r01f.cloud.nexmo.api.interfaces;

import r01f.cloud.nexmo.model.NexmoIDS.MessageUUID;
import r01f.cloud.nexmo.model.outbound.NexmoOutboundMessage;

public interface NexmoServicesForMessagingApplication {
	
	public MessageUUID send(final NexmoOutboundMessage out);
}
