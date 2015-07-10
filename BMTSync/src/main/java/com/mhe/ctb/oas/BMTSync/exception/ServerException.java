package com.mhe.ctb.oas.BMTSync.exception;

public class ServerException extends Exception {

	private static final long serialVersionUID = 4172409459179156593L;

	public ServerException() {
		super();
	}
	
	public ServerException(String message) {
		super(message);
	}
	
	public ServerException(Exception e) {
		super(e);
	}
	
	public ServerException(String message, Exception e) {
		super(message, e);
	}
}
