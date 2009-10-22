package com.ctb.exception.studentManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * StudentCreationException.java
 * @author John_Wang
 *
 * Exception thrown when student creation fails
 */
public class StudentDataCreationException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public StudentDataCreationException(String message) {
        super(message);
    }
} 