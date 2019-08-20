package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.cloud.aws.sns.AWSSNSClientConfig;
import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.core.services.notifier.config.NotifierEnums.SMSNotifierImpl;
import r01f.guids.CommonOIDs.AppCode;
import r01f.services.latinia.LatiniaServiceAPIData;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Accessors(prefix="_")
public class NotifierConfigForSMS
     extends NotifierConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OwnedContactMean<Phone> _from;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifierConfigForSMS(final AppCode appCode,
								final boolean enabled,
								final NotifierImpl impl,final ContainsConfigData config,
								final OwnedContactMean<Phone> from) {
		super(appCode,
			  NotifierType.SMS,
			  enabled,
			  impl,config);
		_from = from;
	}
	public NotifierConfigForSMS(final XMLPropertiesForAppComponent props) {
		this(props,
			 // builds the impl-dependent config
			 new NotifierImplDependentConfigProviderFromProperties() {
					@Override
					public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
																 final XMLPropertiesForAppComponent props) {
						ContainsConfigData outCfg = null;
						if (SMSNotifierImpl.AWS.is(impl)) {
							outCfg = AWSSNSClientConfig.fromXMLProperties(props,
																		  "notifier/sms");
						}
						else if (SMSNotifierImpl.LATINIA.is(impl)) {
							outCfg = LatiniaServiceAPIData.createFrom(props,
																	  "notifier/sms");
						}
						else {
							throw new IllegalStateException(impl + " is NOT a supported SMS notifier");
						}
						return outCfg;
					}
			 });
	}
	public NotifierConfigForSMS(final XMLPropertiesForAppComponent props,
								final NotifierImplDependentConfigProviderFromProperties implDependentPropsProvider) {
		super(NotifierType.SMS,
			  props,
			  implDependentPropsProvider);
		Phone fromPhone = props.propertyAt(_xPathBaseForCommonProperties() + "/from/@phone")
							   .asPhone("012");
		String fromOwner = props.propertyAt(_xPathBaseForCommonProperties() + "/from")
								.asString("Zuzenean");

		_from = new OwnedContactMean<Phone>(fromPhone,fromOwner);
	}
}
