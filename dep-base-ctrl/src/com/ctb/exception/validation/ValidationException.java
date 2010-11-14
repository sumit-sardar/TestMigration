package com.ctb.exception.validation; 

import com.ctb.exception.CTBBusinessException;

/**
 * ValidationException.java
 * @author Nate_Cohen
 *
 * Exception thrown when a platform request is found to be 
 * invalid/not authorized
 */
public class ValidationException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ValidationException(String message) {
        super(message);
    }
} 
