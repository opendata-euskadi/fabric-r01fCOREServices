package r01f.core.services.mail.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.mime.MimeType;

@Accessors(prefix="_")
public class EMailMessageAttachment
  implements Serializable {

	private static final long serialVersionUID = -3953489838599622315L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter private final String _name;
    @Getter private final byte[] _data;
    @Getter private final MimeType _contentType;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public EMailMessageAttachment(final String name,
    							  final byte[] data,
    							  final MimeType contentType) {
        _name = name;
        _data = data;
        _contentType = contentType;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    public MimeType getContentTypeOrDefault(final MimeType defContentType) {
    	return _contentType != null ? _contentType
    								: defContentType != null ? defContentType : MimeType.OCTECT_STREAM;
    }
}
