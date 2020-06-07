package r01f.cloud.nexmo.model;

import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="content")
@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")

public interface MessageContent {
	//
}
