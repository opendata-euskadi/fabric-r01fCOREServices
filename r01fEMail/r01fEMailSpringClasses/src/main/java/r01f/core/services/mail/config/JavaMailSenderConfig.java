package r01f.core.services.mail.config;

import r01f.config.ContainsConfigData;

public interface JavaMailSenderConfig
		 extends ContainsConfigData {
	
	public JavaMailSenderImpl getImpl();
	public <C extends JavaMailSenderConfig> C as(final Class<C> type);
}
