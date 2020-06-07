package r01f.cloud.nexmo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="content")
@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class MessageContentBase 
	implements MessageContent {
/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="type")
	@Getter  private  final MessageContentType _type;
}
