package r01f.brms.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;

import lombok.experimental.Accessors;


@Accessors(prefix="_")
public abstract class DroolsKIESessionServiceBase
  	implements  DroolsKIESessionService {	
    protected final  KieServices _kieServices;
////////////////////////////////////////////////////////////////
// CONSTRUCTOR
///////////////////////////////////////////////////////////////    
	public DroolsKIESessionServiceBase () {		
		_kieServices=KieServices.Factory.get();
	}
	
	public DroolsKIESessionServiceBase (final KieServices kieServices) {		
		_kieServices=KieServices.Factory.get();
	}	
////////////////////////////////////////////////////////////////
//CONSTRUCTOR
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
