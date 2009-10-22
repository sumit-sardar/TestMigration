package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;
/**
 * Exception thrown when user data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Mallik_Korivi
 */
public class InvalidNoOfProgramsException extends CTBBusinessException
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public InvalidNoOfProgramsException(String message) {
        super(message);
    }
} 
