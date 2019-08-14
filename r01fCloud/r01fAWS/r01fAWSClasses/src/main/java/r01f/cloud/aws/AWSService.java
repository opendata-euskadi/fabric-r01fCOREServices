package r01f.cloud.aws;

public enum AWSService {
	SES,		// Simple email service
	SNS;		// Simple notification service

	public String nameLowerCase() {
		return this.name().toLowerCase();
	}
}
