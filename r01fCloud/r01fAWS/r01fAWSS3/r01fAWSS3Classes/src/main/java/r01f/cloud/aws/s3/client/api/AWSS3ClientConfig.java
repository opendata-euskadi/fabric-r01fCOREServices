package r01f.cloud.aws.s3.client.api;

import java.nio.charset.Charset;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.AWSClientConfig;
import r01f.cloud.aws.AWSClientConfigBuilder;
import r01f.cloud.aws.AWSService;
import r01f.debug.Debuggable;
import r01f.httpclient.HttpClientProxySettings;
import r01f.patterns.FactoryFrom;
import r01f.types.url.Url;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;
import software.amazon.awssdk.regions.Region;

@Accessors(prefix="_")
public class AWSS3ClientConfig
	 extends AWSClientConfig
	 implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter @Setter private Url _endPoint;
    @Getter @Setter private HttpClientProxySettings _proxySettings;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ClientConfig(final Region region,
						     final AWSAccessKey accessKey,final AWSAccessSecret accessSecret) {
		this(region,
			 accessKey,accessSecret,
			 Charset.defaultCharset());
	}
	public AWSS3ClientConfig(final Region region,
							 final AWSAccessKey accessKey,final AWSAccessSecret accessSecret,
							 final Charset charset) {
		this(region,
			 accessKey,accessSecret,
			 charset,
			 null);		// no system properties
	}
	public AWSS3ClientConfig(final Region region,
							 final AWSAccessKey accessKey,final AWSAccessSecret accessSecret,
							 final Charset charset,
							 final Properties systemProps) {
		super(region,
			  accessKey,accessSecret,
			  charset,systemProps);
	}
	public AWSS3ClientConfig(final AWSClientConfig cfg) {
		super(cfg.getRegion(),
			  cfg.getAccessKey(),cfg.getAccessSecret(),
			  cfg.getCharset());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final AWSS3ClientConfig fromXMLProperties(final XMLPropertiesForAppComponent props,
															final String propsRootNode) {
		// Common props (base props access key / secret kyey)
		AWSClientConfig baseCfg = AWSClientConfigBuilder.fromXMLProperties(props,
																    	   propsRootNode,AWSService.S3);


		AWSS3ClientConfig cientConfig =  new AWSS3ClientConfig(baseCfg);

        // Custom S3 props
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

	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
	    outDbgInfo.append("\n S3 Config :{ \n ").append(Strings.customized(" Endpoint {}",
	    																   _endPoint ));
	    outDbgInfo.append("\n").append(Strings.customized("secretKey {}",
	    	                                                                	_accessSecret  != null ? _accessSecret : " is null!" ));
	    outDbgInfo.append("\n").append(Strings.customized("accessKey {}",
	    												  _accessKey  != null ? _accessKey : " is null!" ));
	    outDbgInfo.append("\n").append(Strings.customized("proxyEnabled  {}",
	    												  _proxySettings  != null && _proxySettings.isEnabled() ? " yes!" : " no!" ));
		outDbgInfo.append(" } \n")	;
	    return outDbgInfo.toString();
	}
}
