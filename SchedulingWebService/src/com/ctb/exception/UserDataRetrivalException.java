package com.ctb.exception; 

import com.ctb.exception.CTBBusinessException;

/**
 * UserDataRetrivalException.java
 * @author Tata Consultency Services
 *
 * Exception thrown when user creation fails
 */
public class UserDataRetrivalException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UserDataRetrivalException(String message) {
        super(message);
    }
} 