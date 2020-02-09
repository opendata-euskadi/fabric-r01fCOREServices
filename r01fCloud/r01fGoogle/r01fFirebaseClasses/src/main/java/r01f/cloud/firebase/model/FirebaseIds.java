package r01f.cloud.firebase.model;

import r01f.guids.OIDBaseImmutable;
import r01f.objectstreamer.annotations.MarshallType;

public abstract class FirebaseIds {
	/**
	 * A token for a registred device token.
	 * @author PCI
	 *
	 */
	@MarshallType(as="registredDeviceToken")
	public static class RegistredDeviceToken
		extends OIDBaseImmutable<String> {
		private static final long serialVersionUID = -5867457273405673410L;

		private RegistredDeviceToken(final String id) {
			super(id);
		}
		public static RegistredDeviceToken of(final String id) {
			return new RegistredDeviceToken(id);
		}
	}
	/**
	 * A topic represents a group of one or more devices which are subscribed to.
	 * @author PCI
	 *
	 */
	@MarshallType(as="registredDevicesTopic")
	public static class RegistredDevicesTopic
				extends OIDBaseImmutable<String> {

		private static final long serialVersionUID = -5867457273405673410L;
		private RegistredDevicesTopic(final String id) {
			super(id);
		}
		public static RegistredDevicesTopic of(final String id) {
			return new RegistredDevicesTopic(id);
		}
	}

}
