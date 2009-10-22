package com.ctb.exception.customerManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * CustomerDataNotFoundException.java
 * Exception thrown when Customer data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class CustomerDataNotFoundException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomerDataNotFoundException(String message) {
        super(message);
    }
} 
