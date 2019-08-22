package r01f.core.services.notifier.spi;

import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * spi provider for notifier services
 * (see: https://www.baeldung.com/java-spi)
 * BEWARE!!	There MUST exist a file named as the FQN of the spi provider INTERFACE at META-INF folder
 * 			of every concrete implementation
 * 			The content of this file must be the FQN of the spi provider interface IMPLEMENTATION
 */
public interface NotifierSPIProviderForEMail {
	NotifierServiceForEMail provideEMailNotifier(final NotifierConfigForEMail config);
	NotifierConfigForEMail provideEMailNotifierConfig(final XMLPropertiesForAppComponent props,
												      final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider);
}