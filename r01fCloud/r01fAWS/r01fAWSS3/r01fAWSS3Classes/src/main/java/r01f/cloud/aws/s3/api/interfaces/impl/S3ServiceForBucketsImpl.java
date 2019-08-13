package r01f.cloud.aws.s3.api.interfaces.impl;

import com.amazonaws.services.s3.AmazonS3;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.s3.S3BucketName;
import r01f.cloud.aws.s3.api.interfaces.S3ServiceForBuckets;

@Slf4j
public class S3ServiceForBucketsImpl
	 extends S3ServiceBaseImpl
  implements S3ServiceForBuckets {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public S3ServiceForBucketsImpl(final AmazonS3 s3Client)  {
		super(s3Client);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CREATE / DELETE BUCKET
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean existBucket(final S3BucketName bucketName){
		log.warn("existBucket  bucket {} ?",bucketName );
		return _s3Client.doesBucketExistV2(bucketName.getId());
	}
	@Override
	public boolean notExistBucket(final S3BucketName bucketName){
		log.warn("existBucket  bucket {} ?",bucketName );
		return !_s3Client.doesBucketExistV2(bucketName.getId());
	}

	@Override
	public void createBucket(final S3BucketName bucketName){
		log.warn("Create  bucket {}",bucketName );
		_s3Client.createBucket(bucketName.getId());
	}
	@Override
	public void deleteBucket(final S3BucketName bucketName){
		log.warn("Delete  bucket {}",bucketName );
		_s3Client.deleteBucket(bucketName.getId());
	}
}
