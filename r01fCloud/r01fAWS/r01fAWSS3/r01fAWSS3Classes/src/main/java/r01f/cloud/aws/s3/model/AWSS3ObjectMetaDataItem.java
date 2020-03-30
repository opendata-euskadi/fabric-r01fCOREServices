package r01f.cloud.aws.s3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(prefix="_")
public class AWSS3ObjectMetaDataItem {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter AWSS3ObjectMetadataItemId _id;
	@Getter @Setter String  _value;
}
