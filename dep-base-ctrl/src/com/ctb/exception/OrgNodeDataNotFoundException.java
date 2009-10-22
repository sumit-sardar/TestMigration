package com.ctb.exception; 

import com.ctb.exception.CTBBusinessException;

/**
 * OrgNodeDataNotFoundException.java
 * @author TCS
 *
 * Exception thrown when org node data corresponding to request
 * parameters can not be obtained from the data store
 */
public class OrgNodeDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public OrgNodeDataNotFoundException(String message) {
        super(message);
    }
} 
