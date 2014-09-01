package com.ctb.exception;

/**
 * Customized Exception class
 * @author TCS
 *
 */
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
