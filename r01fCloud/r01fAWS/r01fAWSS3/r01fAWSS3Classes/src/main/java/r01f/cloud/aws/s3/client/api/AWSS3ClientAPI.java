package r01f.cloud.aws.s3.client.api;

import static software.amazon.awssdk.http.SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.TlsTrustManagersProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
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

		System.out.println("....building s3clientConfig :" + config.debugInfo());

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

		// ... custom http settings
		if ( config.getHttpSettings() != null ) {
			ApacheHttpClient.Builder httpClientBuilder = ApacheHttpClient.builder();
			// Set attributes, from GLOBAL_HTTP_DEFAULTS.
			software.amazon.awssdk.utils.AttributeMap.Builder attributeMapBuilder =
					SdkHttpConfigurationOption.GLOBAL_HTTP_DEFAULTS.toBuilder(); // See https://github.com/aws/aws-sdk-java-v2/blob/master/http-clients/apache-client/src/main/java/software/amazon/awssdk/http/apache/ApacheHttpClient.java
			//Disable SSLCertChecking
			if (config.getHttpSettings().isDisableCertChecking()) {
				log.warn(" ...Disabling cert checking");
				System.out.println(" disabling cert checking");
				attributeMapBuilder.put(TRUST_ALL_CERTIFICATES, Boolean.TRUE);
			} else {
				System.out.println(" cert checking is not disabled...");
			}
			TlsTrustManagersProvider trust = new TlsTrustManagersProvider() {
				@Override
				public TrustManager[] trustManagers() {

					return trustAllTrustManager();
				}};
			httpClientBuilder.tlsTrustManagersProvider(trust);
			//httpClientBuilder.buildWithDefaults(attributeMapBuilder.build());



			// Check proxySettings.
			if ( config.getHttpSettings().getProxySettings() != null
					&& config.getHttpSettings().getProxySettings().isEnabled() ) {
				software.amazon.awssdk.http.apache.ProxyConfiguration.Builder proxyBuilder =
						ProxyConfiguration.builder()
										 .useSystemPropertyValues(false)
                                         .endpoint(URI.create(Strings.customized("http://{}:{}",
                                        		                                 config.getHttpSettings().getProxySettings().getProxyHost(),
                		                                                         config.getHttpSettings().getProxySettings().getProxyPort())));
	            if (config.getHttpSettings().getProxySettings().getUser() != null ) {
	            	proxyBuilder.username(config.getHttpSettings().getProxySettings().getUser().asString());
	            }
	            if (config.getHttpSettings().getProxySettings().getPassword() != null ) {
	            	 proxyBuilder.password(config.getHttpSettings().getProxySettings().getPassword().asString());
	            }
	            ProxyConfiguration proxyConfig = proxyBuilder.build();
	            log.warn(" \n \n Add proxy {}",proxyConfig);
	            httpClientBuilder.proxyConfiguration(proxyConfig);
			}
		    clientBuilder = clientBuilder.httpClient(httpClientBuilder.build());
		}

		S3Client client = clientBuilder.build();
		// now, yes...call to the builder build.

		return client;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private static TrustManager[] trustAllTrustManager() {
         return new TrustManager[] {
             new X509TrustManager() {
                 @Override
                 public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                     log.debug("Accepting a client certificate: " + x509Certificates[0].getSubjectDN());
                 }

                 @Override
                 public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                     log.debug("Accepting a client certificate: " + x509Certificates[0].getSubjectDN());
                 }

                 @Override
                 public X509Certificate[] getAcceptedIssuers() {
                     return new X509Certificate[0];
                 }
             }
         };
     }

}
