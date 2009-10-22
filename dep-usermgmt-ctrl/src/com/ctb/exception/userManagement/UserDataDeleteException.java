package com.ctb.exception.userManagement; 

import com.ctb.exception.CTBBusinessException;
/**
 * UserDataDeleteException.java
 * @author Tata Consultency Services
 *
 * Exception thrown when user deletion fails
 */

public class UserDataDeleteException extends CTBBusinessException {
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public UserDataDeleteException (String message) {
        super(message);
    }
} 