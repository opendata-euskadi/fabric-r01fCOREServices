package r01f.core.services.notifier.bootstrap;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.inject.Provider;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Singleton;
import com.google.inject.util.Providers;

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
public class NotifierGuiceModule
  implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final NotifiersConfigs _notifiersConfig;
	protected final boolean _installedInsidePrivateModule;	// set to true if being binded inside a PrivateModule and want to expose bindings
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	protected NotifierGuiceModule(final NotifiersConfigs notifierConfigs,
								  final boolean installedInsidePrivateModule) {
		_notifiersConfig = notifierConfigs;
		_installedInsidePrivateModule = installedInsidePrivateModule;
	}
	public static NotifierGuiceModule createUsing(final NotifiersConfigs notifierConfigs) {
		return new NotifierGuiceModule(notifierConfigs,
									   false);
	}
	public static NotifierGuiceModule createToBeInstalledInPrivateModuleUsing(final NotifiersConfigs notifierConfigs) {
		return new NotifierGuiceModule(notifierConfigs,
									   true);
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		if (_notifiersConfig == null) throw new IllegalStateException(" Cannot use NotifierGuiceModule  without providing a NotifiersConfigs, check your properties.");

		// Bind notifier services
		_bindEMailNotifierServices(binder);
		_bindSMSNotifierServices(binder);
		_bindVoiceNotifierServices(binder);
		_bindPushNotifierServices(binder);
		

		// Log
		if (_notifiersConfig.getForLog() != null) {
			binder.bind(NotifierConfigForLog.class)
				  .toInstance(_notifiersConfig.getForLog());
		} else {
			binder.bind(NotifierConfigForLog.class)
				  .toProvider(Providers.<NotifierConfigForLog>of(null));
			log.warn("The notifier LOG config is null > any bind will be made");
		}
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierConfigForLog.class);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Binds the [notifier] and it's [config]
	 * Note that an alternate implementation is:
	 *		@Provides @Singleton @Exposed	
	 *		NotifierServiceForXXX _provideXXXNotifier() {
	 *			return _createNotifierServiceForXXX();
	 *		} 
	 * @return
	 */
	protected void _bindEMailNotifierServices(final Binder binder) {
		if (_notifiersConfig.getForEMail() != null) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForEMail.class)
				  .toInstance(_notifiersConfig.getForEMail());
			// [2] - Bind notifier
			binder.bind(NotifierServiceForEMail.class)
				  .toProvider(new Provider<NotifierServiceForEMail>() {
									@Override
									public NotifierServiceForEMail get() {
										return _createEmailNotifier();
									}
				  			  })
				  .in(Singleton.class);
		} else {
			log.warn("The notifier EMAIL config is null > any bind will be made");
			binder.bind(NotifierConfigForEMail.class)
				  .toProvider(Providers.<NotifierConfigForEMail>of(null));
			
			binder.bind(NotifierServiceForEMail.class)
				  .toProvider(Providers.<NotifierServiceForEMail>of(null));
		}
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierConfigForEMail.class);
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierServiceForEMail.class);
	}
	protected NotifierServiceForEMail _createEmailNotifier() {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	SMS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Binds the [notifier] and it's [config]
	 * Note that an alternate implementation is:
	 *		@Provides @Singleton @Exposed	
	 *		NotifierServiceForXXX _provideXXXNotifier() {
	 *			return _createNotifierServiceForXXX();
	 *		} 
	 * @return
	 */
	protected void _bindSMSNotifierServices(final Binder binder) {
		if (_notifiersConfig.getForSMS() != null) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForSMS.class)
				  .toInstance(_notifiersConfig.getForSMS());
			// [2] - Bind notifier
			binder.bind(NotifierServiceForSMS.class)
				  .toProvider(new Provider<NotifierServiceForSMS>() {
									@Override
									public NotifierServiceForSMS get() {
										return _createSMSNotifier();
									}
				  			  })
				  .in(Singleton.class);
		} else {
			log.warn("The notifier SMS config is null > any bind will be made");
			binder.bind(NotifierConfigForSMS.class)
				  .toProvider(Providers.<NotifierConfigForSMS>of(null));
			
			binder.bind(NotifierServiceForSMS.class)
				  .toProvider(Providers.<NotifierServiceForSMS>of(null));
		}
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierConfigForSMS.class);
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierServiceForEMail.class);
	}	
	protected NotifierServiceForSMS _createSMSNotifier() {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	VOICE
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Binds the [notifier] and it's [config]
	 * Note that an alternate implementation is:
	 *		@Provides @Singleton @Exposed	
	 *		NotifierServiceForXXX _provideXXXNotifier() {
	 *			return _createNotifierServiceForXXX();
	 *		} 
	 * @return
	 */
	protected void _bindVoiceNotifierServices(final Binder binder) {
		if (_notifiersConfig.getForVoice() != null) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForVoice.class)
				  .toInstance(_notifiersConfig.getForVoice());
			// [2] - Bind notifier
			binder.bind(NotifierServiceForVoicePhoneCall.class)
				  .toProvider(new Provider<NotifierServiceForVoicePhoneCall>() {
									@Override
									public NotifierServiceForVoicePhoneCall get() {
										return _createVoiceNotifier();
									}
				  			  })
				  .in(Singleton.class);
		} else {
			log.warn("The notifier VOICE config is null > any bind will be made");
			binder.bind(NotifierConfigForVoice.class)
				  .toProvider(Providers.<NotifierConfigForVoice>of(null));
			
			binder.bind(NotifierServiceForVoicePhoneCall.class)
				  .toProvider(Providers.<NotifierServiceForVoicePhoneCall>of(null));
		}
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierConfigForVoice.class);
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierServiceForEMail.class);
	}	
	protected NotifierServiceForVoicePhoneCall _createVoiceNotifier() {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	PUSH
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Binds the [notifier] and it's [config]
	 * Note that an alternate implementation is:
	 *		@Provides @Singleton @Exposed	
	 *		NotifierServiceForXXX _provideXXXNotifier() {
	 *			return _createNotifierServiceForXXX();
	 *		} 
	 * @return
	 */
	protected void _bindPushNotifierServices(final Binder binder) {
		if (_notifiersConfig.getForPushMessage() != null) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForPushMessage.class)
				  .toInstance(_notifiersConfig.getForPushMessage());
			// [2] - Bind notifier
			binder.bind(NotifierServiceForPushMessage.class)
				  .toProvider(new Provider<NotifierServiceForPushMessage>() {
									@Override
									public NotifierServiceForPushMessage get() {
										return _createPushNotifier();
									}
				  			  })
				  .in(Singleton.class);
		} else {
			log.warn("The notifier Push Message config is null > any bind will be made");
			binder.bind(NotifierConfigForPushMessage.class)
				  .toProvider(Providers.<NotifierConfigForPushMessage>of(null));
			
			binder.bind(NotifierServiceForPushMessage.class)
				  .toProvider(Providers.<NotifierServiceForPushMessage>of(null));
		}
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierConfigForPushMessage.class);
		if (_installedInsidePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(NotifierServiceForEMail.class);
	}	
	protected NotifierServiceForPushMessage _createPushNotifier() {
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
