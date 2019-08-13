package r01f.cloud.aws.sns;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.sns.model.AWSSNSSmsMessageAttributesBuilder;
import r01f.cloud.aws.sns.model.AWSSNSSmsSenderID;
import r01f.cloud.aws.sns.model.AWSSNSSmsType;
import r01f.types.contact.Phone;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
public class AWSSNSTest {
	@Test
	public void testSendEMail() {
		AWSSNSClient snsCli = AWSSNSClientBuilder.region(Region.EU_WEST_1)
									.using(AWSAccessKey.forId("AKIASOHENLOJ3EVFIVG7"),
										   AWSAccessSecret.forId("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"))
									.build();
		PublishResponse res = snsCli.sendSMS(Phone.of("+34688671967"),
											 "OMG!!",
											 AWSSNSSmsMessageAttributesBuilder.forSender(AWSSNSSmsSenderID.forId("me"))
											 								  .noMaxPrice()
											 								  .usingSmsOfType(AWSSNSSmsType.PROMOTIONAL_NON_CRITICAL)
											 								  .build());
		log.warn("SMS message sent (id={}",res.messageId());
	}
}
