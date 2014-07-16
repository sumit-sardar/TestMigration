package com.ctb.exception;

/**
 * Customized Exception Class 
 * @author TCS
 *
 */
public class CTBBusinessException extends Exception{ 
    static final long serialVersionUID = 1L;    
    private String message;    
    public String getMessage() {
    	return message;
    }    
    public void setMessage(String message) {
    	this.message = message;
    }
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CTBBusinessException(String message) {
        super(message);
        this.setMessage(message); 
    }
}
