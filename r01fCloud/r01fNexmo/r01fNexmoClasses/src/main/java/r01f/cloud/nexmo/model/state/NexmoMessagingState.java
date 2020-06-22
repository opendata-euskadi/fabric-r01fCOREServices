package r01f.cloud.nexmo.model.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.locale.Language;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.Phone;

//[ TODO: THIS SHOUL BE AT BUSSINNESS APPLCIATION
//]>
@MarshallType(as="messagingState")
@ConvertToDirtyStateTrackable			// changes in state are tracked
@RequiredArgsConstructor
@Accessors(prefix="_")
public abstract class NexmoMessagingState {
	
	@Getter
	@MarshallField(as="phoneNumber")
	protected final Phone _phone;
	
	@MarshallField(as="language")
	@Getter @Setter
	protected Language _language;
	
	public abstract void reset(); 
}
