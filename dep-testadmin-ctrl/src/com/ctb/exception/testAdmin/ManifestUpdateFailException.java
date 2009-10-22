package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;
/**
 * Exception thrown when Manifest of a student has not been 
 * updated in the data store
 * 
 * @author Nate_Cohen
 */
public class ManifestUpdateFailException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ManifestUpdateFailException(String message) {
        super(message);
    }
} 
