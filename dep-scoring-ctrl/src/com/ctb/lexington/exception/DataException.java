package com.ctb.lexington.exception;


public class DataException extends Exception {

	public DataException() {
		super();
	}


	public DataException(String message) {
		super(message);
	}

	public DataException(Exception cause) {
		super(cause);
	}

	public DataException(String message, Exception cause) {
		super(message, cause);
	}

}
