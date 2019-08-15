package r01f.core.services.notifier.config;

import r01f.config.ContainsConfigData;
import r01f.core.services.notifier.config.NotifierEnums.NotifierImpl;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 *	Notifier config is divided into TWO different parts
 *		- A 'common' part: contains common notifier properties like whether
 *		  				   the notifier is enabled or the [from] address
 *						   All these properties DO NOT depend upon the specific
 *						   implementation of the notifier
 *
 *		- A notifier impl specific part that contains properties that depend upon the
 *		  notifier implementation like api keys or whatever
 *
 * This provider provides the config for the impl-dependent part
 */
/////////////////////////////////////////////////////////////////////////////////////////
public interface NotifierImplDependentConfigProviderFromProperties {
	public ContainsConfigData provideConfigUsing(final NotifierImpl impl,
												 final XMLPropertiesForAppComponent props);
}
