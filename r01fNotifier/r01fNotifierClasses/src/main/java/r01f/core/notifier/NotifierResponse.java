package r01f.core.notifier;

import r01f.core.services.notifier.NotifierServiceResponse;

/**
 * Just an interface to return a reponse about the nofifier:
 *
 *  1. Just to know if notification was sucessfull.
 *  2. Get the underlaying services response ( smms latinia,  push-firebase...)
 *  
 *  If there is some custom or extended implementation of Notifier just return :
 *  		   return new  NotifierResponse() {
									@Override
									public boolean wasSuccessful() {									
										return serviceResponse.wasSuccessful(); //<< reponse based on underlaying service response.
									}						
								
									@Override 
									public NotifierServiceResponse<Phone> getServiceResponse() {										
										 return serviceResponse;
									}} ;
	    }

 *  			
 * @author PCI
 *
 */
public interface NotifierResponse {
	
	public boolean wasSuccessful();	
	
	public <T> NotifierServiceResponse<T> getServiceResponse();	
}
