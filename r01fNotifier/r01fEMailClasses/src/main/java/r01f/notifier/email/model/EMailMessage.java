package r01f.notifier.email.model;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class EMailMessage {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter private final EMailRFC822Address _from;
	@Getter private final Collection<EMailRFC822Address> _to;
    @Getter private final Collection<EMailRFC822Address> _cc;
    @Getter private final Collection<EMailRFC822Address> _bcc;

    @Getter private final String _subject;

    @Getter private final String _plainTextBody;
    @Getter private final String _htmlBody;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public EMailMessage(final EMailRFC822Address from,final EMailRFC822Address to,
    				    final String subject,final String plainTextBody) {
    	this(from,Lists.newArrayList(to),
    		 null,null,
    		 subject,plainTextBody,null);	// plain text body (no html)
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,
    				   	final String subject,final String plainTextBody) {
    	this(from,to,
    		 null,null,
    		 subject,plainTextBody,null);	// plain text body (no html)
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,final Collection<EMailRFC822Address> cc,
    				   	final String subject,final String plainTextBody) {
    	this(from,to,
    		 cc,null,
    		 subject,plainTextBody,null);	// plain text body (no html)
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,final Collection<EMailRFC822Address> cc,final Collection<EMailRFC822Address> bcc,
    				   	final String subject,final String plainTextBody) {
    	this(from,to,
    		 cc,bcc,
    		 subject,plainTextBody,null);	// plain text body (no html)
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR (setting html)
/////////////////////////////////////////////////////////////////////////////////////////
    public EMailMessage(final EMailRFC822Address from,final EMailRFC822Address to,
    				   	final String subject,
    				   	final String plainTextBody,final String htmlBody) {
    	this(from,Lists.newArrayList(to),
    		 null,null,
    		 subject,plainTextBody,htmlBody);
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,
    				   	final String subject,
    				   	final String plainTextBody,final String htmlBody) {
    	this(from,to,
    		 null,null,
    		 subject,plainTextBody,htmlBody);
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,final Collection<EMailRFC822Address> cc,
    				   	final String subject,
    				   	final String plainTextBody,final String htmlBody) {
    	this(from,to,
    		 cc,null,
    		 subject,plainTextBody,htmlBody);
    }
    public EMailMessage(final EMailRFC822Address from,final Collection<EMailRFC822Address> to,final Collection<EMailRFC822Address> cc,final Collection<EMailRFC822Address> bcc,
    				   	final String subject,
    				   	final String plainTextBody,final String htmlBody) {
    	_from = from;
        _to = to;
        _cc = cc;
        _bcc = bcc;

        _subject = subject;
        _plainTextBody = plainTextBody;
        _htmlBody = htmlBody;
    }
}
