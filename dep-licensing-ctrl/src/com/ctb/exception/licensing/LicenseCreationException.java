package com.ctb.exception.licensing; 

import com.ctb.exception.CTBBusinessException;

public class LicenseCreationException extends CTBBusinessException
{ 
 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public LicenseCreationException(String message) {
        super(message);
    }
} 
