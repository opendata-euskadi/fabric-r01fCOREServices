package r01f.core.services.notifier;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;

@Accessors(prefix="_")
public class NotifierResponseError<T>
	extends NotifierResponseBase<T>
  implements NotifierResponse<T> {	
	
	@Getter protected final NotifierResponseErrorType _errorType;
	@Getter protected final String  _errorDetail;
///////////////////////////////////////////////////
//CONSTRUCTOR
//////////////////////////////////////////////////		
	public NotifierResponseError(final NotifierTaskOID taskOid, final T to,
                                 final NotifierType notifierType, final  NotifierResponseErrorType errorType) {
		this(taskOid, to,notifierType, errorType, null);	

	}
	public NotifierResponseError(final NotifierTaskOID taskOid,   final T to,
			                     final NotifierType notifierType, final NotifierResponseErrorType errorType, final String errorDetail) {
		super(taskOid, to, false,notifierType);	
		_errorType = errorType;
		_errorDetail = errorDetail;
	}
	
}