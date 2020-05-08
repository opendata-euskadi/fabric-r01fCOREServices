package r01f.cloud.aws.s3.model;

import r01f.enums.EnumExtended;
import r01f.enums.EnumExtendedWrapper;

public enum AWSS3RequestedOperation
 implements EnumExtended<AWSS3RequestedOperation> {
	HEAD,
	GET,
	PUT,
	DELETE;

/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private static final transient EnumExtendedWrapper<AWSS3RequestedOperation> DELEGATE = EnumExtendedWrapper.wrapEnumExtended(AWSS3RequestedOperation.class);

	@Override
	public boolean isIn(final AWSS3RequestedOperation... els) {
		return DELEGATE.isIn(this,els);
	}
	public boolean isNOTIn(final AWSS3RequestedOperation... els) {
		return DELEGATE.isNOTIn(this,els);
	}
	@Override
	public boolean is(final AWSS3RequestedOperation el) {
		return DELEGATE.is(this,el);
	}
	public boolean isNOT(final AWSS3RequestedOperation el) {
		return DELEGATE.isNOT(this,el);
	}
}
