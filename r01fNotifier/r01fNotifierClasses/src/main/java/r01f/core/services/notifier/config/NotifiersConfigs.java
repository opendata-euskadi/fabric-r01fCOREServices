package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.debug.Debuggable;
import r01f.util.types.Strings;

@Accessors(prefix="_")
public class NotifiersConfigs
  implements ContainsConfigData,
  			 Debuggable {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CharSequence debugInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Notifiers config:\n");
		sb.append(Strings.customized("\t\t-EMail: {}",_forEMail != null
															? _forEMail.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-  SMS: {}",_forSMS != null
															? _forSMS.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-Voice: {}",_forVoice != null
															? _forVoice.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-  Log: {}",_forLog != null
															? _forLog.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		return sb;
	}
}