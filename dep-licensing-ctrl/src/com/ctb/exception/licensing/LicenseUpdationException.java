package com.ctb.exception.licensing; 

import com.ctb.exception.CTBBusinessException;

public class LicenseUpdationException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    
    /**
	 * Construct a new exception
	 * @param message
	 */
    public LicenseUpdationException(String message) {
        super(message);
    }
} 


