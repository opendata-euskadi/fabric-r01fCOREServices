package r01f.core.services.notifier;

/**
 * Models a notification response
 * @param <A>
 */
public interface NotifierResponse<T> {
	public NotifierTaskOID getTaskOid();
	public T getTo();
	public boolean wasSuccessful();

}
