package r01f.core.services.mail.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.types.CanBeRepresentedAsString;
import r01f.types.contact.ContactMean;
import r01f.types.contact.EMail;
import r01f.util.types.collections.CollectionUtils;


/**
 * Represents a RFC822 address
 */
@Slf4j
@Accessors(prefix="_")
@RequiredArgsConstructor
public class EMailRFC822Address
  implements Serializable,
  			 CanBeRepresentedAsString,
  			 ContactMean {

	private static final long serialVersionUID = -2963399480304370901L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final EMail _email;
    @Getter private final String _name;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
    public static EMailRFC822Address of(final EMail email) {
    	return new EMailRFC822Address(email,null);
    }
    public static EMailRFC822Address of(final EMail email,final String name) {
    	return new EMailRFC822Address(email,name);
    }
    public static EMailRFC822Address of(final String email) {
    	return new EMailRFC822Address(EMail.of(email),null);
    }
    public static EMailRFC822Address of(final String email,final String name) {
    	return new EMailRFC822Address(EMail.of(email),name);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
    	return this.asString();
    }
	@Override
	public String asString() {
		return this.asRFC822Address();
	}
    public String asRFC822Address() {
    	return EMailRFC822Address.asRFC822Address(this);
    }
    public InternetAddress asRFC822InternetAddress() {
    	return this.asRFC822InternetAddressUsing(Charset.defaultCharset());
    }
    public InternetAddress asRFC822InternetAddressUsing(final Charset charset) {
    	return EMailRFC822Address.asRFC822InternetAddress(this,
    													  charset);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	TRANSFORM
/////////////////////////////////////////////////////////////////////////////////////////
    public static InternetAddress asRFC822InternetAddress(final EMailRFC822Address addr,
    													  final Charset charset) {
    	try {
			return new InternetAddress(addr.getEmail().asString(),
									   addr.getName(),
									   charset.name());
		} catch (UnsupportedEncodingException e) {
			log.error("Error converting {} email={} name={} to {}: {}",
					  EMailRFC822Address.class.getSimpleName(),
					  addr.getEmail(),addr.getName(),
					  InternetAddress.class.getSimpleName(),
					  e.getMessage(),e);
		}
    	return null;
    }
    public static InternetAddress[] multipleAsRFC822InternetAddress(final Collection<EMailRFC822Address> addrs) {
    	return EMailRFC822Address.multipleAsRFC822InternetAddress(addrs,
    															  Charset.defaultCharset());
    }
    public static InternetAddress[] multipleAsRFC822InternetAddress(final Collection<EMailRFC822Address> addrs,
    													  		    final Charset charset) {
    	if (CollectionUtils.isNullOrEmpty(addrs)) return null;
    	return FluentIterable.from(addrs)
    						 .transform(new Function<EMailRFC822Address,InternetAddress>() {
												@Override
												public InternetAddress apply(final EMailRFC822Address addr) {
													return EMailRFC822Address.asRFC822InternetAddress(addr,
																									  charset);
												}
    						 			})
    						 .toArray(InternetAddress.class);
    }
    public static String asRFC822Address(final EMailRFC822Address addr) {
        // rfc822 format
        return String.format("\"%s\" <%s>",
        					 addr.getName() != null ? addr.getName() : addr.getEmail(),addr.getEmail());
    }
    public static String multipleAsRFC822Address(final Collection<EMailRFC822Address> addrs) {
    	return CollectionUtils.toStringCommaSeparated(addrs);
    }
    public static EMailRFC822Address fromRFC822AddressString(final String addr) {
    	Collection<InternetAddress> iAddrs = _parseInternetAddress(addr);
    	if (iAddrs == null) throw new IllegalArgumentException(addr + " is NOT a valid rfc822 address as xx@domain.com <name>");
    	if (iAddrs.size() > 1) throw new IllegalArgumentException("This method only accepts a single rfc822 address formatted as xx@domain.com <name>");
    	return EMailRFC822Address.fromRFC822Address(Iterables.get(iAddrs,0));
    }
    public static EMailRFC822Address[] multipleFromRFC822AddressString(final String addr) {
    	Collection<InternetAddress> iAddrs = _parseInternetAddress(addr);
    	if (iAddrs == null) throw new IllegalArgumentException(addr + " is NOT a valid rfc822 address of comma separated addresses as xx@domain.com <name>");
    	return FluentIterable.from(iAddrs)
    						 .transform(new Function<InternetAddress,EMailRFC822Address>() {
												@Override
												public EMailRFC822Address apply(final InternetAddress iAddr) {
													return EMailRFC822Address.fromRFC822Address(iAddr);
												}
    						 			})
    						 .toArray(EMailRFC822Address.class);
    }
    private static Collection<InternetAddress> _parseInternetAddress(final String addr) {
    	InternetAddress[] iAddr = null;
    	try {
    		iAddr = InternetAddress.parse(addr,true);
    	} catch (AddressException addrEx) {
    		log.error("Could not parse RFC822 email address {}: {}",
    				  addr,
    				  addrEx.getMessage(),addrEx);
    	}
    	return iAddr != null ? Lists.newArrayList(iAddr)
    						 : null;
    }
    public static EMailRFC822Address fromRFC822Address(final Address addr) {
    	if (!(addr instanceof InternetAddress)) throw new IllegalArgumentException();
		InternetAddress iAddr = ((InternetAddress)addr);
		return new EMailRFC822Address(EMail.of(iAddr.getAddress()),
									  iAddr.getPersonal());
    }
    public static Collection<EMailRFC822Address> multipleFromRFC822Address(final Collection<Address> addrs) {
    	if (CollectionUtils.isNullOrEmpty(addrs)) return null;
    	return FluentIterable.from(addrs)
    						.transform(new Function<Address,EMailRFC822Address>() {
												@Override
												public EMailRFC822Address apply(final Address addr) {
													return EMailRFC822Address.fromRFC822Address(addr);
												}
    								   })
    						.toList();
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	TRANSFORM
/////////////////////////////////////////////////////////////////////////////////////////
	public static final Function<String,EMailRFC822Address> FROM_STRING_TRANSFORM = new Function<String,EMailRFC822Address>() {
																							@Override
																							public EMailRFC822Address apply(final String email) {
																								return EMailRFC822Address.fromRFC822AddressString(email);
																							}
																			  	    };
	public static final Function<EMailRFC822Address,String> TO_STRING_TRANSFORM = new Function<EMailRFC822Address,String>() {
																						@Override
																						public String apply(final EMailRFC822Address email) {
																							return email.asRFC822Address();
																						}
																			  	   };
}
