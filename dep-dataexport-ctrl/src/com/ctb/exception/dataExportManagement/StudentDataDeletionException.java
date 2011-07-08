package com.ctb.exception.dataExportManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * StudentDeletionException.java
 * @author John_Wang
 *
 * Exception thrown when student data deletion fails
 */
public class StudentDataDeletionException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public StudentDataDeletionException(String message) {
        super(message);
    }
} 