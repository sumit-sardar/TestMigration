/*
 * OE2Constants.java
 *
 * Created on August 22, 2002, 10:27 AM
 */

package com.ctb.lexington.util;
/**
 *
 * @author  Tai Truong
 * @version
 */
public class OE2Constants {

	public static final String INTERNET_EXPLORER     = "Microsoft Internet Explorer";
	public static final String NETSCAPE_COMMUNICATOR = "Netscape Communicator";
	public static final String PC_PLATFORM 			 = "win";
	public static final String MAC_PLATFORM 		 = "mac";

    public static final int CONTACT_TYPE_ID_1 = 10;

	public static final String OEADMIN_TYPE_LABEL    = "OEAdmin";
    public static final String PHASE_ID_LABEL        = "PhaseID";
    public static final String ORG_NODE_ID_LABEL     = "OrgNodeID";
    public static final String CONTACT_TYPE_ID_LABEL = "ContactTypeID";
    public static final String CONTACT_LABEL         = "Contact";
    public static final String OENODE_LABEL          = "OENode";
    public static final String EDIT_CONTACT_ROWS_UPDATED_LABEL = "EditContactRowsUpdated";
    public static final String USER_NAME_LABEL       = "UserName";
    public static final String UPDATED_ENROLLMENT_COUNTHT_LABEL = "SubjectsHelperHT";
    public static final String EDIT_ENROLLMENT_ROWS_UPDATED_LABEL = "EditEnrollmentRowsUpdated";

    public static final String   COUNTRY_LABEL       = "country";
    public static final String   DIST_LABEL          = "dist";
    public static final String   DIST_CODE_LABEL     = "distCode";
    public static final String   EMAIL_LABEL         = "email";
    public static final String   FAX_LABEL           = "fax";
    public static final String   PHONE_LABEL         = "phone";
    public static final String   SCHOOL_LABEL        = "school";
    public static final String   SCHOOL_CODE_LABEL   = "REQ_PARAM_SCHOOL_CODE_NAME";
    public static final String   STATE_LABEL         = "state";
    public static final String   STREET_LABEL        = "street";
    public static final String   TEST_ADMIN_LABEL    = "testAdmin";
    public static final String   TEST_COOD_LABEL     = "testCood";
    public static final String   ZIP_LABEL           = "zip";

    public static final String   NAV_SUB_TASK_LABEL  = "NavSubTask";
    //>>
    public static final String USER_ID_LABEL = "UserID";
    public static final String PHASE_NAME_LABEL = "REQ_PARAM_TEST_AD_NAME";
    public static final String PARENT_ORG_NODE_ID_LABEL = "ParentOrgID";
    public static final String PARENT_ORG_NAME_LABEL = "REQ_PARAM_PARENT_NAME";
    public static final String PARENT_ORG_CODE_LABEL = "REQ_PARAM_PARENT_CODE_NAME";
    public static final String PARENT_ORG_CAT_NAME_LABEL = "REQ_PARAM_PARENT_CAT_NAME";
    public static final String SCHOOL_NAME_LABEL = "REQ_PARAM_SCHOOL_NAME";
    public static final String ADDED_SCHOOL_NODE_ID_LABEL = "AddedSchoolNodeID";
    //<<
    //org node category names, these are what will be displayed
    public static final String ORG_CATEGORY_STATE           = "State";
    public static final String ORG_CATEGORY_COUNTY          = "County";
    public static final String ORG_CATEGORY_CORPORATION     = "Corporation";
    public static final String ORG_CATEGORY_DIOCESE         = "Diocese";
    public static final String ORG_CATEGORY_DISTRICT        = "District";
    public static final String ORG_CATEGORY_SCHOOL          = "School";

    // labels used for form names (NOTE: must match form name in lexington-forms.xml)
	public static final String FORM_NAME_EDIT_DISTRICT		= "frmEditDistrict";
	public static final String FORM_NAME_EDIT_SCHOOL		= "frmEditSchool";
	// New School action can use same form definition as Edit School
	public static final String FORM_NAME_NEW_SCHOOL			= FORM_NAME_EDIT_SCHOOL;

