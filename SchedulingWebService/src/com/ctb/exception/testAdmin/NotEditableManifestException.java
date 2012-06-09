package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;
/**
 * Exception thrown when  student test session completion status is other than 'SC' 
 * and his manifest has been trid to update 
 * 
 * @author Nate_Cohen
 */
public class NotEditableManifestException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Construct a new exception
	 * @param message
	 */
    public NotEditableManifestException(String message) {
        super(message);
    }
} 
 
