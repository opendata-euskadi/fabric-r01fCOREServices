package r01f.core.services.notifier;

import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;


public interface NotifierServicesForVoicePhoneCall
         extends NotifierService<OwnedContactMean<Phone>,Phone,		// from & to
							 	 String>  {
	// just a marker interface
}
