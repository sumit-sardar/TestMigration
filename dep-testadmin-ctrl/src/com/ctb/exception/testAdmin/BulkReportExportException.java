package com.ctb.exception.testAdmin;

import com.ctb.exception.CTBBusinessException;

/**
 * BulkReportExportException
 * @author TCS
 *
 * Exception thrown when bulk report export data corresponding to request
 * parameters can not be obtained from the data store
 */
public class BulkReportExportException extends CTBBusinessException {

	private static final long serialVersionUID = 1L;
	/**
	 * Construct a new exception
	 * @param message
	 */
	public BulkReportExportException(String message) {
		super(message);
	}

}
