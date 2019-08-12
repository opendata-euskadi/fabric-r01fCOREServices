package r01f.mail;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import r01f.service.ServiceCanBeDisabled;

public class JavaMailSenderAWSSESImpl
  implements JavaMailSender,
  			 ServiceCanBeDisabled {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private boolean _disabled;

/////////////////////////////////////////////////////////////////////////////////////////
//	ServiceCanBeDisabled
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isEnabled() {
		return !_disabled;
	}
	@Override
	public boolean isDisabled() {
		return _disabled;
	}
	@Override
	public void setEnabled() {
		_disabled = false;
	}
	@Override
	public void setDisabled() {
		_disabled = true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void send(final SimpleMailMessage simpleMessage) throws MailException {
		// TODO Auto-generated method stub

	}
	@Override
	public void send(final SimpleMailMessage... simpleMessages) throws MailException {
		for (SimpleMailMessage s : simpleMessages ) {
			this.send(s);
		}
	}
	@Override
	public void send(final MimeMessage mimeMessage) throws MailException {
		// TODO Auto-generated method stub

	}
	@Override
	public void send(final MimeMessage... mimeMessages) throws MailException {
		// TODO Auto-generated method stub

	}
	@Override
	public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
		// TODO Auto-generated method stub

	}
	@Override
	public void send(final MimeMessagePreparator... mimeMessagePreparators) throws MailException {
		// TODO Auto-generated method stub

	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}
	@Override
	public MimeMessage createMimeMessage(final InputStream contentStream) throws MailException {
		return null;
	}
}
