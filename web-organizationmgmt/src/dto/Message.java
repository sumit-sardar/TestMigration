package dto; 

/**
 *@author Tata Consultancy Services 
 *Message class have the information of pageflow messages,field names , titles and some constant details
 */

public class Message implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private String title = null;
    private String content = null;
    private String type = null;

    public static final String ERROR          = "errorMessage";
    public static final String INFORMATION    = "informationMessage";
    public static final String ALERT          = "alertMessage";

    public static final String REQUIRED_TEXT            = "Please enter/select this value to continue.";
    public static final String REQUIRED_TEXT_MULTIPLE   = "Please enter/select these values to continue.";
    public static final String INVALID_NUMBER_FORMAT  = "Please re-enter numeric fields in valid format";
    public static final String INVALID_NAME_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
    public static final String INVALID_DATE  = "Please re-select valid month, day, and year.";
    public static final String INVALID_EMAIL  = "Please enter a valid email address.";
    public static final String INVALID_CUSTOMER_ID = "Please enter a valid Customer ID";
    public static final String INVALID_CHARS_TITLE = "One or more fields contain invalid characters or invalid values:"; 
    public static final String INVALID_FORMAT_TITLE = "One or more fields contain invalid formats or invalid values:"; 
    public static final String INVALID_DUP_FORMAT_TITLE = "One or more fields contain invalid formats, duplicate or invalid values:"; 
    public static final String DUPLICATE_FRAMEWORK_ENTRY = "Please do not enter duplicate layers.";
    public static final String INVALID_FRAMEWORK_CHARS = "Please re-enter your string with these characters:  #, /, \\, -, ', (, ), &, +, comma, period, space";
    
    // Information on Organization
    public static final String INVALID_NAME_CHARS_ORG  = "Please re-enter your information with these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space"; 
    
    public static final String INVALID_NAME_CHARS_STUDENT  = "Please re-enter your information with these characters: A-Z, a-z, 0-9, /, \\, -, _, ', (, ), &, +, comma, period, space"; 
    
    public static final String ADD_TITLE      = "Add Organization";
    public static final String ADD_SUCCESSFUL = "New organization has been successfully added.";
    public static final String ADD_ERROR = "Failed to create this organization.";
    public static final String ADD_TITLE_WEB      = "CTB/McGraw-Hill OAS - Add Organization";

    public static final String EDIT_TITLE      = "Edit Organization: ";
    public static final String EDIT_SUCCESSFUL    = "Organization information has been updated successfully.";
    public static final String EDIT_ERROR = "Failed to update this organization.";
    public static final String INVALID_PARENT = "An organization must belong to an organization above it. Select a valid parent organization.";
    public static final String EDIT_TITLE_WEB      = "CTB/McGraw-Hill OAS - Edit Organization";
    
    public static final String DELETE_TITLE      = "Delete Organization";
    public static final String DELETE_FILE_TITLE      = "Delete File";
    public static final String DELETE_SUCCESSFUL = "Organization has been deleted successfully.";
    public static final String DELETE_FILE_SUCCESSFUL = "File has been deleted successfully.";
    public static final String DELETE_ERROR_TAS = "You cannot delete this organization.";
    public static final String DELETE_ERROR_UNKNOWN = "Failed to delete this organization.";
    
    public static final String VIEW_ORG_TITLE = "View Organization: ";
    
    public static final String FIND_ORG_TITLE  = "Find Organization";
    
     // Information on Customer
    public static final String ADD_CUSTOMER_TITLE      = "Add Customer ";
    public static final String ADD_CUSTOMER_SUCCESSFUL    = "Customer information has been created successfully.";
    public static final String ADD_CUSTOMER_ERROR = "Failed to add this customer.";
    
    public static final String ADD_FRAMEWORK_TITLE      = "Create Framework ";
    public static final String ADD_FRAMEWORK_SUCCESSFUL    = "Customer framework has been created successfully.";
    public static final String ADD_FRAMEWORK_ERROR = "Failed to add this framework.";
    
    public static final String EDIT_CUSTOMER_TITLE      = "Edit Customer: ";
    public static final String EDIT_CUSTOMER_SUCCESSFUL    = "Customer information has been updated successfully.";
    public static final String EDIT_CUSTOMER_ERROR = "Failed to update this customer.";

    public static final String EDIT_FRAMEWORK_TITLE      = "Edit Framework: ";
    public static final String EDIT_FRAMEWORK_SUCCESSFUL    = "Framework has been updated successfully.";
    public static final String EDIT_FRAMEWORK_ERROR = "Failed to update this framework.";

    
    public static final String VIEW_TITLE    = "View Customer: ";
    public static final String VIEW_FRAMEWORK_TITLE = "View Customer Framwork: ";
    
   

    public static final String UNKNOWN_REQUEST      = "Sorry, we didn't understand your request. Please try again.";
    
    public static final String CREATE_ADMIN_TITLE      = "Create Administrator ";
    public static final String CREATE_ADMIN_SUCCESSFUL    = "Administrator has been created successfully.";
    public static final String CREATE_ADMIN_ERROR = "Failed to create this Administrator.";
    
    public static final String MANAGE_ORG_ERROR = "Failed to manage this organization.";
    public static final String MANAGE_CUSTOMER_ERROR = "Failed to manage this customer.";
    public static final String FIND_CUSTOMER_ERROR = "Failed to find this customer.";
    
    public static final String ANY_STATE = "Any state";
    public static final String SELECT_STATE = "Select a state";
    public static final String SELECT_CUSTOMER_ENTITY = "Select a customer type";
    
    
    public static final String FIND_TITLE      = "Find Customer";
    
    
    public static final String FIND_FOUND_CUSTOMER = "The following customers match your search criteria.";
    public static final String FIND_NO_RESULT  = "There is no customer to match your search criteria.";
    
    // field For Organization
    public static final String SELECT_ENTITY = "Select a layer";
    public static final String FIELD_ORG_NAME = "Name";
    public static final String FIELD_LEVEL_NAME = "Layer";
    public static final String FIELD_PARENT_NAME = "Parent Org";
    public static final String FIELD_ORGCODE_NAME = "Org Code";
    public static final String FIELD_PARENT_ID = "Parent ID";
    
    // field For License
    public static final String FIELD_LICENSE_AVALIABLE = "Avaliable";
    public static final String FIELD_LICENSE_CONSUME = "Consume";
    public static final String REQUIRED_LICENSE_TEXT   = "Please enter this value to continue.";
    public static final String ADD_UPDATED_LICENSE   = "Manage Licenses:";
    public static final String INVALID_LICENSE_TEXT = "One or more fields contain invalid values:";
    public static final String INVALID_LICENSE_VALUE = "Please enter a positive numeric value.";
   
     // field For Customer
    
    public static final String FIELD_CUSTOMER_NAME = "Customer Name";
    public static final String FIELD_CUSTOMER_TYPE = "Customer Type";
    public static final String FIELD_STATE = "State";
    //START - Changes for LASLINK PRODUCT 
    public static final String FIELD_MDRNUMBER = "MDR Number";
    //LLO-099 MDR Validation
    public static final String INVALID__MINLENGTH_FORMAT  = "Please enter valid  minimum length: 8 characters.";
    public static final String INVALID__MDRNUMBER_FORMAT  = "Please re-enter your information with a unique combination of 0-9.";
    
    //END - Changes for LASLINK PRODUCT 
    public static final String FIELD_CTB_CONTACT = "CTB Contact";
    public static final String FIELD_CTB_CONTACT_EMAIL = "CTB Contact Email";
    public static final String FIELD_CUSTOMER_CONTACT = "Customer Contact";
    public static final String FIELD_CUSTOMER_CONTACT_EMAIL = "Customer Contact Email";
    public static final String FIELD_CUSTOMER_CONTACT_PHONE = "Customer Contact Phone";
    public static final String FIELD_EXT_CUSTOMER_ID = "External Customer ID";
    public static final String FIELD_CUSTOMER_ID = "Customer ID";
    public static final String FIELD_BILLING_ADDRESS_LINE1 = "Billing Address Line1";
    public static final String FIELD_BILLING_ADDRESS_LINE2 = "Billing Address Line2";
    public static final String FIELD_BILLING_ADDRESS_LINE3 = "Billing Address Line3";
    public static final String FIELD_MAILING_ADDRESS_LINE1 = "Mailing Address Line1";
    public static final String FIELD_MAILING_ADDRESS_LINE2 = "Mailing Address Line2";
    public static final String FIELD_MAILING_ADDRESS_LINE3 = "Mailing Address Line3";
    public static final String FIELD_BILLING_CITY = "Billing City";
    public static final String FIELD_MAILING_CITY = "Mailing City";
    public static final String FIELD_BILLING_ZIP = "Billing Zip Code";
    public static final String FIELD_MAILING_ZIP = "Mailing Zip Code";
    public static final String FIELD_FRAMEWORK = "Framework";
    
    public static final String FIELD_EMAIL = "Email";
    
    //invalid field
    public static final String INVALID_CITY_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
    public static final String INVALID_ADDR_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, #, /, \\, -, ', (, ), &, +, comma, period, space";
    //msc
    public static final String MISSING_REQUIRED_FIELD = "Missing required field";
    public static final String MISSING_REQUIRED_FIELDS = "Missing required fields";
    
    
    //upload download
    
    public static final String UPLOAD_TITLE = "Upload File";
    public static final String DOWNLOAD_TITLE = "Download Data";
    public static final String DOWNLOAD_ERROR_MSG = "Unable to download because download file size is greater than the max file size of excel";
    public static final String FIND_NO_FILE_RESULT  = "There is no File uploaded at this time.";
    public static final String FILE_HISTORY_ERROR = "Failed to get the file list.";
    public static final String ERROR_FILE_TITLE = "Failed to get the File";
    
    //ISTEP CR003
    public static final String FIND_TEST_SESSION_TITLE      = "Find Test Session";
    public static final String STUDENT_LOGIN_ID 			= "Student Login";
    public static final String STUDENT_TEST_ACCESS_CODE 	= "Access Code";
    public static final String FIND_NO_TESTDATA_RESULT      = "Test Access Code not found.";
    public static final String FIND_NO_STUDENT_DATA         = "Student not found in subtest.";
    public static final String FIND_NO_SUBTEST_DATA_RESULT  = "Student not found in test session. ";

    public static final String TEST_ROSTER_UPDATION_TITLE  = "Reset Test Session";	
    public static final String TEST_ROSTER_UPDATION_FAILED  = "Test Roster Updation Failed";	
    public static final String TEST_ROSTER_UPDATION_SUCCESS	= "has been reset successfully for";
    public static final String FIND_STUDENT_ERROR			= "Student Not Found";
    public static final String TEST_STUDENT_UPDATION_SUCESS = "The specified test session has been reset successfully for all selected students.";

    public static final String TEST_TICKET_ID  			= "Ticket ID";	
    public static final String TEST_REQUEST_DESCRIPTION	= "Reason for reset";
    public static final String TEST_SERVICE_REQUESTOR	= "Requestor";
    public static final String TEST_RESET_FAILED ="Subtest failed to reset.";
    

    public Message() {
        this.title = "";
        this.content = "";
        this.type = null;
    }
    public Message(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }
       
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
       
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getRequiredErrorMsg
     */
    public static String getRequiredErrorMsg(String str) {
        return str + " is required.";
    }

    /**
     * getInvalidCharErrorMsg
     */
    public static String getInvalidCharErrorMsg(String str) {
        return str + " contains invalid characters.";
    }

    /**
     * buildErrorString
     */
    public static String buildErrorString(String field, int count, String str) {
        String result = str;
        
        if ( count == 1 ) {
            
            result += field;
            
        } else {
            
            result += (", " + field); 
                       
        } 
               
        return result;
    }
    
    /**
     * buildPageTitle
     */
    public static String buildPageTitle(String title, String customerName) {
        return title + customerName;
    }
        
} 
