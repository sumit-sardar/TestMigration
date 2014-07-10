package com.ctb.exception;

public class FileHeaderException extends CTBBusinessException { 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public FileHeaderException(String message) {
        super(message);
    }
}
