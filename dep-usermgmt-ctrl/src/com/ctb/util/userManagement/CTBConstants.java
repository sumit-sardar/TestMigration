package com.ctb.util.userManagement;



/*
 * CTBConstants.java
 *
 * Copyright CTB/McGraw-Hill, 2002
 * CONFIDENTIAL
 */

import java.math.BigDecimal;

/**
 * <code>CTBConstants</code> defines constants used throughout the application.
 *
 * @author  Tata Consultency Services
 * 
 */

public final class CTBConstants
{
	static public final String SESSION_PREFERENCES = "sessionPrefs";
	
	static public final String IS_COPY = "isCopy";
	static public final String FORM_NAME = "form_name";
	public static final String USER_TIME_ZONE = "user_time_zone";
	static public final String PAGE_ERROR = "page_error";
	static public final String PAGE_EXCEPTION = "page_exception";

	static public final String DEFAULT_HASH_NAME = "defaulthash";

	static public final String VALIDATION_ERROR_MESSAGE =
								  "Errors encountered validating the form.";


	static public final String GRNDS_CONFIG_DOMAIN = "lexington";
	static public final String CONFIG_KEY_PREFIX_SUBSCRIPTION =
							  "presentation.product.subscription-display-name.";
	static public final String CONFIG_KEY_PREFIX_CMS_ID =
							  "presentation.product.display-cms-id.";

	/** Used to store the timestamp of the current request. */
	static public final String REQ_TIMESTAMP = "req.timestamp";
	static public final String REQ_PREV_TIMESTAMP = "req.prev.timestamp";

	/** Used as the field name in a form to timestamp the form. */
	static public final String FORM_TIMESTAMP = "form.timestamp";


	// -- Date Format --
	// Suggestion: Move to com.ctb.lexington.util.DateUtils
	static public final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";

	// -- Default form field length values
	static public final String FIELD_MAX_LENGTH = "64";
	static public final String NORMS_GROUP_PARAMETER = "normsGroup";
	static public final String FIELD_MIN_LENGTH = "0";


	/**
	 * Key to store the {@link com.ctb.lexington.web.form.CTBFormBuilder}
	 * object in the web container context.
	 */
	public static final String FORM_BUILDER = "CTBFormBuilderKey";

	 /**
	 * Key to store the CTBMessageFormatter object in the web container context.
	 */
	public static final String MESSAGE_FORMATTER = "CTBMessageFormatterKey";

	 /**
	 * Keys dealing with the browser checkgin
	 */
	public static final String VALID_BROWSER_LABEL = "validBrowserLabel";
	public static final String BROWSER_TYPE_OBJECT_LABEL = "browserTypeObjectLabel";

	public static final String CTX_INCOMPAT_BROWSER_URI = "incompat_browser_uri.context.parameter";





	//keys for the email templates
	//must match the template file name in the email-templates.xml file
	public static final String HELP_EMAIL_TEMPLATE = "help_email.txt";
	public static final BigDecimal SCALESCORENOTPRESENT = new BigDecimal(-200);
	public static final String HARDCODEDTESTEDFLAG = "Y";
	public static final String HARDCODEDRETESTFLAG = "N";
	public static final String OAS = "oas";

	//public static final String WELCOME_EMAIL_TEMPLATE = "welcome_email.txt";
	//public static final String PASSWORD_EMAIL_TEMPLATE = "password_email.txt";
	//public static final String CHANGED_PASSWORD_NOTIFICATION_EMAIL_TEMPLATE = "changed_password_notification_email.txt";

	// -- keys for activation status --
	public static final String ACTIVATION_STATUS_ACTIVE   = "AC";
	public static final String HARDCODEDVALIDSCORE = "Y";
	public static final String HARDCODEDPASSFAILFLAG = null;
	public static final String ACTIVATION_STATUS_INACTIVE = "IN";
	public static final String ACTIVATION_STATUS_DELETED  = "DE";
	public static final String ACTIVATION_STATUS_PENDING  = "PE";

	// -- keys for itemset types

	public static final String ITEM_SET_TYPE_RE = "RE";
	public static final String ITEM_SET_TYPE_TD = "TD";
	public static final String ITEM_SET_TYPE_TC = "TC";
	public static final String ITEM_SET_TYPE_SC = "SC";
	public static final String ITEM_SET_TYPE_AT = "AT";

	public static final String TABE_PRODUCT_CODE = "TB";
	// -- keys for itemSet levels
	public static final int ITEM_SET_CATEGORY_STRAND_LEVEL = 4;
	public static final int ITEM_SET_CATEGORY_STANDARD_LEVEL = 5;
	public static final int ITEM_SET_CATEGORY_BENCHMARK_LEVEL = 6 ;
	public static final int ITEM_SET_CATEGORY_CONTENT_AREA_LEVEL = 3 ;

	// -- keys for active session --
	public static final String ACTIVE_SESSION_ACTIVE = "T";
	public static final String ACTIVE_SESSION_INACTIVE = "F";

	// -- keys for validation of test administration and item sets.
	public static final String VALID   = "VA";
	public static final String INVALID = "IN";

	// -- generic true/false keys.
	public static final String TRUE  = "T";
	public static final String FALSE = "F";


	/**
	 * A session key whose value is a {@link java.lang.Object} array containing
	 * the user's username at index 0 and password at index 1.
	 */
	public static final String CTX_CREDENTIALS = "credentials.session.attribute";


	/**
	 * A session key whose value is a {@link java.lang.Boolean} indicating
	 * whether the current user has been authenticated.
	 */
	public static final String CTX_AUTHENTICATED =
											  "authenticated.session.attribute";

	public static final String CTX_LOGIN_URI = "login_uri.context.parameter";

	public static final String CTX_PRE_LOGIN_URI =
											  "pre_login_uri.session.attribute";

	public static final String LOGIN_SERVLET_NAME = "login";
	public static final int LOGIN_ATTEMPT_LIMIT = 5;
	public static final int LOGIN_ATTEMPT_LOCKOUT_TIME = -30;

	//GRNDS Attribute names
	public static final String GRNDS_CONVERSATION = "grnds_conversation"; // i.e. lexington/managestudents
	public static final String GRNDS_COMMAND = "grnds_command"; // i.e. createUserProfile
	public static final String GRNDS_SERVLET_MAPPING = "grnds_servlet_mapping"; // i.e. lexington

	//timeout error page parameters
	public static final String TIMEOUT_CONVERSATION = "timeout_conversation"; // i.e. managestudents
	public static final String TIMEOUT_COMMAND = "timeout_command"; // i.e. createUserProfile

	//Constants to be passed to determine what type of Hierarchy
	public static final String NODE_TYPE = "nodeType";
	public static final String STUDENT_TYPE = "studentType";
	public static final String USER_TYPE = "userType";
	public static final String TEST_ADMIN_TYPE = "testAdminType";
	public static final String TEST_CATALOG_TYPE = "testCatalogType";

	//Constants used for test admin hierarhcy
	public static final String PREVIOUS = "previous";
	public static final String CURRENT = "current";
	public static final String FUTURE = "future";
	public static final String TUTORIAL = "";



	//Constants used for to set navigation in headers and contextual navigation
	public static final String CONVERSATION_MANAGE_ORGANIZATIONS = "manageorganizations";
	public static final String COMMAND_SELECT_ORGANIZATION = "selectOrganization";

	public static final String CONVERSATION_EDIT_ORGANIZATION_NODE = "editorganizationnode";
	public static final String COMMAND_VIEW_ORGANIZATION_NODES = "viewOrganizationNodes";

	public static final String CONVERSATION_MANAGE_STUDENTS = "managestudents";
	public static final String COMMAND_STUDENT_PROFILE_SEARCH = "studentProfileSearch";
	public static final String COMMAND_EDIT_STUDENT_PROFILE = "editStudentProfile";

	//deterine the expanded Hierarchy Type

	public static final String EXPANDED_STUDENT = "expandedStudent";
	public static final String EXPANDED_USER = "expandedUser";
	public static final String EXPANDED_TEST_ADMIN = "expandedTestAdmin";
	public static final String EXPANDED_GROUP = "expandedGroup";

	// used by the OptionListFactory to grab the initialization parameters
	// off the context.  The two parameters configure a YearOptionList object.

	public static final String FIRST_YEAR_TAG="optionlist.year.first";
	public static final String LAST_YEAR_TAG ="optionlist.year.last";

	// used by the OptionListFactory to grab the initialization parameters
	// off the context.  The two parameters configure a YearOptionList object
	// for test administration.
	public static final String FUTURE_FIRST_YEAR_TAG="optionlist.year.first.future";
	public static final String FUTURE_LAST_YEAR_TAG ="optionlist.year.last.future";

	// the following three parameters are used by the YearOptionList object to determine the
	// contents of the year combo box.
	public static final String CURRENT_VALUE = "current";
	public static final String ADD_YEAR_VALUE = "+";
	public static final String SUBTRACT_YEAR_VALUE = "-";


	// used by the OptionListFactory to grab the initialization parameters
	// off the context.  The two parameters configure a LoginTimeOptionList object
	// for test administration.
	public static final String FIRST_LOGIN_TIME_TAG = "optionlist.logintime.first";
	public static final String LAST_LOGIN_TIME_TAG   = "optionlist.logintime.last";
	public static final String STEP_LOGIN_TIME_MINUTES_TAG = "optionlist.logintime.stepminute";
	public static final String SHORT_STATE_SALUTATION = "State...";
	public static final String SHORT_MONTH_SALUTATION = "Month...";
	public static final String VIEW_UPLOADS_PAGE_NUMBER = "ViewUploadsPageNumber";
	public static final String SHORT_DAY_SALUTATION = "Day...";
	public static final String SHORT_YEAR_SALUTATION = "Year...";

	public static final String STUDENT_VALIDATION_STATUS="studentValidationStatus";
	//public static final String MULTIPLE_MARKS_ON_DATE = "Multiple marks";
	public static final String MULTIPLE_MARKS_ON_DATE = "*";
	public static final String SIX_MONTH_WARNING = "SixMonthsWarning";
    public static final String TERRANOVA = "TerraNova";
    public static final String TERRANOVA_PRODUCT_CODE = "TV";
    public static final String TN_AAS_PROD_ID = "AAS";
    public static final String UP_BUTTON_TAG = "up";
    public static final String DOWN_BUTTON_TAG = "down";
    public static final String STUDENT_ITEM_SET_STATUS_LOCAL_HOME = "StudentItemSetStatusLocalHome";
    public static final String TEST_ROSTER_MANAGER_LOCAL_HOME = "TestRosterManagerLocalHome";
	public static final String  PROCTOR_ROLE = "PROCTOR";
	public static final String  COORDINATOR_ROLE = "COORDINATOR";
    public static final String USER_ROLE = "userRole";

    /**
	 * Values must be in sync with lexington.properties!
	 * These values are referenced by .jsp layer and session EJB
	 */
	public static final class PAGER_TYPE {
		/*public static final String KEM_UNSCORED_STUDENTS        = KeyEntryManagerEJB.UNSCORED_STUDENT_PAGER_NAME;
		public static final String KEM_SCORED_STUDENTS          = KeyEntryManagerEJB.SCORED_STUDENT_PAGER_NAME;
		public static final String KEM_PARTIALLY_SCORED_STUDENTS= KeyEntryManagerEJB.PARTIALLY_SCORED_STUDENT_PAGER_NAME;

		public static final String TAM_HOME_PAGE_CONVERSATION    = TestAdministrationManagerEJB.HOME_PAGE_PAGER_NAME;
		public static final String TAM_HOME_PAGE_PROCTOR_SESSION = TestAdministrationManagerEJB.HOME_PAGE_PROCTOR_TESTADMINS_PAGER_NAME;
		public static final String TAM_VIEW_ALL_TEST_SESSIONS    = TestAdministrationManagerEJB.VIEW_ALL_TEST_SESSIONS_PAGER_NAME;
		public static final String TAM_REPORTS_PAGE_CONVERSATION = TestAdministrationManagerEJB.REPORTS_PAGER_NAME;
		public static final String TAM_PROCTOR_REPORTS_PAGE_CONVERSATION = TestAdministrationManagerEJB.PROCTOR_REPORTS_PAGER_NAME;
		public static final String TAM_VIEW_PROCTORS             = TestAdministrationManagerEJB.VIEW_PROCTORS_PAGER_NAME;

		public static final String TRM_VALIDATE_ROSTER           = TestRosterManagerEJB.VALIDATE_ROSTER_PAGER_NAME;
		public static final String TRM_VIEW_ROSTER_STUDENTS      = TestRosterManagerEJB.VIEW_ROSTER_PAGER_NAME;

		public static final String ACHIEVEMENT_SESSIONS			 = "FormativeTestAdministrationsPager";
		public static final String ACHIEVEMENT_ROSTERS			 = "FormativeTestRostersPager";

		public static final String REPORTING_GROUP				 = TestAdministrationManagerEJB.REPORTING_GROUP_PAGER_NAME;*/
		
		public static final String FILTERED_SCAN_FILES_FOR_USER_PAGER  = "FilteredScanFilesForUserPager";
		public static final String FILTERED_SORTED_STUDENTS_IN_ROSTER_PAGER  = "FilteredSortedStudentsInRosterPager";
		public static final String FILTERED_SORTED_STUDENTS_IN_HEADER_PAGER  = "FilteredSortedStudentsInHeaderPager";
		public static final String FILTERED_SORTED_TEST_ADMINS_FOR_USER_PAGER  = "FilteredSortedTestAdminsForUserPager";

		public static final String TEST_ITEM_PAGER			 	= "TestItemPager";
		public static final String SEARCH_ITEM_PAGER			= "SearchItemPager";
		public static final String STIMULUS_PAGER			 	= "StimulusPager";
		public static final String ITEMS_FOR_STIMULUS_PAGER	    = "ItemsForStimulusPager";
		
	}

	public static final String ATS_PAGER_REPORT_GROUP_STUDENTS = "ATSReportGroupStudentsPager";

	/**
	 * Must be in sync with:
	 * 1. conf/weblogic/conf.xml - "definition"; i.e. == JNDIName!
	 * 2. ejb-jars/lexington/META-INF/weblogic-ejb-jar.xml - "references"
	 */
	public static final class JMS_QUEUE {
		public static final String SCORING        = "ScoringJMSQueue";
		public static final String SCORING_STARTED= "ScoringStartedJMSQueue";
	}

