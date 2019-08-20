package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.cloud.twilio.TwilioConfig;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.core.services.notifier.config.NotifierEnums.VoiceNotifierImpl;
import r01f.guids.CommonOIDs.AppCode;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public class NotifierConfigForVoice
     extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OwnedContactMean<Phone> _from;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifierConfigForVoice(final AppCode appCode,
								  final boolean enabled,
								  final NotifierImpl impl,final ContainsConfigData config,
								  final OwnedContactMean<Phone> from) {
		super(appCode,
			  NotifierType.VOICE,
			  enabled,
			  impl,config);
		_from = from;
	}
	public NotifierConfigForVoice(final XMLPropertiesForAppComponent props) {
		this(props,
			 // builds the impl-dependent config
			 new NotifierImplDependentConfigProviderFromProperties() {
					@Override
					public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																 final XMLPropertiesForAppComponent props) {
						ContainsConfigData outCfg = null;
						if (VoiceNotifierImpl.TWILIO.is(impl)) {
							outCfg = TwilioConfig.createFrom(props,
															 "notifier/voice");
						}
						else {
							throw new IllegalStateException(impl + " is NOT a supported voice notifier");
						}
						return outCfg;
					}
			 });
	}
	public NotifierConfigForVoice(final XMLPropertiesForAppComponent props,
								  final NotifierImplDependentConfigProviderFromProperties implDependentPropsProvider) {
		super(NotifierType.VOICE,
			  props,
			  implDependentPropsProvider);
		Phone fromPhone = props.propertyAt(_xPathBaseForCommonProperties() + "/from/@phone")
							   .asPhone("012");
		String fromOwner = props.propertyAt(_xPathBaseForCommonProperties() + "/from")
								.asString("Zuzenean");

		_from = new OwnedContactMean<Phone>(fromPhone,fromOwner);
	}
}
