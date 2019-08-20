package r01f.core.services.notifier.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class NotifiersConfigs
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final NotifierConfigForEMail _forEmail;
	@Getter private final NotifierConfigForSMS _forSMS;
	@Getter private final NotifierConfigForVoice _forVoice;
	@Getter private final NotifierConfigForLog _forLog;
}
