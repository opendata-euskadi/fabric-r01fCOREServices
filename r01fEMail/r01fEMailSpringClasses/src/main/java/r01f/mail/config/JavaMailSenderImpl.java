package r01f.mail.config;

public enum JavaMailSenderImpl {
	SMTP,			// ie ms exchange
	AWS_SES,
	GOOGLE_API,
	GOOGLE_SMTP,
	THIRD_PARTY_MAIL_HTTPSERVICE;
}
