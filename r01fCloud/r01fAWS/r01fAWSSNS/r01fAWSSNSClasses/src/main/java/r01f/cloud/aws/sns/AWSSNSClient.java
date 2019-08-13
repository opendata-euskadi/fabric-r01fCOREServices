package r01f.cloud.aws.sns;

import java.util.Map;

import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.types.contact.Phone;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;


public class AWSSNSClient {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SnsClient _snsClient;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSSNSClient(final AWSSNSClientConfig cfg) {
		_snsClient = SnsClient.builder()
						 .region(cfg.getRegion())
						 // Credentials: https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html
						 .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(cfg.getAccessKey().asString(),
								 																		  cfg.getAccessSecret().asString())))
						 .build();
	}
	AWSSNSClient(final Region region,
				 final AWSAccessKey accessKey,final AWSAccessSecret accessSecret) {
		this(new AWSSNSClientConfig(region,
									accessKey,accessSecret));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public PublishResponse sendSMS(final Phone phone,
								   final String message,
								   final Map<String, MessageAttributeValue> smsAttributes) {
		PublishRequest req = PublishRequest.builder()
										   .message(message)
										   .phoneNumber(phone.asString())
										   .messageAttributes(smsAttributes)
										   .build();
		PublishResponse res = _snsClient.publish(req);
		return res;
	}
}
