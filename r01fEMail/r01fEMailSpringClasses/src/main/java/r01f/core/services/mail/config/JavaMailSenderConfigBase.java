package r01f.core.services.mail.config;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
abstract class JavaMailSenderConfigBase 
    implements JavaMailSenderConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS  
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final JavaMailSenderImpl _impl;
	@Getter protected final boolean _disabled;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER  
/////////////////////////////////////////////////////////////////////////////////////////
	public JavaMailSenderConfigBase(final JavaMailSenderImpl impl,
									final boolean disabled) {
		_impl = impl;
		_disabled = disabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	  
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isEnabled() {
		return !_disabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	  
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <C extends JavaMailSenderConfig> C as(final Class<C> type) {
		return (C)this;
	}
}