    public static final String FORM_VALIDATION_ERROR_LABEL	= "FormValidationError";
    public static final String FORM_VALIDATION_ERROR_MSG	= "Correct the errors identified below, then click <b>SAVE</b>.";

    // CommandException codes
    public static final String CE_CODE_DUPESCHOOLCODE		= "6921";

    // CTBFormValidationException codes
    public static final String FVE_CODE_DUPESCHOOLCODE		= "4010";
    public static final String FVE_MSG_DUPESCHOOLCODE		= "School Code must be unique for a given District regardless of phase.";
    public static final String FVE_CODE_NOCHANGES			= "4011";
    public static final String FVE_MSG_NOCHANGES			= "You have made no changes to this form.<br>Edit the information, then click <b>Save</b>, or click <b>Cancel</b> to exit this page.";

    // labels used for form fields; also used in logging changes
    public static final String FORM_LABEL_SCHOOL_STATUS		= "School Status";
    public static final String FORM_LABEL_DISTRICT_NAME		= "District";
    public static final String FORM_LABEL_DISTRICT_CODE		= "District Code";
    public static final String FORM_LABEL_SCHOOL_NAME		= "School Name";
    public static final String FORM_LABEL_SCHOOL_CODE		= "School Code";
    public static final String FORM_LABEL_ADDRESS			= "Address";
    public static final String FORM_LABEL_CITY				= "City";
    public static final String FORM_LABEL_STATE				= "State";
    public static final String FORM_LABEL_ZIP_CODE			= "Zip/Postal Code";
    public static final String FORM_LABEL_COUNTRY			= "Country";
	public static final String FORM_LABEL_CONTACT_NAME		= "Test Coordinator";
	public static final String FORM_LABEL_PRIMARY_PHONE		= "Phone";
	public static final String FORM_LABEL_PRIMARY_PHONE_EXT	= "ext";
	public static final String FORM_LABEL_FAX				= "Fax";
	public static final String FORM_LABEL_EMAIL				= "Email";

    //for Select School to add achool, edit "district" contact points
    public static final String THIS_NODE                    = "ThisNode";
    public static final String PARENT_NODE                  = "ParentNode";
    public static final String EDIT_DIST_CONTACT_URI        = "/editdistrictcontact/editcontactcmd";
    public static final String ADD_SCHOOL_URI               = "/addschoolnode/stepzerocmd";
    public static final String SCHOOL_DETAIL_URI            = "/editschoolenrollments/editenrollmentscmd";
    public static final String SELECT_SCHOOL_URI            = "/selectschool/testadminselectcmd";

    public static final String EDIT_CONTACT_CLIENT_LABEL     = "ECClient";
    public static final String SELECT_SCHOOL_PAGE     = "SelectSchool";
    public static final String SCHOOL_DETAIL_PAGE     = "SchoolDetail";
    public static final String PHASES_HT = "PHT";
    public static final String SCHOOL_DETAIL_CLIENT_LABEL = "SDClient";
    public static final String EDIT_SCHOOL_CONTACT_CONFIRM_PAGE = "SCConfirm";
    public static final String SELECT_SCHOOL_CLIENT_LABEL = "SSClient";
    public static final String EDIT_DIST_CONTACT_CONFIRM_PAGE = "DCConfirm";
    public static final String ADD_SCHOOL_CONFIRM_PAGE = "ADSConfirm";
    public static final String CONTEXTUAL_NAV = "CNAV";

	public static final String CURRENT_SCHOOL_VIEW_LABEL    = "currentSchoolView";
	public static final String DISTRICT_VIEW_ALL            = "districtViewAll";
	public static final String SCHOOL_VIEW_PENDING          = "pending";
	public static final String SCHOOL_VIEW_COMPLETED        = "completed";
	public static final String FIRST_SCHOOL_VIEW_NUMBER    	= "firstSchoolViewNumber";
	public static final String TOTAL_NUMBER_SCHOOLS    		= "TotalNumberSchools";
	public static final String NUMBER_SAVED_SCHOOLS         = "numberSavedSchools";

