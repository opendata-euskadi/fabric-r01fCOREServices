package r01f.cloud.firebase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.cloud.firebase.model.FirebaseIds.PushMessageDataItemId;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class PushMessageDataItem {
//////////////////////////////////////////////////////////////
// MEMBERS
//////////////////////////////////////////////////////////////
	@Getter private final PushMessageDataItemId _id;
    @Getter private final String _value;
////////////////////////////////////////////////////////////
// OTHER CONSTRUCTORS
///////////////////////////////////////////////////////////

}
