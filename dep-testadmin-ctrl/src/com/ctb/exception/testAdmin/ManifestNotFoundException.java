package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;
/**
 * Exception thrown when Student Manifest corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Nate_Cohen
 */
public class ManifestNotFoundException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Construct a new exception
	 * @param message
	 */
    public ManifestNotFoundException(String message) {
        super(message);
    }
} 
