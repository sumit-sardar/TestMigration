package com.ctb.exception.studentManagement; 

import com.ctb.exception.CTBBusinessException;

/**
 * UnsupportedFilterParamException.java
 * @author John_Wang
 *
 * Exception thrown when an unsupported filter parameter is used for dynamic sql generation
 */
public class UnsupportedFilterParamException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UnsupportedFilterParamException(String message) {
        super(message);
    }
} 