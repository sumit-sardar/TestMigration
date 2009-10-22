package com.ctb.exception.uploadDownloadManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * DeleteFileException.java
 * Exception thrown when user data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class DeleteFileException extends CTBBusinessException { 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public DeleteFileException(String message) {
        super(message);
    }
} 
