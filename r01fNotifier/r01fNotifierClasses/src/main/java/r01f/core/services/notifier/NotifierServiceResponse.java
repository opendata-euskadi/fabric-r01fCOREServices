package r01f.core.services.notifier;

import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;

/**
 * Models a notification response
 * @param <A>
 */
public interface NotifierServiceResponse<T> {
	public NotifierTaskOID getTaskOid();
	public T getTo();
	public boolean wasSuccessful();	
	public NotifierServiceResponseError<T> asResponseError();
	public NotifierServiceResponseOK<T> asResponseOK();

}
