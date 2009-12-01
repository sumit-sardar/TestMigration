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
    
    private String message;
    
    public String getMessage() {
    	return message;
    }
    
    public void setMessage(String message) {
    	System.out.println("setmessage==>"+message);
    	this.message = message;
    }
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CTBBusinessException(String message) {
        super(message);
        this.setMessage(message);  //Changed for defect 60480 ,60488 ,60489 ,60490 ,60491
        OASLogger.getLogger("TestAdmin").debug(message, this);
    }
} 
