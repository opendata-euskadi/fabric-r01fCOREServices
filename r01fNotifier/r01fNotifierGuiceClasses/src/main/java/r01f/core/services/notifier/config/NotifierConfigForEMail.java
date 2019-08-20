package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.core.services.mail.config.JavaMailSenderConfig;
import r01f.core.services.mail.config.JavaMailSenderConfigBuilder;
import r01f.core.services.mail.config.JavaMailSenderImpl;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.guids.CommonOIDs.AppCode;
import r01f.types.contact.EMail;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public class NotifierConfigForEMail
     extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final EMailRFC822Address _from;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifierConfigForEMail(final AppCode appCode,
								  final boolean enabled,
								  final NotifierImpl impl,final ContainsConfigData config,
								  final EMail fromMail,final String fromName) {
		super(appCode,
			  NotifierType.EMAIL,
			  enabled,
			  impl,config);
		_from = EMailRFC822Address.of(fromMail,fromName);
	}
	public NotifierConfigForEMail(final XMLPropertiesForAppComponent props) {
		this(props,
			 // builds the impl-dependent config
			 new NotifierImplDependentConfigProviderFromProperties() {
					@Override
					public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																 final XMLPropertiesForAppComponent props) {
						JavaMailSenderImpl springMailSenderImpl = JavaMailSenderImpl.from(impl);
						JavaMailSenderConfig springMailSenderCfg = JavaMailSenderConfigBuilder.of(springMailSenderImpl)
																							  .from(props,
																									"notifier/email");
						return springMailSenderCfg;
					}
			});
	}
	public NotifierConfigForEMail(final XMLPropertiesForAppComponent props,
							      final NotifierImplDependentConfigProviderFromProperties implDependentPropsProvider) {
		super(NotifierType.EMAIL,
			  props,
			  implDependentPropsProvider);
		EMail fromMail = props.propertyAt(_xPathBaseForCommonProperties() + "/from/@mail")
							  .asEMail("Zuzenean-No-Reply@euskadi.eus");
		String fromName = props.propertyAt(_xPathBaseForCommonProperties() + "/from")
							   .asString("Zuzenean");

		_from = EMailRFC822Address.of(fromMail,fromName);
	}
}
