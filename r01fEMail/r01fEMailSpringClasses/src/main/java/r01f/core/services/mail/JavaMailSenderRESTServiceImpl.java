package r01f.core.services.mail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.httpclient.HttpClientProxySettings;
import r01f.httpclient.HttpRequestFormParameter;
import r01f.httpclient.HttpRequestFormParameterForText;
import r01f.httpclient.HttpRequestPayload;
import r01f.httpclient.HttpResponse;
import r01f.httpclient.HttpResponseCode;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.Strings;

@Slf4j
public class JavaMailSenderRESTServiceImpl
     extends JavaMailSenderBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Url _restServiceEndPointUrl;
	@Getter private final HttpClientProxySettings _proxySettings;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public JavaMailSenderRESTServiceImpl(final Url restServiceEndpointUrl ,
									     final HttpClientProxySettings proxySettings) {
		_restServiceEndPointUrl = restServiceEndpointUrl;
		_proxySettings = proxySettings;
	}
	public static JavaMailSender create(final Url restServiceEndPointUrl,
										final HttpClientProxySettings proxySettings) {
		if (Strings.isNullOrEmpty(restServiceEndPointUrl.asString())) throw new IllegalArgumentException("Invalid URL for Third Party Mail Sender");
		JavaMailSender outJavaMailSender =  new JavaMailSenderRESTServiceImpl(restServiceEndPointUrl,
																			  proxySettings);
		return outJavaMailSender;
	}
	public static JavaMailSender create(final Url restServiceEndPointUrl) {
		return JavaMailSenderRESTServiceImpl.create(restServiceEndPointUrl,
					  								null);	// no proxy
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SIMPLE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void send(final SimpleMailMessage... simpleMessages) throws MailException {
		for (SimpleMailMessage simpleMessage : simpleMessages) {
			EMail from = EMail.of(simpleMessage.getFrom());
			EMail[] to = FluentIterable.from(simpleMessage.getTo())
									   .transform(EMail.FROM_STRING_TRANSFORM)
									   .toArray(EMail.class);
			String text = simpleMessage.getText();
			String subject = simpleMessage.getSubject();
			_doSend(from,to,
					subject,text);
		}
	}
	private void _doSend(final EMail from,final EMail[] to,
						 final String subject,final String text) {
		List<HttpRequestFormParameter> parameters = new ArrayList<HttpRequestFormParameter>();
		if (from != null) parameters.add(HttpRequestFormParameterForText.of(from.asString())
														  				.withName("from"));
		parameters.add(HttpRequestFormParameterForText.of(to[0].asString())
													  .withName("to"));
		parameters.add(HttpRequestFormParameterForText.of(subject)
													  .withName("subject"));
		parameters.add(HttpRequestFormParameterForText.of(text)
													  .withName("messageText"));
		HttpResponse response = null;
		try {
			log.debug("[JavaMailSender (rest service)]: URL {}",
					  _restServiceEndPointUrl);
			if (_proxySettings != null) {
				log.debug("[JavaMailSender (rest service)]: using proxy");
				response = HttpClient.forUrl(_restServiceEndPointUrl)
									 .POSTForm()
									 .withPOSTFormParameters(parameters)
									 .getResponse()
									 .usingProxy(_proxySettings).withoutTimeOut().noAuth();
			} else {
				log.debug("[JavaMailSender (rest service)]: NO proxy");
				response = HttpClient.forUrl(_restServiceEndPointUrl)
									 .POSTForm()
									 .withPOSTFormParameters(parameters)
									 .getResponse()
									 .notUsingProxy().withoutTimeOut().noAuth();
			}
			if (!response.getCode().isIn(HttpResponseCode.OK)) {
				log.warn("[JavaMailSender (rest service)]: HTTP mail service response Code : {} > {}",
						 response.getCode(),response.loadAsString());
				throw new JavaMailSenderRESTServiceImplException(Strings.customized("[JavaMailSender (rest service)]: Remote Server Error: {}",
																					   response.loadAsString()));
			}
		} catch (MalformedURLException e) {
			log.error("[JavaMailSender (rest service)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("[JavaMailSender (rest service)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		} catch (Throwable e) {
			log.error("[JavaMailSender (rest service)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MIME
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void doSend(final MimeMessage[] mimeMessages, @Nullable final Object[] originalMessages) throws MailException {
		for (MimeMessage mimeMessage : mimeMessages) {
			try {
				_doSendMimeMessage(mimeMessage);
			} catch (Throwable msgEx) {
				log.error("Error while sending mail message: {}",
					      msgEx.getMessage(),msgEx);
			}
		}
	}
	private void _doSendMimeMessage(final MimeMessage mimeMessage) throws IOException,
																		  MessagingException {
		EMail[] to = FluentIterable.from(mimeMessage.getFrom())
								   .transform(new Function<Address,EMail>() {
													@Override
													public EMail apply(final Address addr) {
														return EMail.of(addr.toString());
													}
								   			  })
								   .toArray(EMail.class);

	    EMail[] from = FluentIterable.from(mimeMessage.getRecipients(RecipientType.TO))
								   .transform(new Function<Address,EMail>() {
													@Override
													public EMail apply(final Address addr) {
														return EMail.of(addr.toString());
													}
								   			  })
								   .toArray(EMail.class);
		String subject = mimeMessage.getSubject();

		log.debug("[JavaMailSender (rest service)]: MIME MESSAGE SUPPORTED");
		InputStream is = mimeMessage.getInputStream();
		try {
			Url url = _restServiceEndPointUrl.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("to",to[0]),
																				UrlQueryStringParam.of("from",from[0]),
																				UrlQueryStringParam.of("subject",subject)));
			HttpResponse response = null;
			if (_proxySettings != null) {
				 response = HttpClient.forUrl(url)
							      .POST()
							           .withPayload(HttpRequestPayload.wrap(is))
							       .getResponse()
							       		.usingProxy(_proxySettings).withoutTimeOut().noAuth();
			} else {
				response = HttpClient.forUrl(_restServiceEndPointUrl)
							      .POST()
							           .withPayload(HttpRequestPayload.wrap(is))
							       .getResponse()
							       		.notUsingProxy().withoutTimeOut().noAuth();
			}
			if (!response.getCode().isIn(HttpResponseCode.OK)) {
				log.error("[JavaMailSender (rest service)] > ERROR Response Code {}",
						  response.getCode(),response.loadAsString() );
				throw new JavaMailSenderRESTServiceImplException(Strings.customized("Remote Server Error at rest Mail Service {}",
																					   response.loadAsString()));
			}
		} catch (MalformedURLException e) {
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		} catch (IOException e) {
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		} catch (Throwable e) {
			throw new JavaMailSenderRESTServiceImplException(e.getLocalizedMessage());
		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 	INNER CLASSES
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class JavaMailSenderRESTServiceImplException
		 extends MailException {
		private static final long serialVersionUID = -8313498571229772866L;
		public JavaMailSenderRESTServiceImplException(final String msg) {
			super(msg);
		}
	}
}
