package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * TestAdminDataNotFoundException.java
 * @author Nate_Cohen
 *
 * Exception thrown when Test Admin data corresponding to request
 * parameters can not be obtained from the data store
 */
public class TestAdminDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public TestAdminDataNotFoundException(String message) {
        super(message);
    }
} 
