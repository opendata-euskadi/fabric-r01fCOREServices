package r01f.cloud.aws.s3.services;

import com.google.inject.Binder;
import com.google.inject.Module;

import r01f.cloud.aws.s3.client.api.AWSS3ClientAPI;
import r01f.cloud.aws.s3.client.api.AWSS3ClientConfig;


public class S3GuiceModule
	implements Module {
///////////////////////////////////////////////////////////////////////
/// MEMBERS
///////////////////////////////////////////////////////////////////////	
	private final AWSS3ClientConfig _clientConfig;
///////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
///////////////////////////////////////////////////////////////////////	
	public S3GuiceModule(final AWSS3ClientConfig clientConfig ) {
		_clientConfig = clientConfig;
	}
///////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
///////////////////////////////////////////////////////////////////////	
	@Override
	public void configure(final Binder binder) {
		binder.bind(AWSS3ClientAPI.class)
		      .toInstance(new AWSS3ClientAPI(_clientConfig));	
	}
}