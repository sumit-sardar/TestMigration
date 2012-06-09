package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when a new test session could not be
 * created using provided request parameters.
 * 
 * @author Nate_Cohen
 */
public class TransactionTimeoutException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Construct a new exception
	 * @param message
	 */
    public TransactionTimeoutException(String message) {
        super(message);
    }
} 
