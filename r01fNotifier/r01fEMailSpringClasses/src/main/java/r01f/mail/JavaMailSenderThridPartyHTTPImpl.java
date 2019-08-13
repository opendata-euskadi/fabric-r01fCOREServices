package r01f.mail;

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
public class JavaMailSenderThridPartyHTTPImpl
     extends JavaMailSenderBase {

/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final HttpClientProxySettings _proxySettings;
	@Getter private final Url _thirdPartyProviderUrl;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public JavaMailSenderThridPartyHTTPImpl(final Url thirdPartyProviderUrl ,
											final HttpClientProxySettings proxySettings) {
		_thirdPartyProviderUrl = thirdPartyProviderUrl;
		_proxySettings = proxySettings;
	}
	/**
	 * Creates a {@link JavaMailSender} to send an email using a http enabled Third Party Mail Sender
	 * @param host
	 * @param port
	 * @return
	 */
	public static JavaMailSender create(final Url thirdPartyProviderUri,
										final HttpClientProxySettings proxySettings) {
		if (Strings.isNullOrEmpty(thirdPartyProviderUri.asString())) throw new IllegalArgumentException("Invalid URL for Third Party Mail Sender");
		JavaMailSender outJavaMailSender =  new JavaMailSenderThridPartyHTTPImpl(thirdPartyProviderUri,
																				 proxySettings);
		return outJavaMailSender;
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
			log.debug("[JavaMailSender (3rd party http)]: URL {}",
					  _thirdPartyProviderUrl);
			if (_proxySettings != null) {
				log.debug("[JavaMailSender (3rd party http)]: using proxy");
				response = HttpClient.forUrl(_thirdPartyProviderUrl)
									 .POSTForm()
									 .withPOSTFormParameters(parameters)
									 .getResponse()
									 .usingProxy(_proxySettings).withoutTimeOut().noAuth();
			} else {
				log.debug("[JavaMailSender (3rd party http)]: NO proxy");
				response = HttpClient.forUrl(_thirdPartyProviderUrl)
									 .POSTForm()
									 .withPOSTFormParameters(parameters)
									 .getResponse()
									 .notUsingProxy().withoutTimeOut().noAuth();
			}
			if (!response.getCode().isIn(HttpResponseCode.OK)) {
				log.warn("[JavaMailSender (3rd party http)]: HTTP mail service response Code : {} > {}",
						 response.getCode(),response.loadAsString());
				throw new JavaMailSenderThridPartyHTTPImplException(Strings.customized("[JavaMailSender (3rd party http)]: Remote Server Error: {}",
																					   response.loadAsString()));
			}
		} catch (MalformedURLException e) {
			log.error("[JavaMailSender (3rd party http)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("[JavaMailSender (3rd party http)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
		} catch (Throwable e) {
			log.error("[JavaMailSender (3rd party http)]: Error > {}",
					  e.getMessage(),e);
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
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

		log.debug("[JavaMailSender (3rd party http)]: MIME MESSAGE SUPPORTED");
		InputStream is = mimeMessage.getInputStream();
		try {
			Url url = _thirdPartyProviderUrl.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("to",to[0]),
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
				response = HttpClient.forUrl(_thirdPartyProviderUrl)
							      .POST()
							           .withPayload(HttpRequestPayload.wrap(is))
							       .getResponse()
							       		.notUsingProxy().withoutTimeOut().noAuth();
			}
			if (!response.getCode().isIn(HttpResponseCode.OK)) {
				log.error("[JavaMailSender (3rd party http)] > ERROR Response Code {}",
						  response.getCode(),response.loadAsString() );
				throw new JavaMailSenderThridPartyHTTPImplException(Strings.customized("Remote Server Error int HTTP Mail Service {}",response.loadAsString()));
			}
		} catch (MalformedURLException e) {
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
		} catch (IOException e) {
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
		} catch (Throwable e) {
			throw new JavaMailSenderThridPartyHTTPImplException(e.getLocalizedMessage());
		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// INNER CLASSES
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class JavaMailSenderThridPartyHTTPImplException
		 extends MailException {
		private static final long serialVersionUID = -8313498571229772866L;
		public JavaMailSenderThridPartyHTTPImplException(final String msg) {
			super(msg);
		}
	}
}
