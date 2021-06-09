package r01f.core.notifier;

import r01f.core.services.notifier.NotifierResponse;

public interface Notifier<M,T> { // Message & Subscriber(To)
	/**
	 * @return true if the notifier is enabled, false otherwise
	 */
	public boolean isEnabled();
	/**
	 * Send the notification
	 * @param <T>
	 * @param message
	 */
	public NotifierResponse<T> sendNotification(final M message);
}
