package r01f.cloud.aws.s3.client.api.delegates;

import java.util.Collection;

import r01f.cloud.aws.s3.api.interfaces.AWSS3ServicesForFiler;
import r01f.cloud.aws.s3.api.interfaces.impl.AWSS3ServicesForFilerImpl;
import r01f.cloud.aws.s3.model.AWSS3Bucket;
import r01f.cloud.aws.s3.model.AWSS3FileFilter;
import r01f.cloud.aws.s3.model.AWSS3ObjectSummary;
import r01f.file.FileName;
import r01f.types.Path;
import software.amazon.awssdk.services.s3.S3Client;

public class AWSS3ClientAPIDelegateForFiler
  implements AWSS3ServicesForFiler {
///////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final AWSS3ServicesForFilerImpl _serviceForFolderFilerImpl;

///////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ClientAPIDelegateForFiler(final S3Client s3Client) {
		_serviceForFolderFilerImpl = new  AWSS3ServicesForFilerImpl(s3Client);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	EXISTENCE
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean existsFolder(final AWSS3Bucket bucket,
								final Path path,
								final boolean physicallyExistenceCheck) {
		return _serviceForFolderFilerImpl.existsFolder(bucket,
													   path,
													   physicallyExistenceCheck);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COPY / MOVE / RENAME
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean copyFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final AWSS3FileFilter fileFilter,
							  final boolean overwrite) {
		return _serviceForFolderFilerImpl.copyFolder(bucket,
													 srcPath,dstPath,
													 fileFilter,
													 overwrite);
	}
	@Override
	public boolean moveFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final boolean overwrite) {
		return _serviceForFolderFilerImpl.moveFolder(bucket,
													 srcPath,dstPath,
													 overwrite);
	}
	@Override
	public boolean renameFolder(final AWSS3Bucket bucket,final Path existingPath,
								final FileName newName) {
		return _serviceForFolderFilerImpl.renameFolder(bucket,existingPath,
													   newName);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CREATE & DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean createFolder(final AWSS3Bucket bucket,
								final Path path) {
		return _serviceForFolderFilerImpl.createFolder(bucket,
													   path);
	}
	@Override
	public boolean deleteFolder(final AWSS3Bucket bucket,
								final Path path) {
		return _serviceForFolderFilerImpl.deleteFolder(bucket,
													   path);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LIST
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,final  Path folderPath,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive)  {
		return _serviceForFolderFilerImpl.listFolderContents(bucket,folderPath,
															 fileFilter,
															 recursive,
															 false);		// do NOT exclude folders
	}
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,final  Path folderPath,
															  final boolean recursive)  {
		return _serviceForFolderFilerImpl.listFolderContents(bucket,folderPath,
															 null,
															 recursive,
															 false);		// do NOT exclude folders
	}
	@Override
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,final Path folderPath,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive,
															 final boolean excludeFolderTypes) {
		return _serviceForFolderFilerImpl.listFolderContents(bucket,folderPath,
															 fileFilter,
															 recursive,
															 excludeFolderTypes);
	}
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,final Path folderPath,
															 final boolean recursive,
															 final boolean excludeFolderTypes) {
		return _serviceForFolderFilerImpl.listFolderContents(bucket,
															 folderPath,
															 null,			// no filter
															 recursive,
															 excludeFolderTypes);
	}
	@Override
	public Collection<AWSS3ObjectSummary> listBucketContents(final AWSS3Bucket bucket,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive) {
		return _serviceForFolderFilerImpl.listBucketContents(bucket,
															 fileFilter,
															 recursive);
	}
}