	//STS
	public static final String COMMAND_STS_VIEW_HOMEPAGE = "stsViewHomepage";
	public static final String STS_WEB_SERVER_PROPERTY_NAME = "ctb.report.stswebserver";
	public static final String STS_REPORT_SERVER_PROPERTY_NAME = "ctb.report.stsreportservername";
	public static final String CONVERSATION_STS_REPORTS = "stsReports";

	public static final String COMMAND_STS_VIEW_REPORTS = "stsViewReports";
	public static final String COMMAND_PROCESS_VIEW_REPORTS = "processViewReports";
	public static final String COMMAND_STS_PREVIEW_SAMPLE_REPORT = "stsPreviewSampleReport";
	public static final String COMMAND_STS_SELECT_REPORT_TEST = "stsSelectReportTest";
	public static final String SHOULD_DISABLE_OPTIONS = "anyStudentTookTest";
	public static final String COMMAND_STS_RUN_MY_REPORTS = "stsRunMyReports";

	public static final String COMMAND_PROCESS_SELECT_REPORT_TEST = "processSelectReportTest";
	public static final String COMMAND_STS_SELECT_REPORT_INTER = "stsSelectReportInter";
	public static final String COMMAND_PROCESS_SELECT_REPORT_INTER = "processSelectReportInter";
	public static final String COMMAND_STS_SELECT_REPORT_TYPE = "stsSelectReportType";

	public static final String COMMAND_PROCESS_SELECT_REPORT_TYPE = "processSelectReportType";
	public static final String COMMAND_STS_TOGGLE_HIERARCHY = "stsToggleHierarchy";
	public static final String COMMAND_STS_TOGGLE_HIERARCHY_INTER = "stsToggleHierarchyInter";
	public static final String COMMAND_STS_RUN_REPORTS = "stsRunReports";
	// R3
	public static final String COMMAND_STS_CREATE_REPORT_TYPE = "stsCreateReportType";
	public static final String COMMAND_PROCESS_CREATE_REPORT_TYPE = "processCreateReportType";
	public static final String COMMAND_STS_CREATE_REPORT_STUDENT_SINGLE = "stsCreateReportStudentSingle";
	public static final String COMMAND_PROCESS_CREATE_REPORT_STUDENT_SINGLE = "processCreateReportStudentSingle";
	public static final String COMMAND_STS_CREATE_REPORT_STUDENT_MULTIPLE = "stsCreateReportStudentMultiple";
	public static final String COMMAND_PROCESS_CREATE_REPORT_STUDENT_MULTIPLE = "processCreateReportStudentMultiple";
	public static final String COMMAND_STS_CREATE_REPORT_FIND_STUDENT = "stsCreateReportFindStudent";
	public static final String COMMAND_PROCESS_CREATE_REPORT_FIND_STUDENT = "processCreateReportFindStudent";
	public static final String COMMAND_STS_CREATE_REPORT_FIND_STUDENT_RESULTS = "stsCreateReportFindStudentResults";
	public static final String COMMAND_PROCESS_CREATE_REPORT_FIND_STUDENT_RESULTS = "processCreateReportFindStudentResults";

	public static final String COMMAND_STS_CREATE_REPORT_FILTER = "stsCreateReportFilter";

	public static final String COMMAND_PROCESS_CREATE_REPORT_FILTER = "processCreateReportFilter";
	public static final String COMMAND_STS_CREATE_REPORT_TEST_SINGLE = "stsCreateReportTestSingle";
	public static final String COMMAND_PROCESS_CREATE_REPORT_TEST_SINGLE = "processCreateReportTestSingle";
	public static final String COMMAND_STS_CREATE_REPORT_TEST_MULTIPLE = "stsCreateReportTestMultiple";
	public static final String COMMAND_PROCESS_CREATE_REPORT_TEST_MULTIPLE = "processCreateReportTestMultiple";
	public static final String COMMAND_STS_CREATE_REPORT_OPTION = "stsCreateReportOption";
	public static final String COMMAND_PROCESS_CREATE_REPORT_OPTION = "processCreateReportOption";
	public static final String COMMAND_STS_CREATE_REPORT_CONFIRM = "stsCreateReportConfirm";
	public static final String COMMAND_PROCESS_CREATE_REPORT_CONFIRM = "processCreateReportConfirm";


	//STS Reporting Constants
	public static String REPORT_INDIVIDUAL_STUDENT = "Student Test History";
	public static String REPORT_COHORT_SUMMARY = "Cohort Summary";
	public static String REPORT_TEST_ADMIN_COMPARISON = "Group Comparison";
	public static String REPORT_TEST_ADMIN_COMPARISON_1 = "Group Comparison_1";

	public static String REPORT_RETEST = "Retest Candidates List";
	public static String REPORT_NO_RETEST = "Retest Candidates Not Tested";
	public static String REPORT_MATCH_QUALITY = "Student Match Audit";
	public static String REPORT_TEST_ADMIN_TREND = "Group Trend";

	public static String REPORT_ISTEP_GQE = "ISTEP GQE";

	//R3
	public static String STS_ACTIVATION_STATUS_ACTIVE = "Y";
	public static String STS_ACTIVATION_STATUS_INACTIVE = "N";

	public static String REPORT_SINGLE_STUDENT_SINGLE_TEST = "singlesingle";
	public static String REPORT_SINGLE_STUDENT_MULTIPLE_TEST = "singlemultiple";
	public static String REPORT_MULTIPLE_STUDENT_SINGLE_TEST = "multiplesingle";
	public static String REPORT_MULTIPLE_STUDENT_MULTIPLE_TEST = "multiplemultiple";

	public static String REPORT_PROFICIENCY_CHANGE = "Change in Proficiency - Summary Report";

	public static String REPORT_PERCENTAGE_CHANGE = "Change in Percentage - Summary Report";

	public static String REPORT_INDIVIDUAL_STUDENT_NRT = "Student Report (NRT)";

	public static String REPORT_INDIVIDUAL_STUDENT_CRT = "Student Report (CRT)";

	public static String REPORT_PROFICIENCY_CHANGE_AC = "Change in Proficiency - Summary Report";

	public static String REPORT_PERCENTAGE_CHANGE_AC = "Change in Percentage - Summary Report";

	public static String REPORT_INDIVIDUAL_STUDENT_NRT_AC = "Student Report (NRT)";

	public static String REPORT_INDIVIDUAL_STUDENT_CRT_AC = "Student Report (CRT)";

	//End R3

	public static String REPORT_INDIVIDUAL_STUDENT_AC = "StudentSearchResults";

	public static String REPORT_COHORT_SUMMARY_AC = "CohortSummary";

	public static String REPORT_TEST_ADMIN_COMPARISON_AC = "TestAdministrationComparison";

	public static String REPORT_RETEST_AC = "RetestCandidatesList";

	public static String REPORT_RETEST_RUN_AC = "RetestCandidatesList";

	public static String REPORT_NO_RETEST_AC = "RetestCandidatesNotTested";

	public static String REPORT_NO_RETEST_RUN_AC = "RetestCandidatesNotTested";

	public static String REPORT_MATCH_QUALITY_AC = "MatchQuality";

	public static String REPORT_TEST_ADMIN_TREND_AC = "TestAdministrationTrend";

	public static String REPORT_INDIANA_NODE = "INDIANA";



	public static String REPORT_LAST_NAME_AC = "STS_STUDENT_DIM_STUDENT_LAST_NAME";

	public static String REPORT_FIRST_NAME_AC = "STS_STUDENT_DIM_STUDENT_FIRST_NAME";

	public static String REPORT_MIDDLE_INITIAL_AC = "STS_STUDENT_DIM_STUDENT_MIDDLE_INITIAL";

	public static String REPORT_STUDENT_ID_AC = "STS_STUDENT_DIM_STUDENT_IDENTIFIER_1";

	public static String REPORT_SCHOOL_AC = "STS_ORG_NODE_DIM_NAME";

	public static String REPORT_COHORT_AC = "STS_COHORT_DIM_COHORT_NAME";

	public static String REPORT_ORG_AC = "userOrgNode";



	public static String REPORT_STS_TEST_DIM_TEST_NAME_AC = "STS_TEST_DIM_TEST_NAME";

	public static String REPORT_TEST_ADMIN_AC = "TestAdministration";

	public static String REPORT_SCHOOL_ORGANIZATION_AC = "SchoolOrganization";

	public static String REPORT_SORT_AC = "Sort";

	public static String REPORT_STS_CURRICULUM_DIM_NAME_AC = "STS_CURRICULUM_DIM_NAME";
	public static final String TERRANOVA_TEST_NORM_GROUP = "terranovaTestNormGroup";
    public static final String TNA_PRODUCT_RANGE_ID = "41";

	public static String REPORT_STS_TEST_DIM_TEST_NAME_RETEST_CANDIDATES_AC = "STS_TEST_DIM_TEST_NAME";

	public static String REPORT_AT_RISK_RETEST_CANDIDATES_AC = "AtRiskOnly";

	public static String REPORT_SCHOOL_ORGANIZATION_RETEST_CANDIDATES_AC = "SchoolOrganization";

	public static String REPORT_SORT_RETEST_CANDIDATES_AC = "Sort";

	public static String REPORT_STS_CURRICULUM_DIM_NAME_RETEST_CANDIDATES_AC = "STS_CURRICULUM_DIM_NAME";

	public static String REPORT_ALPHA_AC = "Alphabetical";

	public static String REPORT_AT_RISK_AC = "Y";



	public static String REPORT_RETEST_SORT_AC = "Sort";

	public static String REPORT_RETEST_AT_RISK_AC = "AtRiskOnly";

	public static String REPORT_RETEST_ORG_AC = "SchoolOrganization";



	public static String REPORT_NORETEST_ADMIN_AC = "AdminID";

	public static String REPORT_NORETEST_SORT_AC = "Sort";

	public static String REPORT_NORETEST_ORG_AC = "SchoolOrganization";



	public static String REPORT_COHORT_COHORT_AC = "rptCohort";

	public static String REPORT_COHORT_ORG_NODE_AC = "rptOrgNode";

	public static String REPORT_COHORT_TEST_AC = "rptTest";



	public static String REPORT_TREND_TEST_AC = "STS_TEST_DIM_TEST_NAME";

	public static String REPORT_TREND_ADMINS_AC = "adminList";

	public static String REPORT_TREND_COHORT_AC = "STS_COHORT_DIM_COHORT_DIM_ID";

	public static String REPORT_TREND_PERFORM_AC = "STS_TEST_RESULT_FACT_PERFORMANCE_LEVEL";

	public static String REPORT_TREND_SCALE_AC = "scaleScoreRange";

	public static String REPORT_TREND_GENDER_AC = "STS_DEMOGRAPHICS_DIM_GENDER";

	public static String REPORT_TREND_ETHNICITY_AC = "STS_DEMOGRAPHICS_DIM_ETHNICITY";

	public static String REPORT_TREND_ENG_SECOND_AC = "STS_DEMOGRAPHICS_DIM_ENGLISH_SECOND_LANGUAGE";

	public static String REPORT_TREND_LEP_AC = "STS_DEMOGRAPHICS_DIM_LEP_LEVEL";

	public static String REPORT_TREND_SOCIO_AC = "STS_DEMOGRAPHICS_DIM_SOCIOECONOMIC_STATUS";

	public static String REPORT_TREND_SPECIAL_AC = "STS_DEMOGRAPHICS_DIM_SPECIAL_EDUCATION";
	public static String TIME_LIMIT = "multisubtestsTimeLimit";

	public static String REPORT_TREND_ORG_AC = "userOrgNode";



	public static String REPORT_MATCH_ADMIN_AC = "STS_ADMINISTRATION_DIM_ADMIN_DIM_ID";

	public static String REPORT_MATCH_ORG_AC = "STS_ORG_NODE_DIM_ORG_NODE_DIM_ID";



	public static String REPORT_COMPARE_ADMINS_A_AC = "adminID_A";

	public static String REPORT_COMPARE_COHORT_A_AC = "rcohort_A";

	public static String REPORT_COMPARE_PERFORM_A_AC = "perfLev_A";

	public static String REPORT_COMPARE_SCALE_A_AC = "scaleScore_A";

	public static String REPORT_COMPARE_GENDER_A_AC = "gender_A";

	public static String REPORT_COMPARE_ETHNICITY_A_AC = "ethnicity_A";

	public static String REPORT_COMPARE_ENG_SECOND_A_AC = "esl_A";

	public static String REPORT_COMPARE_LEP_A_AC = "lep_A";

	public static String REPORT_COMPARE_SOCIO_A_AC = "socioecon_A";

	public static String REPORT_COMPARE_SPECIAL_A_AC = "specialEd_A";

	public static String REPORT_COMPARE_ORG_A_AC = "uschoolOrg_A";

	public static String REPORT_COMPARE_ADMINS_B_AC = "adminID_B";

	public static String REPORT_COMPARE_COHORT_B_AC = "rcohort_B";

	public static String REPORT_COMPARE_PERFORM_B_AC = "perfLev_B";

	public static String REPORT_COMPARE_SCALE_B_AC = "scaleScore_B";

	public static String REPORT_COMPARE_GENDER_B_AC = "gender_B";

	public static String REPORT_COMPARE_ETHNICITY_B_AC = "ethnicity_B";

	public static String REPORT_COMPARE_ENG_SECOND_B_AC = "esl_B";

	public static String REPORT_COMPARE_LEP_B_AC = "lep_B";

	public static String REPORT_COMPARE_SOCIO_B_AC = "socioecon_B";

	public static String REPORT_COMPARE_SPECIAL_B_AC = "specialEd_B";

	public static String REPORT_COMPARE_ORG_B_AC = "uschoolOrg_B";

	public static String REPORT_COMPARE_CONTENT_AC = "contentArea";

	public static String REPORT_COMPARE_FORMAT_AC = "tacFormat";


	// R3 CreateReport members
	public static String REPORT_ADMIN_AC = "Admins";

	public static String REPORT_TEST_NAME_AC = "TestName";

	public static String REPORT_ADMIN1_AC = "Admin1";

	public static String REPORT_ADMIN2_AC = "Admin2";

