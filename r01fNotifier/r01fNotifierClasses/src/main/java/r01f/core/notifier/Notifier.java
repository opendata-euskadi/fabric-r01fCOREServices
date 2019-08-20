package r01f.core.notifier;

public interface Notifier<M> {	// message
	/**
	 * @return true if the notifier is enabled, false otherwise
	 */
	public boolean isEnabled();
	/**
	 * Send the notification
	 * @param message
	 */
	public void sendNotification(final M message);
}
