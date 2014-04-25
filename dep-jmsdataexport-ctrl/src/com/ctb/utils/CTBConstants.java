package com.ctb.utils;

import com.ctb.util.PropertiesLoader;

public class CTBConstants {

	public static final String EMAIL_PLACEHOLDER_JOB_ID = "<#jobid#>";
	public static final String EMAIL_CONTENT_PLACEHOLDER_JOB_STATUS = "<#jobstatus#>";
	public static final String EMAIL_PLACEHOLDER_DATAFILE = "<#datafile#>";
	public static final String EMAIL_CONTENT_PLACEHOLDER_ORDERFILE = "<#orderfile#>";
	
	public static final String EMAIL_FROM = PropertiesLoader.getDetail("dataexport.email.from");
	public static final String EMAIL_FROM_ALIAS_LASLINKS = "LAS Links � OAS Account Management";
	public static final int JOB_STATUS_PENDING = 1;
	public static final int JOB_STATUS_COMPLETE = 2;
	public static final int JOB_STATUS_FAILED = 3;
	public static final int JOB_STATUS_FILE_GENERATION_STARTED = 4;
	public static final int JOB_STATUS_FILE_GENERATION_COMPLETED = 5;
	public static final int JOB_STATUS_FILE_GENERATION_FAILED = 6;
	public static final int JOB_STATUS_FILE_TRANSFER_STARTED = 7;
	public static final int JOB_STATUS_FILE_TRANSFER_COMPLETED = 8;
	public static final int JOB_STATUS_FILE_TRANSFER_FAILED = 9;
	public static final int JOB_STATUS_FILE_TRANSFER_INPROGRESS = 10;
	public static final int JOB_STATUS_PROGRESS = 11;
	

}
