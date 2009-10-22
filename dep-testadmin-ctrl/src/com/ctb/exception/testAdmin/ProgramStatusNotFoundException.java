package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when program status corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author John_Wang
 */
public class ProgramStatusNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ProgramStatusNotFoundException(String message) {
        super(message);
    }
} 
