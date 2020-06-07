package r01f.brms.drools;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.config.ContainsConfigData;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Slf4j
@Accessors(prefix="_")
public class DroolsKieConfig
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final DroolsAPIData _apiData;

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public DroolsKieConfig(final DroolsAPIData apiData) {
	
		_apiData = apiData;
	}
	public static DroolsKieConfig createFrom(final XMLPropertiesForAppComponent xmlProps) {
		return DroolsKieConfig.createFrom(xmlProps,
									     "nexmo");
	}
	public static DroolsKieConfig createFrom(final XMLPropertiesForAppComponent xmlProps,
										     final String propsRootNode) {
		// ensure the root node
		String thePropsRootNode = Strings.isNullOrEmpty(propsRootNode) ? "twilio" : propsRootNode;

		
		// Get the twilio api info from the properties file
		DroolsAPIData apiData = DroolsKieConfig.apiDataFromProperties(xmlProps,
																   thePropsRootNode);

		// return the config
		return new DroolsKieConfig(apiData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	static DroolsAPIData apiDataFromProperties(final XMLPropertiesForAppComponent props,
											   final String propsRootNode) {
		
		return null;
	}
}
