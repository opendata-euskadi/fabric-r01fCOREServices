package r01f.cloud.nexmo.notifier;

import r01f.cloud.nexmo.NexmoConfig;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierConfigForVoice;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierServiceImplDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.VoiceNotifierImpl;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class NexmoNotifierConfig
	 extends NotifierConfigForVoice {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public NexmoNotifierConfig(final XMLPropertiesForAppComponent props,
								final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		super(props,
			  // builds the impl-dependent config
			  new NotifierServiceImplDependentConfigProviderFromProperties() {
					@Override
					public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																 final XMLPropertiesForAppComponent props) {
						if (impl == null) throw new IllegalStateException("null mail sender impl (review notifier properties)!");
						
						ContainsConfigData outCfg = null;
						if (VoiceNotifierImpl.TWILIO.is(impl)) {
							outCfg = NexmoConfig.createFrom(props,
															 "notifier/voice");
						}
						else {
							throw new IllegalStateException(impl + " is NOT a supported voice notifier");
						}
						return outCfg;
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
		return _impl.is(NotifierImpl.forId("nexmo"));
	}
}
