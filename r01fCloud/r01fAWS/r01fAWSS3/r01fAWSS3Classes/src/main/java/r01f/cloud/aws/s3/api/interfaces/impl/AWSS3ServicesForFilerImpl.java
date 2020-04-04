package r01f.cloud.aws.s3.api.interfaces.impl;

import static r01f.cloud.aws.s3.model.AWSS3FolderPath.DELIMITER;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import r01f.cloud.aws.s3.api.interfaces.AWSS3ServicesForFiler;
import r01f.cloud.aws.s3.model.AWSS3Bucket;
import r01f.cloud.aws.s3.model.AWSS3FileFilter;
import r01f.cloud.aws.s3.model.AWSS3FolderPath;
import r01f.cloud.aws.s3.model.AWSS3ObjectKey;
import r01f.cloud.aws.s3.model.AWSS3ObjectSummary;
import r01f.file.FileName;
import r01f.httpclient.HttpResponseCode;
import r01f.mime.MimeTypes;
import r01f.types.Path;
import r01f.util.types.collections.CollectionUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

@Slf4j
public class AWSS3ServicesForFilerImpl
	 extends AWSS3ServicesBaseImpl
  implements AWSS3ServicesForFiler {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AWSS3ServicesForFilerImpl(final S3Client s3Client)  {
		super(s3Client);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks:
	 * 	 > Checks the folder has a logical existence within a hierarchy as part of the key of a file.
	 *   > Checks the folder has a physical existence with certain characteristics, the key has the common delimiter, it has 0 bytes.
	 */
	@Override
	public boolean existsFolder(final AWSS3Bucket bucket,
								final Path path,
								final boolean physicallyExistenceCheck)  {
		log.warn("Check if folder exists bucket/key={}/{}",
				 bucket,path);
		if (!path.asString().contains(AWSS3FolderPath.DELIMITER)) {
			log.warn("... object bucket/key={}/{} was supposed to be a FOLDER but it has NO path delimiter={}",
					 bucket,path,DELIMITER);
			return false;
		}

		//	Checks the folder has a logical existence within a hierarchy as part of the key of a file.
		Collection<AWSS3ObjectSummary> folderContents = this.listFolderContents(bucket,path,
																			    null,	// no filter
																			    false,	// not recursive
																			    false);	// do not exclude folders
		boolean folderExistsLogically = CollectionUtils.hasData(folderContents);
		if  (!physicallyExistenceCheck) {	//|| (physicallyExistenceCheck && !folderExistsLogically ) ) {
			return folderExistsLogically;
		}
		 //	Checks the folder has a physical existence with certain characteristics, the key has the common delimiter, it has 0 bytes.
		AWSS3FolderPath folderPath = AWSS3FolderPath.forPath(path);
		try {
			HeadObjectRequest req = HeadObjectRequest.builder()
													 .bucket(bucket.asString())
													 .key(folderPath.asString())
													 .build();
			HeadObjectResponse headRes = _s3Client.headObject(req);
			if (! headRes.contentType().equals(MimeTypes.OCTECT_STREAM.asString())) {
				log.warn("... object bucket/key={}/{} was supposed to be a FOLDER but it has content-type=octect-stream",
						 bucket,path);
				return false;
			}
			if (headRes.contentLength() > 0) {
				log.warn("... object bucket/key={}/{} was supposed to be a FOLDER but it has content-length > 0",
						 bucket,path);
				return false;
			}
		} catch (final S3Exception s3Ex) {
			if (HttpResponseCode.of(s3Ex.statusCode()) == HttpResponseCode.NOT_FOUND ) {
				log.warn("Folder NOT found at={}",
						 path.asString());
				return false;
			}
			log.error(" Error {}",
					  s3Ex.getMessage(),s3Ex);
			throw s3Ex;
		}
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COPY / MOVE / RENAME
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean copyFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final AWSS3FileFilter fileFilter,
							  final boolean overwrite) {
		throw new UnsupportedOperationException(">Not yet implemented");
	}
	@Override
	public boolean moveFolder(final AWSS3Bucket bucket,
							  final Path srcPath,final Path dstPath,
							  final boolean overwrite)  {
		throw new UnsupportedOperationException(">Not yet implemented");
	}
	@Override
	public boolean renameFolder(final AWSS3Bucket bucket,
								final Path existingPath,final FileName newName) {
		throw new UnsupportedOperationException(">Not yet implemented");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CREATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean createFolder(final AWSS3Bucket bucket,
								final Path path)  {
		Collection<AWSS3FolderPath> fp = AWSS3FolderPath.getAllFoldersForPath(path);
		for (AWSS3FolderPath folder : fp ) {
			boolean existsFolder = this.existsFolder(bucket,
													 Path.valueOf(folder.asString()),
													 true);		// physicallyExistenceCheck
			if (!existsFolder) {
				 log.warn("folder {} does NOT exists: it'll be created",folder);
				 PutObjectRequest req = PutObjectRequest.builder()
						 								.bucket(bucket.asString())
						 								.key(folder.asString())
						 								.build();
				 RequestBody body = RequestBody.empty();	// there's NO such a thing as folder in s3: an empty object is created
				 _s3Client.putObject(req,
						 			 body);
				/*_s3Client.putObject(req,
									body);*/
			}
		}
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DELETE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean deleteFolder(final AWSS3Bucket bucket,
								final Path path) {
		throw new UnsupportedOperationException(">Not yet implemented");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LIST
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Collection<AWSS3ObjectSummary> listBucketContents(final AWSS3Bucket bucket,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive) {
		return this.listFolderContents(bucket,Path.from("/"),
									   fileFilter,
									   recursive,
									   false);		// do not exclude folders
	}
	@Override
	public Collection<AWSS3ObjectSummary> listFolderContents(final AWSS3Bucket bucket,
															 final Path folderPath,
															 final AWSS3FileFilter fileFilter,
															 final boolean recursive,
															 final boolean excludeFolderTypes) {
		AWSS3FolderPath prefix = AWSS3FolderPath.forPath(folderPath);
		ListObjectsRequest req = _buildListObjectsRequest(bucket,prefix);
		ListObjectsResponse listing = _s3Client.listObjects(req);

		Collection<AWSS3ObjectSummary> results = Lists.newArrayList();
		// [1] - Filter folder results and its children if requested (recursive)
		Collection<AWSS3ObjectSummary> folderResults = _listFolderContentsOfTypeFolder(listing,
																						bucket,
																						prefix);
		if (CollectionUtils.hasData(folderResults)) {
			if (!excludeFolderTypes) {
				results.addAll(folderResults);
			}
			if (recursive) {
				for (final AWSS3ObjectSummary folder : folderResults ) {
					Collection<AWSS3ObjectSummary> contents = listFolderContents(bucket,
																				 Path.valueOf(folder.getKey().asString()),
																				 null,		// no file filtr
																				 recursive,excludeFolderTypes);
					results.addAll(contents);
				}
			}
		}
		// [2] - Filter file results.
		Collection<AWSS3ObjectSummary> fileResults = _listFolderContentsOfTypeFile(listing,
																					bucket,
																					prefix);
		if (CollectionUtils.hasData(fileResults)) {
			results.addAll(fileResults);
		}
		return results;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This builds a List Object Reques for a given folderpath.
	 * @param bucket
	 * @param folderPath
	 * @return
	 */
	private static ListObjectsRequest _buildListObjectsRequest(final AWSS3Bucket bucket,
													    	   final AWSS3FolderPath folderPath) {
		ListObjectsRequest req = folderPath.asString()
										   .equalsIgnoreCase(DELIMITER)
									// Root CASE, that means..bucket level.
									? ListObjectsRequest.builder()
														.bucket(bucket.asString())
														.delimiter(DELIMITER)
														.build()
									// not root
									: ListObjectsRequest.builder()
														.bucket(bucket.asString())
														.delimiter(DELIMITER)
														.prefix(folderPath.asString())
														.build();
	   return req;
	}
	/**
	 * From a ObjectListing result, gets the folder objects.
	 * @param listing
	 * @param bucket
	 * @param folderPath
	 * @return
	 */
	private static Collection<AWSS3ObjectSummary> _listFolderContentsOfTypeFolder(final ListObjectsResponse listing ,
																				  final AWSS3Bucket bucket,
																				  final AWSS3FolderPath folderPath) {
		// Filter folder results and its children if requested (recursive)
		Collection<AWSS3ObjectSummary> folderResults = null;
		if (CollectionUtils.hasData(listing.commonPrefixes())) {
			folderResults = FluentIterable.from(listing.commonPrefixes())
									.transform(new Function<CommonPrefix,AWSS3ObjectSummary>() {
														@Override
														public AWSS3ObjectSummary apply(final CommonPrefix prefix) {
															 AWSS3ObjectSummary folderItem = new AWSS3ObjectSummary();
															 folderItem.setBucket(bucket);
															 folderItem.setKey(AWSS3ObjectKey.forId(prefix.prefix()));
															 folderItem.setFolder(true);
															 return folderItem;
														}
												})
									.toList();
		}
		return folderResults;
	}
	/**
	 * From a ObjectListing result, gets the file objects.
	 * @param listing
	 * @param bucket
	 * @param folderPath
	 * @return
	 */
	private static Collection<AWSS3ObjectSummary> _listFolderContentsOfTypeFile(final ListObjectsResponse listing ,
																		  		final AWSS3Bucket bucket,
																		  	    final AWSS3FolderPath folderPath) {
		if (!listing.hasContents()) {
			return null;
		}

		Collection<AWSS3ObjectSummary> fileResults = null;
		List<S3Object> s3Objs = listing.contents();

		// Remove root folder (prefix) , returned as object.
		// First , find the root element.
		Collection<S3Object> toRemove = Collections2.filter(s3Objs,
														    new Predicate<S3Object>() {
																@Override
																public boolean apply(final S3Object s3Obj) {
																	return s3Obj.key().equalsIgnoreCase(folderPath.asString());
																}
															});

		System.out.println( " to remove List :" + toRemove.size());
		// ..and then, remove from summary ( "toRemove" is a list with just one element)
		s3Objs.removeAll(toRemove);

		fileResults = FluentIterable.from(s3Objs)
							.transform(new Function<S3Object,AWSS3ObjectSummary>() {
												@Override
												public AWSS3ObjectSummary apply(final S3Object s3Obj) {
													AWSS3ObjectSummary folderItem = new AWSS3ObjectSummary();
													folderItem.setBucket(bucket);
													folderItem.setKey(AWSS3ObjectKey.forId(s3Obj.key()));
													folderItem.setFolder(false);
													return folderItem;
												}
										})
							.toList();
		return fileResults;
	}

}
