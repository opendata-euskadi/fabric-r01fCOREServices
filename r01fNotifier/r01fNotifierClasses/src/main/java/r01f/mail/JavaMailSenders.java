package r01f.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.google.common.collect.FluentIterable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.notifier.email.EMailMessageBuilder;
import r01f.notifier.email.model.EMailMessage;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class JavaMailSenders {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static JavaMailSendersSendStep using(final JavaMailSender javaMailSender) {
		return new JavaMailSenders() { /* nothing */ }
					.new JavaMailSendersSendStep(javaMailSender);
	}
	@RequiredArgsConstructor
	public class JavaMailSendersSendStep {
		private final JavaMailSender _javaMailSender;

		public void send(final EMailMessage emailMsg) throws MessagingException {
			if (emailMsg.hasAttachments()) {
				// MimeMessage
				MimeMessage mimeMessage = EMailMessageBuilder.createMimeMessageFor(emailMsg)
															 .withDefaultCharset()
															 .usingDefaultSession()
															 .build();
				_javaMailSender.send(mimeMessage);
			} else {
				// Simple message
				SimpleMailMessage simpleMessage = _simpleMailMessageFrom(emailMsg);
				_javaMailSender.send(simpleMessage);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static SimpleMailMessage _simpleMailMessageFrom(final EMailMessage emailMsg) {
		SimpleMailMessage outEMailMsg = new SimpleMailMessage();
		outEMailMsg.setFrom(emailMsg.getFrom().asString());
		if (CollectionUtils.hasData(emailMsg.getTo())) outEMailMsg.setTo(FluentIterable.from(emailMsg.getTo())
																				.transform(EMail.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		if (CollectionUtils.hasData(emailMsg.getCc())) outEMailMsg.setCc(FluentIterable.from(emailMsg.getCc())
																				.transform(EMail.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		if (CollectionUtils.hasData(emailMsg.getBcc())) outEMailMsg.setCc(FluentIterable.from(emailMsg.getBcc())
																				.transform(EMail.TO_STRING_TRANSFORM)
																				.toArray(String.class));
		outEMailMsg.setSubject(emailMsg.getSubject());
		if (Strings.isNOTNullOrEmpty(emailMsg.getHtmlBody())) {
			outEMailMsg.setText(emailMsg.getHtmlBody());
		} else if (Strings.isNOTNullOrEmpty(emailMsg.getPlainTextBody())) {
			outEMailMsg.setText(emailMsg.getPlainTextBody());
		}
		return outEMailMsg;
	}
}
