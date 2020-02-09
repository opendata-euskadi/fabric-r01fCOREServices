package r01f.core.services.notifier;

import r01f.guids.CommonOIDs.AppCode;



public interface NotifierServiceForPushMessage<T,TK>
         extends NotifierService<AppCode,NotifierPushMessageSubcriber<T,TK>,// subscriber topic / token
                                 NotifierPushMessage> {
	//

}