	public static final String VIEWTYPE_DISTRICT_ALL    	= "districtAll";
	public static final String VIEWTYPE_DISTRICT_PENDING    = "districtPending";
	public static final String VIEWTYPE_DISTRICT_COMPLETED  = "districtCompleted";
	public static final String VIEWTYPE_SCHOOL_ALL      	= "schoolAll";
	public static final String VIEWTYPE_SCHOOL_PENDING      = "schoolPending";
	public static final String VIEWTYPE_SCHOOL_COMPLETED    = "schoolCompleted";

    public static final String OE_TREE_NAME = "oeTestTreeTag";

    public static final int LOG_LEVEL_1 = 1;
    public static final int LOG_LEVEL_2 = 3;
    public static final int LOG_LEVEL_3 = 7;

    //Below constants pertain to OE R2
    public static final String OPEN_SCHOOL_STATUS_LABEL = "0";
    public static final String CLOSE_SCHOOL_STATUS_LABEL = "1";
    public static final String IS_SCHOOL_CLOSED_LABEL = "isSchoolClosed";
    public static final String NEW_SCHOOL_LABEL = "newSchoolLabel";
    public static final String LOG_SCHOOL_ADDED = "SCH ADD";
    public static final String LOG_SCHOOL_CLOSED = "SCH CLOS";
    public static final String LOG_SCHOOL_OPENED = "SCH OPEN";
    public static final String LOG_SCHOOL_MODIFIED = "SCH MOD";
    public static final String LOG_DISTRICT_MODIFIED = "DIS MOD";
    public static final String LOG_CONTACT_MODIFIED = "CON MOD";

    public static final String[] BANNED_CHARACTERS = {";","<",">","\"",")","(","%","&","+"};

    public static final String UNITED_STATES_COUNTRY_CODE = "US";
    public static final String CANADA_COUNTRY_CODE = "CA";
    public static final String UNITED_STATES_COUNTRY_LABEL = "United States of America";
    public static final String CANADA_COUNTRY_LABEL = "Canada";

    // New constants for OE R3 conversations and commands
	public static final String CONTEXT_PATH_DEFAULT                       = "oe";
	public static final String SERVLET_MAPPING_DEFAULT                    = "enroll";

	public static final String CONVERSATION_DEFAULT                       = "default";
	public static final String COMMAND_DEFAULT                       	  = "default";

	public static final String CONVERSATION_OE_HOME                       = "oehome";
	public static final String COMMAND_OE_HOME                            = "oeHome";
	public static final String COMMAND_SELECT_ADMIN                       = "selectAdmin";
	public static final String COMMAND_HOW_ALPHA_NAV_WORK                 = "howAlphaNavigatorWork";
	public static final String COMMAND_MANAGE_USERS                 	  = "manageUsers";

	public static final String CONVERSATION_MANAGE_ENROLLMENTS            = "manageenrollments";
	public static final String COMMAND_INIT_ENROLLMENTS         		  = "initEnrollments";
	public static final String COMMAND_SELECT_ENROLLMENTS_SCHOOLS         = "selectEnrollmentsSchools";
	public static final String COMMAND_VALIDATE_ENROLLMENTS               = "validateEnrollments";
	public static final String COMMAND_ENTER_ENROLLMENTS                  = "enterEnrollments";
	public static final String COMMAND_NEXTSCHOOLSET                      = "nextSchoolSet";
	public static final String COMMAND_PREVIOUSSCHOOLSET                  = "previousSchoolSet";
	public static final String COMMAND_RESET_ENROLLMENTS               	  = "resetEnrollments";
	public static final String COMMAND_FINISHUP_ENROLLMENTS               = "finishUpEnrollments";
	public static final String COMMAND_DONE_ENROLLMENTS                   = "doneEnrollments";
	public static final String COMMAND_HOW_ENTER_ENROLLMENT_WORK          = "howEnterEnrollmentWork";

