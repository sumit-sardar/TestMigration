package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when change Password fails
 * 
 * @author Tata Consultancy Services
 *
 * 
 */

public class UserPasswordUpdateException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UserPasswordUpdateException(String message) {
        super(message);
    }
} 