	public static String REPORT_ENGLISH_SECOND_LANGUAGE_AC = "EngSecLan";

	public static String REPORT_ETHNICITY_AC = "Ethnicity";

	public static String REPORT_GENDER_AC = "Gender";

	public static String REPORT_LEP_LEVEL_AC = "LEPLevel";

	public static String REPORT_CONTENT_LIST_AC = "Content";

	public static String REPORT_ORG_NODE_LIST_AC = "OrgNodes";

	public static String REPORT_STUDENT_LIST_AC = "Students";

	public static String REPORT_SOCIOECONOMIC_STATUS_AC = "SocEcon";

	public static String REPORT_SPECIAL_EDUCATION_AC = "SpecialEd";


	// STS sample images

	public static String IMAGE_INDIVIDUAL_STUDENT = "StudentTestHistory.gif";

	public static String IMAGE_COHORT_SUMMARY = "CohortSummary.gif";

	public static String IMAGE_TEST_ADMIN_COMPARISON = "GroupComparison.gif";

	public static String IMAGE_RETEST = "RetestCandidatesList.gif";

	public static String IMAGE_NO_RETEST = "RetestCandidatesNotTested.gif";

	public static String IMAGE_MATCH_QUALITY = "StudentMatchAudit.gif";

	public static String IMAGE_TEST_ADMIN_TREND = "GroupTrend.gif";



	// ATS form constants

	public static String INDIANA_CUSTOMER_NAME = "Indiana Department of Education";
	public static int INDIANA_CUSTOMER_ID = 1040;

	public static String TEST_SCORE_TYPE = "testScoreType";

	public static String FORM_ATS_CONFIRM = "frmATSConfirm";
	public static String FORM_ATS_SINGLE_STUDENT = "frmATSSingleStudent";
	public static String FORM_ATS_ORG_NODE = "frmOrgNode";
	public static String FORM_ATS_MULTIPLE_STUDENT_GROUP = "frmATSMultipleStudentGroup";
	public static String FORM_ATS_MULTIPLE_STUDENT_NAME = "frmATSMultipleStudentName";
	public static String FORM_ATS_FIND_STUDENT = "frmATSFindStudent";
	public static String FORM_ATS_FIND_STUDENT_RESULTS = "frmATSFindStudentResults";
	public static String FORM_ATS_SINGLE_TEST = "frmATSSingleTest";
	public static String FORM_ATS_MULTIPLE_TEST = "frmATSMultipleTest";
	public static String FORM_ATS_DEMOGRAPHIC = "frmATSDemographic";
	public static String FORM_ATS_OPTION = "frmATSOption";
	public static String FORM_ATS_SEARCH = "frmATSSearch";
	public static String FORM_ATS_SEARCH_RESULT = "frmATSSearchResult";
	public static String FORM_ATS_RAD_STUDENT = "radATSStudent";
	public static String FORM_ATS_CHK_ORG = "chkATSOrg";
	public static String FORM_ATS_CHK_STUDENT = "chkATSStudent";
	public static String FORM_ATS_VIS_ORG = "visATSOrg";
	public static String FORM_ATS_VIS_STUDENT = "visATSStudent";
	public static final String VALID_SCORE = "Y";
	public static final String INVALID_SCORE = "N";
	public static String FORM_ATS_PAG_ORG = "pagATSOrg";
	public static String FORM_ATS_PAG_STUDENT = "pagATSStudent";
	public static String FORM_ATS_CHK_CONTENT = "chkATSContent";
	public static String FORM_ATS_RAD_ORG = "radATSOrg";
	public static String FORM_ATS_RAD_ADMIN = "radATSAdmin";
	public static String FORM_ATS_CHK_ADMIN = "chkATSAdmin";
	public static String FORM_ATS_CHK_FILTER = "chkATSFilter";
	public static String FORM_STUDENT_DIM_ID = "studentDimId";
	public static String FORM_ATS_RAD_REPORT_NAME = "radATSReportName";

	public static String FORM_RAD_REPORT = "radSTSReport";

	public static String FORM_REPORT_TYPE = "reportType";

	public static String FORM_STS_TEST = "frmSTSTest";

	public static String FORM_SELECT_FROM_LIST = "Select from List";

	public static String FORM_SELECT_ALL = "All&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	public static String FORM_LAST_NAME = "lastName";

	public static String FORM_FIRST_NAME = "firstName";

	public static String FORM_MIDDLE_INITIAL = "middleInitial";

	public static String FORM_STUDENT_ID = "studentId";

	public static String FORM_SCHOOL = "school";

	public static String FORM_COHORT = "cohort";

	public static String FORM_TEST_NAME = "testName";

	public static String FORM_TEST_ADMINISTRATION = "testAdministration";

	public static String FORM_TEST_ADMINISTRATION_CHECKBOXES = "testAdministrationCheckboxes";

	public static String FORM_CONTENT_AREA = "contentArea";

	public static String FORM_PERFORMANCE_INDICATOR = "performanceIndicator";

	public static String FORM_SCALE_SCORE_1 = "scaleScoreRange1";

	public static String FORM_SCALE_SCORE_2 = "scaleScoreRange2";

	public static String FORM_FORMAT = "format";

	public static String FORM_GENDER = "gender";

	public static String FORM_ETHNICITY = "ethnicity";

	public static String FORM_ENGLISH_SECOND = "englishSecond";

	public static String FORM_LEP_LEVEL = "lepLevel";

	public static String FORM_SOCIO = "socioeconomic";

	public static String FORM_SPECIAL_ED = "specialEducation";

	public static String FORM_ORG_NODE_1 = "chkOrganizationNodeID_1";

	public static String FORM_SORT = "sort";

	public static String FORM_AT_RISK = "atRisk";

	public static String FORM_BIRTHDATE = "birthdate";

	public static String FORM_BIRTHDATE_DAY = "birthdateDay";

	public static String FORM_BIRTHDATE_MONTH = "birthdateMonth";


	public static String FORM_BIRTHDATE_YEAR = "birthdateYear";

	public static String ATS_STUDENT_LIST = "atsStudentList";

	public static String ATS_CURRICULUM_LIST = "atsCurriculumList";

	public static String ATS_REPORT_NAME_LIST = "atsReportNameList";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_LIST = "atsCustomerDemographicsList";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_GENDER = "atsCustomerDemographicsGender";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_ETHNICITY = "atsCustomerDemographicsEthnicity";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_SPECIAL_EDUCATION = "atsCustomerDemographicsSpecialEducation";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_ENGLISH_SECOND_LANGUAGE = "atsCustomerDemographicsEnglishSecondLanguage";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_LEP_LEVEL = "atsCustomerDemographicsLepLevel";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_SOCIOECONOMIC_STATUS = "atsCustomerDemographicsSocioeconomicStatus";

	public static String ATS_CUSTOMER_DEMOGRAPHICS_ALL = "atsCustomerDemographicsAll";

	public static String ATS_ADMIN_LIST = "atsAdminList";

	public static String ATS_STUDENT_NAME_LIST = "atsStudentNameList";

	public static String ATS_ORG_NAME_LIST = "atsOrgNameList";


	public static String ATS_ADMIN_NAME_LIST = "atsAdminNameList";

	public static String ATS_ADMIN_DATE_LIST = "atsAdminDateList";

	public static String ATS_ADMIN_GRADE_LIST = "atsAdminGradeList";

	public static String SEARCH_DISPLAY_STRING = "searchString";

	public static String FORM_TEST_ADMINISTRATION_2 = "testAdministration_2";

	public static String FORM_COHORT_2 = "cohort_2";

	public static String FORM_CONTENT_AREA_2 = "contentArea_2";

	public static String FORM_PERFORMANCE_INDICATOR_2 = "performanceIndicator_2";

	public static String FORM_SCALE_SCORE_1_2 = "scaleScoreRange1_2";

	public static String FORM_SCALE_SCORE_2_2 = "scaleScoreRange2_2";

	public static String FORM_FORMAT_2 = "format_2";

	public static String FORM_GENDER_2 = "gender_2";

	public static String FORM_ETHNICITY_2 = "ethnicity_2";

	public static String FORM_ENGLISH_SECOND_2 = "englishSecond_2";

	public static String FORM_LEP_LEVEL_2 = "lepLevel_2";

	public static String FORM_SOCIO_2 = "socioeconomic_2";

	public static String FORM_SPECIAL_ED_2 = "specialEducation_2";

	public static String FORM_ORG_NODE = "treeReportTypeStsIntertreeSelectInput";



	//STS Login Constants

	public static String LOGIN_URI_EXT = "/default/stsViewHomepage";

	public static String LOGOUT_URI_EXT = "/userlogin/userLogout";

	public static String LOGIN_STS_CUSTOMER = "Indiana Department of Education";

	public static String LOGIN_STS_CUSTOMER_IA = "Indiana Department of Education";

	public static String STS_USER = "stsUser";

	public static String STS_FORM_SELECTIONS = "selections";

	public static String ATS_ROLE_NAME = "atsRoleName";

	public static String ATS_ORG_NODE_LIST = "atsOrgNodeList";

	//STS Report Parameters Constants

	public static String REPORT_PARAM_TYPE_TEST_NAME = "TEST_NAME";

	public static String REPORT_PARAM_TYPE_ADMIN_NAME = "ADMIN_NAME";

	public static String REPORT_PARAM_TYPE_COHORT_NAME = "COHORT_NAME";

	public static String REPORT_PARAM_TYPE_SUB_TEST_NAME = "SUB_TEST_NAME";

	public static String REPORT_PARAM_TYPE_PERFORMANCE_LEVEL = "PERFORMANCE_LEVEL";

	public static String REPORT_PARAM_TYPE_GENDER = "GENDER";

	public static String REPORT_PARAM_TYPE_ETHNICITY = "ETHNICITY";

	public static String REPORT_PARAM_TYPE_SPECIAL_EDUCATION = "SPECIAL_EDUCATION";

	public static String REPORT_PARAM_TYPE_ENGLISH_SECOND_LANGUAGE = "ENGLISH_SECOND_LANGUAGE";

	public static String REPORT_PARAM_TYPE_LEP_LEVEL = "LEP_LEVEL";

	public static String REPORT_PARAM_TYPE_SOCIOECONOMIC_STATUS = "SOCIOECONOMIC_STATUS";

	public static String REPORT_PARAM_TYPE_WITHDRAW_REASON = "WITHDRAW_REASON";

	public static String REPORT_OPTION_LIST_NAME = "reportParams";

	public static String REPORT_PARAM_STUDENT_ID_LIST = "studentIdList";

	public static String REPORT_PARAM_ORG_ID_LIST = "orgIdList";

	public static String REPORT_PARAM_ADMIN_ID_LIST = "adminIdList";

	public static String REPORT_PARAM_REPORT_NAME = "reportReportName";

	public static String REPORT_PARAM_CONTENT_LIST = "contentList";

	// STS Edit
	// Conversation Commands
	public static String CONVERSATION_STS_EDIT = "stsEdit";

	public static String COMMAND_STS_SEARCH_EDIT = "stsSearchEdit";
	public static String COMMAND_PROCESS_SEARCH_EDIT = "processSearchEdit";
	public static String COMMAND_STS_RESULTS_EDIT = "stsResultsEdit";
	public static String COMMAND_PROCESS_RESULTS_EDIT = "processResultsEdit";
	public static String COMMAND_STS_EDIT = "stsEdit";
	public static String COMMAND_PROCESS_EDIT = "processEdit";
	public static String COMMAND_STS_CONFIRM_EDIT = "stsConfirmEdit";
	// Flags
	public static String EDIT_LAST_NAME_FLAG = "editLastNameFlag";
	public static String EDIT_FIRST_NAME_FLAG = "editFirstName";
	public static String EDIT_MIDDLE_INITIAL_FLAG = "editMiddleInitialFlag";
	public static String EDIT_BIRTHDATE_FLAG = "editBirthdateFlag";
	public static String EDIT_GENDER_FLAG = "editGenderFlag";
	public static String EDIT_STUDENT_ID_FLAG = "editStudentIdFlag";
	public static String EDIT_WITHDRAW_FLAG = "editWithdrawFlag";
	public static String EDIT_ETHNICITY_FLAG = "editEthnicityFlag";
	public static String EDIT_ESL_FLAG = "editESLFlag";
	public static String EDIT_LEP_FLAG = "editLEPFlag";
	public static String EDIT_SOCIOECONOMIC_FLAG = "editSocioeconomicFlag";
	public static String EDIT_SPECIAL_ED_FLAG = "editSpecialEdFlag";
	public static String EDIT_DEMO_FLAG = "editDemoFlag";
	// Form Names
	public static String EDIT_FORMNAME_SEARCH = "frmSearchEditSTS";
	public static String EDIT_FORMNAME_RESULTS = "frmResultsEditSTS";
	public static String EDIT_FORMNAME_EDIT = "frmEditSTS";
	// Form Values
	public static String SEARCH_LAST_NAME = "searchLastName";
	public static String SEARCH_FIRST_NAME = "searchFirstName";
	public static String SEARCH_MIDDLE_INITIAL = "searchMiddleInitial";
	public static String SEARCH_STUDENT_ID = "searchStudentId";
	public static String SEARCH_SCHOOL = "searchSchool";

	public static String EDIT_USER_ID = "editUserId";

	public static String SEARCH_STUDENT_RAD = "radSTSEdit";

	public static String EDIT_LAST_NAME = "editLastName";
	public static String EDIT_FIRST_NAME = "editFirstName";
	public static String EDIT_MIDDLE_INITIAL = "editMiddleInitial";
	public static String EDIT_BIRTHDATE = "editBirthdate";
	public static String EDIT_GENDER = "editGender";
	public static String EDIT_STUDENT_ID = "editStudentId";
	public static String EDIT_WITHDRAW = "editWithdraw";
	public static String EDIT_ETHNICITY = "editEthnicity";
	public static String EDIT_ESL = "editESL";
	public static String EDIT_LEP = "editLEP";
	public static String EDIT_SOCIOECONOMIC = "editSocioeconomic";
	public static String EDIT_SPECIAL_ED = "editSpecialEd";

	public static String EDIT_UPDATED_BY = "updatedBy";
	public static String EDIT_UPDATED_DATE = "updatedDate";

	// End STS Edit



