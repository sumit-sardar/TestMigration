package dto; 
 
public class Message implements java.io.Serializable 
{ 
    static final long serialVersionUID = 1L;
    
    private String title = null;
    private String content = null;
    private String type = null;

    // message type
    public static final String ERROR          = "errorMessage";
    public static final String INFORMATION    = "informationMessage";
    public static final String ALERT          = "alertMessage";

    // field
    public static final String FIELD_FIRST_NAME = "First Name";
    public static final String FIELD_MIDDLE_NAME = "Middle Name";
    public static final String FIELD_LAST_NAME = "Last Name";
    public static final String FIELD_ADDRESS_LINE1 = "Address Line1";
    public static final String FIELD_ADDRESS_LINE2 = "Address Line2";
    public static final String FIELD_CITY = "City";
    public static final String FIELD_OLD_PASSWORD = "Old Password";
    public static final String FIELD_NEW_PASSWORD = "New Password";
    public static final String FIELD_CONFIRM_PASSWORD = "Confirm Password";
    public static final String FIELD_HINT_QUESTION = "Hint Question";
    public static final String FIELD_HINT_ANSWER = "Hint Answer";
    public static final String FIELD_EMAIL = "Email";
    public static final String FIELD_TIME_ZONE = "Time Zone";
    public static final String FIELD_ROLE = "Role";
    public static final String FIELD_EXTERNAL_USER_ID = "External User Id";//ext_pin1 is added for DEX CR
    public static final String FIELD_ORG_ASSIGNMENT = "Organization Assignment";
    public static final String FIELD_ZIP = "Zip";
    public static final String FIELD_PRIMARY_PHONE = "Primary Phone";
    public static final String FIELD_SECONDARY_PHONE = "Secondary Phone";
    public static final String FIELD_FAX_NUMBER = "Fax Number";

    // invalid characters and format
    public static final String INVALID_FORMAT_TITLE = "One or more fields contain invalid formats or invalid values:"; 
    public static final String INVALID_CHARS_TITLE = "One or more fields contain invalid characters or invalid values:"; 
    public static final String REQUIRED_TEXT            = "Please enter/select this value to continue.";
    public static final String REQUIRED_TEXT_MULTIPLE   = "Please enter/select these values to continue.";
    public static final String INVALID_CHARS  = "Please re-enter your information without these characters: !, @, #, $, %, ^";
    public static final String INVALID_ADDRESS_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, #, /, \\, -, ', (, ), &, +, comma, period, space";
    public static final String INVALID_CITY_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
    public static final String INVALID_NUMBER_CHARS  = "Please re-enter your information with these characters: A-Z, a-z, 0-9, space";
    public static final String INVALID_NUMBER  = "Please re-enter your number with 0-9";
    public static final String INVALID_NUMBER_FORMAT  = "Please re-enter numeric fields in valid format";
    public static final String INVALID_NAME_CHARS  = "Please re-enter your information with only these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space" ;
    public static final String INVALID_DATE  = "Please re-select valid month, day, and year.";
    public static final String INVALID_EMAIL  = "Please enter a valid email address";
    public static final String INVALID_PHONE = "Please enter a valid phone number"; 
    public static final String INVALID_FAX = "Please enter a valid fax number"; 
    public static final String INVALID_ZIP = "Please enter a valid zip code"; 
    public static final String INVALID_LOGIN = "Please enter a valid Login ID";

    // find user
    public static final String FIND_TITLE      = "Find User";
    public static final String FIND_NO_RESULT  = "There is no user to match your search criteria.";
    public static final String FIND_FOUND_AT_AND_BELOW = "The following users are at and below your organization.";
    public static final String FIND_FOUND_WITH_CRITERIA = "The following users match your search criteria.";

    // add user
    public static final String ADD_TITLE      = "Add User";
    public static final String ADD_SUCCESSFUL = "The new user has been successfully added to your organization.";
    public static final String ADD_SUCCESSFUL_NO_EMAIL = "The new user has been successfully added to your organization.<br/>"
            +"An email address was not provided for this new user. Therefore, the welcome email message with login information"
            +" cannot be sent directly to the user. Please ensure the new user knows he or she has an account set up and give the user his or her username and password.";
    public static final String ADD_ERROR = "Failed to create this user.";
    public static final String ADD_ADMINISTRATOR_TITLE      = "Add Administrator";
    public static final String ADD_TITLE_WEB      = "CTB/McGraw-Hill OAS - Add User";
    public static final String ADD_ADMINISTRATOR_TITLE_WEB      = "CTB/McGraw-Hill OAS - Add Administrator";

