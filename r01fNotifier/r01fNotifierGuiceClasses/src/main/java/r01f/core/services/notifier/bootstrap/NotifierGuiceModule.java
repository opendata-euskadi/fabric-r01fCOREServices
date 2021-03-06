package r01f.core.services.notifier.bootstrap;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.google.inject.Binder;
import com.google.inject.Exposed;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceForPushMessage;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierServiceForVoicePhoneCall;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierConfigForVoice;
import r01f.core.services.notifier.config.NotifiersConfigs;
import r01f.core.services.notifier.spi.NotifierSPIProviderForEMail;
import r01f.core.services.notifier.spi.NotifierSPIProviderForPushMessage;
import r01f.core.services.notifier.spi.NotifierSPIProviderForSMS;
import r01f.core.services.notifier.spi.NotifierSPIProviderForVoice;


@Slf4j
@RequiredArgsConstructor
public class NotifierGuiceModule
  implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final NotifiersConfigs _notifiersConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		if (  _notifiersConfig == null ) {
			throw new IllegalStateException(" Cannot use NotifierGuiceModule  without providing a NotifiersConfigs ,"
											+ " check your properties.");
		}
				
		// Bind configs
		if (_notifiersConfig.getForLog() != null) {
			binder.bind(NotifierConfigForLog.class)
				  .toInstance(_notifiersConfig.getForLog());
		} else {
			log.warn("The notifier LOG config is null > any bind will be made");
		}
		if (_notifiersConfig.getForEMail() != null) {
			binder.bind(NotifierConfigForEMail.class)
				  .toInstance(_notifiersConfig.getForEMail());
		} else {
			log.warn("The notifier EMAIL config is null > any bind will be made");
		}
		if (_notifiersConfig.getForSMS() != null) {
			binder.bind(NotifierConfigForSMS.class)
				  .toInstance(_notifiersConfig.getForSMS());
		} else {
			log.warn("The notifier SMS config is null > any bind will be made");
		}
		if (_notifiersConfig.getForVoice() != null) {
			binder.bind(NotifierConfigForVoice.class)
				  .toInstance(_notifiersConfig.getForVoice());
		} else {
			log.warn("The notifier VOICE config is null > any bind will be made");
		}
		if (_notifiersConfig.getForPushMessage() != null) {
			binder.bind(NotifierConfigForPushMessage.class)
				  .toInstance(_notifiersConfig.getForPushMessage());
		} else {
			log.warn("The notifier Push Message config is null > any bind will be made");
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  NOTIFIER SERVICE PROVIDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Provides a {@link NotifierServiceForEMail} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton @Exposed	// creates a single instance of the java mail sender
	NotifierServiceForEMail _provideEMailNotifier() {
		if (_notifiersConfig.getForEMail() == null) throw new IllegalStateException("NO EMail notifier configured!");

		log.info("[Notifier]: SPI finding {} implementations",
				  NotifierSPIProviderForEMail.class);
		// BEWARE! there MUST exists a file named as the spi provider interface FQN at the META-INF folder
		//		   of every implementation project
		NotifierServiceForEMail outSrvc = null;
		for (Iterator<NotifierSPIProviderForEMail> pIt = ServiceLoader.load(NotifierSPIProviderForEMail.class).iterator(); pIt.hasNext(); ) {
			NotifierSPIProviderForEMail prov = pIt.next();

			outSrvc = prov.provideEMailNotifier(_notifiersConfig.getForEMail());
		}
		if (outSrvc == null) throw new IllegalStateException("Could NOT find any email notifier implementation!");
		return outSrvc;
	}
	/**
	 * Provides a {@link NotifierServiceForSMS} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton @Exposed	// creates a single instance of the java mail sender
	NotifierServiceForSMS _provideSMSNotifier() {
		if (_notifiersConfig.getForSMS() == null) throw new IllegalStateException("NO SMS notifier configured!");

		log.info("[Notifier]: SPI finding {} implementations",
				  NotifierSPIProviderForSMS.class);
		// BEWARE! there MUST exists a file named as the spi provider interface FQN at the META-INF folder
		//		   of every implementation project
		NotifierServiceForSMS outSrvc = null;
		for (Iterator<NotifierSPIProviderForSMS> pIt = ServiceLoader.load(NotifierSPIProviderForSMS.class).iterator(); pIt.hasNext(); ) {
			NotifierSPIProviderForSMS prov = pIt.next();

			if (_notifiersConfig.getForSMS().getImpl().is(prov.getImpl())) {
				log.info("\t...found impl={} (ENABLED)",
						 prov.getImpl());
				outSrvc = prov.provideSMSNotifier(_notifiersConfig.getForSMS());
			} else {
				log.info("\t...found impl={} (NOT ENABLED)",
						 prov.getImpl());
			}
		}
		if (outSrvc == null) throw new IllegalStateException("Could NOT find any SMS notifier implementation!");
		return outSrvc;
	}
	/**
	 * Provides a {@link NotifierServicesForVoice} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton @Exposed	// creates a single instance of the twilio service
	NotifierServiceForVoicePhoneCall _provideVoiceNotifier() {
		if (_notifiersConfig.getForVoice() == null) throw new IllegalStateException("NO Voice notifier configured!");

		log.info("[Notifier]: SPI finding {} implementations",
				  NotifierSPIProviderForVoice.class);
		// BEWARE! there MUST exists a file named as the spi provider interface FQN at the META-INF folder
		//		   of every implementation project
		NotifierServiceForVoicePhoneCall outSrvc = null;
		for (Iterator<NotifierSPIProviderForVoice> pIt = ServiceLoader.load(NotifierSPIProviderForVoice.class).iterator(); pIt.hasNext(); ) {
			NotifierSPIProviderForVoice prov = pIt.next();

			if (_notifiersConfig.getForVoice().getImpl().is(prov.getImpl())) {
				log.info("\t...found impl={} (ENABLED)",
						 prov.getImpl());
				outSrvc = prov.provideVoiceNotifier(_notifiersConfig.getForVoice());
			} else {
				log.info("\t...found impl={} (NOT ENABLED)",
						 prov.getImpl());
			}
		}
		if (outSrvc == null) throw new IllegalStateException("Could NOT find any Voice notifier implementation!");
		return outSrvc;
	}
	
	
	/**
	 * Provides a {@link NotifierServicesForPush} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton @Exposed	// creates a single instance of the push service (firebase)
	NotifierServiceForPushMessage _providePushNotifier() {
		if (_notifiersConfig.getForPushMessage() == null) throw new IllegalStateException("NO push notifier configured!");

		log.info("[Notifier]: SPI finding {} implementations",
				  NotifierSPIProviderForPushMessage.class);
		// BEWARE! there MUST exists a file named as the spi provider interface FQN at the META-INF folder
		//		   of every implementation project
		NotifierServiceForPushMessage outSrvc = null;
		for (Iterator<NotifierSPIProviderForPushMessage> pIt = ServiceLoader.load(NotifierSPIProviderForPushMessage.class).iterator(); pIt.hasNext(); ) {
			NotifierSPIProviderForPushMessage prov = pIt.next();

			if (_notifiersConfig.getForPushMessage().getImpl().is(prov.getImpl())) {
				log.info("\t...found impl={} (ENABLED)",
						 prov.getImpl());
				outSrvc = prov.providePushMessageNotifier(_notifiersConfig.getForPushMessage());
			} else {
				log.info("\t...found impl={} (NOT ENABLED)",
						 prov.getImpl());
			}
		}
		if (outSrvc == null) throw new IllegalStateException("Could NOT find any Voice notifier implementation!");
		return outSrvc;
	}
}
