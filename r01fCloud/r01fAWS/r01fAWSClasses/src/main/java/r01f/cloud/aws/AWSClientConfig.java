package r01f.cloud.aws;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import software.amazon.awssdk.regions.Region;

@Accessors(prefix="_")
public class AWSClientConfig {
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
