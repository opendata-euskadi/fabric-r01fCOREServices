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
	@Getter private final NotifierConfigForPushMessage _forPushMessage;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS,
							final NotifierConfigForVoice forVoice,
							final NotifierConfigForLog forLog,
							final NotifierConfigForPushMessage forPushMessage) {
		_forEMail = forEmail;
		_forSMS = forSMS;
		_forVoice = forVoice;
		_forLog = forLog;
		_forPushMessage = forPushMessage;
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail) {
		this(forEmail,	// email
			 null,		// SMS
			 null,		// Voice
			 null,		// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForLog forLog) {
		this(forEmail,	// email
			 null,		// SMS
			 null,		// Voice
			 forLog,	// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForSMS forSMS) {
		this(null,		// email
			 forSMS,	// SMS
			 null,		// Voice
			 null,		// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForSMS forSMS,
							final NotifierConfigForLog forLog) {
		this(null,		// Email
			 forSMS,	// SMS
			 null,		// Voice
			 forLog,	// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS) {
		this( forEmail,	// Email
			 forSMS,	// SMS
			 null,		// Voice
			 null,		// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForEMail forEmail,
							final NotifierConfigForSMS forSMS,
							final NotifierConfigForLog forLog) {
		this( forEmail,	// Email
			 forSMS,	// SMS
			 null,		// Voice
			 forLog,	// log
			 null);		// push message
	}
	public NotifiersConfigs(final NotifierConfigForPushMessage forPushMessage) {
		this( null,	// Email
		null,		// SMS
		null,		// Voice
		null,		// log
		forPushMessage);	// push message
	}
	public NotifiersConfigs(final NotifierConfigForPushMessage forPushMessage,
			final NotifierConfigForLog forLog) {
		this( null,	// Email
		null,		// SMS
		null,		// Voice
		forLog,		// log
		forPushMessage);	// push message
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Notifiers config:\n");
		sb.append(Strings.customized("\t\t-      EMail: {}",_forEMail != null
															? _forEMail.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-        SMS: {}",_forSMS != null
															? _forSMS.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-      Voice: {}",_forVoice != null
															? _forVoice.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-        Log: {}",_forLog != null
															? _forLog.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		sb.append(Strings.customized("\t\t-PushMessage: {}",_forPushMessage != null
															? _forPushMessage.isEnabled() ? "ENABLED" : "DISABLED"
															: "NULL"));
		return sb;
	}
}