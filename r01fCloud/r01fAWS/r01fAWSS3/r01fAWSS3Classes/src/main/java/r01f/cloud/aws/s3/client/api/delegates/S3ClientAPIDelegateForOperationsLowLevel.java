package r01f.cloud.aws.s3.client.api.delegates;

import com.amazonaws.services.s3.AmazonS3;

import r01f.cloud.aws.s3.api.interfaces.S3ServiceForMultipartOperationsLowLevel;
import r01f.cloud.aws.s3.api.interfaces.impl.S3ServiceForMultipartOperationsLowLevelImpl;
import r01f.cloud.aws.s3.model.GetRequest;
import r01f.cloud.aws.s3.model.PutRequest;

public class S3ClientAPIDelegateForOperationsLowLevel
  implements S3ServiceForMultipartOperationsLowLevel {
///////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final S3ServiceForMultipartOperationsLowLevelImpl  _serviceForMultipartOperationsLowLevel;

///////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public S3ClientAPIDelegateForOperationsLowLevel(final AmazonS3 s3Client) {
		_serviceForMultipartOperationsLowLevel = new  S3ServiceForMultipartOperationsLowLevelImpl(s3Client);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//METHODS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void putObject(PutRequest putRequest) {
		_serviceForMultipartOperationsLowLevel.putObject(putRequest);
	}
	@Override
	public void getObject(GetRequest downloadRequest) {
		_serviceForMultipartOperationsLowLevel.getObject(downloadRequest);
	}
}
