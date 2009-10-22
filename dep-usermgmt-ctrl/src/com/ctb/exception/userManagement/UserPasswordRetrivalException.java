package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when user password retrival fails
 * @author Tata Consultancy Services
 *
 * 
 */

public class UserPasswordRetrivalException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UserPasswordRetrivalException(String message) {
        super(message);
    }
} 