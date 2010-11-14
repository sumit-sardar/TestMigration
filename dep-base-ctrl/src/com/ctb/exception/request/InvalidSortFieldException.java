package com.ctb.exception.request; 

import com.ctb.exception.CTBBusinessException;

/**
 * InvalidSortFieldException
 * @author Nate_Cohen
 *
 * Exception thrown when sorting is requested on a field
 * which does not exist in the data objects being sorted
 */
public class InvalidSortFieldException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public InvalidSortFieldException (String message) {
        super(message);
    }
}
