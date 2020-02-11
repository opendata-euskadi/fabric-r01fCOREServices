package r01f.core.services.notifier;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessage {
	@Getter private String _title;
	@Getter private String _body;
	@Getter private Map<String,String> _keyValueData; //will be transformed to provider (firebase, etc..), key value format.
}