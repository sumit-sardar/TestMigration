package com.ctb.exception.frameWorkManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * FrameWorkUpdationException.java
 * Exception thrown when frameWork data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class FrameWorkUpdationException extends CTBBusinessException{

        
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public FrameWorkUpdationException(String message) {
        super(message);
    }
} 
