package com.mhe.ctb.oas.BMTSync.exception;

public class UnknownTestAdminException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownTestAdminException(long testAdminId) {
		super(String.format("Unknown Test Admin Exception. [testAdminId=%d]",testAdminId));
	}	

}
