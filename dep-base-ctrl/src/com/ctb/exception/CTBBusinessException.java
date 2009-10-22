package com.ctb.exception; 

import com.ctb.util.OASLogger;

/**
 * CTBBusinessException
 * @author Nate_Cohen
 *
 * Generic business exception type, all platform exceptions extend this
 */
public class CTBBusinessException extends Exception
{ 
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CTBBusinessException(String message) {
        super(message);
        OASLogger.getLogger("TestAdmin").debug(message, this);
    }
} 
