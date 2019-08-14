package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.types.contact.EMail;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public abstract class NotifierConfigForEMailBase
     		  extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final EMailRFC822Address _from;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigForEMailBase(final AppCode appCode,
								     	 final boolean enabled,
								     	 final NotifierImpl impl,
								     	 final EMail fromMail,final String fromName) {
		super(appCode,
			  NotifierType.EMAIL,
			  enabled,
			  impl);
		_from = EMailRFC822Address.of(fromMail,fromName);
	}
	protected NotifierConfigForEMailBase(final XMLPropertiesForAppComponent props) {
		super(NotifierType.EMAIL,
			  props);
		EMail fromMail = props.propertyAt(_xPathBase() + "/from/@mail")
							  .asEMail("Zuzenean-No-Reply@euskadi.eus");
		String fromName = props.propertyAt(_xPathBase() + "/from")
							   .asString("Zuzenean");

		_from = EMailRFC822Address.of(fromMail,fromName);
	}
}
