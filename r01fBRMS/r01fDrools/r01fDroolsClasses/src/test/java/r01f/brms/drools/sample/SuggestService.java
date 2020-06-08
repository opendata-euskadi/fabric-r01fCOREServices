package r01f.brms.drools.sample;


import org.kie.api.runtime.KieSession;

import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class SuggestService {

    private KieSession _kieSession;
    
/////////////////////////////////////////////////////////
// CONSTRUCTOR	
/////////////////////////////////////////////////////////	
    public SuggestService(final KieSession kieSession) {
    	_kieSession = kieSession;
    }    
//////////////////////////////////////////////////////////
    
 //////////////////////////////////////////////////////////  
    public OutboundSuggest suggest(final InboundInput  input){
    	OutboundSuggest suggest = new OutboundSuggest();
        _kieSession.insert(input);
        _kieSession.setGlobal("suggest",suggest);
        _kieSession.fireAllRules();
      
        return  suggest;

    }

}
