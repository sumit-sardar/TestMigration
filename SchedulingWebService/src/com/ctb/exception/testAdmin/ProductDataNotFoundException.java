package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when Product data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author Nate_Cohen
 */
public class ProductDataNotFoundException extends CTBBusinessException
{
    static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ProductDataNotFoundException(String message) {
        super(message);
    }
} 
