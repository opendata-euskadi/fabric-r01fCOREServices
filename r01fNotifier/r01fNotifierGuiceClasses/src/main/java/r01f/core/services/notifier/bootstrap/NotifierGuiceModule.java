package r01f.core.services.notifier.bootstrap;

import org.springframework.mail.javamail.JavaMailSender;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import lombok.RequiredArgsConstructor;
import r01f.cloud.aws.sns.AWSSNSClient;
import r01f.cloud.aws.sns.AWSSNSClientConfig;
import r01f.cloud.aws.sns.notifier.AWSSNSNotifierServices;
import r01f.cloud.twilio.TwilioConfig;
import r01f.cloud.twilio.TwilioService;
import r01f.cloud.twilio.TwilioServiceProvider;
import r01f.cloud.twilio.notifier.TwilioNotifierServices;
import r01f.core.services.mail.JavaMailSenderProvider;
import r01f.core.services.mail.config.JavaMailSenderConfig;
import r01f.core.services.mail.notifier.JavaMailSenderNotifierServices;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierServiceForVoicePhoneCall;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierConfigForVoice;
import r01f.core.services.notifier.config.NotifierEnums.SMSNotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.VoiceNotifierImpl;
import r01f.core.services.notifier.config.NotifiersConfigs;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.services.latinia.LatiniaService;
import r01f.services.latinia.LatiniaServiceAPIData;
import r01f.services.latinia.LatiniaServiceProvider;
import r01f.services.latinia.notifier.LatiniaNotifierServices;

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
		// Bind configs
		binder.bind(NotifierConfigForLog.class)
			  .toInstance(_notifiersConfig.getForLog());

		binder.bind(NotifierConfigForEMail.class)
			  .toInstance(_notifiersConfig.getForEmail());

		binder.bind(NotifierConfigForSMS.class)
			  .toInstance(_notifiersConfig.getForSMS());

		binder.bind(NotifierConfigForVoice.class)
			  .toInstance(_notifiersConfig.getForVoice());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  NOTIFIER SERVICE PROVIDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Provides a {@link NotifierServiceForEMail} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton	// creates a single instance of the java mail sender
	NotifierServiceForEMail _provideEMailNotifier() {
		// [1] - Get the config
		JavaMailSenderConfig springMailSenderCfg = _notifiersConfig.getForEmail()
																   .getConfigAs(JavaMailSenderConfig.class);
		// [2] - Build the spring mail sender
		JavaMailSenderProvider springMailSenderProvider = new JavaMailSenderProvider(springMailSenderCfg);
		JavaMailSender sprigMailSender = springMailSenderProvider.get();

		// [3] - Build the notifier service
		return new JavaMailSenderNotifierServices(sprigMailSender);
	}
	/**
	 * Provides a {@link NotifierServiceForSMS} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton	// creates a single instance of the java mail sender
	NotifierServiceForSMS _provideSMSNotifier(@ModelObjectsMarshaller final Marshaller marshaller) {
		NotifierServiceForSMS outSrvc = null;
		if (SMSNotifierImpl.AWS.is(_notifiersConfig.getForSMS().getImpl())) {
			// [1] - Get the aws sns service config
			AWSSNSClientConfig awsSNSCfg = _notifiersConfig.getForSMS().getConfigAs(AWSSNSClientConfig.class);
			// [2] - Create a aws sns client
			AWSSNSClient awsSNSCli = new AWSSNSClient(awsSNSCfg);

			// [3] - Build the notifier service
			outSrvc = new AWSSNSNotifierServices(awsSNSCli);
		}
		else if (SMSNotifierImpl.LATINIA.is(_notifiersConfig.getForSMS().getImpl())) {
			// [1] - Get the latinia service config
			LatiniaServiceAPIData apiData = _notifiersConfig.getForSMS().getConfigAs(LatiniaServiceAPIData.class);
			// [2] - Create a Latinia Service
			LatiniaServiceProvider latiniaServiceProvider = new LatiniaServiceProvider(apiData,
																			   		   marshaller);
			LatiniaService latiniaService = latiniaServiceProvider.get();

			// [3] - Build the notifier service
			outSrvc = new LatiniaNotifierServices(latiniaService);
		}
		else {
			throw new IllegalStateException(_notifiersConfig.getForSMS().getImpl() + " is NOT a supported SMS notifier");
		}
		return outSrvc;
	}
	/**
	 * Provides a {@link NotifierServicesForVoice} implementation
	 * @param props
	 * @return
	 */
	@Provides @Singleton	// creates a single instance of the twilio service
	NotifierServiceForVoicePhoneCall _provideVoiceNotifier() {
		NotifierServiceForVoicePhoneCall outSrvc = null;
		if (VoiceNotifierImpl.TWILIO.is(_notifiersConfig.getForVoice().getImpl())) {
			// [1] - get the twilio config
			TwilioConfig twCfg = _notifiersConfig.getForVoice().getConfigAs(TwilioConfig.class);
			// [2] - Create the twilio service
			TwilioServiceProvider twServiceProvider = new TwilioServiceProvider(twCfg);
			TwilioService twService = twServiceProvider.get();

			// [3] - Build the notifier service
			outSrvc = new TwilioNotifierServices(twService);
		}
		else {
			throw new IllegalStateException(_notifiersConfig.getForVoice().getImpl() + " is NOT a supported voice notifier");
		}
		return outSrvc;
	}
}
