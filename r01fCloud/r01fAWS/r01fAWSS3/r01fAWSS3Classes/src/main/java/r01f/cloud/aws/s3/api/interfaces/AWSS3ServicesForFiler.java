package r01f.cloud.aws.s3.api.interfaces;

import java.io.IOException;
import java.util.Collection;

import r01f.cloud.aws.s3.model.AWSS3Bucket;
import r01f.cloud.aws.s3.model.AWSS3FileFilter;
import r01f.cloud.aws.s3.model.AWSS3ObjectSummary;
import r01f.file.FileName;
import r01f.types.Path;

public interface AWSS3ServicesForFiler {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Checks if a folder exists into bucket
	 * @param bucket
	 * @param path
	 * @param physicallyExistenceCheck :
	 *		  - a folder can exist as a 0-bytes object into a bucker or
	 *		  - just and only implicitly forming part of the name of an object foo/myfolder
	 * @return
	 */
	public boolean existsFolder(final AWSS3Bucket bucket,final Path path, 
								final boolean physicallyExistenceCheck);
	/**
	 * Copies a folder
	 * @param srcPath
	 * @param dstPath
	 * @param fileFilter
	 * @return
	 * @throws IOException
	 */
	public boolean copyFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final AWSS3FileFilter fileFilter,
							  final boolean overwrite) throws IOException;
	/**
	 * Moves a folder
	 * @param srcPath
	 * @param dstPath
	 * @param fileFilter
	 * @param overwrite
	 * @return
	 * @throws IOException
	 */
	public boolean moveFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final boolean overwrite) ;
	/**
	 * Renames a folder
	 * @param existingPath
	 * @param newName
	 * @return
	 * @throws IOException
	 */
	public boolean renameFolder(final AWSS3Bucket bucket,final Path existingPath,
						  		final FileName newName) ;
/////////////////////////////////////////////////////////////////////////////////////////
//  CREATE & DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a dir
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean createFolder(final AWSS3Bucket bucket,final Path path);
	/**
	 * Deletes a directory no matter if it's not empty
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean deleteFolder(final  AWSS3Bucket bucket,final Path path) ;
/////////////////////////////////////////////////////////////////////////////////////////
//  LIST
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Lists a bucket's contents
	 * @param bucket
	 * @param fileFilter
	 * @param recursive
	 * @return
	 */
	public Collection<AWSS3ObjectSummary> listBucketContents(final AWSS3Bucket bucket,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive);
	/**
	 * Lists a dir's contents, 1st level or complete subdirectories if recursive mode to true, but excluding folder types.
	 * @param bucket
	 * @param folderPath
	 * @param filter the filter, <code>null</code> if filter is not applied.
	 * @param recursive if <code>true</code> explore all subdir files.
	 * @return files and dirs
	 * @throws IOException
	 */
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,final Path folderPath, 
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive, 
															 final boolean excludeFolderTypes);
}
