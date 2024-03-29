package r01f.core.services.notifier.spi;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierConfigForVoice;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * Discovers all notifier impls and return the enabled impl
 */
@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class NotifierSPIUtil {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static NotifierConfigForEMail emailNotifierConfigFrom(final XMLPropertiesForAppComponent props,
																 final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		log.warn("[Notifier] discovering email notifiers");

		// Use java's SPI to get the available configs
		final Collection<NotifierConfigForEMail> cfgs = Lists.newArrayList();
		ServiceLoader.load(NotifierSPIProviderForEMail.class)
			 .forEach(new Consumer<NotifierSPIProviderForEMail>() {
							@Override
							public void accept(final NotifierSPIProviderForEMail prov) {
								log.info("\t...found email notifier provided by {}",
										 prov.getClass());
								try {
									cfgs.add(prov.provideEMailNotifierConfig(props,
																			 appDepConfigProvider));
								} catch (IllegalStateException ise) {
									log.warn("\tEmail notifier provided by {} not accepted : {}",
											  prov.getClass(), ise.getMessage());
								}
							}
					  });
		// Get the config for the selected service impl
		NotifierConfigForEMail selectedImplConfig = FluentIterable.from(cfgs)
														.filter(new Predicate<NotifierConfigForEMail>() {
																		@Override
																		public boolean apply(final NotifierConfigForEMail cfg) {
																			return cfg.isSelectedImpl();
																		}
																})
														.first().orNull();
		if (selectedImplConfig == null) {
			throw new IllegalStateException("Could NOT find email config for selected impl (check there exists an email notifier dependency)!");
		}
		log.info("\t > The selected email notifier impl is {}",selectedImplConfig.getImpl());
		return selectedImplConfig;
	}
	public static NotifierConfigForSMS smsNotifierConfigFrom(final XMLPropertiesForAppComponent props,
															 final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		log.warn("[Notifier] discovering sms notifiers");

		// Use java's SPI to get the available configs
		final Collection<NotifierConfigForSMS> cfgs = Lists.newArrayList();
		ServiceLoader.load(NotifierSPIProviderForSMS.class)
			 .forEach(new Consumer<NotifierSPIProviderForSMS>() {
							@Override
							public void accept(final NotifierSPIProviderForSMS prov) {
								log.info("\t...found sms notifier provided by {}",
										 prov.getClass());
								try {
									cfgs.add(prov.provideSMSNotifierConfig(props,
																		   appDepConfigProvider));
								} catch (IllegalStateException ise) {
									log.warn("\tSMS notifier provided by {} not accepted : {}",
											 prov.getClass(), ise.getMessage());
								}
							}
					  });
		// Get the config for the selected service impl
		NotifierConfigForSMS selectedImplConfig = FluentIterable.from(cfgs)
														.filter(new Predicate<NotifierConfigForSMS>() {
																		@Override
																		public boolean apply(final NotifierConfigForSMS cfg) {
																			return cfg.isSelectedImpl();
																		}
																})
														.first().orNull();
		if (selectedImplConfig == null) {
			throw new IllegalStateException("Could NOT find sms config for selected impl (check there exists a sms notifier dependency)!");
		}
		log.info("\t > The selected sms notifier impl is {}",selectedImplConfig.getImpl());
		return selectedImplConfig;
	}
	public static NotifierConfigForVoice voiceNotifierConfigFrom(final XMLPropertiesForAppComponent props,
																 final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		log.warn("[Notifier] discovering voice notifiers");

		// Use java's SPI to get the available configs
		final Collection<NotifierConfigForVoice> cfgs = Lists.newArrayList();
		ServiceLoader.load(NotifierSPIProviderForVoice.class)
			 .forEach(new Consumer<NotifierSPIProviderForVoice>() {
							@Override
							public void accept(final NotifierSPIProviderForVoice prov) {
								log.info("\t...found voice notifier provided by {}",
										 prov.getClass());
								try {
									cfgs.add(prov.provideVoiceNotifierConfig(props,
																	     appDepConfigProvider));
								} catch (IllegalStateException ise) {
									log.warn("\tVoice notifier provided by {} not accepted : {}",
											 prov.getClass(), ise.getMessage());
								}
							}
					  });
		// Get the config for the selected service impl
		NotifierConfigForVoice selectedImplConfig = FluentIterable.from(cfgs)
														.filter(new Predicate<NotifierConfigForVoice>() {
																		@Override
																		public boolean apply(final NotifierConfigForVoice cfg) {
																			return cfg.isSelectedImpl();
																		}
																})
														.first().orNull();
		if (selectedImplConfig == null) {
			throw new IllegalStateException("Could NOT find voice call config for selected impl (check there exists a voice notifier dependency)!");
		}
		log.info("\t > The selected voice notifier impl is {}",selectedImplConfig.getImpl());
		return selectedImplConfig;
	}
	public static NotifierConfigForPushMessage pushMessageNotifierConfigFrom(final XMLPropertiesForAppComponent props,
																			 final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		log.warn("[Notifier] discovering push message notifiers");

		// Use java's SPI to get the available configs
		final Collection<NotifierConfigForPushMessage> cfgs = Lists.newArrayList();
		ServiceLoader.load(NotifierSPIProviderForPushMessage.class)
						.forEach(new Consumer<NotifierSPIProviderForPushMessage>() {
									@Override
									public void accept(final NotifierSPIProviderForPushMessage prov) {
										log.info("\t...found push message notifier provided by {}",
												 prov.getClass());
										try {
										cfgs.add(prov.providePushMessageNotifierConfig(props,
																					   appDepConfigProvider));
										} catch (IllegalStateException ise) {
											log.warn("\tPush notifier provided by {} not accepted : {}",
													 prov.getClass(), ise.getMessage());
										}
									}
						});
		// Get the config for the selected service impl
		NotifierConfigForPushMessage selectedImplConfig = FluentIterable.from(cfgs)
																		.filter(new Predicate<NotifierConfigForPushMessage>() {
																						@Override
																						public boolean apply(final NotifierConfigForPushMessage cfg) {
																							return cfg.isSelectedImpl();
																						}
																				})
																		.first().orNull();
		if (selectedImplConfig == null) {
			throw new IllegalStateException("Could NOT find push message config for selected impl (check there exists a push message notifier dependency)!");
		}
		log.info("\t > The selected push message notifier impl is {}",selectedImplConfig.getImpl());
		return selectedImplConfig;
	}
	
}
