package com.ctb.utils;

import java.io.File;

/**
 * This Class is used to store all Constants used throughout the Program
 * 
 * @author TCS
 * 
 */

public class Constants {

	public static final boolean SUCCESS = true;
	public static final boolean FAILED = false;
	public static final String REQUIREDFIELD_FIRST_NAME = "First Name";
	public static final String MIDDLE_NAME = "Middle Name";
	public static final String REQUIREDFIELD_LAST_NAME = "Last Name";
	public static final String EMAIL = "Email";
	public static final String REQUIREDFIELD_TIME_ZONE = "Time Zone";
	public static final String REQUIREDFIELD_ROLE = "Role";
	public static final String ADDRESS_LINE_1 = "Address Line 1";
	public static final String ADDRESS_LINE_2 = "Address Line 2";
	public static final String ADDRESS_LINE_3 = "Address Line 3";
	public static final String CITY = "City";
	public static final String STATE_NAME = "State";
	public static final String ZIP = "Zip";
	public static final String PRIMARY_PHONE = "Primary Phone";
	public static final String SECONDARY_PHONE = "Secondary Phone";
	public static final String FAX = "Fax Number";
	public static final String EXT_SCHOOL_ID = "External User Id";

	public static final String ACTIVATION_STATUS_ACTIVE = "AC";

	public static final String FILE_SEPARATOR = File.separator;

	public static final String ROLE_NAME_ADMIN = "ADMINISTRATOR";
	public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR = "ADMINISTRATIVE COORDINATOR";
	public static final String ROLE_NAME_COORDINATOR = "COORDINATOR";
	public static final String ROLE_NAME_PROCTOR = "PROCTOR";

	public static final String MATCH_ORG_CODE = "Match_Upload_Org_Ids";

	public static final int USER_ID = 1;
	public static final String RESET_PASSWORD = "F";
	public static final int PASSWORD_LENGTH = 8;
	public static final String TRUE  = "T";
	public static final String ALPHA_ARRAY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUM_ARRAY = "1234567890";

	public static final Integer EMAIL_TYPE_WELCOME = new Integer(1);
	public static final String USER_MAIL_BODY = "Your recent data upload file has "
			+ "finished processing. Be sure to log in and view your upload to verify whether "
			+ "all records were successfully loaded.";
	public static final String EMAIL_FROM = "iknow_account_management@ctb.com";
	public static final String EMAIL_SUBJECT = "Data upload";

	// Error File Names
	public static final String ERROR_FIELD_NAME = "ERRONEOUS COLUMN_NAME";
	public static final String ERROR_FIELD_DESCRIPTION = "ERROR_CODE";

	public static final String REQUIRED_FIELD_ERROR = "Required Field Missing";
	public static final String LOGICAL_FIELD_ERROR = "Logical Error";
	public static final String MINIMUM_FIELD_ERROR = "Minimum Length Error";
	public static final String MAXIMUM_FIELD_ERROR = "Maximum Length Error";
	public static final String INVALID_FIELD_ERROR = "Invalid Field";
	
	//static thread count should be 1 always
	public static final int THREADCOUNT = 1;
}
