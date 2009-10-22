package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * RosterDataNotFoundException.java
 * @author Nate_Cohen
 *
 * Exception thrown when Roster data corresponding to request
 * parameters can not be obtained from the data store
 */
public class RosterDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public RosterDataNotFoundException(String message) {
        super(message);
    }
} 
