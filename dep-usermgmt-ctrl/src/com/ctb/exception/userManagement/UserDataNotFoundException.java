package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * UserDataNotFoundException.java
 * Exception thrown when user data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultency Services
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
