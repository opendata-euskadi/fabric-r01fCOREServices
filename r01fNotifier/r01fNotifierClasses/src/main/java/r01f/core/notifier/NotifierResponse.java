package r01f.core.notifier;

import r01f.core.services.notifier.NotifierServiceResponse;

public interface NotifierResponse {
	
	public boolean wasSuccessful();	
	
	public <T> NotifierServiceResponse<T> getServiceResponse();	
}
