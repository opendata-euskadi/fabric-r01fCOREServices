package r01f.cloud.aws.s3.api.interfaces;

import java.io.File;
import java.io.InputStream;

import r01f.cloud.aws.s3.S3BucketName;
import r01f.cloud.aws.s3.S3ObjectKey;
import r01f.cloud.aws.s3.model.DeleteResult;
import r01f.cloud.aws.s3.model.PutResult;
import r01f.cloud.aws.s3.model.S3Object;
import r01f.cloud.aws.s3.model.metadata.ObjectMetaData;


public interface S3ServiceForObjects {
/////////////////////////////////////////////////////////////////////////////////////////
//  PUT OBJECTS METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public PutResult putObject(final S3BucketName bucketName,final S3ObjectKey key,
							   final File file);

	public PutResult putObject(final S3BucketName bucketName,final S3ObjectKey key,
							   final InputStream stream);

	public PutResult putObject(final S3BucketName bucketName,final S3ObjectKey key,
							   final InputStream stream,final ObjectMetaData objectMetada);
/////////////////////////////////////////////////////////////////////////////////////////
//  GET OBJECTS
/////////////////////////////////////////////////////////////////////////////////////////
	public S3Object getObject(final S3BucketName bucketName,final S3ObjectKey key);

/////////////////////////////////////////////////////////////////////////////////////////
//	DELETE OBJECTS
/////////////////////////////////////////////////////////////////////////////////////////
	public DeleteResult deleteObject(final S3BucketName bucketName,final S3ObjectKey key);
}
