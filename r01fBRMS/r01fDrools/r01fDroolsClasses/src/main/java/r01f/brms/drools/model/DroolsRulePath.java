package r01f.brms.drools.model;

import java.util.Collection;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.IsPath;
import r01f.types.Path;

@MarshallType(as="rule")
@Accessors(prefix="_")
@NoArgsConstructor
public class DroolsRulePath	
	extends Path
	   implements  IsPath {
 private static final long serialVersionUID = 5972916340798490423L;
	//////////////////////////////////////////////////////////////////////////////////
	//CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////	
	public DroolsRulePath(final String path) {
		super(path);
	}
	public DroolsRulePath(final String... path) {
		super(path);
	}
	public DroolsRulePath(final IsPath path) {
		super(path);
	}
	public DroolsRulePath(final Collection<String> elements) {
		super(elements);
	}
}