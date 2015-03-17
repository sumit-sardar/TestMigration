package com.mhe.ctb.oas.BMTSync.controller;

public class RestURIConstants {
	
	private RestURIConstants() {}
	/** QA server default endpoint. */
	public static final String SERVER_URI = "http://sync-gain-qa-elb.ec2-ctb.com";
	
	/** REST Path for student API. */
	public static final String POST_STUDENTS = "/api/v1/bmt/student"; 
	
	/** REST Path for roster API. */
	public static final String POST_ASSIGNMENTS = "/api/v1/bmt/assignment";
	
	/** REST Path for testadmin API. */
	public static final String POST_TESTADMIN = "/api/v1/bmt/testadministration";
}

