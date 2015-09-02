package cn.com.esrichina.gisdrive.exceptions;

public class GisdriveException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1210437040837412886L;

	public GisdriveException(String message) {
		super(message);
	}

	public GisdriveException(Throwable t) {
		super(t);
		this.setStackTrace(t.getStackTrace());
	}

	public GisdriveException(String message, Throwable t) {
		super(message, t);
		this.setStackTrace(t.getStackTrace());
	}
}
