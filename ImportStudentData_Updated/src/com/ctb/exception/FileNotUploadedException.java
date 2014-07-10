package com.ctb.exception;

public class FileNotUploadedException extends CTBBusinessException{
	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public FileNotUploadedException (String message) {
        super(message);
    }
}
