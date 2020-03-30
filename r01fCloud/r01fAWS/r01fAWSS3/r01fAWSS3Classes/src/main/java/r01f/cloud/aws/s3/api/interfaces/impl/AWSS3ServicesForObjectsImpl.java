package r01f.cloud.aws.s3.api.interfaces.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.s3.api.interfaces.AWSS3ServicesForObjects;
import r01f.cloud.aws.s3.model.AWSS3Bucket;
import r01f.cloud.aws.s3.model.AWSS3ObjectKey;
import r01f.cloud.aws.s3.model.AWSS3ObjectDeleteResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectGetResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectHeadResult;
import r01f.cloud.aws.s3.model.AWSS3ObjectMetaDataItem;
import r01f.cloud.aws.s3.model.AWSS3ObjectPutResult;
import r01f.cloud.aws.s3.model.AWSS3OperationSettings;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

@Slf4j
public class AWSS3ServicesForObjectsImpl
	 extends AWSS3ServicesBaseImpl
  implements AWSS3ServicesForObjects {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ServicesForObjectsImpl(final S3Client s3Client)  {
		super(s3Client);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PUT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SneakyThrows
	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							   	 	   final InputStream streamToUpload ,
							   	 	   final Collection<AWSS3ObjectMetaDataItem> customMetadata) {
		if (streamToUpload == null) throw new IllegalArgumentException(Strings.customized("The stream to be uploaded to bucket/key={}/{} cannot be null!!",
																						  bucket,key));
		log.info("PUT stream on bucket/key={}/{}",
				 bucket,key);
		
		// User multi-part uploading
		// [1] - Init the multipart upload
		CreateMultipartUploadRequest multipartUploadReq = CreateMultipartUploadRequest.builder()
																					  .bucket(bucket.asString())
																					  .key(key.asString())
																					  // All systems compatible with S3 should provide a metadata system,
																					  // ... but there are some that don't, f.e  MINIO
																					  .metadata(_customMetadataToMap(customMetadata))
																					  .build();
		CreateMultipartUploadResponse multipartUploadRes = _s3Client.createMultipartUpload(multipartUploadReq);
		String multipartUploadId = multipartUploadRes.uploadId();
		log.debug("...created multipart upload request with id={}",multipartUploadId);
		
		// [2] - Upoad individual parts
		Collection<CompletedPart> completedParts = Lists.newArrayList();
		int currPartNum = 1;
		byte[] buffer = new byte[1 * 1024 * 1024];	// 1Mb buffer
		int readed = streamToUpload.read(buffer);
		while (readed > 0) {
			log.debug("\t- part {}",currPartNum);
			UploadPartRequest uploadPartReq = UploadPartRequest.builder()
															   .bucket(bucket.asString())
															   .key(key.asString())
															   .uploadId(multipartUploadId)
															   .partNumber(1)
															   .build();
			String etag = _s3Client.uploadPart(uploadPartReq, 
											   RequestBody.fromByteBuffer(ByteBuffer.wrap(buffer,0,readed)))
								   .eTag();
			CompletedPart completedPart = CompletedPart.builder()
													   .partNumber(currPartNum)
													   .eTag(etag)
													   .build();
			completedParts.add(completedPart);
			
			// next part
			readed = streamToUpload.read(buffer);
			currPartNum = currPartNum + 1;
		} 
		if (CollectionUtils.isNullOrEmpty(completedParts)) throw new IllegalStateException("No parts to be uploded at bucket/key=" + bucket + "/" + key + ": NO data!!");
		
		// [3] - call completeMultipartUpload operation to tell S3 to merge all uploaded
		// 		 parts and finish the multipart operation.
		CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
																					.parts(completedParts)
																					.build();
		CompleteMultipartUploadRequest completeMultipartUploadReq = CompleteMultipartUploadRequest.builder()
																								  .bucket(bucket.asString())
																								  .key(key.asString())
																								  .uploadId(multipartUploadId)
																								  .multipartUpload(completedMultipartUpload)
																								  .build();
		CompleteMultipartUploadResponse res = _s3Client.completeMultipartUpload(completeMultipartUploadReq);
		
		// [4] - Return
		return AWSS3ObjectPutResult.fromPutObjectResponseOn(bucket,key)
								.with(res);
	}
	@Override
	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket, final AWSS3ObjectKey key,final byte[] bytes,
									   final Collection<AWSS3ObjectMetaDataItem> customMetadata) {
		if (bytes == null) throw new IllegalArgumentException(Strings.customized("The bytes to be stored at bucket/key={}/{} cannot be null!!!",
																  				 bucket,key));
		log.info("PUT bytes at bucket/key={}/{}",
				 bucket,key);
		PutObjectRequest req = PutObjectRequest.builder()
											   .bucket(bucket.asString())
											   .key(key.asString())
											   // All systems compatible with S3 should provide a metadata system,
											   // ... but there are some that don't, f.e  MINIO
											   .metadata(_customMetadataToMap(customMetadata))
											   .build();
		PutObjectResponse putRes = _s3Client.putObject(req,
													   RequestBody.fromBytes(bytes));
		return AWSS3ObjectPutResult.fromPutObjectResponseOn(bucket,key)
								.with(putRes);
	}
	@Override
	public AWSS3ObjectPutResult putObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							     	   final File file,
							     	   final Collection<AWSS3ObjectMetaDataItem> customMetadata) {
		if (file == null) throw new IllegalArgumentException(Strings.customized("The file to be stored at bucket/key={}/{} cannot be null!!!",
																  				bucket,key));
		log.info("PUT file at bucket/key={}/{}",
				 bucket,key);
		PutObjectRequest req = PutObjectRequest.builder()
											   .bucket(bucket.asString())
											   .key(key.asString())
											   // All systems compatible with S3 should provide a metadata system,
											   // ... but there are some that don't, f.e  MINIO
											   .metadata(_customMetadataToMap(customMetadata))
											   .build();
		PutObjectResponse putRes = _s3Client.putObject(req,
													   RequestBody.fromFile(file));
		return AWSS3ObjectPutResult.fromPutObjectResponseOn(bucket,key)
								.with(putRes);
	}
    @Override
	public void putHugeObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
    					  	  final File file,
    					  	  final AWSS3OperationSettings operationSettings,
    					  	  final Collection<AWSS3ObjectMetaDataItem> customMetadata) {
    	throw new UnsupportedOperationException("Not yet implemented!");		// see https://github.com/aws/aws-sdk-java-v2/tree/master/docs/design/services/s3/transfermanager
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	HEAD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public AWSS3ObjectHeadResult headObject(final AWSS3Bucket bucket, final AWSS3ObjectKey key) {
		log.info("HEAD on object at bucket/key={}/{}",
				 bucket,key);
		HeadObjectRequest headReq = HeadObjectRequest.builder()
												 .bucket(bucket.asString())
												 .key(key.asString())
												 .build();
		HeadObjectResponse headRes = _s3Client.headObject(headReq);
		return AWSS3ObjectHeadResult.fromHeadResponseOn(bucket,key)
								 .with(headRes);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	GET 
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("resource")
	public AWSS3ObjectGetResult getObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key) {
		log.info("GET object at bucket/key={}/{}",
				 bucket,key);
		GetObjectRequest req = GetObjectRequest.builder()
												.bucket(bucket.asString())
												.key(key.asString())
												.build();
		ResponseInputStream<GetObjectResponse> resIs = _s3Client.getObject(req,
																		   ResponseTransformer.toInputStream());
		return AWSS3ObjectGetResult.fromGetObjectResponseOn(bucket,key)
								.returning(resIs);
	}
	@Override
	public void getHugeObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key,
							  final AWSS3OperationSettings operationSettings) {
		throw new UnsupportedOperationException("Not yet implemented!");		// see https://github.com/aws/aws-sdk-java-v2/tree/master/docs/design/services/s3/transfermanager
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public AWSS3ObjectDeleteResult deleteObject(final AWSS3Bucket bucket,final AWSS3ObjectKey key) {
		log.info("DELETE object at bucket/key={}/{}",
				 bucket,key);
		DeleteObjectRequest req = DeleteObjectRequest.builder()
													 .bucket(bucket.toString())
													 .key(key.asString())
													 .build();
		DeleteObjectResponse res = _s3Client.deleteObject(req);
		return AWSS3ObjectDeleteResult.fromDeleteObjectResponseOn(bucket,key)
							 .with(res);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UTIL
/////////////////////////////////////////////////////////////////////////////////////////
	private static Map<String,String> _customMetadataToMap(final Collection<AWSS3ObjectMetaDataItem> metadata) {
		Map<String,String> outMetaData = null;
		if (CollectionUtils.hasData(metadata)) {
			outMetaData = Maps.newHashMapWithExpectedSize(metadata.size());
			for (AWSS3ObjectMetaDataItem item : metadata) {
				outMetaData.put(item.getId().asString(),
							    item.getValue());
			}
		} else {
			outMetaData = Maps.newHashMap();
		}
		return outMetaData;
	}
}
