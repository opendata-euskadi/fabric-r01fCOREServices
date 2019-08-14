package r01f.core.services.notifier;

import java.util.Collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.patterns.Factory;

public interface NotifierService<F,T,	// FROM & TO
								 M> {
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public abstract NotifierResponse<T> notify(F from,T to,
							   				   Factory<M> messageToBeDeliveredFactory);

	public abstract Collection<NotifierResponse<T>> notifyAll(F from,Collection<T> to,
											  				  Factory<M> messageToBeDeliveredFactory);
/////////////////////////////////////////////////////////////////////////////////////////
//  Request & Response
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public class NotifierResponseImpl<T>
	  implements NotifierResponse<T> {
		@Getter private final NotifierTaskOID _taskOid;
		@Getter private final T _to;
		@Getter private final boolean _success;

		@Override
		public boolean wasSuccessful() {
			return _success;
		}
	}
}
