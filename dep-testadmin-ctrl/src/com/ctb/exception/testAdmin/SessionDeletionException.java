package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when a new test session could not be
 * deleted using provided request parameters.
 * 
 * @author Nate_Cohen
 */
public class SessionDeletionException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Construct a new exception
	 * @param message
	 */
    public SessionDeletionException(String message) {
        super(message);
    }
} 
