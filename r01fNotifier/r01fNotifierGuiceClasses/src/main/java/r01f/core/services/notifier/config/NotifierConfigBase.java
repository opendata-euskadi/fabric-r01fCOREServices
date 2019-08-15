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
    implements NotifierConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final AppCode _appCode;
	@Getter protected final NotifierType _type;
	@Getter protected final boolean _enabled;
	@Getter protected final NotifierImpl _impl;
	@Getter protected final ContainsConfigData _config;

	@SuppressWarnings("unchecked")
	public <C extends ContainsConfigData> C getConfigAs(final Class<C> configType) {
		return (C)_config;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected NotifierConfigBase(final AppCode appCode,
								 final NotifierType type,
								 final boolean enabled,
								 final NotifierImpl impl,final ContainsConfigData config) {
		_appCode = appCode;
		_type = type;
		_enabled = enabled;
		_impl = impl;
		_config = config;
	}
	protected NotifierConfigBase(final NotifierType type,
								 final XMLPropertiesForAppComponent props,
								 final NotifierImplDependentConfigProviderFromProperties implDependentPropsProvider) {
		this(// appCode
			 props.getAppCode(),
			 // type
			 type,
			 // enabled
			 props.propertyAt(_xPathBaseForCommonProperties(type) + "/@enabled")
			 	  .asBoolean(true),
			 // impl
			 _implFrom(type,props),
			 // notifier impl dependent config
			 implDependentPropsProvider != null ? implDependentPropsProvider.provideConfigUsing(_implFrom(type,props),props)
					 							: null);
	}
	private static NotifierImpl _implFrom(final NotifierType type,
										  final XMLPropertiesForAppComponent props) {
		return props.propertyAt(_xPathBaseForCommonProperties(type) + "/@impl")
			 	    .asOID(new FactoryFrom<String,NotifierImpl>() {
								@Override
								public NotifierImpl from(final String id) {
									return NotifierImpl.forId(id);
								}
			 	  		   });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Notifier config is divided into TWO different parts
//		- A 'common' part: contains common notifier properties like whether
//		  				   the notifier is enabled or the [from] address
//						   All these properties DO NOT depend upon the specific
//						   implementation of the notifier
//
//		- A notifier impl specific part that contains properties that depend upon the
//		  notifier implementation like api keys or whatever
/////////////////////////////////////////////////////////////////////////////////////////
	protected String _xPathBaseForCommonProperties() {
		return _xPathBaseForCommonProperties(_type);
	}
	protected static String _xPathBaseForCommonProperties(final NotifierType type) {
		return "/notifier/notifiers/" + type.asStringLowerCase();
	}
}
