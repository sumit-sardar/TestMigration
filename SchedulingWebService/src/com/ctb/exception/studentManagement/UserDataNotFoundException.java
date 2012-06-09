package com.ctb.exception.studentManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when user data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author John_Wang
 */
public class UserDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UserDataNotFoundException(String message) {
        super(message);
    }
} 
