package com.ctb.exception.request; 

import com.ctb.exception.CTBBusinessException;

/**
 * InvalidFilterFieldException
 * @author Nate_Cohen
 *
 * Exception thrown when filtering is requested on a field
 * which does not exist in the data objects being filtered
 */
public class InvalidFilterFieldException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public InvalidFilterFieldException (String message) {
        super(message);
    }
}
