package r01f.brms.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import lombok.experimental.Accessors;


@Accessors(prefix="_")
public abstract class DroolsKIESessionServiceBase
  	implements  DroolsKIESessionService {		
////////////////////////////////////////////////////////////////
// MEMBERS
///////////////////////////////////////////////////////////////    
    protected final  KieServices _kieServices;
    protected final  DroolsKIEConfig _kieConfig;
////////////////////////////////////////////////////////////////
// CONSTRUCTOR
///////////////////////////////////////////////////////////////    
	public DroolsKIESessionServiceBase (final DroolsKIEConfig kieConfig) {		
		_kieServices = KieServices.Factory.get();
		_kieConfig = kieConfig;
	}	
	public DroolsKIESessionServiceBase (final DroolsKIEConfig kieConfig,
			                            final KieServices kieServices) {		
		_kieServices = kieServices;
		_kieConfig = kieConfig;
	}	
////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT
///////////////////////////////////////////////////////////////   
	public abstract  KieModule buildKieModule();
	public abstract  KieContainer buildKieContainer(final KieModule module);
////////////////////////////////////////////////////////////////
//  MAIN METHODS.
///////////////////////////////////////////////////////////////    
	@Override
	public KieSession getKieSession() {			
	    KieModule kieModule = buildKieModule();
	    KieContainer kContainer = buildKieContainer(kieModule);
        return kContainer.newKieSession();
	}		
////////////////////////////////////////////////////////////////
// PROTECTED METHODS
///////////////////////////////////////////////////////////////    
     protected void getKieRepository() {
        final KieRepository kieRepository = _kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
				                        @Override
										public ReleaseId getReleaseId() {
				                        			return kieRepository.getDefaultReleaseId();
          	 }
        });
	 }
}
