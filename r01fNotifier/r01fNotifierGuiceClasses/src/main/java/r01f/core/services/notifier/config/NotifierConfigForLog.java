package r01f.core.services.notifier.config;

import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public class NotifierConfigForLog
     extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// nothing specific
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifierConfigForLog(final AppCode appCode,
								   final boolean enabled) {
		super(appCode,
			  NotifierType.LOG,
			  enabled,
			  null,null);
	}
	public NotifierConfigForLog(final XMLPropertiesForAppComponent props) {
		super(NotifierType.LOG,
			  props,
			  null);
	}
}
