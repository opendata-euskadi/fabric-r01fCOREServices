package r01f.core.services.notifier;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessage {
	public NotifierPushMessage(final String title, final String body) {
		this(title, body, null);
	}

	public NotifierPushMessage(final String title, final String body, final Map<String, String> keyValueData) {
		_title = title;
		_body = body;
		_keyValueData = keyValueData != null ? keyValueData : new HashMap<String, String>();
	}

	@Getter private String _title;
	@Getter private String _body;
	@Getter private Map<String,String> _keyValueData; //will be transformed to provider (firebase, etc..), key value format.
}