	// The following constants are used as keys in the CTBMessage object.



	// Cross-conversation parameter labels

	public static final String DISPLAY_MESSAGE_PARAMETER_LABEL="displayMessageParameterLabel";



	// org node parameter labels

	public static final String NODE_LIST_ARRAY_PARAMETER_LABEL="organizationNodeArrayHashmapLabel";

	public static final String NODE_ID_PARAMETER_LABEL        ="organizationNodeIDParameterLabel";

	public static final String NODE_ID_ARRAY_PARAMETER_LABEL  =NODE_ID_PARAMETER_LABEL + ".array";

	public static final String NODE_NAME_PARAMETER_LABEL      ="organizationNodeNameParameterLabel";

	public static final String TOGGLE_NODE_ID_PARAMETER_LABEL ="toggleOrganizationNodeIDParameterLabel";

	public static final String FILE_UPLOAD_FORM_NAME          ="frmUploadOrganizationFile";

	public static final String NODE_CATEGORY_ARRAY_LIST       ="nodeCategoryBeanArrayListLabel";

	public static final String NODE_DUMMY_SUFFIX = " Placeholder";

	public static final String NODE_DUMMY_DESC = "";





	// org node category constants

	public static final Integer NODE_CATEGORY_GROUP_LEVEL = new Integer( 0 );

	public static final String NODE_CATEGORY_GROUP_SUFFIX = " Group";

	public static final String NODE_CATEGORY_NAMES_ARRAY_LABEL = "categoryNamesArrayListLabel";



	// test catalog parameter labels

	public static final String TEST_CATALOG_ID_PARAMETER_LABEL        = "testCatalogIDParameterLabel";

	public static final String TEST_CATALOG_ID_ARRAY_PARAMETER_LABEL  = TEST_CATALOG_ID_PARAMETER_LABEL + ".array";

	public static final String TEST_ADMINISTRATION_ID_PARAMETER_LABEL = "testAdministrationIDParameterLabel";

	public static final String TOGGLE_TEST_CATALOG_ID_PARAMETER_LABEL = "toggleTestCatalogIDParameterLabel";

	public static final String SUBCRIPTION_ID_PARAMETER_LABEL         = "subscriptionIDParameterLabel";



	// user parameter labels

	public static final String CUSTOMER_ID_PARAMETER_LABEL           = "customerIDParameterLabel";

	public static final String ADMINISTRATOR_ID_PARAMETER_LABEL      = "administratorIDParameterLabel";

	public static final String CURRENT_USER_ID_PARAMETER_LABEL       = "currentUserIDParameterLabel";

	public static final String USER_ID_PARAMETER_LABEL               = "userIDParameterLabel";

	public static final String PASSWORD_PARAMETER_LABEL              = "passwordParameterLabel";

	public static final String CONFIRM_PASSWORD_PARAMETER_LABEL      = "confirmPasswordParameterLabel";

	public static final String HINT_QUESTION_PARAMETER_LABEL         = "hintQuestionParameterLabel";

	public static final String HINT_ANSWER_PARAMETER_LABEL           = "hintAnswerParameterLabel";

	public static final String FORCE_PASSWORD_CHANGE_PARAMETER_LABEL = "forcePasswordChangeParameterLabel";



	// student parameter labels

	public static final String STUDENT_ID_PARAMETER_LABEL       = "studentIDParameterLabel";

	public static final String STUDENT_ID_ARRAY_PARAMETER_LABEL = STUDENT_ID_PARAMETER_LABEL + ".array";

	public static final String GROUP_ID_PARAMETER_LABEL         = "groupIDParameterLabel";

	public static final String GROUP_ID_ARRAY_PARAMETER_LABEL   =  GROUP_ID_PARAMETER_LABEL + ".array";

	public static final String STUDENT_LOGIN_NAME_PARAMETER_LABEL       = "studentLoginNameParameterLabel"; // Used in Edit Student Error page



	// query parameter labels

	public static final String FIRST_NAME_PARAMETER_LABEL              = "firstNameParameterLabel";

	public static final String LAST_NAME_PARAMETER_LABEL               = "lastNameParameterLabel";

	public static final String USERNAME_PARAMETER_LABEL                = "userNameParameterLabel";

	public static final String EMAIL_PARAMETER_LABEL                   = "emailParameterLabel";

	public static final String ROLE_PARAMETER_LABEL                    = "roleParameterLabel";

	public static final String BIRTHDAY_PARAMETER_LABEL                = "dateofBirthParameterLabel";

	public static final String GENDER_PARAMETER_LABEL                  = "genderParameterLabel";

	public static final String GRADE_PARAMETER_LABEL                   = "gradeParameterLabel";

	public static final String STUDENT_ORGANIZATION_ID_PARAMETER_LABEL = "studentOrganizationIDParameterLabel";



	public static final String REQ_PASSWORD_SENT = "userlogin.pwdsentmsg";



	// test administration parameter labels

	public static final String ACCESS_CODE_PARAMETER_LABEL        ="accessCodeParameterLabel";



	// IBS parameter labels
	public static final String PRODUCT_LIST_PARAMETER_LABEL = "productListParameterLabel";
	public static final String CURRICULUM_NODE_LIST_PARAMETER_LABEL = "curriculumNodeListParameterLabel";
	public static final String ITEM_LIST_PARAMETER_LABEL = "itemListParameterLabel";
	public static final String GROUP_ITEM_LIST_PARAMETER_LABEL = "groupItemListParameterLabel";
	public static final String GROUP_MATERIAL_PARAMETER_LABEL = "groupMaterialParameterLabel";
	public static final String CURRENT_PRODUCT_PARAMETER_LABEL = "currentProductParameterLabel";
	public static final String TESTLET_PARAMETER_LABEL = "testletParameterLabel";
	public static final String ITEMDETAIL_PARAMETER_LABEL = "itemdetailParameterLabel";
	public static final String REPOSITORY_NEW_TESTS_LABEL = "RepositoryNewTestsLabel";
	public static final String REPOSITORY_MY_TESTS_LABEL = "RepositoryMyTestsLabel";
	public static final String REPOSITORY_SHARED_TESTS_LABEL = "RepositorySharedTestsLabel";
	public static final String REPOSITORY_UNFINISHED_TESTS_LABEL = "RepositoryUnfinishedTestsLabel";
	public static final String REPOSITORY_CTB_TESTS_LABEL = "RepositoryCTBTestsLabel";
	public static final String REPOSITORY_STANDARD_TESTS_LABEL = "RepositoryStandardTestsLabel";
	public static final String SIZE_OF_CRITERIA_LIST = "sizeOfCriteriaList";
	public static final String HAS_RETRIEVED_WORKSHEET = "hasRetrievedWorksheet";
	public static final String TEST_HAS_BEEN_SAVED = "testHasBeenSaved";
	public static final String CURRICULUM_NODE_BROWSER = "curriculumNodeBrowser";


	// IBS Select Items labels
	public static final String SHOW_GROUP_ITEMS_PARAMETER_LABEL = "showGroupItemsParameterLabel";
	public static final String CATEGORY_PATH_NODES_LIST_PARAMETER_LABEL = "categoryPathNodesListParameterLabel";
	public static final String CHILDREN_NODES_LIST_PARAMETER_LABEL = "childrenNodesListParameterLabel";
	public static final int ITEM_DESCRIPTION_CONCATENTATION_LENGTH = 80;
	public static final int CURRICULUM_NODE_CONCATENTATION_LENGTH = 51;
	public static final int CURRICULUM_NODE_CONCATENTATION_LENGTH_WITH_ID = 41;
	//public static final int CR_ITEM_CONCATENTATION_LENGTH_ADJUSTMENT = 8;
	public static final String SELECTED_ITEM_IDS = "selectedItemIdsParameterLabel";
	public static final String SELECTED_ITEM_ID = "selectedItemId";


	// Default values for primary administrator of an organization

	public static final String DEFAULT_ROLE = "Administrator";



	public static final String DEFAULT_COUNTRY = "US";

	//used in the test catalog select single node page
	//to determine whether or not you can toggle a node
	//the condition is if a radio button is selected under the node, you may not toggle.
	public static final String NO_TOGGLE_FLAG = "noToggleFlag";

	/**
	 * The request attribute key for the GRNDS qualified name.
	 * This attribute is used by GRNDS to determine the conversation name and
	 * command. Unset this attribute before using a request dispatcher to
	 * forward to another conversation.
	 */
	public static final String REQ_GRNDS_QNAME = "grnds.request.qname";


	// the integer constant for megagbyte
	public static final int MEGABYTE                                   = 1048576;

	// the integer constant for kilobyte
	public static final int KILOBYTE                                   = 1024;

	public static final int NO_ID = -1;


	// the version of this code -- update with each release
	public static final String LEXINGTON_VERSION                        = "3.1";

	// arrays used for random password generation
	public static final String ALPHA_ARRAY                              = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUM_ARRAY                                = "1234567890";
    public static final String SPECIAL_CHAR_ARRAY                       = "%&<>\")(+~`!@#$^*=|}{][:;?/\\.,";
	// the length of student passwords
	public static final int GENERATED_STUDENT_PASSWORD_LENGTH           = 6;

	// the length of user passwords
	public static final int GENERATED_USER_PASSWORD_LENGTH              = 6;
	public static final int MIN_USER_PASSWORD_LENGTH                    = 6;
	public static final int MAX_USER_PASSWORD_LENGTH                    = 30;
    
    //password expire month
    
    public static final int PASSWORD_EXPIRE_MONTH                       = 3;

	// the length of access codes
	public static final int GENERATED_ACCESS_CODE_LENGTH                = 10;
	public static final int MAX_ACCESS_CODE_LENGTH                      = 32;
	public static final int MIN_ACCESS_CODE_LENGTH                      = 6;

	// Max Password Histories
	public static final int PASSWORD_HISTORY_LIMIT                   = 5;

	// the length of user names
	public static final int GENERATED_USERNAME_FIRST_NAME               = 12;
	public static final int GENERATED_USERNAME_LAST_NAME                = 12;
	public static final int MIN_USERNAME_LENGTH                         = 3;
	public static final int MAX_USERNAME_LENGTH                         = 30;

	// the role names
	public static final String ROLE_NAME_ALL                                 = "ALL";
	public static final String ROLE_NAME_CTB_ROOT                            = "CTB ROOT";
	public static final String ROLE_NAME_ACCOUNT_MANAGER                     = "ACCOUNT MANAGER";
	public static final String ROLE_NAME_ADMINISTRATOR                       = "ADMINISTRATOR";
	public static final String ROLE_NAME_COORDINATOR                         = "COORDINATOR";
	public static final String ROLE_NAME_ACCOMMODATIONS_COORDINATOR          = "ADMINISTRATIVE COORDINATOR";
	public static final String ROLE_NAME_CTB_ADMINISTRATOR                   = "CTB ADMINISTRATOR";
	public static final String ROLE_NAME_PROCTOR                             = "PROCTOR";

	public static final String ROLE_NAME_TEST_ADMIN_PROCTOR                  = "COORDINATOR";

	public static final String ROLE_NAME_ATS_ADMIN                           = "ATS ADMIN";
	public static final String ROLE_NAME_ATS_COORDINATOR                     = "ATS COORDINATOR";
	public static final String ROLE_NAME_ATS_SUPER_ADMIN                     = "ATS SUPER ADMIN";
	
	public static final String ROLE_NAME_ITEM_AUTHOR                         = "ITEM AUTHOR";
	public static final String ROLE_NAME_ITEM_ADMINISTRATOR                  = "ITEM ADMINISTRATOR";
	
	// CTB IDS
	public static final Integer CTB_SYSTEM_USER_ID = new Integer(1);
	public static final Integer CTB_ROOT_USER_ID   = new Integer(4);
	public static final int CTB_CLIENT_USER_ID = 6;
	public static final int CTB_ROOT_ORG_NODE_ID                        = 1;
	public static final int CTB_ORG_NODE_ID                             = 2;
	public static final int CTB_CUSTOMER_ID                             = 2;
	public static final int CTB_ROOT_CUSTOMER_ID                        = 1;

	// CTB Name
	public static final String CTB_ORG_NODE_ID_STRING                          = "2";
	public static final String CTB_ROOT_ORG_NODE_NAME                          = "CTB";

	//report constants
	public static final String REPORT_REDIRECT_URL_LABEL = "ctb.report.redirecturl";
	public static final String MY_REPORTS_REDIRECT_URL_LABEL = "ctb.report.myreportsurl";

	public static final String WEB_SERVER_PROPERTY_NAME = "ctb.report.webserver";
	public static final String REPORT_SERVER_PROPERTY_NAME = "ctb.report.reportservername";
	public static final String REPORT_SERVER_LISTEN_PORT = "ctb.report.reportserverlistenport";
	public static final String REPORT_DAEMON_LISTEN_PORT = "ctb.report.reportdaemonlistenport";
	public static final String FOLDER_PROPERTY_NAME = "ctb.report.foldername";
	public static final String VOLUME_PROPERTY_NAME = "iknow";
	public static final String REPORT_VOLUME_NAME = "ctb.report.volumename";
    public static final String REPORT_SERVER_USERNAME = "ctb.report.username";
    public static final String REPORT_SERVER_PASSWORD = "ctb.report.password";
    public static final String REPORT_SERVER_WAITTIME = "ctb.report.waittime";
    //public static final String VOLUME_PROPERTY_NAME = "iknow";

	//report names
	public static final String INDIVIDUAL_TEST_TICKET_REPORT = "IndividualTestTicket";
	public static final String SUMMARY_TEST_TICKET_REPORT = "SummaryTestTicket";
	public static final String INDIVIDUAL_TUTORIAL_TICKET_REPORT = "IndividualTutorialTicket";
	public static final String SUMMARY_TUTORIAL_TICKET_REPORT = "SummaryTutorialTicket";

	public static final String PERFORMANCE_GROUPING_REPORT = "PerformanceGroupingReport";
	public static final String ITEM_ANALYSIS_REPORT = "ItemAnalysisReport";
	public static final String SUMMARY_REPORT = "SummaryReport";
	public static final String INDIVIDUAL_ACHIEVEMENT_REPORT = "IndividualAchievement";
	public static final String SUMMARY_ACHIEVEMENT_REPORT = "SummaryAchievement";
	public static final String HAS_CHECKED_NODES = "hasCheckedNodes.key";



