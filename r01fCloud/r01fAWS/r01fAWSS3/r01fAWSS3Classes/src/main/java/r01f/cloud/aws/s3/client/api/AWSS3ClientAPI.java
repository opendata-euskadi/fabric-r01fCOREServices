package r01f.cloud.aws.s3.client.api;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForBuckets;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForFiler;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForObjects;
import r01f.exceptions.Throwables;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


/**
 * Base type for every API implementation of S3API
 */
@Accessors(prefix="_")
public class AWSS3ClientAPI {
///////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	 private final S3Client _s3Client;
	 private final Charset _charset;
	 
	/**
	 * BUCKETS SUB-APIs (created at the constructor)
	 */
	@Getter private final AWSS3ClientAPIDelegateForBuckets _forBuckets;
	/**
	 * S3 OBJECTS SUB-APIs (created at the constructor)
	 */
	@Getter private final AWSS3ClientAPIDelegateForObjects _forObjects;
	/**
	 * S3 FOLDER FILER SUB-APIs (created at the constructor)
	 */
	@Getter private final AWSS3ClientAPIDelegateForFiler  _forFiler;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private AWSS3ClientAPI(final Region region,	// ie: Region.EU_WEST_1
				 final AWSAccessKey accessKey,final AWSAccessSecret accessSecret,
				 final Charset charset) {
		this(new AWSS3ClientConfig(region,
								   accessKey,accessSecret,
								   charset));
	 }
	@SuppressWarnings("null")
	public AWSS3ClientAPI(final AWSS3ClientConfig config) {
		// Checks if client config is not null		
		if (config == null) Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of S3api, a client config, must be provided"));
		
		_s3Client = S3Client.builder()
							 .region(config.getRegion())
							 // Credentials: https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html
							 .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(config.getAccessKey().asString(),
									 																		  config.getAccessSecret().asString())))
							 .build();
		_charset = config.getCharset();
		
		// build every sub-api
		_forBuckets = new AWSS3ClientAPIDelegateForBuckets(_s3Client);
		_forObjects  = new AWSS3ClientAPIDelegateForObjects (_s3Client);
		_forFiler = new AWSS3ClientAPIDelegateForFiler(_s3Client);
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//  SUB-APIs
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ClientAPIDelegateForBuckets forBuckets() {
		return _forBuckets;
	}
	public AWSS3ClientAPIDelegateForObjects forObjects() {
		return _forObjects;
	}
	public AWSS3ClientAPIDelegateForFiler forFiler() {
		return _forFiler;
	}
}
