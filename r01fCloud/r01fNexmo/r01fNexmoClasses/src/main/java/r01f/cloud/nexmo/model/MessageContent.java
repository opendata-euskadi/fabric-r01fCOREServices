package r01f.cloud.nexmo.model;

import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshallTypeInfoInclude;
@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
@MarshallPolymorphicTypeInfo(typeIdPropertyName="type",
                             includeTypeInfo=@MarshallTypeInfoInclude(type=MarshalTypeInfoIncludeCase.ALWAYS))
public interface MessageContent {
	public MessageContentType getType();
}
