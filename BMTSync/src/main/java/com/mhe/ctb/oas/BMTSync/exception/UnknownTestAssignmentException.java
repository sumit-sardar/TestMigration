package com.mhe.ctb.oas.BMTSync.exception;

public class UnknownTestAssignmentException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownTestAssignmentException(long testAdminId, long studentId)
	{
		super(String.format("Unknown Test Assignment. [testAdminId=%d,studentId=%d]",testAdminId, studentId));
	}	

}
