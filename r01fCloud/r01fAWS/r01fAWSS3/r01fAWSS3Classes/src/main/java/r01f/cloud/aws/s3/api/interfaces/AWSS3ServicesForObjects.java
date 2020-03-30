package r01f.cloud.aws.s3.api.interfaces;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import r01f.cloud.aws.s3.model.AWSS3Bucket;
import r01f.cloud.aws.s3.model.AWSS3ObjectKey;
import r01f.cloud.aws.s3.model.AWSS3ObjectDeleteResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectGetResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectHeadResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectMetaDataItem;
import r01f.cloud.aws.s3.model.AWSS3ObjectPutResult;
import r01f.cloud.aws.s3.model.AWSS3OperationSettings;


public interface AWSS3ServicesForObjects {
/////////////////////////////////////////////////////////////////////////////////////////
//  PUT
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							     	   final File file,
							     	   final Collection<AWSS3ObjectMetaDataItem> customMetadata);

	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							     	   final byte[] bytes,
							     	   final Collection<AWSS3ObjectMetaDataItem> customMetadata);
	
	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							     	   final InputStream stream,
							     	   final Collection<AWSS3ObjectMetaDataItem> customMetadata);
    public void putHugeObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
    					  	  final File file,
    					  	  final AWSS3OperationSettings operationSettings,
    					  	  final Collection<AWSS3ObjectMetaDataItem> customMetadata);
/////////////////////////////////////////////////////////////////////////////////////////
//  GET
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ObjectGetResult getObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key);
	public void getHugeObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							  final AWSS3OperationSettings operationSettings);
/////////////////////////////////////////////////////////////////////////////////////////
//	HEAD
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ObjectHeadResult headObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key);
/////////////////////////////////////////////////////////////////////////////////////////
//	DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ObjectDeleteResult deleteObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key);
}
