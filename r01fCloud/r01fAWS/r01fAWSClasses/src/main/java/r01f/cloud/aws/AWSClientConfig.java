package r01f.cloud.aws;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import software.amazon.awssdk.regions.Region;

@Accessors(prefix="_")
public class AWSClientConfig
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final Region _region;
	@Getter protected final AWSAccessKey _accessKey;
	@Getter protected final AWSAccessSecret _accessSecret;
	@Getter protected final Charset _charset;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSClientConfig(final Region region,
						   final AWSAccessKey accessKey,final AWSAccessSecret accessSecret,
						   final Charset charset) {
		_region = region;
		_accessKey = accessKey;
		_accessSecret = accessSecret;
		_charset = charset;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FROM PROPERTIES
/////////////////////////////////////////////////////////////////////////////////////////
	public static AWSClientConfigBuilder builder() {
		return new AWSClientConfigBuilder() { /* nothing */ };
	}
}
