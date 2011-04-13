package com.ctb.util; 

/**
 * <code>CTBConstants</code> defines constants used throughout the application.
 *
 * @author  Tata Consultancy Services
 * 
 */

public final class CTBConstants { 
    
    public static final int CTB_ROOT_ORG_NODE_ID = 1 ;
    public static final int CTB_ORG_NODE_ID = 2;
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;
    public static final String T = "T";
    public static final String F = "F" ;
    public static final String ACTIVATION_STATUS_DELETED  = "DE";
    public static final String ACTIVATION_STATUS_ACTIVE = "AC";
    public static final String ACTIVATION_STATUS_IN_PROGRESS = "IN";
    public static final String TABE_CUSTOMER = "TABE Customer";
    public static final String DB_TABE_CUSTOMER = "TABE_Customer";
    public static final String TERRANOVA_CUSTOMER = "TerraNova Customer";
    public static final String DB_TERRANOVA_CUSTOMER = "TERRANOVA_Customer";
    public static final String OTHER_CUSTOMER = "Other Customer";
    //START - Changes for LASLINK PRODUCT 
    public static final String LASLINK_CUSTOMER = "LasLink Customer";
    public static final String DB_LASLINK_CUSTOMER = "LASLINK_Customer";
    //END - Changes for LASLINK PRODUCT 
    public static final Integer MAX_CTB_CUSTOMER = new Integer(10);
	public static final int MAX_PAGE = 5;
    public static final int DEFAULT_STATE_CATEGORY_ID = 1000; 
	public static final Integer EMAIL_TYPE_WELCOME = new Integer(1);
    public static final Integer EMAIL_TYPE_PASSWORD = new Integer(2);
    public static final Integer EMAIL_TYPE_NOTIFICATION = new Integer(3);
    public static final String OAS_QA_URL = "https://oastest1.ctb.com/"; 
    public static final String OAS_PRODUCTION_URL = "https://oas.ctb.com/"; 
    public static final String STATE_KEY = "STATES";
    public static final String US_STATE_ARRAY = "usStateArray" ;
    public static final String MANAGE_CUSTOMER = "manageCustomer";
    public static final String MAIL_LOGIN_SUBJECT = "OAS User Login"; 
    public static final String MAIL_PASSWORD_SUBJECT = "Password";
    public static final String MAIL_PASSWORD_NOTIFICATION = "Password Notification";
    public static final String ROLE_NAME_ACCOUNT_MANAGER = "ACCOUNT MANAGER";
    public static final String ROLE_NAME_ADMIN = "ADMINISTRATOR";
    public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR = "ADMINISTRATIVE COORDINATOR";
    public static final String ROLE_NAME_COORDINATOR = "COORDINATOR";
    public static final String ROLE_NAME_PROCTOR = "PROCTOR";
    public static final String REQUIREDFIELD =  " First Name , Last Name , Time Zone , Role " ;
    public static final String MAXLENGTH32 = " First Name , Middle Name , Last Name ";
    public static final String MAXLENGTH64 = " Email , Time Zone ";
    public static final String MAXLENGTH255 = " Time Zone ";
    public static final String SERVER_FOLDER_NAME = "Temp";
    public static final String REQUIREDFIELD_FIRST_NAME = "First Name";
    public static final String MIDDLE_NAME = "Middle Name";
    public static final String REQUIREDFIELD_LAST_NAME = "Last Name";
    public static final String EMAIL = "Email";
    public static final String REQUIREDFIELD_TIME_ZONE = "Time Zone";
    public static final String REQUIREDFIELD_ROLE = "Role";
    public static final String REQUIREDFIELD_GRADE = "Grade";
    public static final String REQUIREDFIELD_DATE_OF_BIRTH = "Date of Birth";
    public static final String REQUIREDFIELD_GENDER = "Gender";
    public static final String CONTACT_NAME = "Contact Name";
    public static final String ADDRESS_LINE_1 = "Address Line 1";
    public static final String ADDRESS_LINE_2 = "Address Line 2";
    public static final String ADDRESS_LINE_3 = "Address Line 3";
    public static final String STUDENT_ID = "Student ID";
    public static final String STUDENT_ID2 = "Student ID2";
    public static final String EXT_PIN1 = "External User Id";
    
    public static final String SCREEN_READER = "Allow Screen Reader";
    public static final String CALCULATOR = "Online calculator";
    public static final String TEST_PAUSE = "Test Pause";
    public static final String UNTIMED_TEST="Untimed Test";
    public static final String HIGHLIGHTER = "Highlighter";
    public static final String QUESTION_BACKGROUND_COLOR = "Question Background Color";
    public static final String QUESTION_FONT_COLOR = "Question Font Color";
    public static final String QUESTION_FONT_SIZE= "Question Font Size";
    public static final String ANSWER_BACKGROUND_COLOR = "Answer Background Color";
    public static final String ANSWER_FONT_COLOR = "Answer Font Color";
    public static final String ANSWER_FONT_SIZE = "Answere Font Size";
    public static final String FONT_SIZE = "Font Size";
    
