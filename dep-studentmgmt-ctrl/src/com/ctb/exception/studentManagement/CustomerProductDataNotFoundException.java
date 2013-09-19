/**
 * 
 */
package com.ctb.exception.studentManagement;

import com.ctb.exception.CTBBusinessException;

/**
 * @author DIPAK
 */
public class CustomerProductDataNotFoundException extends CTBBusinessException {

	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
	public CustomerProductDataNotFoundException(String message) {
	    super(message);
	}
}
