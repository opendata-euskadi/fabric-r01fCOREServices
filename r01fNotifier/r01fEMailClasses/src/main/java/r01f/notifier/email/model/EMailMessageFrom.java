package r01f.notifier.email.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.types.CanBeRepresentedAsString;
import r01f.types.contact.EMail;


@Accessors(prefix="_")
@RequiredArgsConstructor
public class EMailMessageFrom
  implements CanBeRepresentedAsString,
  			 Serializable {

	private static final long serialVersionUID = -2963399480304370901L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final EMail _email;
    @Getter private final String _name;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
    public static EMailMessageFrom of(final EMail email,final String name) {
    	return new EMailMessageFrom(email,name);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
	public String asString() {
        return String.format("\"%s\" <%s>",
        					 _name,_email);
    }
}
