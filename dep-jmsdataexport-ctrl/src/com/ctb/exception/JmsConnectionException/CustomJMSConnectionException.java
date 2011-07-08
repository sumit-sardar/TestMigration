package com.ctb.exception.JmsConnectionException;

import com.ctb.exception.CTBBusinessException;





public class CustomJMSConnectionException extends CTBBusinessException
{ 
 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomJMSConnectionException(String message) {
        super(message);
    }
}