    // edit user
    public static final String EDIT_TITLE      = "Edit User";
    public static final String EDIT_SUCCESSFUL = "User information has been updated successfully.";
    public static final String EDIT_ERROR = "Failed to update this user.";
    public static final String EDIT_TITLE_WEB      = "CTB/McGraw-Hill OAS - Edit User";

    // delete user
    public static final String DELETE_TITLE    = "Delete User";
    public static final String DELETE_SUCCESSFUL = "User has been deleted successfully.";
    public static final String DELETE_ERROR = "Failed to delete this user.";
    
    // view user
    public static final String VIEW_TITLE    = "View User";
    // change password
    public static final String CHANGE_PASSWORD_TITLE = "Change Password";
    public static final String CHANGE_PASSWORD_SUCCESSFUL = "Password has been changed successfully.";
    public static final String INVALID_PASSWORD = "Passwords must contain at least six characters. Passwords must contain at least one letter and at least one number. Please re-enter your information with only these characters: A-Z, a-z, 0-9, _, -,’";
    /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- Start*/
    public static final String INVALID_DEX_PASSWORD = "Passwords must contain at least eight characters. Passwords must contain at least one letter and at least one number. Please re-enter your information with only these characters: A-Z, a-z, 0-9, _, -,$";
    /* Changed/Added for DEx Phase 2 on 22-Apr-09 by TCS -- End*/
    public static final String PASSWORD_MISMATCH = "You must enter the same password in the New Password and Confirm Password fields.";
    public static final String WRONG_PASSWORD = "Please provide a correct old password";
    public static final String OLD_NEW_PASSWORD_MATCH = "Please provide a new password";
    public static final String REPEATED_PASSWORD = "New password cannot be any of five previous passwords";
    public static final String CHANGE_PASSWORD_ERROR = "Failed to change user password";
    
    //admin creation
     public static final String ADMIN_CREATION_TITLE = "Administrator Creation Error";
     public static final String ADMIN_CREATION_ERROR = "You cannot add another administrator to your own layer in " 
            + "the organization, only to a lower layer. A higher-layer administrator must add other administrators to your layer.";
    // dropdown selection
    public static final String SELECT_TIME_ZONE = "Select a time zone";
    public static final String SELECT_STATE = "Select a state";
    public static final String SELECT_ROLE = "Select a role";
    public static final String SELECT_HINT_QUESTION = "Select a hint question";
    public static final String ANY_ROLE = "Any role";
    
        
    // misc
    public static final String MISSING_REQUIRED_FIELD = "Missing required field";
    public static final String MISSING_REQUIRED_FIELDS = "Missing required fields";
    public static final String SAVE_USER_PROFILE_EXCEPTION = "Problem occured while saving user profile informations";
    
    //password
    public static final String CHAANGE_PASSWORD_EXCEPTION = "Problem occured while changing password";
    
   
    /* START- Added for Deferred Defect 62758 
	 * User can't be associated with different organizations across different customers
	*/
    public static final String USER_CREATION_TITLE = "User Information Error";
    public static final String USER_CREATION_ERROR = "An user cannot be associated with more than one customer account." ;
    //END- Added for Deferred Defect 62758 
         
    public Message()
    {
        this.title = "";
        this.content = "";
        this.type = null;
    }
    public Message(String title, String content, String type)
    {
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
    public static String getRequiredErrorMsg(String str)
    {
        return str + " is required.";
    }

    /**
     * getInvalidCharErrorMsg
     */
    public static String getInvalidCharErrorMsg(String str)
    {
        return str + " contains invalid characters.";
    }

    /**
     * getFindMessageForOrgNode
     */
    public static String getFindMessageForOrgNode(String orgNodeName, boolean found)
    {
        if (found)
            return "The following users are at <b>" + orgNodeName + "</b>";
        else
            return "There is no user at <b>" + orgNodeName + "</b>";
    }

    /**
     * buildPageTitle
     */
    public static String buildPageTitle(String title, String firstName, String lastName)
    {
        return title + firstName + " " + lastName;
    }
    
    /**
     * buildErrorString
     */
    public static String buildErrorString(String field, int count, String str)
    {
        String result = str;
        if (count == 1) {
            result += field;
        }
        else {
            result += (", " + field);            
        }        
        return result;
    }
        
} 
