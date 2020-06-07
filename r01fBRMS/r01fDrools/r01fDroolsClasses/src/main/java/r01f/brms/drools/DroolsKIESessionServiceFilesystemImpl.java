package r01f.brms.drools;

import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class DroolsKIESessionServiceFilesystemImpl
	extends DroolsKIESessionServiceBase
  	implements  DroolsKIESessionService {	
/////////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT	
////////////////////////////////////////////////////////////////////////////////
	@Override
	public KieSession getKieSession() {		
	     getKieRepository();
        KieFileSystem kieFileSystem = _kieServices.newKieFileSystem();

        kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/BackwardChaining.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/SuggestApplicant.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/Product_rules.xls"));
        KieBuilder kb = _kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();

        KieContainer kContainer = _kieServices.newKieContainer(kieModule.getReleaseId());

        return kContainer.newKieSession();
	}
/////////////////////////////////////////////////////////////////////////////////
// PRIVATE METHODS	
////////////////////////////////////////////////////////////////////////////////
	

}
