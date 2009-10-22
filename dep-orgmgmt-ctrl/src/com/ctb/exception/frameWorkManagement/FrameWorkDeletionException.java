package com.ctb.exception.frameWorkManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * FrameWorkDeletionException.java
 * Exception thrown when frameWork data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */


public class FrameWorkDeletionException extends CTBBusinessException{

    
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public FrameWorkDeletionException(String message) {
        super(message);
    }
} 
