package de.araneaconsult.idm.oauth;

public class ResponseException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private String error;
	
	public ResponseException(int errorCode, String error) {
		super(error.toString());
		
		this.errorCode = errorCode;
		this.error = error;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getError() {
		return error;
	}
}