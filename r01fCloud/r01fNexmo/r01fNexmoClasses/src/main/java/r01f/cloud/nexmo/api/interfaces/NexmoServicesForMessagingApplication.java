package r01f.cloud.nexmo.api.interfaces;

import r01f.cloud.nexmo.model.outbound.NexmoOutboundMessage;

public interface NexmoServicesForMessagingApplication {
	
	public void send(final NexmoOutboundMessage out);
}
