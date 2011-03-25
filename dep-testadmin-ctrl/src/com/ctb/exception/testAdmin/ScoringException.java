package com.ctb.exception.testAdmin;

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when scoring data corresponding to request
 * parameters can not be obtained from the data store
 * @author TCS
 */
public class ScoringException extends CTBBusinessException {

	private static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ScoringException(String message) {
        super(message);
    }
}
