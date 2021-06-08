package r01f.core.services.notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;

@RequiredArgsConstructor
@Accessors(prefix="_")
public abstract class NotifierResponseBase<T>
  implements NotifierResponse<T> {
	@Getter protected final NotifierTaskOID _taskOid;	
	@Getter protected final T _to;
	@Getter protected final boolean _success;
	@Getter protected final NotifierType _notifierType;

	@Override
	public boolean wasSuccessful() {
		return _success;
	}
	@Override
	public NotifierResponseError<T> asResponseError() {
		if ( this instanceof NotifierResponseError )
			return (NotifierResponseError<T> ) this;
		throw  new IllegalStateException("Cannot cast a NotifierResponseOK to NotifierResponseError");
	}
	@Override
	public NotifierResponseOK<T> asResponseOK() {
		if ( this instanceof NotifierResponseOK )
			return (NotifierResponseOK<T> ) this;
		throw  new IllegalStateException("Cannot cast a NotifierResponseError to NotifierResponseOK");
	}	
}