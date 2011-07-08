package com.ctb.exception.dataExportManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when student data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author John_Wang
 */
public class JobDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public JobDataNotFoundException(String message) {
        super(message);
    }
} 
