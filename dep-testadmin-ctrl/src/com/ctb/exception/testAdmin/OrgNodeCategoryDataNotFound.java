package com.ctb.exception.testAdmin; 

import com.ctb.exception.CTBBusinessException;

/**
 * OrgNodeCategoryDataNotFound.java
 * @author TCS
 *
 * Exception thrown when org node category data corresponding to request
 * parameters can not be obtained from the data store
 */
public class OrgNodeCategoryDataNotFound extends CTBBusinessException{
	static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
    public OrgNodeCategoryDataNotFound(String message) {
        super(message);
    }
}
