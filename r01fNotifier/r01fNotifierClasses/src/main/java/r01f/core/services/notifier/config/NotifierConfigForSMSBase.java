package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public abstract class NotifierConfigForSMSBase
     		  extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OwnedContactMean<Phone> _from;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigForSMSBase(final AppCode appCode,
								 	  final boolean enabled,
								 	  final NotifierImpl impl,
								 	  final OwnedContactMean<Phone> from) {
		super(appCode,
			  NotifierType.SMS,
			  enabled,
			  impl);
		_from = from;
	}
	protected NotifierConfigForSMSBase(final XMLPropertiesForAppComponent props) {
		super(NotifierType.SMS,
			  props);
		Phone fromPhone = props.propertyAt(_xPathBase() + "/from/@phone")
							   .asPhone("012");
		String fromOwner = props.propertyAt(_xPathBase() + "/from")
								.asString("Zuzenean");

		_from = new OwnedContactMean<Phone>(fromPhone,fromOwner);
	}
}
