package r01f.cloud.firebase.service;

import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.firebase.service.FirebaseServiceImpl.FirebaseAPIData;
import r01f.config.ContainsConfigData;
import r01f.guids.CommonOIDs.Environment;
import r01f.httpclient.HttpClientProxySettings;
import r01f.httpclient.HttpClientProxySettingsBuilder;
import r01f.io.util.StreamEncrypterDecrypter;
import r01f.types.Path;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesEnv;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Slf4j
@Accessors(prefix="_")
public class FirebaseConfig
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final FirebaseAPIData _apiData;
	@Getter private final HttpClientProxySettings _proxySettings;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public FirebaseConfig(final FirebaseAPIData apiData,
						  final HttpClientProxySettings proxySettings) {
		_proxySettings = proxySettings;
		_apiData = apiData;
	}
	public static FirebaseConfig createFrom(final XMLPropertiesForAppComponent xmlProps) {
		return createFrom(xmlProps,
						 "firebase");
	}
	public  static FirebaseConfig createFrom(final XMLPropertiesForAppComponent xmlProps,
										     final String propsRootNode) {
		// ensure the root node
		String thePropsRootNode = Strings.isNullOrEmpty(propsRootNode) ? "firebase" : propsRootNode;

		// Test proxy connection to see if proxy is needed
		HttpClientProxySettings proxySettings = null;
		try {
			proxySettings = HttpClientProxySettingsBuilder.guessProxySettings(xmlProps,
																			  thePropsRootNode);
		} catch (final Throwable th) {
			log.error("Error while guessing the proxy settings to use Twilio: {}",
					  th.getMessage(),th);
		}

		// Get the google credentials api info from the properties file
		FirebaseAPIData apiData = FirebaseConfig.apiDataFromProperties(xmlProps,
																       thePropsRootNode);

		// return the config
		return new FirebaseConfig(apiData,
								  proxySettings);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	 static FirebaseAPIData apiDataFromProperties(final XMLPropertiesForAppComponent props,
											      final String propsRootNode) {
		String credentialsPath = props.propertyAt(propsRootNode + "/credentials")
								 .asString();		
		String keyAsString = props.propertyAt(propsRootNode + "/credentials/@key")
				                    .asString();	
		log.warn("Loading Google Firebase Credentials from {}",
				                                             credentialsPath);
		GoogleCredentials googleCredentials;
		try {			
			InputStream  stream  = Thread.currentThread().getContextClassLoader().getResourceAsStream(credentialsPath);
			if (stream == null) {
				Environment env = XMLPropertiesEnv.guessEnvironmentFromSystemEnvProp();
				Path credentialPAthByEnv = Path.from( env.getId(),credentialsPath);
				log.warn("Try to read by env {} , as relative path {} " , env.getId(),  credentialPAthByEnv.asRelativeString());				
				stream  = Thread.currentThread().getContextClassLoader().getResourceAsStream(credentialPAthByEnv.asRelativeString());
			}
			
			if (stream == null) {
				throw new IllegalStateException(Strings.customized("Cannot find firebase credentials at {}", credentialsPath)) ;
			}
			if (keyAsString == null ){			
				googleCredentials = GoogleCredentials.fromStream(stream);			
			} else {				
				StreamEncrypterDecrypter encdec = new StreamEncrypterDecrypter(keyAsString.trim().toCharArray());
				googleCredentials = GoogleCredentials.fromStream(encdec.decrypt(stream));
			}			
			// Create the firebase service
			FirebaseAPIData apiData = new FirebaseAPIData(googleCredentials);
			return apiData;
		} catch (final IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getLocalizedMessage());
		}
	}
	 
///////////////////////////////////////////////////////////////////////////////////////////////////
	 
////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