  /**
   * Name of attribute to be used to uniquely identify the test delivery status
   * as a property object.
   */
  public static String TD_STATUS = "testdelivery.status";

  /**
   * Name of attribute to be used to uniquely identify test delivery status
   * code within attributes or properties.
   */
  public static String TD_STATUS_CODE = "testdelivery.status.code";

  /**
   * Name of attribute to be used to uniquely identify test delivery status
   * message within attributes or properties.
   */
  public static String TD_STATUS_MESSAGE = "testdelivery.status.message";

  /**
   * Name of attribute to be used to uniquely identify the need to close a
   * test session within attributes or properties.
   */
  public static String TD_SESSION_CLOSE_TEST = "testdelivery.session.closeTest";

  //the values below are used for mapping email recipient email addresses
  //to type of query
  private static String accountManagement = "oas_account_management@ctb.com";

  private static String customerSupport = "oas_customer_services@ctb.com";

  /** recipient for new orders/billing inquiries*/
  public static String EMAIL_ADDRESS_NEW_ORDER_OR_BILLING_INQUIRY = customerSupport;

  /** recipient for Adding/Changing Administrators/Coordinators inquiries*/
  public static String EMAIL_ADDRESS_CHANGING_ADMINISTRATORS_INQUIRY = accountManagement;

  /** recipient for Viewing/Printing Reports inquiries*/
  public static String EMAIL_ADDRESS_VIEWING_REPORTS_INQUIRY = accountManagement;

  /** recipient for Creating Test Administration inquiries*/
  public static String EMAIL_ADDRESS_CREATING_TEST_ADMINISTRATIONS_INQUIRY = accountManagement;

  /** recipient for working with test tickets inquiries*/
  public static String EMAIL_ADDRESS_TEST_TICKETS_INQUIRY = accountManagement;

  /** recipient for Adding/Moving Students inquiries*/
  public static String EMAIL_ADDRESS_ADDING_OR_MOVING_STUDENTS_INQUIRY = accountManagement;

  /** recipient for Navigating Screens inquiries*/
  public static String EMAIL_ADDRESS_NAVIGATING_SCREENS_INQUIRY = accountManagement;

  /** recipient for Administering Tests inquiries*/
  public static String EMAIL_ADDRESS_ADMINISTERING_TESTS_INQUIRY = accountManagement;

  /** recipient for Other inquiries*/
  public static String EMAIL_ADDRESS_OTHER_INQUIRY = accountManagement;
  public static final String EMAIL_SUBJECT_WELCOME = "OAS User Login";
  public static final String EMAIL_SUBJECT_PASSWORD = "Password";
  public static final String EMAIL_SUBJECT_PASSWORD_CHANGE = "Password Notification";

  public static final String EMAIL_USER_FROM_FIELD = "oas_account_management@ctb.com";
  //public static final String EMAIL_TYPE_WELCOME = "welcomeEmail";
  //public static final String EMAIL_TYPE_PASSWORD_CHANGE = "userPasswordChangeEmail";
  //public static final String EMAIL_TYPE_PASSWORD = "passwordEmail";

  public static final String EMAIL_TYPE_HELP = "helpEmail";
  public static final String EMAIL_MAP_MSG_TYPE = "emailType";
  public static final String EMAIL_MAP_MSG_TO = "emailTo";
  public static final String EMAIL_MAP_MSG_FROM = "emailFrom";

  public static final String EMAIL_MAP_MSG_SUBJECT = "emailSubject";
  public static final String EMAIL_MAP_MSG_CONTENT = "emailContent";
  public static final String EMAIL_MAP_MSG_USERNAME = "username";
  public static final String EMAIL_MAP_MSG_PASSWORD = "password";
  public static final String EMAIL_MAP_MSG_IKNOW_URL = "iknowURL";

  public static final String OAS_IKNOW_URL =
				  "https://testadministration.ctb.com/iknow";
  
  public static final String OAS_QA_URL =
				  "https://oastest1.ctb.com/";                
                  
  public static final String OE_IKNOW_URL =	"https://oe.ctb.com";

  //public static String SWF_COLUMN_NAME              = "swfColumnName";
  public static String SWF_ITEM_SET_ID              = "swfItemSetId";

  // Variables to be used by the SWF servlet.
  public static String SWF_TABLE_NAME        = "session.swf.table_name";
  public static String SWF_PRIMARY_KEY       = "session.swf.primary_key";
  public static String SWF_PRIMARY_KEY_VALUE = "session.swf.primary_key_value";
  public static String SWF_COLUMN_NAME       = "session.swf.column_name";

  // Variables to be used by StimulusAccessServlet
  public static String STIMULUS_GRAPHIC_NAME = "session.stimulus.graphic_name";

  // for Manage/EditOrganizationNode Conversation
 // public static final String BEAN_LABEL_TOP_NODE = NodeVO.VO_LABEL +"_TOP_NODE";
 // public static final String BEAN_ARRAY_LABEL_TOP_NODE = NodeVO.VO_ARRAY_LABEL +"_TOP_NODE";
  //public static String SINGLE_ENTITY_TYPE_BOOLEAN = "singleEntityTypeBoolean";
 // public static String ORIGINAL_NODE_BEAN = "originalNodeVO";
  //public static String TOP_LEVEL_ADMIN_BOOLEAN = "topLevelAdminBoolean";
  //public static String STUDENTS_ASSOCIATED_BOOLEAN = "studentsAssociated";


  // Grnds Authorization EJB
  public static String GRNDS_AUTHORIZATION_EJB = "AuthorizationEJB";

  //Test Administration/Session Ddefault Login Window Constants
  public static String DEFAULT_LOGIN_BEGIN_TIME = "080000"; //8AM
  public static String DEFAULT_LOGIN_END_TIME   = "150059"; //3PM

  // Constant used in determining what Pager to return
  public static int LIST_PAGER_SIZE_LIMIT = 80;

  //test-tutorial session constants
  public static final String NEW_TUTORIAL_SESSION_FORM = "frmNewTutorialSession";
  public static final String WIZARD_STEP_LABEL = "wizardStepLabel";

  //R2 Test Library Constants
  public static final String TEST_TYPE_LABEL = "testTypeLabel";
  public static final String PRODUCT_ID_LABEL = "productIdLabel";
  public static final String PRODUCT_TYPE = "productNameLabel";
  public static final String PRODUCT_NAME_LABEL = "productNameLabel";
  public static final String TEST_ID_LABEL = "testIdLabel";
  public static final String ITEMSET_ID_LABEL = "ItemSetIdLabel";
  public static final String ASSEMBLY_TEST_LABEL = "AT";
  public static final String TEST_CATALOG_LABEL = "TC";
  public static final String TEST_PUBLISHED_STATUS = "PB";
  public static final String TEST_IN_PROGRESS_STATUS = "IP";
  public static final String TEST_REPUBLISH_IN_PROGRESS_STATUS = "RP";
  public static final String TEST_UNPUBLISHED_STATUS = "UP";
  public static final String TEST_ERROR_PUBLISHED_STATUS = "EP";
  public static final String GLOBAL_ANSWER_CHOICE_MAP_LABEL = "globalAnswerChoiceMapLabel";
  public static final String REPOSITORY_LABEL = "repositoryLabel";
  public static final String IS_SHARED_LABEL = "isSharedLabel";
  public static final String TEST_SESSION_REFERRER_LABEL = "testSession";
  public static final String IS_RESEARCH_STUDY_TEST_LABEL = "isResearchStudyTest";
  public static final String IS_ASSEMBLED_TEST_LABEL = "isAssembledTest";
  public static final String IS_TEST_PRINTABLE_LABEL = "isTestPrintable";
  public static final String IS_TEST_SCANNABLE_LABEL = "isTestScannable";
  public static final String IS_TEST_KEYENTERABLE_LABEL = "isTestKeyenterable";
  public static final String IS_CTB_TEST_LABEL = "isCTBTest";
  public static final String REFERRER_LABEL = "referrerLabel";
  public static final String GRADE_LABEL = "gradeLabel";
  public static final String LEVEL_LABEL = "levelLabel";
  public static final String FORM_LABEL = "formLabel";
  public static final String TEST_DISPLAY_NAME = "testDisplayName";
  public static final String TEST_DETAILS_SELECTION = "testDetailsSelection";

  public static final String TEST_HAS_CR_ONLY_ITEM = "CRONLY";
  public static final String TEST_HAS_SR_ONLY_ITEM = "SRONLY";
  public static final String TEST_HAS_SR_CR_BOTH_ITEM = "CRSRBOTH";

  //Tutorial Login Window Constants
  public static final String TUTORIAL_LOGIN_START_TIME = "000000";
  public static final String TUTORIAL_LOGIN_END_TIME = "235959";

  public static String WEB_TREE_DESC_ATTRIB = "tree.WebTreeDescriptor";
  public static final String DUMMY_NODE_USER_OBJ = "Top";

  //>>for Online Enrollment,  put here all additions for oe
  //>> oe-start
  public static final String DESTINATION_OE = "OE";
  public static final String OE2_LANDING_URI = "/oehome/default";
  public static final String OE_LANDING_URI = "/selectschool/testadminlandcmd";
  public static String RESEARCH_PRODUCTS_BOOLEAN = "researchProducts";
  public static final String ROLE_NAME_OE_ADMINISTRATOR = "OEAdmin";
  //<< oe-end

   // Message - driven beans
  public static final String ITEM_SET_ID = "itemSetId";
  public static final String PUBLISHED = "PB";

  //constants for special codes
  public static final String MULTIPLE_RESPONSE_LABEL = "MR";
  public static final String SINGLE_RESPONSE_LABEL = "SR";
  public static final String HAS_SPECIAL_CODES_LABEL = "hasSpecialCodes";
  public static final String STUDENT_FIRST_NAME = "studentFirstName";
  public static final String STUDENT_LAST_NAME = "studentLastName";

  public static final String HAS_RESEARCH_DATA_LABEL = "hasResearchData";
  public static final String RESEARCH_DATA_LIST_LABEL = "researchDataList";

  //constants for ATS R2
  public static final String NOT_SELECTED = "NOT_SELECTED";

  //constants for key-entry scoring
  public static final String KEY_ENTRY_STUDENT_FILTER_STATUS_LABEL = "keyEntryStudentFilterStatusLabel";
  public static final String KEY_ENTRY_STUDENT_FILTER_SHOW_LABEL = "keyEntryStudentFilterShowLabel";
  public static final String KEY_ENTRY_STUDENT_FILTER_HIDE_LABEL = "keyEntryStudentFilterHideLabel";
  
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_1 = "studentFilterField1";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_2= "studentFilterField2";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_3 = "studentFilterField3";
  public static final String KEY_ENTRY_STUDENT_FILTER_SELECTED_OPERATORS = "studentFilterSelectedOperators";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_1 = "studentFilterOperator1";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_2 = "studentFilterOperator2";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_3 = "studentFilterOperator3";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_4 = "studentFilterOperator4";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_5 = "studentFilterOperator5";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERATOR_6 = "studentFilterOperator6";
  public static final String KEY_ENTRY_STUDENT_FILTER_SELECTED_OPERANDS = "studentFilterSelectedOperands";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_1 = "studentFilterOperand1";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_2 = "studentFilterOperand2";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_3 = "studentFilterOperand3";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_4 = "studentFilterOperand4";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_5 = "studentFilterOperand5";
  public static final String KEY_ENTRY_STUDENT_FILTER_OPERAND_6 = "studentFilterOperand6";
  
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_UNSELECTED = "studentFilterFieldUnselected";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_LAST_NAME = "studentFilterFieldLastName";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_FIRST_NAME = "studentFilterFieldFirstName";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_MIDDLE_NAME = "studentFilterFieldMiddleName";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_SCORING_STATUS = "studentFilterFieldScoringStatus";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_SCORED_BY = "studentFilterFieldScoredBy";
  public static final String KEY_ENTRY_STUDENT_FILTER_FIELD_ORG_PATH = "studentFilterFieldOrgPath";
  
  public static final String KEY_ENTRY_STUDENT_FILTER_OP_CONTAINS = "Contains";
  public static final String KEY_ENTRY_STUDENT_FILTER_OP_EQUALS = "Equals";
  public static final String KEY_ENTRY_STUDENT_FILTER_OP_BEGINS_WITH = "Begins with";
  
  public static final String SELECTED_NODE_NAME_LABEL = "selectedNodeNameLabel";
  public static final String SELECTED_NODE_ID_LABEL = "selectedNodeIdLabel";
  public static final String KEY_ENTRY_STATUS_SCORED_TEST_LABEL = "keyEntryStatusScoredTestLabel";
  public static final String KEY_ENTRY_STATUS_UNSCORED_TEST_LABEL = "keyEntryStatusUnscoredTestLabel";

  public static final String SELECTED_STUDENT_VO_LABEL = "selectedStudentVOLabel";
  public static final String SELECTED_TESTADMIN_VO_LABEL = "selectedTestAdminVOLabel";
  public static final String KEY_ENTRY_STATUS_SCORED_STUDENT_LABEL = "keyEntryStatusScoredStudentLabel";
  public static final String KEY_ENTRY_STATUS_UNSCORED_STUDENT_LABEL = "keyEntryStatusUnscoredStudentLabel";
  public static final String KEY_ENTRY_STATUS_PARTIAL_STUDENT_LABEL = "keyEntryStatusPartialStudentLabel";

  public static final String REPOSITORY_UNSCORED_STUDENT_LABEL = "RepositoryUnscoredStudentLabel";
  public static final String REPOSITORY_PARTIAL_SCORED_STUDENT_LABEL = "RepositoryPartialScoredStudentLabel";
  public static final String REPOSITORY_SCORED_STUDENT_LABEL = "RepositoryScoredStudentLabel";

