package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;
/**
 * Exception thrown when failed to add the student to 
 * a session in the data store
 * 
 * @author Nate_Cohen
 */
public class StudentNotAddedToSessionException extends CTBBusinessException
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public StudentNotAddedToSessionException(String message) {
        super(message);
    }
} 