    public static final String ACOMOD_SCREEN_READER = "Screen_Reader";
    public static final String ACOMOD_CALCULATOR = "Calculator";
    public static final String ACOMOD_TEST_PAUSE = "Test_Pause";
    public static final String ACOMOD_UNTIMED_TEST="Untimed_Test";
    public static final String ACOMOD_HIGHLIGHTER = "Highlighter";
    
    
    //mail configuration
    public static final String EMAIL_FROM = "iknow_account_management@ctb.com";
    public static final String EMAIL_CONTENT_PLACEHOLDER_USERID = "<#userid#>";
    public static final String EMAIL_CONTENT_PLACEHOLDER_PASSWORD = "<#password#>";


    public static final String CITY = "City";
    public static final String STATE_NAME = "State";
    public static final String ZIP = "Zip";
    public static final String PRIMARY_PHONE = "Primary Phone";
    public static final String SECONDARY_PHONE = "Secondary Phone";
    public static final String FAX = "Fax Number";
    public static final String REQUIRED = "R";
    public static final String INVALID = "I";
    public static final String MAXLENGTH = "M";
    public static final String LOGICAL = "L";
    public static final String COMMON_PATH = "COMMONPATH";
    public static final String CUSTOMER_CONF_NAME = "Match_Upload_Org_Ids";
    
    
    public static final String MATCH_STUDENT_ID = "Match_Upload_Student_Ids";
    public static final String MATCH_ORG_CODE = "Match_Upload_Org_Ids";
    
    //public static final String CUSTOMER_CONF_NAME = "Match_UploadOrg_Ids";
    
    
    
    // color List
    
    public static final String WHITE = "WHITE";
    public static final String BLACK = "BLACK";
    public static final String YELLOW = "YELLOW";
    public static final String GREEN = "GREEN";
    public static final String LIGHT_BLUE = "LIGHT BLUE" ; 
    public static final String LIGHT_PINK = "LIGHT PINK" ;
    public static final String LIGHT_YELLOW = "LIGHT YELLOW" ;
    public static final String DARK_BLUE = "DARK BLUE" ;
    public static final String DARK_BROWN = "DARK BROWN";
    
    public static final String WHITE_INIT = "White";
    public static final String BLACK_INIT = "Black";
    public static final String LIGHT_YELLOW_INIT = "Light Yellow" ;
    
    //Color code list
    
    public static final String WHITE_CODE = "#FFFFFF";
    public static final String BLACK_CODE = "#000000";
    public static final String LIGHT_BLUE_CODE = "#CCECFF";
    public static final String LIGHT_PINK_CODE= "#FFCCCC";
    public static final String LIGHT_YELLOW_CODE = "#FFFFB0";
    public static final String DARK_BLUE_CODE = "#000080";
    public static final String DARK_BROWN_CODE = "#663300";
    public static final String YELLOW_CODE = "#FFFF99";
    public static final String GREEN_CODE = "#00CC00";
    
    
    // Date Validation
    
    public final static int DATE_VALID = 0;
    public final static int DATE_INVALID = 1;
    public final static int DATE_INVALID_MONTH = 2;
    public final static int DATE_INVALID_DAY = 3;
    
    //Gender
    
    public final static String MALE_CODE = "M";
    public final static String FEMALE_CODE = "F";
    public final static String UNKNOWN_CODE = "U";

    public final static String MALE = "Male";
    public final static String FEMALE = "Female";
    public final static String UNKNOWN = "Unknown";
    
    //Font
    public final static String STANDARD_FONT = "Standard";
    public final static String LARGER_FONT = "Large";
    
    public final static String STANDARD_FONT_SIZE = "1";
    public final static String LARGER_FONT_SIZE = "1.5";
    
    public static final String USER_MAIL_BODY = "Your recent data upload file has " + 
            "finished processing. Be sure to log in and view your upload to verify whether " +
            "all records were successfully loaded."; 
    public static final String STUDENT_MAIL_BODY = "Your recent data upload file has " +
            "finished processing. Be sure to log in and view your upload to verify whether " +
            "all records were successfully loaded."; 
    
    public static final String NONE = "None";
    
    public static final String ACOMODATION_TRUE = "TRUE";
    public static final String ACOMODATION_FALSE = "FALSE";
    
    public static final String ACOMODATION_YES = "yes";
    public static final String ACOMODATION_NO = "no";
    
    public static final String ACOMODATION_Y = "y";
    public static final String ACOMODATION_N = "n";
    
    public static final String GRADE_AD ="AD";
    public static final String GRADE_AE ="AE";
    public static final String GRADE_JV ="JV";
    public static final String GRADE_K ="K";
    public static final int MAX_EXCEL_SIZE = 65000;
    
    //Multiple demographic values
    
    public static final String DEMOGRAPHIC_VALUSE_SEPARATOR = ";";
    public static final String MULTIPLE_DEMOGRAPHIC = "MULTIPLE";
    
    public static final String CUSTOMER_CONFIG_UPLOAD_DOWNLOAD = "Allow_Upload_Download";
    
    public static final String EMAIL_SUBJECT = "Data upload";
    
  //For GACRCT2010CR007 --  	Configuration for Birth Date mandatory and Non mandatory field. 
    public static final String DISABLE_MANDATORY_BIRTH_DATE  =  "Disable_Mandatory_Birth_Date";

    
 } 
