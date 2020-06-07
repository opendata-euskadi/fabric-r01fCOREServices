package r01f.cloud.nexmo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.cloud.nexmo.model.NexmoIDS.PeerID;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.Phone;

@MarshallType(as="peer")
@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
@RequiredArgsConstructor
public class Peer
     implements ModelObject {

	private static final long serialVersionUID = 3848509510590725933L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="type")
	@Getter private final  PeerType _type;

	@MarshallField(as="number")
	@Getter private final  Phone _number;
	

	@MarshallField(as="id")
	@Getter private final  PeerID _id;
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public Peer(final PeerType type, final Phone number ) {
		this(type,number,null);
	}

}