  public static final String HAS_STUDENT_LOGGED_IN_LABEL = "hasStudentLoggedInLabel";
  public static final String HAS_CR_ITEMS_LABEL = "hasCRItemsLabel";
  public static final String HAS_CR_ITEMS_ONLY_LABEL = "hasCRItemsOnlyLabel";
  public static final String DISPLAY_CR_ITEMS_ONLY = "displayCRItemsOnly";
  public static final String HAS_SCORED_STUDENTS = "hasScoredStudents";
  public static final String HAS_ONLINE_CR_ITEM_LABEL = "hasOnlineCRItemLabel";
  public static final String ONLINE_CR_RESPONSES_LABEL = "onlineCRResponsesLabel";
  public static final String ONLINE_CR_ITEMS_LABEL = "onlineCRItemsLabel";
  public static final String ITEM_RESPONSE_LIST = "itemResponseList";
  public static final String SUBTEST_LIST = "subtestList";
  public static final String SELECTED_SUBTEST = "selectedSubtest";
  public static final String SELECTED_SUBTEST_ID = "selectedSubtestId";
  public static final String SELECTED_ITEM_TYPE = "selectedItemType";
  public static final String ACTION = "action";
  public static final String ACTION_NONE = "noAction";
  public static final String ACTION_LOAD_DATA = "loadData";
  public static final String ACTION_SAVE = "save";
  public static final String ACTION_FILTER_BY = "filterBy";
  public static final String ACTION_RESET_PAGE = "resetPage";
  public static final String ACTION_SCORE_UNMARKED = "scoreUnmarked";
  public static final String ACTION_INVALIDATE_STUDENT = "invalidateStudent";
  public static final String ACTION_PAGE_REQUESTED = "pageRequested";
  public static final String ACTION_SORT_REQUESTRD = "sortRequest";
  public static final String QUESTION_ORDER_SORT = "questionOrderSort";
  public static final String QUESTION_TYPE_SORT = "questionTypeSort";
  public static final String PAGE_SIZE = "pageSize";
  public static final String PAGE_NUMBER = "pageNumber";
  public static final String TOTAL_COUNT = "totalCount";
  public static final String FILTER_COUNT = "filterCount";
  public static final String STUDENT_LIST = "studentList";
  public static final String FILTER_ACTION = "filterAction"; 
  public static final String SHOW_FILTERS = "showFilters";
  public static final String HIDE_FILTERS = "hideFilters";
  public static final String TOOGLE_VALIDATION = "toggleValidationStatus";
  public static final String REFRESH_LIST = "refreshList";
  public static final String APPLY_FILTERS = "applyFilters";
  public static final String CLEAR_ALL_FILTER = "clearAllFilters";
  public static final String INPUT_FILTERS = "inputFilters";
  public static final String TEST_LIST = "testList";
  public static final String PRODUCT_LIST = "productList";
  public static final String LEVEL_LIST = "levelList";

  //constants for item type
  public static final String ITEM_TYPE_SR = "SR";
  public static final String ITEM_TYPE_CR = "CR";
  public static final String ITEM_TYPE_ALL = "ALL";
  public static final String ITEM_DEFAULT_DESC = "<I>Click <B>Printable Preview</B> for details.</I>";


  //constants for ScanUpload
  // conversation/commands
  public static final String SCAN_UPLOAD_CONVERSATION = "scanupload";
  public static final String SELECT_ORG_LEVEL_COMMAND = "selectOrgLevel";
  public static final String SELECT_ORG_LEVEL_COMMAND_CLEAR_WEB = "selectOrgLevel?webTree=clear";
  public static final String SELECT_SCAN_FILES_COMMAND = "selectScanFiles";
  public static final String UPLOAD_SCAN_FILES_COMMAND = "uploadScanFiles";
  public static final String VIEW_SCAN_FILES_COMMAND = "viewScanUploads";
  public static final String MATCH_SELECT_ORG_LEVEL_COMMAND = "matchSelectOrgLevel";
  public static final String MATCH_SELECTED_ORG_LEVELS_COMMAND = "matchSelectedOrgLevels";
  public static final String MATCH_SCAN_UPLOAD_COMMAND = "matchScanUploads";
  public static final String MATCH_SCAN_STUDENT_COMMAND = "matchScanStudent";
  public static final String MATCH_SCAN_STUDENTS_COMMAND = "matchScanStudents";
  public static final String SORT_MATCH_SCAN_STUDENTS_COMMAND = "sortMatchScanStudents";
  public static final String MATCH_VIEW_SCAN_FILE_COMMAND = "matchViewScanFile";
  public static final String VIEW_SCAN_FILE_COMMAND = "viewScanFile";  // not yet implemented

  // return to page
  public static final String BACKTO_VIEW_ALL_UPLOADS_PAGE = "goToViewAllUpLoads";
  public static final String BACKTO_VIEW_SCANFILE_DETAIL_PAGE = "goToViewScanFileDetails";

  // session attributes
  public static final String UPLOAD_DETAIL_VOS_ATTRIBUTE = "uploadDetailVosAttribute";
  public static final String UPLOAD_DETAIL_VOS_ATTRIBUTE_ORIG = "uploadDetailVosAttributeOriginal";
  public static final String MATCH_UPLOAD_SELECTED_ORG_NODES_ATTRIBUTE = "matchUploadSelectedOrgNodesAttribute";
  public static final String MATCH_UPLOAD_DETAIL_VOS_ATTRIBUTE = "matchUploadDetailVosAttribute";
  public static final String MATCH_UPLOAD_DETAIL_VOS_ATTRIBUTE_ORIG = "matchUploadDetailVosAttributeOriginal";
  public static final String VIEW_UPLOAD_DETAILS_ATTRIBUTE = "viewUploadDetailsAttribute";
  public static final String SCAN_STUDENT_TO_MATCH_ATTRIBUTE = "scanStudentToMatchAttribute";
  public static final String SCAN_STUDENTS_TO_MATCH_ATTRIBUTE = "scanStudentsToMatchAttribute";
  public static final String STUDENTS_IN_ROSTER_ATTRIBUTE = "studentInRosterAttribute";
  public static final String MATCHED_STUDENTS_IN_ROSTER_ATTRIBUTE = "matchedStudentsInRosterAttribute";
  public static final String SCAN_FILE_DETAIL_VO_ATTRIBUTE = "scanFileDetailVOAttribute";
  public static final String SCAN_HEADER_TO_MATCH_ATTRIBUTE = "scanHeaderDetailVOAttribute";
  public static final String VIEW_SCAN_STUDENTS_PAGE_NUMBER = "viewScanStudentPageNum";
  public static final String SCAN_UPLOAD_VO_ATTRIBUTE = "scanUploadVO";
  public static final String MATCH_SCAN_FILE_ATTRIBUTE = "scanFileToMatch";
  public static final String SCAN_UPLOAD_FILES_ATTRIBUTE = "ScanUploadFilesAttribute";
  public static final String TEST_ACCESS_CODES_ATTRIBUTE = "TestAccessCodesAttribute";
  public static final String MATCH_STUDENT_PAGE_NUMBER_ATTRIBUTE = "matchStudentPageNumber";
  public static final String SEQUENTIAL_PAGER_ATTRIBUTE = "sequencePagerAttribute";
  public static final String MATCH_STUDENT_ACCESS_CODE_ATTRIBUTE = "matchStudentAccessCodeAttribute";
  public static final String NUM_STUDENTS_MATCHED_ATTRIBUTE = "numStudentsMatchedAttribute";

  // request parameters
  public static final String SCANUPLOAD_STATUS_SELECT_SCANFILES_LABEL = "scanUploadStatusSelectScanFilesLabel";
  public static final String SCANUPLOAD_STATUS_UPLOAD_SCANFILES_LABEL = "scanUploadStatusUploadScanFilesLabel";
  public static final String SCANUPLOAD_STATUS_VIEW_SCANUPLOADS_LABEL = "scanUploadStatusViewScanUploadsLabel";
  public static final String SCANUPLOAD_STATUS_VIEW_ARCHIVEDUPLOADS_LABEL = "scanUploadStatusViewArchivedUploadsLabel";
  public static final String SCANFILE_LIST_PARAMETER_LABEL = "ScanFileListParameterLabel";
  public static final String SELECTED_TAC_PARAMETER_LABEL = "SelectedTestAccessCodeParameterLabel";
  public static final String TAC_LIST_PARAMETER_LABEL = "TestAccessCodeListParameterLabel";
  public static final String TAC_SELECT_PARAMETER_LABEL = "TestAccessCodeSelectParameterLabel";
  public static final String VIEW_SCANFILES_PARAMETER_LABEL = "ViewScanFiles";
  public static final String VIEW_SCAN_FILE_PARAMETER_LABEL = "ViewScanFile";
  public static final String MATCH_SCAN_FILE_PARAMETER_LABEL = "MatchScanFile";
  public static final String SCAN_HEADER_ID_TO_MATCH_PARAMETER = "scanHeaderToMatch";
  public static final String SCANFILE_REMOVE = "fileRemove";
  public static final String MATCH_SCAN_HEADER_PARAMETER = "scanHeaderId";
  public static final String SCAN_UPLOAD_FILE = "scanFileVOsList";
  public static final String SCAN_UPLOAD_FILE_INDEX = "scanFileVOsIndex";
  public static final String SCAN_UPLOAD_FILE_COUNT = "scanFileVOsListCount";
  public static final String PREVIOUS_NEXT_INDEX = "previousNextIndex";
  public static final String PAGENUM = "pageNum";
  public static final String STUDENT_ID_TO_MATCH_TO_PARAMETER = "student";
  public static final String MATCH_FILE = "matchFile";
  public static final String ENABLE_MATCH_STUDENT = "enableMatchStudent";
  public static final String ENABLE_MATCH_TAC = "enableMatchTAC";
  public static final String HIDE_MATCH_TAC_STUDENT = "hideMatchTACStudent";

  public static final String COLUMN_INDEX_PARAMATER = "columnIndex";
  public static final String REVERSE_ORDER_PARAMETER = "reverseOrder";

  // forms
  public static final String VIEW_SCAN_UPLOADS_FORM = "viewScanUploadsForm";

  // input names
  public static final String MATCH_SCAN_FILE_CHECKBOX_NAME = "viewScanFileCheckbox";
  public static final String MATCH_SCAN_STUDENT_CHECKBOX_NAME = "viewScanHeaderCheckbox";
  public static final String MATCH_SCAN_UPLOAD_CHECKBOX = "viewScanUploadCheckbox";
  public static final String MATCH_SCAN_STUDENT_RADIO = "matchScanStudentRadio";
  public static final String MATCH_STUDENT_PARAMETER = "matchStudent";
  public static final String MATCH_STUDENT_PAGE_NUMBER_PARAMETER = "pageNum";
  public static final String SCANFILE_ERRORCODES_PARAMETER_LABEL = "ScanFileErrorCodesParameterLabel";
  public static final String MATCH_SCAN_HEADER_RADIO = "matchScanHeaderRadio";

  // style classes
  public static final String HEADER_CELL_CLASS = "header-cell";
  public static final String TOP_BORDER_CELL_CLASS = "top-line";

  // images for buttons
  public static final String NEXT_BUTTON_JSP = "<ctb:staticRoot/>/images/buttons/Next.gif";
  public static final String NEXT_BUTTON = "/images/buttons/Next.gif";
  public static final String DOWNLOAD_BUTTON = "/images/buttons/download.gif";
  public static final String EXIT_BUTTON = "/images/buttons/exit.gif";
  public static final String CANCEL_BUTTON = "/images/buttons/cancelActive.gif";
  public static final String CANCEL_DISABLE_BUTTON = "/images/buttons/cancelInactive.gif";
  public static final String DONE_BUTTON = "/images/buttons/Done.gif";
  public static final String SUBMIT_BUTTON = "/images/buttons/Submit.gif";
  public static final String UPLOAD_BUTTON = "/images/buttons/uploadActive.gif";
  public static final String UPLOAD_DISABLE_BUTTON = "/images/buttons/uploadInactive.gif";
  public static final String DELETE_BUTTON = "/images/buttons/delete.gif";
  public static final String FILTER_BUTTON = "/images/buttons/Go.gif";
  public static final String RESET_FILTER_BUTTON = "/images/buttons/Reset.gif";
  public static final String SCAN_FILE_DETAIL_BUTTON = "/images/buttons/cancelActive.gif";
  public static final String MATCH_BUTTON = "/images/buttons/matchActive.gif";
  public static final String MATCH_DISABLE_BUTTON = "/images/buttons/matchInactive.gif";
  public static final String MATCH_NEXT_BUTTON = "/images/buttons/matchNextActive.gif";
  public static final String MATCH_NEXT_DISABLE_BUTTON = "/images/buttons/matchNextInactive.gif";
  public static final String MATCH_FILE_BUTTON = "/images/buttons/matchFiles.gif";
  public static final String MATCH_FILE_DISABLE_BUTTON = "/images/buttons/matchFilesInactive.gif";
  public static final String MATCH_TAC_BUTTON = "/images/buttons/matchTacActive.gif";
  public static final String MATCH_TAC_DISABLE_BUTTON = "/images/buttons/matchTACInactive.gif";
  public static final String MATCH_STUDENT_BUTTON = "/images/buttons/matchStuActive.gif";
  public static final String MATCH_STUDENT_DISABLE_BUTTON = "/images/buttons/matchStuInactive.gif";

  // download files
  public static final String REMOTE_SCAN_UTILITY_EXE = "/downloadfiles/iknowScanSetup.exe";
  public static final String DOWNLOADS_FOLDER = "/downloadfiles/";
  public static final String TEST_DELIVERY_CLIENT_PC_EXE = "http://testadministration.ctb.com/projector/1.5/i-know.exe";
  public static final String TEST_DELIVERY_CLIENT_MAC_OLD = "http://testadministration.ctb.com/projector/1.4/i-know.sea";
  public static final String TEST_DELIVERY_CLIENT_MAC_OSX = "http://testadministration.ctb.com/projector/1.4/i-know.dmg";


  // miscellaneous
  public static final int MAXNO_SCANFILES = 10;
  public static final String SELECT_ORG_TREECELL_NAME = "orgLevelTreeCell";
  public static final String SELECT_TREE_NAME = "selectOrgLevel";
  public static final String MATCH_SELECT_TREE_NAME = "matchSelectOrgLevel";
  public static final String SELECT_TAC_PARAMETER_LABEL = "Select a test access code...";

  // scan file status codes
  public static final String FILE_STATUS_PENDING_VALIDATION = "PV";
  public static final String FILE_STATUS_PENDING_MATCHING = "PM";
  public static final String FILE_STATUS_HEADING_ERROR = "EH";
  public static final String FILE_STATUS_PARSING_ERROR = "EP";
  public static final String FILE_STATUS_HIDDEN = "HN";
  public static final String FILE_STATUS_SUCCESS = "SS";
  public static final String FILE_STATUS_NOT_PROCESSED = "NP";

