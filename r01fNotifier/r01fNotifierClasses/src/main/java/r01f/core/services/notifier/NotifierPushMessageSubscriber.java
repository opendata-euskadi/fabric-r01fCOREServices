package r01f.core.services.notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.NotifierOIDs.NotifierPushTopic;
import r01f.guids.CommonOIDs.SecurityToken;

/***
 * The receptor ( or receptors of )a Push Message are identified by a Topic or a token, or both.. *
 * @author PC
 *
 * @param <T>
 * @param <TK>
 */
@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessageSubscriber{
	@Getter private NotifierPushTopic  _topic;
	@Getter private SecurityToken  _token;
///////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////
	public NotifierPushMessageSubscriber forToken(final SecurityToken token) {
	    _token = token;
		return this;
	}
	public NotifierPushMessageSubscriber forTopic(final NotifierPushTopic topic) {
	    _topic = topic;
		return this;
	}
}