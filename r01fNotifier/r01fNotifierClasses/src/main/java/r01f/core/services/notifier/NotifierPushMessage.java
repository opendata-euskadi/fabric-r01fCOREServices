package r01f.core.services.notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Accessors(prefix="_")
@RequiredArgsConstructor
public  class NotifierPushMessage{
	@Getter private String _title;
	@Getter private String _text;
}