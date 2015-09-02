package cn.com.esrichina.gcloud;

public class GCloudServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public GCloudServiceException() {
		super();
	}

	public GCloudServiceException(String message) {
		super(message);
	}

	public GCloudServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public GCloudServiceException(Throwable cause) {
		super(cause);
	}
}