package r01f.cloud.firebase.notifier;

import r01f.cloud.firebase.service.FirebaseConfig;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierAppDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierConfigProviders.NotifierServiceImplDependentConfigProviderFromProperties;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.PushMessageNotifierImpl;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class FirebaseNotifierConfig
	 extends NotifierConfigForPushMessage {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public FirebaseNotifierConfig(final XMLPropertiesForAppComponent props,
								  final NotifierAppDependentConfigProviderFromProperties appDepConfigProvider) {
		super(props,
			  // builds the impl-dependent config
			  new NotifierServiceImplDependentConfigProviderFromProperties() {
													@SuppressWarnings("hiding")
													@Override
													public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																								 final XMLPropertiesForAppComponent props) {
														if (impl == null) throw new IllegalStateException("null push message notifier impl! (review notifier properties)");
														
														ContainsConfigData outCfg = null;
														if (PushMessageNotifierImpl.FIREBASE.is(impl)) {
															outCfg = FirebaseConfig.createFrom(props,
																							  "notifier/push/firebase");
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
		return _impl.is(NotifierImpl.forId("firebase"));
	}
}
