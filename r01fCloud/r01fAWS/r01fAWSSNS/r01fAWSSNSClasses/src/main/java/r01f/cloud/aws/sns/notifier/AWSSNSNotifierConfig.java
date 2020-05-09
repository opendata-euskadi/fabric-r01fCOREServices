package r01f.cloud.aws.sns.notifier;

import r01f.cloud.aws.sns.AWSSNSClientConfig;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierServiceImplDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.SMSNotifierImpl;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class AWSSNSNotifierConfig
	 extends NotifierConfigForSMS {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSSNSNotifierConfig(final XMLPropertiesForAppComponent props,
								final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		super(props,
		      // builds the service impl-dependent config
			  new NotifierServiceImplDependentConfigProviderFromProperties() {
					@Override
						public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																	 final XMLPropertiesForAppComponent props) {
							if (impl == null) throw new IllegalStateException("null push message notifier impl! (review notifier properties)");
							
							ContainsConfigData outCfg = null;
							if (SMSNotifierImpl.AWS.is(impl)) {
								outCfg = AWSSNSClientConfig.fromXMLProperties(props,
																		      "notifier/sms");
							}
							else {
								throw new IllegalStateException(impl + " is NOT a supported push notifier");
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
		return _impl.is(NotifierImpl.forId("aws"));
	}
}
