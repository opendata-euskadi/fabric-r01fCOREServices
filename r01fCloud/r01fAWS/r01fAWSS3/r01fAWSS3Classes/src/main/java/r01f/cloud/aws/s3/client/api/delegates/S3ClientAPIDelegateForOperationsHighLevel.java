package r01f.cloud.aws.s3.client.api.delegates;

import com.amazonaws.services.s3.AmazonS3;

import r01f.cloud.aws.s3.api.interfaces.S3ServiceForMultipartOperationsHighLevel;
import r01f.cloud.aws.s3.api.interfaces.impl.S3ServiceForMultipartOperationsHighLevelImpl;
import r01f.cloud.aws.s3.model.GetRequest;
import r01f.cloud.aws.s3.model.PutRequest;

public class S3ClientAPIDelegateForOperationsHighLevel
  implements S3ServiceForMultipartOperationsHighLevel {
///////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final S3ServiceForMultipartOperationsHighLevelImpl  _serviceForMultipartOperationsHighLevel;

///////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public S3ClientAPIDelegateForOperationsHighLevel(final AmazonS3 s3Client){
		_serviceForMultipartOperationsHighLevel = new  S3ServiceForMultipartOperationsHighLevelImpl(s3Client);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//METHODS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void putObject(final PutRequest putRequest) {
		_serviceForMultipartOperationsHighLevel.putObject(putRequest);
	}
	@Override
	public void getObject(final GetRequest downloadRequest) {
		_serviceForMultipartOperationsHighLevel.getObject(downloadRequest);
	}
}