	public static final String CONVERSATION_MANAGE_SCHOOLS                = "manageschools";
	public static final String COMMAND_MANAGE_SCHOOLS                     = "manageSchools";
	public static final String COMMAND_SELECT_DISTRICT                    = "selectDistrict";
	public static final String COMMAND_VALIDATE_SELECT_DISTRICT           = "validateSelectDistrict";
	public static final String COMMAND_SELECT_DISTRICT_SCHOOL             = "selectDistrictSchool";
	public static final String COMMAND_SELECT_SCHOOL                      = "selectSchool";
	public static final String COMMAND_NEW_SCHOOL                         = "newSchool";
	public static final String COMMAND_CONFIRM_NEW_SCHOOL                 = "confirmNewSchool";
	public static final String COMMAND_EDIT_DISTRICT					  = "editDistrict";
	public static final String COMMAND_CONFIRM_EDIT_DISTRICT       		  = "confirmEditDistrict";
	public static final String COMMAND_EDIT_SCHOOL                        = "editSchool";
	public static final String COMMAND_CONFIRM_EDIT_SCHOOL                = "confirmEditSchool";
	public static final String COMMAND_OPENCLOSE_EDIT_SCHOOL              = "openCloseEditSchool";
	public static final String COMMAND_CONFIRM_OPENCLOSE_EDIT_SCHOOL      = "confirmOpenCloseEditSchool";
	public static final String COMMAND_CHANGE_SCHOOL_STATUS               = "changeSchoolStatus";
	public static final String COMMAND_OPENCLOSE_CHANGE_SCHOOL_STATUS     = "openCloseChangeSchoolStatus";

	public static final String ENNAV_CHOOSESCHOOL_LABEL                   = "ennavChooseSchool";
	public static final String ENNAV_ENTERENROLLMENT_LABEL                = "ennavEnterEnrollment";
	public static final String ENNAV_FINISHUP_LABEL                 	  = "ennavFinishUp";

	public static final String ADDSCHOOLWIZ_STEP_1_LABEL				  = "Choose District";
	public static final String ADDSCHOOLWIZ_STEP_2_LABEL				  = "Enter School Information";
	public static final String ADDSCHOOLWIZ_STEP_3_LABEL				  = "Confirm";

	public static final String COMMAND_SELECT_TEST_ADMIN_ENROLLMENTS      = "selectTestAdmin";
	public static final String COMMAND_SELECT_DISTRICT_SCHOOL_ENROLLMENTS = "selectDistrictSchool";
	public static final String COMMAND_ENTER_ENROLLMENTS_PUBLIC           = "enterEnrollmentsPublic";
	public static final String COMMAND_ENTER_ENROLLMENTS_NONPUBLIC        = "enterEnrollmentsNonpublic";
	public static final String COMMAND_ENTER_ENROLLMENTS_VI               = "enterEnrollmentsVi";
	public static final String COMMAND_CUSTOMIZE_VI_FORM                  = "customizeViForm";
	public static final String COMMAND_MULTI_ENTER_ENROLLMENTS            = "multiEnterEnrollments";
	public static final String COMMAND_VIEW_COMPLETE_SCHOOLS              = "viewCompleteSchools";
	public static final String COMMAND_CONFIRM_ENROLLMENTS_PUBLIC         = "confirmEnrollmentsPublic";
	public static final String COMMAND_CONFIRM_ENROLLMENTS_NONPUBLIC      = "confirmEnrollmentsNonpublic";
	public static final String COMMAND_CONFIRM_ENROLLMENTS_VI             = "confirmEnrollmentsVi";

	public static final String COMMAND_SELECT_TEST_ADMIN_MANAGE_ORGS      = "selectTestAdmin";
	public static final String COMMAND_SELECT_DISTRICT_SCHOOL_MANAGE_ORGS = "selectDistrictSchool";

