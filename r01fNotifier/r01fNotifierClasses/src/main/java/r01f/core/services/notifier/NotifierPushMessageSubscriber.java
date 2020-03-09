package r01f.core.services.notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/***
 * The receptor ( or receptors of )a Push Message are identified by a Topic or a token, or both.. *
 * @author PC
 *
 * @param <T>
 * @param <TK>
 */
@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessageSubscriber<T,TK>{
	@Getter private T _topic;
	@Getter private TK _token;	
///////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////	
	public NotifierPushMessageSubscriber<T, TK> forToken(final TK token) {
	    _token = token;
		return this;
	}
	public NotifierPushMessageSubscriber<T, TK> forTopic(final T topic) {
	    _topic = topic;
		return this;
	}
}