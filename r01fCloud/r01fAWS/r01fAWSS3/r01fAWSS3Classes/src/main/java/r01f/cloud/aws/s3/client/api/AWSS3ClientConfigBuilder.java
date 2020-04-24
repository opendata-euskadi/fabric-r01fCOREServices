package r01f.cloud.aws.s3.client.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.cloud.aws.AWSClientConfig;
import r01f.cloud.aws.AWSClientConfigBuilder;
import r01f.cloud.aws.AWSService;
import r01f.patterns.FactoryFrom;
import r01f.types.url.Url;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

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
		 cientConfig.setEndPoint(endPoint);
		 return cientConfig;
	}
}
