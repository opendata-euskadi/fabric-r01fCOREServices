package r01f.core.services.notifier;

import lombok.experimental.Accessors;
import r01f.core.services.notifier.NotifierOIDs.NotifierTaskOID;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;

@Accessors(prefix="_")
public  class NotifierResponseOK<T>
	extends NotifierResponseBase<T>
  implements NotifierResponse<T> {
///////////////////////////////////////////////////
//CONSTRUCTOR
//////////////////////////////////////////////////	
	public NotifierResponseOK(final NotifierTaskOID taskOid, final T to, final  NotifierType notifierType) {
		super(taskOid, to, true, notifierType);		
	}	
}