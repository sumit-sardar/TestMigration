package com.ctb.exception.userManagement; 


import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when customer configuration data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author TCS
 */
public class CustomerReportDataNotFoundException  extends CTBBusinessException
{ 
     static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public CustomerReportDataNotFoundException(String message) {
        super(message);
    }
} 
