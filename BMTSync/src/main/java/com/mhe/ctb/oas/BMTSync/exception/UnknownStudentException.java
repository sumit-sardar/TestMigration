package com.mhe.ctb.oas.BMTSync.exception;


public class UnknownStudentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownStudentException(long studentId)
	{
		super(String.format("Unknown Student id(%s)", studentId));
	}
	
}
