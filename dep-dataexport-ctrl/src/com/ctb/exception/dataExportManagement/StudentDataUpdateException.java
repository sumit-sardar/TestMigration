package com.ctb.exception.dataExportManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * StudentDataUpdateException.java
 * @author John_Wang
 *
 * Exception thrown when student data update fails
 */
public class StudentDataUpdateException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public StudentDataUpdateException(String message) {
        super(message);
    }
} 