package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when user data update fails
 * @author Tata Consultancy Services
 *
 * 
 */

public class DataManipulationException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public DataManipulationException(String message) {
        super(message);
    }
} 