	public static final String CONVERSATION_REPORTING                     = "reporting";
	public static final String COMMAND_SELECT_REPORT_TYPE                 = "selectReport";
	public static final String COMMAND_CREATE_REPORT                      = "createReport";
	public static final String COMMAND_DOWNLOAD_REPORT                    = "downloadReport";
	public static final String COMMAND_GET_REPORT                    	  = "getReport";
	public static final String COMMAND_HOW_DOWNLOAD_WORK               	  = "howDownloadWork";
	public static final String COMMAND_SELECT_ORGANIZATION                = "selectOrganization";

	public static final String CONVERSATION_MANAGE_USERS                  = "manageusers";
	public static final String COMMAND_USER_PROFILE_SEARCH                = "userProfileSearch";
	public static final String COMMAND_LIST_ALL_USERS                     = "listAllUsers";
	public static final String COMMAND_NEW_USER_PROFILE                   = "newUserProfile";
	public static final String COMMAND_CREATE_USER_PROFILE                = "createUserProfile";
	public static final String COMMAND_UPDATE_USER_PROFILE                = "updateUserProfile";
	public static final String COMMAND_UPDATE_MY_PROFILE                  = "updateMyProfile";
	public static final String COMMAND_DELETE_USER_PROFILE                = "deleteUserProfile";
	public static final String COMMAND_EDIT_USER_PROFILE                  = "editUserProfile";
	public static final String COMMAND_CANCEL_EDIT_PROFILE                = "cancelEditProfile";
	public static final String COMMAND_EDIT_MY_PROFILE                    = "editMyProfile";
	public static final String COMMAND_SEARCH_USERS                       = "searchUsers";
	public static final String COMMAND_RETRIEVE_USER_PROFILE              = "retrieveUserProfile";
	public static final String COMMAND_RETRIEVE_MY_PROFILE                = "retrieveMyProfile";
	public static final String COMMAND_EDIT_PASSWORD                      = "editPassword";
	public static final String COMMAND_UPDATE_PASSWORD                    = "updatePassword";

	public static final String CONVERSATION_USER_LOGIN                    = "userlogin";

	public static final String CONVERSATION_ADD_SCHOOL_NODE               = "addschoolnode";
	public static final String COMMAND_STEP_ZERO                          = "stepZero";

	public static final String CONVERSATION_EDIT_DISTRICT_CONTACT         = "editdistrictcontact";
	public static final String COMMAND_EDIT_DISTRICT_CONTACT              = "editcontactcmd";

	public static final String CONVERSATION_EDIT_SCHOOL_CONTACT           = "editschoolcontact";
	public static final String COMMAND_EDIT_SCHOOL_CONTACT                = "editcontactcmd";

	// display options for AlphaNavigatorTag
	public static final String ALPHA_NAV_TOP                              = "top";
	public static final String ALPHA_NAV_BOTTOM                           = "bottom";
	public static final String ALPHA_NAV_BOTH                             = "both";
	public static final String ALPHA_NAV_NONE                             = "none";

	// parameter name to pass letter or "All", "Other" to manageenrollments/selectDistrictSchool
	public static final String ALPHA_CHOICE_LABEL                        = "alphaChoice";
	public static final String PRIMARY_CHOICE_LABEL                      = "primaryChoice";

	// AlphaData and RowRenderer session/request attributes
	public static final String ALPHA_DATA_LABEL                          = "alphaData";
	public static final String ALPHA_ROW_RENDERER_LABEL                  = "alphaRowRenderer";
	public static final String PRIMARY_DATA_LABEL                        = "primaryData";
	public static final String PRIMARY_ROW_RENDERER_LABEL                = "primaryRowRenderer";
	public static final String SELECTED_ROW_INDEX_LABEL                  = "selectedRowIndex";

	// AlphaNavigator UI stuffs
	public static final int LEADING_SPACES_CELL_WIDTH = 5;
	public static final int NO_SELECTED_ROW_INDEX = -1;
	public static final int NUMBER_CODES = 15;
	public static final int PLUS_MINUS_WIDTH = 20;

