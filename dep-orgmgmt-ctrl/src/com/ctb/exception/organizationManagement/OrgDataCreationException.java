package com.ctb.exception.organizationManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * OrgDataCreationException.java
 * Exception thrown when Organization data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class OrgDataCreationException extends CTBBusinessException
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public OrgDataCreationException(String message) {
        super(message);
    }
} 