  // header status codes
  public static final String HEADER_STATUS_PENDING_VALIDATION = "PV";
  public static final String HEADER_STATUS_PENDING_MATCHING = "PM";
  public static final String HEADER_STATUS_STUDENT_ERROR = "ES";
  public static final String HEADER_STATUS_ACCESSCODE_ERROR = "EA";
  public static final String HEADER_STATUS_HIDDEN = "HN";
  public static final String HEADER_STATUS_SUCCESS = "SS";

  // header status messages
  public static final String HEADER_MSG_PENDING_VALIDATION = "Match: In Validation...";
  public static final String HEADER_MSG_PENDING_MATCHING = "Match: In Progress...";
  public static final String HEADER_MSG_STUDENT_ERROR = "Match: Students";
  public static final String HEADER_MSG_ACCESSCODE_ERROR = "Match: Test Access Code";
  public static final String HEADER_MSG_HIDDEN = "Hidden Status";
  public static final String HEADER_MSG_SUCCESS = "<font color=\"green\" >Matched </font>";
  public static final String HEADER_MSG_TAC_STUDENT_ERROR = "Match: Test Access Code/Students";

  // test access code details
  public static final String SCAN_STUDENTS_FOR_HEADER ="scanStudentsForHeader";
  public static final String SCAN_HEADER_ID = "scanHeaderID";
  public static final String TEST_ACCESS_CODE_DETAIL = "testAccessCodeDetail";
  public static final String SCAN_HEADER_STATUS = "scanHeaderStatus";
  public static final String SCAN_HEADER_VO = "scanHeaderVO";

  // filter codes
  public static final int INITIAL_YEAR = 104; // Date() adds 1900 to value to get current date
  public static final int INITIAL_MONTH = 1;
  public static final int INITIAL_DATE = 1;
  public static final String FILTER_DATE = "filterDate";
  public static final String FILTER_STATE = "filterState";
  public static final String FILTER = "filter";
  public static final String FILTER_RESET = "reset";

// end scan upload

  public static final String SELECT_TEST_STRUCTURE_AVAILABLE_CHOICES = "select_test_structure_available_choices";
  public static final String SELECT_TEST_STRUCTURE_TEST_TYPE = "select_test_structure_test_type";
  public static final String SELECT_TEST_STRUCTURE_FOR_TABE = "select_test_structure_for_tabe";
  public static final String SELECT_TEST_STRUCTURE_FOR_TN = "select_test_structure_for_tn";
  public static final String SELECT_TEST_STRUCTURE_SELECTED_TESTS = "select_test_structure_selected_tests";
  public static final String SELECT_TEST_STRUCTURE_SUBTEST_SECTIONS = "subtestSections";
  public static final String SUBTEST_ORDER_SELECTION = "subtestOrder";
  public static final String SUBTEST_ORDER_DIRECTION = "direction";
  public static final String SUBTEST_SELECTION_ERROR = "subtestSelectionError";
  public static final String SUBTEST_SELECTION_WARNING = "subtestSelectionWarning";
  public static final String TABE_LOCATOR_PRODUCT_NAME = "TABE Locator";
  public static final String TABE_9_BATTERY_PRODUCT_NAME = "TABE 9 Battery";
  public static final String TABE_10_BATTERY_PRODUCT_NAME = "TABE 10 Battery";
  public static final String TABE_9_SURVEY_PRODUCT_NAME = "TABE 9 Survey";
  public static final String TABE_10_SURVEY_PRODUCT_NAME = "TABE 10 Survey";
  public static final String DOES_USER_HAS_TABE_TEST = "does_user_has_tabe_test";
  public static final String IMAGE_BRANDING_BY_PRODUCT = "image_branding_by_product";

  public static final String RAPID_REGISTRATION_WIZARD_STEP = "RapidRegistrationWizardStep";
  public static final String RAPID_REGISTRATION_WIZARD_ON_NEXT = "RapidRegistrationWizardOnNext";
  public static final String RAPID_REGISTRATION_WIZARD_ON_BACK = "RapidRegistrationWizardOnBack";
  public static final String RAPID_REGISTRATION_WIZARD_DEMOGRAPHICS = "RapidRegistrationWizardDemographics";

  public static final String PRODUCT_SELECTOR = "productSelector";
  public static final String SUBTESTS_VIEWABLE = "isSubtestsViewable";
  public static final String SUBTESTS_EDITABLE = "isSubtestsEditable";


  // for determining if test session has active item set
  public static final String HAS_ACTIVE_ITEMSET = "hasActiveItemSet";

  public static final String STUDENT_SEARCH_RESULTS = "studentSearchResults";
  public static final String TEST_SESSIONS = "testSessions";
  public static final String TEST_SESSION_ID = "testSessionId";

  public static final String STUDENTS_FOR_ORG_NODE = "studentsForOrgNode";
  public static final String STUDENTS_FOR_ORG_NODE_MAP = "studentsForOrgNodeMap";
  public static final String STUDENT_SELECTION_STRING = "studentSelectionString";
  public static final String STUDENT_SELECTED = "studentSelected";
  public static final String STUDENT_ID = "studentId";
  public static final String TEST_SESSION_ERROR = "testSessionError";
  public static final String INCLUDE_DEMOGRAPHIC_INFO = "includeDemographicInfo";
  public static final String SELECTED_SUBTESTS = "selectedSubtests";
  public static final String STUDENT_SPECIFIED_INFO = "studentSpecifiedInfo";
  public static final String INIT_STUDENT_VALID = "initStudentValid";
  public static final String INIT_STUDENT_ERROR = "initStudentError";
 
  public static final String DEMOGRAPHICS_CATEGORIES = "demographicsCategories";
  public static final String DEMOGRAPHICS_STUDENT_DATA = "demographicsStudentData";
  public static final String ETHNICITY = "Ethnicity";
  public static final String TEST_SESSION_VO = "testSessionVO";
  public static final String STUDENT_NAME = "studentName";
  public static final String STUDENT_SSN = "studentSSN";
  public static final String TEST_ADMIN_PROCTOR_NAMES = "testAdminProctorNames";
  public static final String PRODUCT_NAME = "productName";
  public static final String INTERNAL_DISPLAY_NAME = "internalDisplayName";
  public static final String DISPLAY_DEMOGRAPHIC_STEP = "displayDemographicStep";
  public static final String TERRANOVA_TEST_NEEDS_SEASON = "terranovaTestNeedsSeason";
  public static final String TERRANOVA_ADULT_ERROR = "terranovaAdultError";

  public static final String PAGE_NUMBER_ATTRIBUTE = "pageNumberAttribute";
  public static final String METHOD_FOR_SELECTING_STUDENT = "methodForSelectingStudent";
  public static final String BROWSE = "browse";
  public static final String CREATE = "create";
  public static final String SEARCH = "search";
  public static final String INIT = "Init";
  public static final String SUBMIT = "Submit";
  public static final String CHANGE = "Change";
  public static final String UNKNOWN = "unknown";
  public static final String SHOW_DEMO_CHECKBOX = "showDemoCheckbox";
  public static final String EDIT_TEST_STRUCTURE_MODE = "editTestStructure";
  public static final String SEARCH_RESULT_STUDENT_ID = "searchResultStudentId";
  public static final String STUDENT_SELECTION_METHOD = "studentSelectionMethod";
  public static final String SELECTION_METHOD_CREATE = "selectionMethodCreate";
  public static final String SELECTION_METHOD_SEARCH = "selectionMethodSearch";

  public static final String AGE_CATEGORY_RADIO_BUTTON_NAME = "ageCategoryRadioButtonName";
  public static final String AGE_CATEGORY = "ageCategoryCurrentValue";
  public static final String ADULT_AGE_CATEGORY = "A";
  public static final String JUVENILLE_AGE_CATEGORY = "J";
  public static final String NO_ERROR = "NoError";
  public static final String ERROR_CODE = "ErrorCode";

  public static final String EDIT_DEMOGRAPHICS = "editDemographics";

  public static final String EXPLICIT_ADD_REMOVE_PARAM = "explicitAddRemove";
  public static final String ADD_VALUE = "add";
  public static final String REMOVE_VALUE = "remove";
  public static final String REMOVE_ORG_NODE_CHECKBOX = "removeOrgNodeChk";
  public static final String PREVIOUSLY_SELECTED_ORG_NODES = "previouslySelectedOrgNodes";
  public static final String TREE_CHECKED_ORG_NODES = "treeCheckedOrgNodes";
  public static final String TESTS_ATTRIBUTE = "testsAttribute";
  public static final String TEST_ID_PREFIX = "testID_";
  public static final String SELECTED_TEST_ID = "selectedTestId";
  public static final String SELECTED_DEMOGRAPHICS = "selectedDemographics";
  public static final String SELECTED_PRODUCT_ID = "selectedProductId";
  public static final String SELECTED_LEVEL = "selectedLevel";
  
  //Test Administration vonversations (TU/TS new/edit)
  public static final String SELECTED_STUDENTS_CHECKED = "selectedStudents";
  public static final String SELECTED_PROCTORS_CHECKED = "selectedProctors";  
  public static final String SELECTED_USERS            = "selectedUsers"; //proctors
  public static final String NON_MODIFIABLE_USERS      = "nonModifiableUserVOList";
  public static final String NON_MODIFIABLE_STUDENTS   = "nonModifiableStudentVOList";
  public static final String NON_VISIBLE_STUDENTS      = "nonVisibleStudentVOList";
  public static final String DUPLICATE_STUDENTS        = "duplicateStudentVOList";
  public static final String LOGGED_IN_STUDENTS        = "loggedInStudentList";
  public static final String SELECTED_STUDENTS_LIST    = "studentVOList";
  public static final String SELECTED_PROCTORS_LIST    = "userVOList";
  public static final String EDIT_TUTORIAL_STUDENTS_TREE = "editTutorialStudentsTree";
  public static final String EDIT_TUTORIAL_PROCTORS_TREE = "editTutorialProctorsTree";  
  public static final String EDIT_TEST_STUDENTS_TREE     = "editTestStudentsTree";
  public static final String EDIT_TEST_PROCTORS_TREE     = "editTestProctorsTree"; 
  public static final String ASSIGN_TUTORIAL_STUDENTS_TREE = "assignTutorialStudentsTree";
  public static final String ASSIGN_TUTORIAL_PROCTORS_TREE = "assignTutorialProctorsTree";  
  public static final String ASSIGN_TEST_STUDENTS_TREE     = "assignTestStudentsTree";
  public static final String ASSIGN_TEST_PROCTORS_TREE     = "assignTestProctorsTree";  
  
  // conversation/commands in reports conversation
  public static final String CONV_REPORTS = "reports";
  public static final String COMMAND_VIEW_REPORTS = "viewReports";
  public static final String COMMAND_PROCESS_VIEW_REPORTS2 = "processViewReports";
  public static final String COMMAND_SELECT_REPORT_TYPE = "selectReportType";
  public static final String COMMAND_SELECT_ACHIEVEMENT_STUDENTS = "selectAchievementStudents";
  public static final String COMMAND_RUN_REPORT = "runReport";
  public static final String COMMAND_VIEW_REPORT_STATUS = "viewReportStatus";
  public static final String COMMAND_DOWNLOAD_REPORT = "downloadReport";
  public static final String COMMAND_DOWNLOAD_REPORT_CONTENT = "downloadReportContent";
  public static final String COMMAND_VIEW_DOWNLOAD_STATUS = "viewDownloadStatus";
  public static final String COMMAND_VIEW_EXISTING_REPORT = "viewExistingReport";
  public static final String COMMAND_RUN_EMBEDDED_REPORT = "runEmbeddedReport";
  public static final String COMMAND_TEST_REPORT = "testReport";
  public static final String COMMAND_RUN_ORGANIZATION_SUMMARY_REPORT = "runOrganizationSummaryReport";
  public static final String COMMAND_RUN_MY_REPORTS = "runMyReports";
  public static final String COMMAND_VIEW_EMBEDDED_OBJECT = "viewEmbeddedObject";
  
  public static String FORM_MY_REPORTS = "frmMyReports";
  public static String FORM_MY_REPORTS_FILTER = "frmMyReportsFilter";
  public static String FORM_MY_REPORTS_CHECKBOX_NAME = "ckDel";
  
  public static String MY_REPORTS_FILTER = "myReportsFilterVO";
  public static String MY_REPORTS_SORT = "myReportsSortVO";
  public static String MY_REPORTS_SORT_COLUMN = "myReportsSortColumn";
  public static String MY_REPORTS_SORT_ORDER = "myReportsSortOrder";
  public static String MY_REPORTS_FOLDER_ITEMS = "myReportsFolderItems";
  public static String MY_REPORTS_DELETE = "myReportsDelete";
  public static String MY_REPORTS_REQUEST = "myReportsRequest";
  public static String DELETE_REQUEST = "deleteRequest";
  
  /////////////////////////////////////////////////////////////////////////////////////
  // upload / manage scanned data
  /////////////////////////////////////////////////////////////////////////////////////
  public static final String INIT_CONVERSATION = "initConv";
  public static final String FROM_COMMAND = "fromCommand";
  public static final String TO_COMMAND = "toCommand";

  // commands in uploadscanneddata conversation
  public static final String CONV_UPLOAD_SCANNED_DATA = "uploadscanneddata";
  public static final String COMMAND_SELECT_ORGNODE = "selectOrgNode";
  public static final String COMMAND_ORGNODE_SELECTED = "orgNodeSelected";
  public static final String COMMAND_SELECT_CRITERIA = "selectCriteria";
  public static final String COMMAND_CRITERIA_SELECTED = "criteriaSelected";
  public static final String COMMAND_SELECT_FILES = "selectFiles";
  public static final String COMMAND_FILES_SELECTED = "filesSelected";
  public static final String COMMAND_CONFIRM_UPLOADS = "confirmUploads";
  public static final String COMMAND_UPLOADS_CONFIRMED = "uploadsConfirmed";

