package com.ctb.exception;

/**
 * Customized Exception class
 * @author TCS
 *
 */
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
