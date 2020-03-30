package r01f.cloud.aws.s3.model;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.types.Path;


@Immutable
@NoArgsConstructor
public class AWSS3FolderPath
	 extends OIDBaseMutable<String> {

	private static final long serialVersionUID = 4162366466990455545L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANT
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String DELIMITER = "/";
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	AWSS3FolderPath(final String id) {
		super(id);
	}
	public static AWSS3FolderPath forPath(final Path path) {
		return new AWSS3FolderPath(path.asString() + DELIMITER);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<AWSS3FolderPath> getAllFoldersForPath(final Path path) {
		Collection<AWSS3FolderPath> st = FluentIterable.from(Splitter.on(DELIMITER)
																  .split(path.asString()))
											.transform(new Function<String,AWSS3FolderPath>() {
																String _previous ;
																@Override
																public AWSS3FolderPath apply(final String input) {
																	  String rejoined = _previous != null 
																			  				? Joiner.on(DELIMITER).join(_previous,input)
																			  			    : input;
															           _previous = rejoined;
															          return AWSS3FolderPath.forPath(Path.valueOf(rejoined));
																}
														})
											.toList();
		return st;
	}
}