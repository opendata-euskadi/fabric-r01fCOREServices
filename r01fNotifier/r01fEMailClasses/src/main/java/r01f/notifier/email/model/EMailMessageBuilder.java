package r01f.notifier.email.model;

import java.util.Collection;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.types.contact.EMail;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class EMailMessageBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    public static final EMaiMessagelBuilderToStep from(final EMailMessageFrom from) {
        return new EMailMessageBuilder() { /* nothing */ }
        				.new EMaiMessagelBuilderToStep(from);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMaiMessagelBuilderToStep {
        private final EMailMessageFrom _from;

        public EMailMessageBuilderCCStep noCC() {
        	return new EMailMessageBuilderCCStep(_from,
        										 null);
        }
        public EMailMessageBuilderCCStep to(final EMail... to) {
        	return new EMailMessageBuilderCCStep(_from,
        										 Lists.newArrayList(to));
        }
        public EMailMessageBuilderCCStep to(final Collection<EMail> to) {
        	return new EMailMessageBuilderCCStep(_from,
        										 to);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderCCStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;

        public EMailMessageBuilderBCCStep noCC() {
        	return new EMailMessageBuilderBCCStep(_from,_to,
        										  null);
        }
        public EMailMessageBuilderBCCStep cc(final EMail... cc) {
        	return new EMailMessageBuilderBCCStep(_from,_to,
        										  Lists.newArrayList(cc));
        }
        public EMailMessageBuilderBCCStep cc(final Collection<EMail> cc) {
        	return new EMailMessageBuilderBCCStep(_from,_to,
        										  cc);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderBCCStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;
        private final Collection<EMail> _cc;

        public EMailMessageBuilderSubjectStep noBCC() {
        	return new EMailMessageBuilderSubjectStep(_from,_to,
        										 	  _cc,null);
        }
        public EMailMessageBuilderSubjectStep bcc(final EMail... bcc) {
        	return new EMailMessageBuilderSubjectStep(_from,_to,
        										      _cc,Lists.newArrayList(bcc));
        }
        public EMailMessageBuilderSubjectStep bcc(final Collection<EMail> bcc) {
        	return new EMailMessageBuilderSubjectStep(_from,_to,
        										 	  _cc,bcc);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderSubjectStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;
        private final Collection<EMail> _cc;
        private final Collection<EMail> _bcc;

        public EMailMessageBuilderBodyStep withSubject(final String subject) {
        	return new EMailMessageBuilderBodyStep(_from,_to,
        										   _cc,_bcc,
										           subject);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderBodyStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;
        private final Collection<EMail> _cc;
        private final Collection<EMail> _bcc;
        private final String _subject;

        public EMailMessageBuilderAttachmentsStep withPlainTextBody(final String plainTextBody) {
        	return new EMailMessageBuilderAttachmentsStep(_from,_to,
        												  _cc,_bcc,
        												  _subject,
        												  plainTextBody,null);
        }
        public EMailMessageBuilderAttachmentsStep withPlainTextAndHTMLBody(final String plainTextBody,final String htmlBody) {
        	return new EMailMessageBuilderAttachmentsStep(_from,_to,
        												  _cc,_bcc,
        												  _subject,
        												  plainTextBody,htmlBody);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderAttachmentsStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;
        private final Collection<EMail> _cc;
        private final Collection<EMail> _bcc;
        private final String _subject;
        private final String _plaintTextBody;
        private final String _htmlBody;

        public EMailMessageBuilderBuildStep noAttachments() {
        	return new EMailMessageBuilderBuildStep(_from,_to,
        											      _cc,_bcc,
        											      _subject,_plaintTextBody,_htmlBody,
        											      null);
        }
        public EMailMessageBuilderBuildStep withAttachments(final EMailMessageAttachment... attachs) {
        	return new EMailMessageBuilderBuildStep(_from,_to,
        											_cc,_bcc,
        											_subject,_plaintTextBody,_htmlBody,
        											Lists.newArrayList(attachs));
        }
        public EMailMessageBuilderBuildStep withAttachments(final Collection<EMailMessageAttachment> attachs) {
        	return new EMailMessageBuilderBuildStep(_from,_to,
        											_cc,_bcc,
        											_subject,_plaintTextBody,_htmlBody,
        											attachs);
        }
    }
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class EMailMessageBuilderBuildStep {
        private final EMailMessageFrom _from;
        private final Collection<EMail> _to;
        private final Collection<EMail> _cc;
        private final Collection<EMail> _bcc;
        private final String _subject;
        private final String _plaintTextBody;
        private final String _htmlBody;
        private final Collection<EMailMessageAttachment> _attachments;

        public EMailMessage build() {
        	return new EMailMessage(_from,_to,
		        					_cc,_bcc,
		        					_subject,
		        					_plaintTextBody,_htmlBody,
		        					_attachments);
        }
    }
}
