package com.ctb.exception.dataExportManagement;

import com.ctb.exception.CTBBusinessException;

/**
 * Exception thrown when customer configuration data corresponding to request
 * parameters can not be obtained from the data store
 * 
 * @author TCS
 */

public class ProductDetailsNotFoundException extends CTBBusinessException {
	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public ProductDetailsNotFoundException(String message) {
        super(message);
    }
}
