package r01f.cloud.aws.s3.client.api;

import java.nio.charset.Charset;
import java.util.Properties;

import lombok.experimental.Accessors;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.AWSClientConfig;
import r01f.cloud.aws.AWSClientConfigBuilder;
import r01f.cloud.aws.AWSService;
import r01f.xmlproperties.XMLPropertiesForAppComponent;
import software.amazon.awssdk.regions.Region;

@Accessors(prefix="_")
public class AWSS3ClientConfig
	 extends AWSClientConfig {
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
			  charset);
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
		AWSClientConfig baseCfg = AWSClientConfigBuilder.fromXMLProperties(props,
																    	   propsRootNode,AWSService.SES);
		return new AWSS3ClientConfig(baseCfg);
	}
}
