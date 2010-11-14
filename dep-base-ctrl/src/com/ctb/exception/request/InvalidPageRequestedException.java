package com.ctb.exception.request; 

import com.ctb.exception.CTBBusinessException;

/**
 * InvalidPageRequestedException
 * @author Nate_Cohen
 *
 * Exception thrown when a page is requested outside the
 * range of available pages of a result array
 */
public class InvalidPageRequestedException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public InvalidPageRequestedException (String message) {
        super(message);
    }
} 
