package r01f.cloud.firebase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;


@Accessors(prefix="_")
@RequiredArgsConstructor
public class PushMessageResponse {
///////////////////////////////////////////////////////////////////////
// Members
//////////////////////////////////////////////////////////////////////
     @Getter private final String _message;
}
