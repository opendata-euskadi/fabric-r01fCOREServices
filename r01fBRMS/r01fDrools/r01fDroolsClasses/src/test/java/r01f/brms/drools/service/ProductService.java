package r01f.brms.drools.service;


import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import lombok.experimental.Accessors;
import r01f.brms.drools.entities.Product;

@Accessors(prefix="_")
public class ProductService {

    private KieSession _kieSession;//=new DroolsBeanFactory().getKieSession();
    
/////////////////////////////////////////////////////////
// CONSTRUCTOR	
/////////////////////////////////////////////////////////	
    public ProductService(final KieSession kieSession) {
    	_kieSession = kieSession;
    }    
//////////////////////////////////////////////////////////
    
 //////////////////////////////////////////////////////////  
    public Product applyLabelToProduct(Product product){
        _kieSession.insert(product);
        _kieSession.fireAllRules();
        System.out.println(product.getLabel());
        return  product;

    }

}
