package com.ctb.exception.customerManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * CustomerUpdationException.java
 * Exception thrown when customer data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class CustomerUpdationException extends CTBBusinessException{

        
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomerUpdationException(String message) {
        super(message);
    }
} 
