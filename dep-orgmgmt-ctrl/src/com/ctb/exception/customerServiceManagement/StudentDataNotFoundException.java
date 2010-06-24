package com.ctb.exception.customerServiceManagement;

import com.ctb.exception.CTBBusinessException;

/** 
 * OrgDataNotFoundException.java
 * Exception thrown when Organization data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */


public class StudentDataNotFoundException extends CTBBusinessException
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public StudentDataNotFoundException(String message) {
        super(message);
    }
} 