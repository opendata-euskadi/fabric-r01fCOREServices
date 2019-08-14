package r01f.core.services.notifier;

import javax.mail.internet.MimeMessage;

import r01f.core.services.mail.model.EMailRFC822Address;

public interface NotifierServicesForEMail
  	     extends NotifierService<EMailRFC822Address,EMailRFC822Address,		// from & to
							 	 MimeMessage> {
	// just a marker interface
}