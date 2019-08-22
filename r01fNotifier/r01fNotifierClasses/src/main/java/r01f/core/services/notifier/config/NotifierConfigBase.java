package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.patterns.FactoryFrom;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
abstract class NotifierConfigBase
    implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final AppCode _appCode;
	@Getter protected final NotifierType _type;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigBase(final NotifierType type,
								 final AppCode appCode) {
		_type = type;
		_appCode = appCode;
	}
	protected NotifierConfigBase(final NotifierType type,
								 final XMLPropertiesForAppComponent props) {
		_type = type;
		_appCode = props.getAppCode();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected static NotifierImpl _implFrom(final NotifierType type,
										    final XMLPropertiesForAppComponent props) {
		return props.propertyAt(_xPathBase(type) + "/@impl")
			 	    .asOID(new FactoryFrom<String,NotifierImpl>() {
								@Override
								public NotifierImpl from(final String id) {
									return NotifierImpl.forId(id);
								}
			 	  		   });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected String _xPathBase() {
		return _xPathBase(_type);
	}
	protected static String _xPathBase(final NotifierType type) {
		return "/notifier/notifiers/" + type.asStringLowerCase();
	}

}
