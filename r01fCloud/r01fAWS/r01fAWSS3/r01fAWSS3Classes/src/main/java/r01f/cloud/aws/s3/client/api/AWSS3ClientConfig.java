package r01f.cloud.aws.s3.client.api;

import java.nio.charset.Charset;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.AWSClientConfig;
import r01f.debug.Debuggable;
import r01f.httpclient.HttpClientProxySettings;
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
		// Common props (base props access key / secret key)
		return AWSS3ClientConfigBuilder.fromXMLProperties(props, propsRootNode);
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
