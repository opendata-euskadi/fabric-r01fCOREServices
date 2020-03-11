package r01f.core.services.notifier;

import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;

/**
 * Models a notification response
 * @param <A>
 */
public interface NotifierResponse<T> {
	public NotifierTaskOID getTaskOid();
	public T getTo();
	public boolean wasSuccessful();

}
