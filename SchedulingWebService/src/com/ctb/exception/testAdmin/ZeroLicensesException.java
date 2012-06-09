package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

public class ZeroLicensesException extends CTBBusinessException
{ 
    static final long serialVersionUID = 1L;
    /**
	 * Construct a new exception
	 * @param message
	 */
    public ZeroLicensesException(String message) {
        super(message);
    }
}