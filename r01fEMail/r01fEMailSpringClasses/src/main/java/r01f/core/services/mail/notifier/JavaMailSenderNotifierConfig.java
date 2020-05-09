package r01f.core.services.mail.notifier;

import r01f.config.ContainsConfigData;
import r01f.core.services.mail.config.JavaMailSenderConfig;
import r01f.core.services.mail.config.JavaMailSenderConfigBuilder;
import r01f.core.services.mail.config.JavaMailSenderImpl;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierServiceImplDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class JavaMailSenderNotifierConfig
     extends NotifierConfigForEMail {
	/**
	 * Loads the EMail Notifier Service config from properties
	 * @param props
	 * @return
	 */
	public JavaMailSenderNotifierConfig(final XMLPropertiesForAppComponent props,
										final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		super(props,
			  // builds the impl-dependent config
			  new NotifierServiceImplDependentConfigProviderFromProperties() {
						@Override
						public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																	 final XMLPropertiesForAppComponent props) {
							if (impl == null) throw new IllegalStateException("null mail sender impl! (review notifier properties)");
							
							JavaMailSenderImpl springMailSenderImpl = JavaMailSenderImpl.from(impl);
							JavaMailSenderConfig springMailSenderCfg = JavaMailSenderConfigBuilder.of(springMailSenderImpl)
																								  .from(props,
																										"notifier/email");
							return springMailSenderCfg;
						}
			   },
			   // the app-dependent config
			   appDepConfigProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isSelectedImpl() {
		return true;
	}
}
