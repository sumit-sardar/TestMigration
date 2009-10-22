package com.ctb.exception.frameWorkManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * FrameWorkCreationException.java
 * Exception thrown when FrameWork data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class FrameWorkCreationException extends CTBBusinessException{ 
   
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public FrameWorkCreationException(String message) {
        super(message);
    }
} 
