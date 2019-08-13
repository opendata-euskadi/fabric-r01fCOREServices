package r01f.cloud.aws.ses;

import java.nio.charset.Charset;

import javax.mail.MessagingException;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.AWSAccessKey;
import r01f.cloud.aws.AWSAccessSecret;
import r01f.cloud.aws.AWSClientConfig;
import r01f.cloud.aws.AWSClientConfigBuilder;
import r01f.mail.model.EMailMessage;
import r01f.mail.model.EMailMessageAttachment;
import r01f.mail.model.EMailMessageBuilder;
import r01f.mail.model.EMailRFC822Address;
import r01f.mime.MimeType;
import r01f.types.contact.EMail;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SendRawEmailResponse;
import software.amazon.awssdk.services.ses.model.SesResponse;

@Slf4j
public class AWSSESTest {
	@Test
	public void testSendEMail() throws MessagingException {
		AWSClientConfig cfg = AWSClientConfigBuilder.region(Region.EU_WEST_1)
										.using(AWSAccessKey.forId("AKIASOHENLOJWOPU37CL"),
											   AWSAccessSecret.forId("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"))
										.charset(Charset.defaultCharset())
										.build();
		AWSSESClientConfig sesCfg = new AWSSESClientConfig(cfg);
		AWSSESClient sesCli = new AWSSESClient(sesCfg);
		EMailMessage msg = _createEMailMessage();
		SesResponse res = sesCli.sendEMail(msg);
		log.warn("EMail message sent (id={}",
				 ((SendEmailResponse)res).messageId());
	}
	@Test
	public void testSendEMailWithAttachment() throws MessagingException {
		AWSClientConfig cfg = AWSClientConfigBuilder.region(Region.EU_WEST_1)
										.using(AWSAccessKey.forId("AKIASOHENLOJWOPU37CL"),
											   AWSAccessSecret.forId("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"))
										.charset(Charset.defaultCharset())
										.build();
		AWSSESClientConfig sesCfg = new AWSSESClientConfig(cfg);
		AWSSESClient sesCli = new AWSSESClient(sesCfg);
		EMailMessage msg = _createEMailMessage();
		SesResponse res = sesCli.sendEMail(msg,
										   new EMailMessageAttachment("test.txt",
							  										  "Test".getBytes(),
							  										  MimeType.TEXT_PLAIN));
		log.warn("EMail with attachments message sent (id={}",
				 ((SendRawEmailResponse)res).messageId());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private EMailMessage _createEMailMessage() {
		return EMailMessageBuilder.from(EMailRFC822Address.of(EMail.of("me@futuretelematics.net"),
																	 "FutureTelematics"))
													    .to(EMailRFC822Address.of(EMail.of("a-lara@ejie.eus"),"Alex"))
													    .noCC().noBCC()
													    .withSubject("Test email")
													    .withPlainTextBody("Test")
													    .build();
	}
}
