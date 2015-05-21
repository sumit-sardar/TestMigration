package com.ctb.exception.testAdmin;

import com.ctb.exception.CTBBusinessException;

public class ProductResourceDataNotFound extends CTBBusinessException {

	static final long serialVersionUID = 1L;

	/**
	 * Construct a new exception
	 * 
	 * @param message
	 */
	public ProductResourceDataNotFound(String message) {
		super(message);
	}
}
