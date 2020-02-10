package r01f.cloud.firebase.notifier;

import r01f.cloud.firebase.model.FirebaseIds.RegistredDeviceToken;
import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;
import r01f.cloud.firebase.service.FirebaseConfig;
import r01f.cloud.firebase.service.FirebaseServiceImpl;
import r01f.core.services.notifier.NotifierServiceForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.spi.NotifierSPIProviderForPushMessage;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * SPI provider for AWS SNS based push provider notifier
 * (see: https://www.baeldung.com/java-spi)
 * BEWARE!!	There MUST exist a file named as the FQN of the spi provider INTERFACE at META-INF folder
 * 			The content of this file must be the FQN of the spi provider interface IMPLEMENTATION
 */
public class SPIProviderForFirebaseNotifierService
  implements NotifierSPIProviderForPushMessage {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public NotifierServiceForPushMessage<RegistredDevicesTopic,RegistredDeviceToken>  providePushMessageNotifier(final NotifierConfigForPushMessage config) {
		// [1] - get the firebase config
		FirebaseConfig fcmCfg = config.getServiceImplConfigAs(FirebaseConfig.class);
		// [2] - Create the firebase service
		FirebaseServiceImpl twService = new FirebaseServiceImpl(fcmCfg);
		// [3] - Build the notifier service
		return new FirebaseNotifierService(twService);
	}
	@Override
	public NotifierImpl getImpl() {
		return NotifierImpl.forId("firebase");
	}
	@Override
	public NotifierConfigForPushMessage providePushMessageNotifierConfig(final XMLPropertiesForAppComponent props,
															             final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		return new FirebaseNotifierConfig(props,
										 appDepConfigProvider);
	}
}
