package r01f.core.services.mail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.mail.model.EMailDestinations;
import r01f.core.services.mail.model.EMailMessage;
import r01f.core.services.mail.model.EMailMessageAttachment;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.mime.MimeBodyPartDispositionType;
import r01f.mime.MimeType;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
@Accessors(prefix="_")
public abstract class EMailMimeMessages {
/////////////////////////////////////////////////////////////////////////////////////////
//	MIME
/////////////////////////////////////////////////////////////////////////////////////////
	public static EMailMimeMessageBuilderCharsetStep createMimeMessageFor(final EMailMessage mailMsg) throws MessagingException {
		return new EMailMimeMessages() { /* nothing */ }
						.new EMailMimeMessageBuilderCharsetStep(mailMsg);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class EMailMimeMessageBuilderCharsetStep {
		private final EMailMessage _mailMessage;

		public EMailMimeMessageBuilderSessionStep withDefaultCharset() {
			return this.withCharset(Charset.defaultCharset());
		}
		public EMailMimeMessageBuilderSessionStep withCharset(final Charset charset) {
			return new EMailMimeMessageBuilderSessionStep(_mailMessage,
														  charset);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class EMailMimeMessageBuilderSessionStep {
		private final EMailMessage _mailMessage;
		private final Charset _charset;

		public EMailMimeMessageBuilderAttachmentsStep usingSession(final Session session) {
			return new EMailMimeMessageBuilderAttachmentsStep(_mailMessage,
															  _charset,
															  session);
		}
		public EMailMimeMessageBuilderAttachmentsStep usingDefaultSession() {
			return this.usingDefaultSessionFrom(new Properties());
		}
		public EMailMimeMessageBuilderAttachmentsStep usingDefaultSessionFrom(final Properties properties) {
			Session session = Session.getDefaultInstance(properties);
			return this.usingSession(session);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class EMailMimeMessageBuilderAttachmentsStep {
		private final EMailMessage _mailMessage;
		private final Charset _charset;
		private final Session _session;

		public EMailMimeMessageBuilderBuildStep noAttachments() throws MessagingException {
			return this.withAttachments(null);
		}
		public EMailMimeMessageBuilderBuildStep withAttachments(final Collection<EMailMessageAttachment> attachments) throws MessagingException {
			return new EMailMimeMessageBuilderBuildStep(_mailMessage,
									  					_charset,
									  					_session,
									  					attachments);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class EMailMimeMessageBuilderBuildStep {
		private final EMailMessage _mailMessage;
		private final Charset _charset;
		private final Session _session;
		final Collection<EMailMessageAttachment> _attachments;

		public MimeMessage build() throws MessagingException {
			return _createMimeMessage(_mailMessage,_attachments,
									  _charset,
									  _session);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static MimeMessage _createMimeMessage(final EMailMessage mailMsg,final Collection<EMailMessageAttachment> attachments,
												  final Charset charset,
												  final Session session) throws MessagingException {
		// Create the html and text mime body part
        MimeBodyPart htmlAndTextMimeBodyPart = _createTextMimeBodyPart(mailMsg.getPlainTextBody(),mailMsg.getHtmlBody(),
        															   charset);

        // Create a mime body part for the attachment
        Collection<MimeBodyPart> attachmentsMimeBodyParts = _createAttachmentsMimeBodyPart(attachments);

        // Create a multipart/mixed parent container.
        MimeMultipart mimeMultiPart = new MimeMultipart("mixed");
        mimeMultiPart.addBodyPart(htmlAndTextMimeBodyPart);
        if (CollectionUtils.hasData(attachmentsMimeBodyParts)) {
	        for (MimeBodyPart attachmentsMimeBodyPart : attachmentsMimeBodyParts) {
	        	mimeMultiPart.addBodyPart(attachmentsMimeBodyPart);
	        }
        }

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        message.setSubject(StringEncodeUtils.encode(Strings.removeNewlinesOrCarriageRetuns(mailMsg.getSubject()),
        										    charset)
        									.toString());
        message.setFrom(mailMsg.getFrom().asRFC822InternetAddressUsing(charset));
        message.setRecipients(javax.mail.Message.RecipientType.TO,
        					  EMailRFC822Address.multipleAsRFC822InternetAddress(mailMsg.getTo(),
        							   											 charset));
        message.setRecipients(javax.mail.Message.RecipientType.CC,
        					  EMailRFC822Address.multipleAsRFC822InternetAddress(mailMsg.getCc(),
        							   											 charset));
        message.setRecipients(javax.mail.Message.RecipientType.BCC,
        					  EMailRFC822Address.multipleAsRFC822InternetAddress(mailMsg.getBcc(),
        							   											 charset));
        message.setContent(mimeMultiPart);

        return message;
	}
	private static MimeBodyPart _createTextMimeBodyPart(final String plainTextBody,final String htmlBody,
														final Charset charset) throws MessagingException {
		// Define the text part.
		MimeBodyPart textMimeBodyPart = null;
		if (Strings.isNOTNullOrEmpty(plainTextBody)) {
	        textMimeBodyPart = new MimeBodyPart();
	        textMimeBodyPart.setContent(plainTextBody,
			        					Strings.customized("text/plain; charset={}",
			        									   charset.name()));
		}
        // Define the HTML part.
        MimeBodyPart htmlMimeBodyPart = null;
        if (Strings.isNOTNullOrEmpty(htmlBody)) {
	        htmlMimeBodyPart = new MimeBodyPart();
	        htmlMimeBodyPart.setContent(htmlBody,
			        					Strings.customized("text/html; charset={}",
			        									   charset.name()));
        }

        // return
        if (textMimeBodyPart != null
         && htmlMimeBodyPart != null) {
	        // Create a multipart/alternative child container that
	        // contains (wraps) the body and html parts
	        MimeMultipart htmlAndTextMimeMultiPart = new MimeMultipart("alternative");
	        htmlAndTextMimeMultiPart.addBodyPart(textMimeBodyPart);
	        htmlAndTextMimeMultiPart.addBodyPart(htmlMimeBodyPart);

	        MimeBodyPart htmlAndTextMimeBodyPart = new MimeBodyPart();
	        htmlAndTextMimeBodyPart.setContent(htmlAndTextMimeMultiPart);

	        return htmlAndTextMimeBodyPart;
        } else if (htmlMimeBodyPart != null) {
        	return htmlMimeBodyPart;
        } else if (textMimeBodyPart != null) {
        	return textMimeBodyPart;
        } else {
        	throw new IllegalArgumentException("Could NOT create a mime body part!");
        }
	}
	private static Collection<MimeBodyPart> _createAttachmentsMimeBodyPart(final Collection<EMailMessageAttachment> attachments) throws MessagingException {
		if (CollectionUtils.isNullOrEmpty(attachments)) return Lists.newArrayList();
		return FluentIterable.from(attachments)
							 .transform(new Function<EMailMessageAttachment,MimeBodyPart>() {
												@Override
												public MimeBodyPart apply(final EMailMessageAttachment attachment) {
													try {
														MimeBodyPart attachmentPart = new MimeBodyPart();
														//DataSource fds = new FileDataSource(filePath.asAbsoluteString());
														DataSource fds = new ByteArrayDataSource(attachment.getData(),
																								 attachment.getContentTypeOrDefault(MimeType.OCTECT_STREAM).asString());
														attachmentPart.setDataHandler(new DataHandler(fds));
														attachmentPart.setFileName(attachment.getName());
														attachmentPart.setDisposition(attachment.getDispositionTypeOrDefault(MimeBodyPartDispositionType.DISPOSITION_ATTACHMENT).asString());
														return attachmentPart;
													} catch (MessagingException msgEx) {
														log.error("Could NOT attach file with name={}: {}",
																  attachment.getName(),
																  msgEx.getMessage(),msgEx);
													}
													return null;
												}
										})
							 .filter(Predicates.notNull())
							 .toList();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static EMailRFC822Address emailMessageFromOf(final MimeMessage mimeMessage) throws MessagingException {
		EMailRFC822Address from = CollectionUtils.hasData(mimeMessage.getFrom())
									? EMailRFC822Address.fromRFC822Address(mimeMessage.getFrom()[0])
									: null;
		return from;
	}
	public static EMailDestinations emailMessageDestinationsOf(final MimeMessage mimeMessage) throws MessagingException {
		Collection<EMailRFC822Address> to = CollectionUtils.hasData(mimeMessage.getRecipients(RecipientType.TO))
												? EMailRFC822Address.multipleFromRFC822Address(Lists.newArrayList(mimeMessage.getRecipients(RecipientType.TO)))
												: null;
		Collection<EMailRFC822Address> cc = CollectionUtils.hasData(mimeMessage.getRecipients(RecipientType.CC))
												? EMailRFC822Address.multipleFromRFC822Address(Lists.newArrayList(mimeMessage.getRecipients(RecipientType.CC)))
												: null;
		Collection<EMailRFC822Address> bcc = CollectionUtils.hasData(mimeMessage.getRecipients(RecipientType.BCC))
												? EMailRFC822Address.multipleFromRFC822Address(Lists.newArrayList(mimeMessage.getRecipients(RecipientType.BCC)))
												: null;
	    return CollectionUtils.hasData(to) || CollectionUtils.hasData(cc) || CollectionUtils.hasData(bcc)
	    			? new EMailDestinations(to,cc,bcc)
	    			: null;
	}
	public static String emailSubjectOf(final MimeMessage mimeMessage) throws MessagingException {
		return mimeMessage.getSubject();
	}
	public static String[] emailBodyOf(final MimeMessage mimeMessage) throws MessagingException {
		String bodyText = null;
		String bodyHtml = null;
		try {
			Object msgCont = mimeMessage.getContent();
			if (msgCont instanceof String) {
				if (Strings.isContainedWrapper(mimeMessage.getContentType())
						   .in(MimeType.TEXT_PLAIN.getName())) {
					bodyText = (String)msgCont;
				} else if (Strings.isContainedWrapper(mimeMessage.getContentType())
						   		  .in(MimeType.HTML.getName(),
						   			  MimeType.XHTML.getName())) {
					bodyHtml = (String)msgCont;
				}
			}
			else if (msgCont instanceof Multipart) {
				Multipart mp = (Multipart)msgCont;
				for (int i=0; i < mp.getCount(); i++) {
					BodyPart bp = mp.getBodyPart(i);
					if (bp.getContent() instanceof String) {
						if (Strings.isContainedWrapper(bp.getContentType())
								   .in(MimeType.TEXT_PLAIN.getName())) {
							bodyText = (String)bp.getContent();
						} else if (Strings.isContainedWrapper(bp.getContentType())
								   		  .in(MimeType.HTML.getName(),
								   			  MimeType.XHTML.getName())) {
							bodyHtml = (String)bp.getContent();
						}
					}
					else if (bp.getContent() instanceof MimeMultipart) {
						MimeMultipart mmp = (MimeMultipart)bp.getContent();
						for (int j=0; j < mmp.getCount(); j++) {
							BodyPart mbp = mmp.getBodyPart(j);
							if (mbp.getContent() instanceof InputStream) continue;	// it's an attachment

							if (mbp.isMimeType(MimeType.TEXT_PLAIN.getName())) {
								bodyText = (String)mbp.getContent();
							} else if (mbp.isMimeType(MimeType.HTML.getName())
									|| bp.isMimeType(MimeType.XHTML.getName())) {
								bodyHtml = (String)mbp.getContent();
							}
						}
					}
				}
			} else if (msgCont instanceof InputStream) {
				// it's an attachment
			}
		} catch (IOException ioEx) {
			log.error("Could NOT get a mime message content: {}",
					  ioEx.getMessage(),ioEx);
		}
		return bodyText != null || bodyHtml != null
					? new String[] { bodyText,bodyHtml }
					: null;
	}
}
