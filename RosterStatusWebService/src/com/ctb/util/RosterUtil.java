package com.ctb.util;

import com.ctb.bean.testAdmin.RosterElement;

import dto.SecureUser;
import dto.StudentStatus;

public class RosterUtil {
	
	public static final String MESSAGE_STATUS_OK = "OK";
	public static final String MESSAGE_SESSION_HAS_NO_STUDENTS = "OK: Session has no students";
	public static final String MESSAGE_INVALID_USER = "Error:Invalid user";
	public static final String MESSAGE_INVALID_DATA = "Error:Invalid data";
	public static final String MESSAGE_STATUS_ROSTER_ERROR = "Error:Failed to get rosters";
	public static final String MESSAGE_SESSION_NOT_FOUND = "Error: Session not found";
	public static final String MESSAGE_STUDENT_NOT_FOUND = "Error: Student not found";

	public static final String ROSTER_STATUS_COMPLETE = "Complete";
	public static final String ROSTER_STATUS_NOT_STARTED = "Not Started";
	public static final String ROSTER_STATUS_PARTIALLY_COMPLETE = "Partially Complete";
	public static final String ROSTER_STATUS_UNKNOWN = "Unknown";
	
	private static final String AUTHENTICATE_USER_NAME = "tng_acuity";
	private static final String AUTHENTICATE_PASSWORD = "acuity101";

	
	/**
	 * authenticateUser
	 */
	public static boolean authenticateUser(SecureUser user) 
	{   
		return user.getUserName().equals(AUTHENTICATE_USER_NAME) && user.getPassword().equals(AUTHENTICATE_PASSWORD);
	}
	
	/**
	 * convertStatus 
	 */
	public static String convertStatus(String status) 
    {
		/*
    	if (status.equals("SC") || status.equals("NT")) 
    		status = ROSTER_STATUS_NOT_STARTED;
    	else
    	if (status.equals("IP") || status.equals("IN") || status.equals("IS") || status.equals("SP") || status.equals("IC")) 
    		status = ROSTER_STATUS_PARTIALLY_COMPLETE;
    	else
       	if (status.equals("CO")) 
       		status = ROSTER_STATUS_COMPLETE;
       	else
    		status = ROSTER_STATUS_UNKNOWN;
    	*/
    	return status;
    }

}
