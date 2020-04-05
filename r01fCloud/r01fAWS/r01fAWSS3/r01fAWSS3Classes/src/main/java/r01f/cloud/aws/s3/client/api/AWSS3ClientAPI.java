package r01f.cloud.aws.s3.client.api;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForBuckets;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForFiler;
import r01f.cloud.aws.s3.client.api.delegates.AWSS3ClientAPIDelegateForObjects;
import r01f.exceptions.Throwables;
import r01f.util.types.Strings;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.http.apache.ProxyConfiguration.Builder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;


/**
 * Base type for every API implementation of S3API
 */
@Slf4j
@Accessors(prefix="_")
public class AWSS3ClientAPI {
///////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
    private final S3Client _s3Client;
	 @SuppressWarnings("unused")
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

		// Checks if client config is not null AmazonS3Client
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of S3api, a client config, must be provided"));
		}
		_s3Client = _buildS3Client(config);
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
/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("resource")
	protected static S3Client _buildS3Client(final AWSS3ClientConfig config) {
		// Checks if client config is not null AmazonS3Client
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of S3api, a client config, must be provided"));
		}
		// Minimun data for builder ( credentials & Amazon Enpoint )
		 //..but don't call to build before checking the rest of parameters.
		S3ClientBuilder clientBuilder = S3Client.builder()
			                                   	 .region(config.getRegion())
		                                         .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(config.getAccessKey().asString(),
				                                                                                                                  config.getAccessSecret().asString())));

		// ...next check endpoint (use the just created clientbuilder)
		if ( config.getEndPoint() != null) {
			URI endPoint = null;
			try {
				endPoint = config.getEndPoint().asUrl().toURI();
			} catch (final MalformedURLException |  URISyntaxException e) {
				Throwables.throwUnchecked(new IllegalArgumentException(e.getLocalizedMessage()));
			}
			clientBuilder.endpointOverride(endPoint);
		}
		// ...next check proxysettings (use the just created clientbuilder)
		if ( config.getProxySettings() != null
				&& config.getProxySettings().isEnabled() ){
			Builder proxyBuilder = ProxyConfiguration.builder()
													 .useSystemPropertyValues(false)
			                                         .endpoint(URI.create(Strings.customized("http://{}:{}",
			                                        		                                 config.getProxySettings().getProxyHost(),
			                		                                                         config.getProxySettings().getProxyPort())));
            if (config.getProxySettings().getUser() != null ) {
            	proxyBuilder.username(config.getProxySettings().getUser().asString());
            }
            if (config.getProxySettings().getPassword() != null ) {
            	 proxyBuilder.password(config.getProxySettings().getPassword().asString());
            }
            ProxyConfiguration proxyConfig = proxyBuilder.build();
            log.warn(" \n \n Add proxy {}",proxyConfig);
            clientBuilder = clientBuilder.httpClient( ApacheHttpClient.builder()
					                                                   .proxyConfiguration(proxyConfig)
					                                                 .build());
		}
		// now, yes...call to the builder build.
		return clientBuilder.build();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
