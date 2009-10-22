package com.ctb.exception.organizationManagement; 

import com.ctb.exception.CTBBusinessException;

/** 
 * OrgDataDeletedException.java
 * Exception thrown when Organization data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Tata Consultancy Services
 */

public class OrgDataDeletedException extends CTBBusinessException
{ 
    
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public OrgDataDeletedException (String message) {
        super(message);
    }
 
} 