  // commands in uploadscanneddata conversation
  public static final String SCHEDULE_TEST_CONV = "scheduletest";
  public static final String SELECT_TEST_COMMAND = "selectTest";
  public static final String SELECT_OPTIONS_COMMAND = "selectOptions";
  public static final String SELECT_STUDENTS_COMMAND = "selectStudents";
  public static final String SELECT_PROCTOR_COMMAND = "selectProctor";
  public static final String CONFIRM_COMMAND = "confirm";
  
  public static final String DEFAULT_ACTION = "defaultAction";
  
  // request attributes/parameters
  public static final String MULTIPLE_NODES_NO_ROOT = "multipleNodesNoRoot";
  public static final String MULTIPLE_NODES = "multipleNodes";
  public static final String PRESELECTED = "preSelected";
  public static final String AUTOMATCH_OPTIONS = "automatchOptions";
  public static final String AUTOMATCH_CRITERIA_RADIO = "automatchCriteriaRadio";
  public static final String AUTOMATCH_OVERWRITE_RADIO = "automatchOverwriteRadio";
  public static final String FILTER_ATTRIBUTE = "filterAttribute";
  public static final String SORT_ATTRIBUTE = "sortAttribute";
  public static final String SORT_PARAMETER = "sortParameter";
  public static final String ASCENDING = "ascending";
  public static final String SCAN_HEADER_ID_ATTRIBUTE = "scanHeaderIdAttribute";
  public static final String SELECTED_SCANNED_FILES = "selectedScannedFiles";
  public static final String SELECTED_SCANNED_FILE = "selectedScannedFile";
  public static final String SELECTED_SCANNED_HEADER = "selectedScannedHeader";
  public static final String SELECTED_SCANNED_STUDENT = "selectedScannedStudent";
  public static final String SELECTED_SCAN_FILE_VO = "selectedScanFileVO";
  public static final String SELECTED_STUDENT_ID = "selectedStudentId";
  public static final String SELECTED_ROSTER_ID = "selectedRosterId";
  public static final String UPLOADED_SCANNED_FILES = "uploadedScannedFiles";
  public static final String FILTER_REQUEST = "filterRequest";
  public static final String SORT_REQUEST = "sortRequest";
  public static final String SORT_DESCRIPTOR = "sortDescriptor";
   
  // commands in managescanneddata conversation
  public static final String CONV_MANAGE_SCANNED_DATA = "managescanneddata";
  public static final String COMMAND_VIEW_ALL_UPLOADS = "viewAllUploads";
  public static final String COMMAND_VIEW_FILE_DETAILS = "viewFileDetails";
  public static final String COMMAND_DELETE_SCAN_FILE = "deleteScanFile";
  public static final String COMMAND_VIEW_HEADER_DETAILS = "viewHeaderDetails";
  public static final String COMMAND_MATCH_HEADER = "matchHeader";
  public static final String COMMAND_HEADER_MATCHED = "headerMatched";
  public static final String COMMAND_MATCH_STUDENT = "matchStudent";
  public static final String COMMAND_STUDENT_MATCHED = "studentMatched";
  public static final String COMMAND_CONFIRM_MATCHED = "confirmMatched";
  public static final String COMMAND_CHOOSE_STUDENT_TO_REMATCH = "chooseStudentToRematch";
  public static final String COMMAND_REMATCH_STUDENTS = "rematchStudents";
  public static final String COMMAND_STUDENT_REMATCHED = "studentRematched";
  
  // wizard
  public static final String WIZARD_STEP = "wizardStep";
  public static final String WIZARD_VO = "wizardVO";
  
  // scan score pagers
  public static final String SCAN_FILE_PAGER_NAME = "ScannedFilePager";
  public static final String MATCH_STUDENT_PAGER_NAME = "MatchStudentPager";
  public static final String REMATCH_STUDENTS_PAGER_NAME = "RematchStudentsPager";
  public static final String MATCH_HEADER_PAGER_NAME = "MatchHeaderPager";
  public static final String VIEW_HEADER_DETAILS_PAGER_NAME = "ViewHeaderDetailsPager";
  
  // scan score filtering
  public static final String FILTER_LIST_ATTRIBUTE = "filterList";
  public static final String FILTER_COLUMN_SELECT = "filterColumn";
  public static final String UPLOAD_DATE_OPTION = "uploadDate";
  public static final String FILE_STATUS_OPTION = "fileStatus";
  public static final String LAST_NAME_OPTION = "lastName";
  public static final String STUDENT_ID_OPTION = "studentId";
  public static final String BIRTH_DATE_OPTION = "birthDate";
  public static final String STUDENT_STATUS_OPTION = "studentStatus";
  public static final String FILE_NAME_OPTION = "fileName";
  public static final String ORG_NODE_NAME_OPTION = "orgNodeName";
  
  public static final String UPLOAD_DATE_OPERATOR_SELECT = "uploadDateOperator";
  public static final String TODAY_OPTION = "today";
  public static final String WITHIN_OPTION = "within";
  public static final String RANGE_OPTION = "range";
  public static final String EXACT_OPTION = "exact";
  
  public static final String UPLOAD_DATE_WITHIN_SELECT = "uploadDateOperand_within";
  public static final String TWO_DAY_OPTION = "2d";
  public static final String THREE_DAY_OPTION = "3d";
  public static final String ONE_WEEK_OPTION = "1w";
  public static final String TWO_WEEK_OPTION = "2w";
  public static final String ONE_MONTH_OPTION = "1m";
  public static final String THREE_MONTH_OPTION = "3m";
  public static final String SIX_MONTH_OPTION = "6m";
  
  public static final String FILE_STATUS_SELECT = "fileStatusOperator";
  public static final String MATCHED_OPTION = "matched";
  public static final String UNMATCHED_OPTION = "unmatched";
  public static final String INPROGRESS_OPTION = "inprogress";
  
  public static final String LAST_NAME_OPERATOR_SELECT = "lastNameOperator";
  public static final String IS_OPTION = "is";
  public static final String STARTS_WITH_OPTION = "startsWith";
  public static final String CONTAINS_OPTION = "contains";
  
  public static final String STUDENT_ID_OPERAND_IS_TEXTBOX = "studentIdOperand_is";
  public static final String STUDENT_ID_OPERAND_STARTS_WITH_TEXTBOX = "studentIdOperand_startsWith";
  public static final String STUDENT_ID_OPERAND_CONTAINS_TEXTBOX = "studentIdOperand_contains";
  
  public static final String AUTOMATCH_OVERWRITE_DEFAULT = "T";
  public static final Integer AUTOMATCH_CRITERIA_ID_DEFAULT = new Integer(1001);
  
  public static final String TEST_ADMIN_VO = "testAdminVO";
  public static final String SCAN_FILE_VO = "scanFileVO";
  
  public static final String HAS_UPLOAD = "hasUpload";
  public static final String USER_ID = "userId";
  public static final String TAC = "tac";
  public static final String TEST_ADMIN_ID = "testAdminId";
   
  public static final String SEQ_PAGER_REQUEST = "seqPagerRequest";
  public static final String SEQ_PAGER_INCREMENT_REQUEST = "seqPagerIncrementRequest";
  public static final String SEQ_PAGER_DECREMENT_REQUEST = "seqPagerDecrementRequest";
  public static final String DELETABLE = "deletable";
  public static final String CAN_DELETE = "canDelete";

  // values for BRANDING_TYPE_CODE in tables PRODUCT_BRANDING_TYPE & PRODUCT
  public static final String BRANDING_TYPE_CODE = "brandingTypeCode";  
  public static final String BRANDING_TYPE_CODE_IKNOW = "IKNOW";  
  public static final String BRANDING_TYPE_CODE_TABE = "TABE";  
  public static final String BRANDING_TYPE_CODE_TERRANOVA = "TN";  
  public static final String BRANDING_TYPE_CODE_TN_ALGEBRA = "TN_ALGEBRA";  
  
  // CIA module
  public static final String CURRICULUM_NODES_PARAMETER = "curriculumNodes";
  public static final String CURRICULUM_NODE_INDEX_PARAMETER = "curriculumNodeIndex";
  public static final String SELECTED_OBJECTIVE_ID_PARAMETER = "selectedObjectiveId";
  public static final String SHOW_ITEMS_OBJECTIVE_ID_PARAMETER = "showItemsObjectiveId";
  public static final String TEST_ITEM_PAGER_DESCRIPTOR_PARAMETER = "testItemPagerDescriptor";
  public static final String SEARCH_ITEM_PAGER_DESCRIPTOR_PARAMETER = "searchItemPagerDescriptor";
  public static final String FRAMEWORK_CONTENT_AREA_PARAMETER = "frameworkContentArea";
  public static final String SELECT_RESPONSE_TYPE_PARAMETER = "selectResponseType";
  public static final String SELECT_STIMULUS_TYPE_PARAMETER = "selectStimulusType";
  public static final String SELECT_STIMULUS_PORTION_PARAMETER = "selectStimulusPortion";
  public static final String SELECTED_TEMPLATES_PARAMETER = "selectedTemplates";
  public static final String SELECTED_TEMPLATE_ID_PARAMETER = "selectedTemplateId";
  public static final String FROM_COMMAND_PARAMETER = "fromCommand";
  public static final String CURRICULUM_FULL_PATH_PARAMETER = "curriculumFullPath";
  public static final String ITEM_PUBLISHED_PARAMETER = "itemPublished";
   
  public static final String CONTENT_AREAS_PARAMETER = "contentAreas";   
  public static final String STIMULUS_SEARCH_PARAMETER = "stimulusSearch";
  public static final String STIMULUS_CODES_PARAMETER = "stimulusCodes";
  
  public static final String CONTENT_AREA_SELECT = "contentArea";
  public static final String STIMULUS_FORMAT_SELECT = "stimulusFormat";
  public static final String STIMULUS_NAME_TEXT = "stimulusName";
  public static final String OBJECTIVE_RADIO = "objectiveRadio";
  public static final String RESPONSE_TYPE_RADIO = "responseTypeRadio";
  public static final String STIMULUS_TYPE_RADIO = "stimulusTypeRadio";
  public static final String STIMULUS_PORTION_RADIO = "stimulusPortionRadio";
  public static final String CREATE_SAME_GRADE_NODE = "createSameGradeNode";
  public static final String CREATE_DIFFERENT_GRADE_NODE = "createDifferentGradeNode";
  
  public static final String STIMULUS_NONE = "stimulusNone";
  public static final String STIMULUS_EXISTING = "stimulusExisting";
  public static final String STIMULUS_NEW = "stimulusNew";
  public static final String DYNAMIC_FILTER_MAP = "dynamicFilterMap";
  public static final String STIMULUS_PORTION_PASSAGE = "stimulusPortionPassage";
  public static final String STIMULUS_PORTION_IMAGE = "stimulusPortionImage";
  public static final String STIMULUS_PORTION_PASSAGE_IMAGE = "stimulusPortionPassageImage";
  public static final String STIMULUS_PORTION_MATH_EQUATION = "stimulusPortionMathEquation";
  public static final String STIMULUS_TREES = "stimulusTrees";

//  public static final String SEARCH_ITEM_TREE = "searchItemTree";
  public static final String ITEMS_FOR_STIMULUS_DESCRIPTOR = "itemsForStimulusDescriptor";
  public static final String SEARCH_ITEM_DESCRIPTOR = "searchItemDescriptor";
  public static final String MANAGE_ITEM_ACTION = "manageItemAction";
  public static final String STIMULUS_SEARCH_REQUEST = "stimulusSearchRequest";
  public static final String ALL = "All";
  public static final String STIMULUS_PAGER_DESCRIPTOR_PARAMETER = "stimulusPagerDescriptor";
  public static final String PREVIEW_STIMULUS = "previewStimulus";
  public static final String STIMULUS_ID = "stimulusId";
  public static final String SELECTED_STIMULUS_ID = "selectedStimulusId";
  public static final String STIMULUS = "stimulus";
  public static final String ITEM_NAME = "itemName";
  public static final String ITEM_ANCESTORS = "itemAncestors";
  public static final String HIGHLIGHTED_ITEM_ID_PARAMETER = "highLightedItemId";
  public static final String STIMULUS_NAME = "stimulusName";
  
  public static final String STIMULUS_CODE_PASSAGE = "PA";
  public static final String STIMULUS_CODE_PASSAGE_IMAGE = "PI";
  public static final String STIMULUS_CODE_IMAGE1 = "I1";
  public static final String STIMULUS_CODE_IMAGE2 = "I2";
  public static final String STIMULUS_CODE_IMAGE3 = "I3";
  
  public static final String STIMULUS_NAME_PASSAGE = "Passage";
  public static final String STIMULUS_NAME_PASSAGE_IMAGE = "Passage and Image";
  public static final String STIMULUS_NAME_IMAGE1 = "Image (size 6\" x 2.5\")";
  public static final String STIMULUS_NAME_IMAGE2 = "Image (size 6\" x 5.5\")";
  public static final String STIMULUS_NAME_IMAGE3 = "Image (size 7\" x 1.5\")";
  
  public static final String MANAGE_ITEMS_LOCAL = "ManageItemsLocal";
  public static final String MANAGE_STIMULUS_LOCAL = "ManageStimulusLocal";

  public static final String ITEM_LIST_ANCHOR = "#itemList";
  public static final String STUDENT_SORT_TYPE = "studentSortType";
  public static final String TEST_SORT_TYPE = "testSortType";
  
  public static final String HAS_REPORTS = "hasReports";
  
  //User Management Email constants
  public static final String EMAIL_FROM = "oas_account_management@ctb.com";
  public static final Integer EMAIL_TYPE_WELCOME = new Integer(1);
  public static final Integer EMAIL_TYPE_PASSWORD = new Integer(2);
  public static final Integer EMAIL_TYPE_NOTIFICATION = new Integer(3);
  public static final String EMAIL_CONTENT_PLACEHOLDER_USERID = "<#userid#>";
  public static final String EMAIL_CONTENT_PLACEHOLDER_PASSWORD = "<#password#>";
  
    /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- Start*/
  public static final String DEX_CONFIGURATION                   = "DEx_Indicator";
  public static final int GENERATED_DEX_USER_PASSWORD_LENGTH     = 8;
  public static final String DEX_SPECIAL_CHAR_ARRAY              = "%&<>\")(+~`'!@#^*=|}{][:;?/\\.,";
     /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- End*/ 
}