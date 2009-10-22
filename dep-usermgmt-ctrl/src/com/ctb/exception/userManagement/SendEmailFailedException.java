package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when user creation fails
 * 
 * @author Tata Consultancy Services
 *
 * 
 */

public class SendEmailFailedException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public SendEmailFailedException(String message) {
        super(message);
    }
} 
