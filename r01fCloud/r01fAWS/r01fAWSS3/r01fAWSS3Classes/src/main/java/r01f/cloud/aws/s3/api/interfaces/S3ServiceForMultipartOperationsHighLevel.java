package r01f.cloud.aws.s3.api.interfaces;

import r01f.cloud.aws.s3.model.GetRequest;
import r01f.cloud.aws.s3.model.PutRequest;


public interface S3ServiceForMultipartOperationsHighLevel {

/////////////////////////////////////////////////////////////////////////////////////////
//  PUT OBJECTS METHODS
/////////////////////////////////////////////////////////////////////////////////////////
    public void putObject(final PutRequest putRequest);
/////////////////////////////////////////////////////////////////////////////////////////
//  GET OBJECTS
/////////////////////////////////////////////////////////////////////////////////////////
	public void getObject(final GetRequest downloadRequest);
}
