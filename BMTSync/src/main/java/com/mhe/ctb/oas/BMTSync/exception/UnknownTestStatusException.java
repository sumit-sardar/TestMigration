package com.mhe.ctb.oas.BMTSync.exception;

public class UnknownTestStatusException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnknownTestStatusException(long rosterId) {
		super(String.format("Unknown Test Status Exception. [Roster Id=%d]",rosterId));
	}	

}
