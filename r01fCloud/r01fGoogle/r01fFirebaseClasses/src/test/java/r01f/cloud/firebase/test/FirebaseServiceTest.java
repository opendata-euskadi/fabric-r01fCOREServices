package r01f.cloud.firebase.test;

import org.junit.jupiter.api.Test;

import r01f.cloud.firebase.model.FirebaseIds.RegistredDevicesTopic;
import r01f.cloud.firebase.model.PushMessageRequest;
import r01f.cloud.firebase.service.FirebaseConfig;
import r01f.cloud.firebase.service.FirebaseService;
import r01f.cloud.firebase.service.FirebaseServiceImpl;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;

import r01f.xmlproperties.XMLPropertiesForApp;
import r01f.xmlproperties.XMLPropertiesForAppComponent;
import r01f.xmlproperties.XMLPropertiesBuilder;

public class FirebaseServiceTest {

	@Test
	public void doPushTest(){

		final XMLPropertiesForApp xmlProps = XMLPropertiesBuilder.createForApp(AppCode.forId("z99"))
																	.notUsingCache();
		AppComponent appComponent =  AppComponent.compose("push","service");

        XMLPropertiesForAppComponent propsForComponent = xmlProps.forComponent(appComponent);

		FirebaseService service =  new FirebaseServiceImpl(FirebaseConfig.createFrom(propsForComponent));
		PushMessageRequest pushMessageRequest = new PushMessageRequest(RegistredDevicesTopic.of("common"),
				                                                       "title",
				                                                       "message");
		service.push(pushMessageRequest);

	}

}
