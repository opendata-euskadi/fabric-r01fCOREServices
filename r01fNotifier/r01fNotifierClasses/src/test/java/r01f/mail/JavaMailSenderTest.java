package r01f.mail;

import javax.mail.MessagingException;

import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import r01f.cloud.google.GoogleAPI.GoogleAPIClientEMailAddress;
import r01f.cloud.google.GoogleAPI.GoogleAPIClientID;
import r01f.cloud.google.GoogleAPI.GoogleAPIClientP12KeyPath;
import r01f.cloud.google.GoogleAPI.GoogleAPIServiceAccountClientData;
import r01f.guids.CommonOIDs.AppCode;
import r01f.mail.model.EMailMessage;
import r01f.mail.model.EMailMessageAttachment;
import r01f.mail.model.EMailMessageBuilder;
import r01f.mail.model.EMailRFC822Address;
import r01f.mime.MimeType;
import r01f.types.contact.EMail;
import r01f.types.url.Host;

public class JavaMailSenderTest {
/////////////////////////////////////////////////////////////////////////////////////////
//	SMTP
/////////////////////////////////////////////////////////////////////////////////////////
	@Test
	public void testSMTP() throws MessagingException {
		JavaMailSender smtpJavaMailSender = JavaMailSenderSMTPImpl.create(Host.from("proxy2"));
		JavaMailSenders.using(smtpJavaMailSender)
					   .send(_createEMailMessage());
	}
	@Test
	public void testSMTPWithAttachments() throws MessagingException {
		JavaMailSender smtpJavaMailSender = JavaMailSenderSMTPImpl.create(Host.from("proxy2"));
		JavaMailSenders.using(smtpJavaMailSender)
					   .send(_createEMailMessage(),
							 new EMailMessageAttachment("test.txt",
				  										"Test".getBytes(),
				  										MimeType.TEXT_PLAIN));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	@Test
	public void testGMail() throws MessagingException {
		JavaMailSender smtpJavaMailSender = JavaMailSenderGmailImpl.create(_createGoogleApiServiceAccountClientData());
		JavaMailSenders.using(smtpJavaMailSender)
					   .send(_createEMailMessage());
	}
	@Test
	public void testGMailWithAttachments() throws MessagingException {
		JavaMailSender smtpJavaMailSender = JavaMailSenderGmailImpl.create(_createGoogleApiServiceAccountClientData());
		JavaMailSenders.using(smtpJavaMailSender)
					   .send(_createEMailMessage(),
							 new EMailMessageAttachment("test.txt",
				  										"Test".getBytes(),
				  										MimeType.TEXT_PLAIN));
	}
	private GoogleAPIServiceAccountClientData _createGoogleApiServiceAccountClientData() {
		return new GoogleAPIServiceAccountClientData(AppCode.forId("r01f"),
												  	 GoogleAPIClientID.of("327116756300-thcjqf1mvrn0geefnu6ef3pe2sm61i2q.apps.googleusercontent.com"),
													 GoogleAPIClientEMailAddress.of("327116756300-thcjqf1mvrn0geefnu6ef3pe2sm61i2q@developer.gserviceaccount.com"),
													 GoogleAPIClientP12KeyPath.loadedFromClassPath("r01f/mail/googleServiceAccount.p12"),
													 EMail.of("admin@futuretelematics.net"));
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
