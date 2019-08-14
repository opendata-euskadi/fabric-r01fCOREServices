package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.patterns.FactoryFrom;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
abstract class NotifierConfigBase
    implements NotifierConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final AppCode _appCode;
	@Getter protected final NotifierType _type;
	@Getter protected final boolean _enabled;
	@Getter protected final NotifierImpl _impl;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigBase(final AppCode appCode,
								 final NotifierType type,
								 final boolean enabled,
								 final NotifierImpl impl) {
		_appCode = appCode;
		_type = type;
		_enabled = enabled;
		_impl = impl;
	}
	protected NotifierConfigBase(final NotifierType type,
								 final XMLPropertiesForAppComponent props) {
		this(// appCode
			 props.getAppCode(),
			 // type
			 type,
			 // enabled
			 props.propertyAt(_xPathBase(type) + "/@enabled")
			 	  .asBoolean(true),
			 // impl
			 props.propertyAt(_xPathBase(type) + "/@impl")
			 	  .asOID(new FactoryFrom<String,NotifierImpl>() {
								@Override
								public NotifierImpl from(final String id) {
									return NotifierImpl.forId(id);
								}
			 	  		 }));
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
