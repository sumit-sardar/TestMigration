package com.ctb.exception.licensing; 

import com.ctb.exception.CTBBusinessException;

public class OrgLicenseDataNotFoundException extends CTBBusinessException
{ 
    
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
	
    public OrgLicenseDataNotFoundException(String message){
        super(message);
    }
} 
