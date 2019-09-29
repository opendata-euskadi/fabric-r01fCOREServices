package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;

@Accessors(prefix="_")
public class NotifiersConfigs
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final NotifierConfigForEMail _forEMail;
	@Getter private final NotifierConfigForSMS _forSMS;
	@Getter private final NotifierConfigForVoice _forVoice;
	@Getter private final NotifierConfigForLog _forLog;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS,
							final NotifierConfigForVoice forVoice,
							final NotifierConfigForLog forLog) {
		_forEMail = forEmail;
		_forSMS = forSMS;
		_forVoice = forVoice;
		_forLog = forLog;
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail) {
		this(forEmail,	// email
			 null,		// SMS
			 null,		// Voice
			 null);		// log
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForLog forLog) {
		this(forEmail,	// email
			 null,		// SMS
			 null,		// Voice
			 forLog);	// log
	}
	public NotifiersConfigs(final NotifierConfigForSMS forSMS) {
		this(null,		// email
			 forSMS,	// SMS
			 null,		// Voice
			 null);		// log
	}
	public NotifiersConfigs(final NotifierConfigForSMS forSMS,
							final NotifierConfigForLog forLog) {
		this(null,		// Email
			 forSMS,	// SMS
			 null,		// Voice
			 forLog);	// log
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS) {
		this( forEmail,	// Email
			 forSMS,	// SMS
			 null,		// Voice
			 null);		// log
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS,
							final NotifierConfigForLog forLog) {
		this( forEmail,	// Email
			 forSMS,	// SMS
			 null,		// Voice
			 forLog);	// log
	}
}
