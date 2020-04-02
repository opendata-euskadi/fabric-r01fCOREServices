package r01f.cloud.aws.s3.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(prefix="_")
public class AWSS3ObjectMetaDataItem {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter  final  AWSS3ObjectMetadataItemId _id;
	@Getter  final String  _value;
}
