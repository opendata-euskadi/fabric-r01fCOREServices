package r01f.cloud.aws.s3.model;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;


@Immutable
@NoArgsConstructor
public class AWSS3ObjectKey
	 extends OIDBaseMutable<String> {

	private static final long serialVersionUID = 4162366466990455545L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ObjectKey(final String id) {
		super(id);
	}
	public static AWSS3ObjectKey forId(final String idAsString) {
		return new AWSS3ObjectKey(idAsString);
	}
}