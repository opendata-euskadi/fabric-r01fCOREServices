package r01f.cloud.aws;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.guids.CommonOIDs.Password;
import r01f.guids.CommonOIDs.UserCode;
import r01f.httpclient.HttpClientProxySettings;
import r01f.patterns.FactoryFrom;
import r01f.types.url.Host;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@NoArgsConstructor(access=AccessLevel.PACKAGE)
@Slf4j
public abstract class AWSClientHttpSettingsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final AWSClientHttpSettings fromXMLProperties(final XMLPropertiesForAppComponent props,
														        final String propsRootNode,final AWSService awsService) {
		// No HttpSettings.
		if (  ! props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings").exist() ) {
			return null;
		}
		// No ProxySettigs.
		boolean disableCertChecking = props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/@disableCertChecking").asBoolean(false);
		if ( ! props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings").exist() ) {
			return new AWSClientHttpSettings(disableCertChecking,null);
		}
		 //////////  ProxySettings
		boolean enabled = props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings/@enabled/").asBoolean(false);
		int port =        props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings/port").asInteger(80);

	    Host host =       props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings/host")
											.asObjectFromString(new FactoryFrom<String,Host>() {
												@Override
												public Host from(final String value) {
													return Host.from(value);
												}
										  },
								      null);
		 UserCode userCode =    props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings/user")
											.asObjectFromString(new FactoryFrom<String,UserCode>() {
												@Override
												public  UserCode from(final String value) {
													return UserCode.forId(value);
												}
										  },
								      null);

		 Password password =   props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/httpSettings/proxySettings/password")
										.asObjectFromString(new FactoryFrom<String,Password>() {
											@Override
											public  Password from(final String value) {
												return Password.forId(value);
											}
									  },
							      null);

		 HttpClientProxySettings proxye = new HttpClientProxySettings(host,port, userCode, password,enabled);
		 log.warn( "Proxy Info {}",proxye.debugInfo());
		// return
		 return new AWSClientHttpSettings(disableCertChecking,proxye);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
