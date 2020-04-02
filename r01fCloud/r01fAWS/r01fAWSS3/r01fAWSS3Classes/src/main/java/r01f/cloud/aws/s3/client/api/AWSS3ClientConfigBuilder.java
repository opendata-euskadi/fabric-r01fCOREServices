package r01f.cloud.aws.s3.client.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.AWSClientConfig;
import r01f.cloud.aws.AWSClientConfigBuilder;
import r01f.cloud.aws.AWSService;
import r01f.guids.CommonOIDs.Password;
import r01f.guids.CommonOIDs.UserCode;
import r01f.httpclient.HttpClientProxySettings;
import r01f.patterns.FactoryFrom;
import r01f.types.url.Host;
import r01f.types.url.Url;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PACKAGE)
public abstract class AWSS3ClientConfigBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final AWSS3ClientConfig fromXMLProperties(final XMLPropertiesForAppComponent props,
														    final String propsRootNode) {
		// Common  aws props (such as access key, secret key, etc..)
		AWSClientConfig baseCfg = AWSClientConfigBuilder.fromXMLProperties(props,
																	     propsRootNode,AWSService.S3);
		AWSS3ClientConfig cientConfig =  new AWSS3ClientConfig(baseCfg);
	    // Custom S3 props  ( a endpoint is required ! )
		Url endPoint = props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/endPoint")
								.asObjectFromString(new FactoryFrom<String,Url>() {
													@Override
													public Url from(final String uriAsString) {
														return Url.from(uriAsString);
													}
											  },
						  			      null);

		 boolean enabled = props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/proxySettings/@enabled/").asBoolean(false);
		 int port =        props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/proxySettings/port").asInteger(80);

		 Host host =       props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/proxySettings/host")
											.asObjectFromString(new FactoryFrom<String,Host>() {
												@Override
												public Host from(final String value) {
													return Host.from(value);
												}
										  },
								      null);
		 UserCode userCode =    props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/proxySettings/user")
											.asObjectFromString(new FactoryFrom<String,UserCode>() {
												@Override
												public  UserCode from(final String value) {
													return UserCode.forId(value);
												}
										  },
								      null);

		 Password password =   props.propertyAt(propsRootNode + "/aws/" + AWSService.S3.nameLowerCase() + "/proxySettings/password")
										.asObjectFromString(new FactoryFrom<String,Password>() {
											@Override
											public  Password from(final String value) {
												return Password.forId(value);
											}
									  },
							      null);

		 HttpClientProxySettings proxye = new HttpClientProxySettings(host,port, userCode, password,enabled);
		 log.warn( "Proxy Info {}",proxye.debugInfo());
		 cientConfig.setProxySettings(proxye);
		 cientConfig.setEndPoint(endPoint);
		 return cientConfig;
	}
}
