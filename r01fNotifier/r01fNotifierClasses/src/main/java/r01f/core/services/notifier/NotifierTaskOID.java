package r01f.core.services.notifier;

import java.util.UUID;

import com.google.common.annotations.GwtIncompatible;

import r01f.guids.OID;
import r01f.guids.OIDBaseImmutable;
import r01f.objectstreamer.annotations.MarshallType;

/**
 * A {@link NotifierMessageToBeDelivered} {@link OID}
 */
@GwtIncompatible("does not have de default no-args constructor")
@MarshallType(as="notifierRequest")
public class NotifierTaskOID
	 extends OIDBaseImmutable<String> {

	private static final long serialVersionUID = -8429215763429456025L;

	protected NotifierTaskOID(final String id) {
		super(id);
	}
	public static NotifierTaskOID forId(final String id) {
		return new NotifierTaskOID(id);
	}
	public static NotifierTaskOID valueOf(final String id) {
		return new NotifierTaskOID(id);
	}
	public static NotifierTaskOID supply() {
		UUID uuid = UUID.randomUUID();
        return NotifierTaskOID.forId(uuid.toString().toUpperCase());
	}
}
