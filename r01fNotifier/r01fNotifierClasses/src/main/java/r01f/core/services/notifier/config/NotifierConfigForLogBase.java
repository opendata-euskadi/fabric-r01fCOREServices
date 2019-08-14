package r01f.core.services.notifier.config;

import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public abstract class NotifierConfigForLogBase
     		  extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// nothing specific
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigForLogBase(final AppCode appCode,
								  	   final boolean enabled,
								  	   final NotifierImpl impl) {
		super(appCode,
			  NotifierType.LOG,
			  enabled,
			  impl);
	}
	protected NotifierConfigForLogBase(final XMLPropertiesForAppComponent props) {
		super(NotifierType.LOG,
			  props);
	}
}