	public static final int TITLE_ROW_HEIGHT = 25;
	public static final int TOOLBAR_ROW_HEIGHT = 25;
	public static final int DATA_ROW_HEIGHT = 25;
	public static final int DATA_COMPLETED_ROW_HEIGHT = 40;

	public static final String EXPANDED_ROW_CLASS = "hilite";
	public static final String ROW_CLASS = "normal";

	// report section
	public static final String ENROLLMENT_STATUS_REPORT = "Enrollment_Status";
	public static final String SCHOOL_CHANGE_REPORT 	= "DistrictSchool_Change";
	public static final String SCHOOL_CHANGE_REPORT_INDIANA	= "CorpSchool_Change";
	public static final String ENROLLMENT_DETAIL_REPORT = "Enrollment_Detail";
	public static final String REPORT_FILE_NAME_LABEL = "ReportFileName";
	public static final String REPORT_DISPLAY_NAME_LABEL = "ReportDisplayName";

	// login user level
	public static final String LEVEL_USER_LABEL = "LevelUser";
    public static final String TOP_LEVEL_USER_LABEL = "TopLevelUser";
	public static final String MIDDLE_LEVEL_USER_LABEL = "MiddleLevelUser";
	public static final String BOTTOM_LEVEL_USER_LABEL = "BottomLevelUser";

	// enrollment section
	public static final int NUMBER_OF_SCHOOL_IN_SET = 3;

	// sesion variables
	public static final String OE2_INFO_VO = "OE2InfoVO";

	public static final String PRIMARY_ALPHA_NAVIGATOR_VO = "PrimaryAlphaNavigatorVO";
	public static final String SECONDARY_ALPHA_NAVIGATOR_VO = "SecondaryAlphaNavigatorVO";

	public static final String ALPHA_NAV_ROW_PARAMETER_LABEL = "rowId";
	public static final String ALPHA_NAV_FILTER_PARAMETER_LABEL1 = "filter1";
	public static final String ALPHA_NAV_FILTER_PARAMETER_LABEL2 = "filter2";
	public static final String ALPHA_NAV_CHOICE_PARAMETER_LABEL = "choice";  // alphanumeric choice from toolbar (eg A, B, C, etc.)
	public static final String ENROLLMENTS_TAB_LABEL = "tabLabel";
	public static final String ALPHA_NAV_SORT_PARAMETER_LABEL = "sort";
	public static final String ALPHA_NAV_ERROR_LABEL = "alphaNavError";
	public static final String ALPHA_NAV_ERROR1_LABEL = "alphaNavError1";
	public static final String ALPHA_NAV_ERROR2_LABEL = "alphaNavError2";

	public static final String SORT_ORDER_NAME = "name";
	public static final String SORT_ORDER_CODE = "code";

	public static final String ALPHA_NAV_INPUT_PREFIX = "AlphaNav";
	public static final String MULTIPLE_ENROLLMENTS_VO = "MultipleEnrollmentsVO";
	public static final String ENROLLMENTS_SAVE_SCHOOL_NUMBER = "SaveSchool";
	public static final String ENROLLMENTS_SKIP_SCHOOL_NUMBER = "SkipSchool";

	public static final int OK_ENROLLMENT_STATUS      = 0;
	public static final int WARNING_ENROLLMENT_STATUS = 1;
	public static final int ERROR_ENROLLMENT_STATUS   = 2;

	public static final int NOT_SAVED_STATUS = 0;
	public static final int PROBLEM_STATUS   = 1;
	public static final int SAVED_STATUS     = 2;

	public static final String HAS_UNSAVED_SCHOOL = "HasUnsavedSchool";
	public static final String HAS_SENT_EMAIL = "hasSentEmail";

	public static final String SCHOOL_IS_CLOSED_LABEL = "schoolClosed";

    /** Creates new OE2Constants */
    public OE2Constants() {
    }
}
