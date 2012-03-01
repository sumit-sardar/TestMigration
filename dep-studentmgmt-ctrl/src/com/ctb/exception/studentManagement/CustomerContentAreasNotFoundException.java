package com.ctb.exception.studentManagement;

import com.ctb.exception.CTBBusinessException;

public class CustomerContentAreasNotFoundException extends CTBBusinessException{
	
	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomerContentAreasNotFoundException(String message) {
        super(message);
    }

}
