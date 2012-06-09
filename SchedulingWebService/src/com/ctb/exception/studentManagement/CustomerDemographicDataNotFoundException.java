package com.ctb.exception.studentManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when customer demographic data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author John_Wang
 */
public class CustomerDemographicDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomerDemographicDataNotFoundException(String message) {
        super(message);
    }
} 