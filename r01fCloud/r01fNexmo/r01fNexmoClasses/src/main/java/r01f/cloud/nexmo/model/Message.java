package r01f.cloud.nexmo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="message")
@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
@NoArgsConstructor
public class Message {

	@MarshallField(as="content")
	@Getter @Setter private MessageContent  _content;
}
