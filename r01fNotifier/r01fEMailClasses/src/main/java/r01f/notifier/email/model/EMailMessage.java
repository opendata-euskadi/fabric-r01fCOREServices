package r01f.notifier.email.model;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.types.contact.EMail;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
public class EMailMessage {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter private final EMailMessageFrom _from;
	@Getter private final Collection<EMail> _to;
    @Getter private final Collection<EMail> _cc;
    @Getter private final Collection<EMail> _bcc;

    @Getter private final String _subject;

    @Getter private final String _plainTextBody;
    @Getter private final String _htmlBody;

    @Getter private final Collection<EMailMessageAttachment> _attachments;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public EMailMessage(final EMailMessageFrom from,final EMail to,
    				    final String subject,final String plainTextBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,Lists.newArrayList(to),
    		 null,null,
    		 subject,plainTextBody,null,	// plain text body (no html)
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,
    				   	final String subject,final String plainTextBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,to,
    		 null,null,
    		 subject,plainTextBody,null,	// plain text body (no html)
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,final Collection<EMail> cc,
    				   	final String subject,final String plainTextBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,to,
    		 cc,null,
    		 subject,plainTextBody,null,	// plain text body (no html)
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,final Collection<EMail> cc,final Collection<EMail> bcc,
    				   	final String subject,final String plainTextBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,to,
    		 cc,bcc,
    		 subject,plainTextBody,null,	// plain text body (no html)
    		 attachments);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR (setting html)
/////////////////////////////////////////////////////////////////////////////////////////
    public EMailMessage(final EMailMessageFrom from,final EMail to,
    				   	final String subject,final String plainTextBody,final String htmlBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,Lists.newArrayList(to),
    		 null,null,
    		 subject,plainTextBody,htmlBody,
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,
    				   	final String subject,final String plainTextBody,final String htmlBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,to,
    		 null,null,
    		 subject,plainTextBody,htmlBody,
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,final Collection<EMail> cc,
    				   	final String subject,final String plainTextBody,final String htmlBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	this(from,to,
    		 cc,null,
    		 subject,plainTextBody,htmlBody,
    		 attachments);
    }
    public EMailMessage(final EMailMessageFrom from,final Collection<EMail> to,final Collection<EMail> cc,final Collection<EMail> bcc,
    				   	final String subject,final String plainTextBody,final String htmlBody,
    				   	final Collection<EMailMessageAttachment> attachments) {
    	_from = from;
        _to = to;
        _cc = cc;
        _bcc = bcc;

        _subject = subject;
        _plainTextBody = plainTextBody;
        _htmlBody = htmlBody;

        _attachments = attachments;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    public boolean hasAttachments() {
    	return CollectionUtils.hasData(_attachments);
    }
}
