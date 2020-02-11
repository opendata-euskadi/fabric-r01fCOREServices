package r01f.core.services.notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/***
 * The receptor ( or receptors of )a Push Message are identified by a Topic and by a token	 *
 * @author PC
 *
 * @param <T>
 * @param <TK>
 */
@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessageSubscriber<T,TK>{
	@Getter private final T _topic;
	@Getter private final TK _token;
}