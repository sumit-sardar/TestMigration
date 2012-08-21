package dto; 

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
    public static final String INVALID_CHARS  = "Please re-enter your string without these characters: !, @, #, $, %, ^, &, (, ), +";
    public static final String INVALID_NUMBER_CHARS  = "Please re-enter your string with these characters: A-Z, a-z, 0-9, space";
    public static final String INVALID_NAME_CHARS  = "Please re-enter your string with these characters: A-Z, a-z, 0-9, /, \\, -, ', (, ), &, +, comma, period, space";
    public static final String INVALID_DATE  = "Please re-select valid month, day, and year.";

    public static final String ADD_TITLE      = "Add Student";
    public static final String ADD_SUCCESSFUL = "Student has been successfully added into your group.";
    public static final String ADD_ERROR = "Failed to create this student.";

    public static final String EDIT_TITLE      = "Edit Student";
    public static final String EDIT_SUCCESSFUL    = "Student information has been updated successfully.";
    public static final String EDIT_ERROR = "Failed to update this student.";

    public static final String DELETE_TITLE      = "Delete Student";
    public static final String DELETE_SUCCESSFUL = "Student has been deleted successfully.";
    public static final String DELETE_ERROR_TAS = "You cannot delete this student. Student is associated with test administrations.";
    public static final String DELETE_ERROR_UNKNOWN = "Failed to delete this student.";

    public static final String MISSING_SELECTION = "Missing selection.";
    public static final String NSS_CONTENT = "Select a student to continue.";
    public static final String NSTS_CONTENT = "Select at least one subtest to continue.";
    
    public static final int MAX_LICENSE = 20;
    public static final int MIN_LICENSE = 5;
    public static final String LOW_LICENSE_COLOR = "RED";
    public static final String MEDIUM_LICENSE_COLOR = "YELLOW";
    public static final String HIGH_LICENSE_COLOR = "GREEN";
    public static final int MAX_LICENSE_PERCENT = 100;
    public static final int MIN_LICENSE_PERCENT = 0;
    //START - Added for Deferred Defect 63097
    public static final String INSUFFICENT_LICENSE_QUANTITY = "There are insufficient licenses available to schedule the number of tests requested.";
    //END - Added for Deferred Defect 63097
    
    //START-  TABE-BAUM 060: Unique Student ID
    public static final String VALIDATE_STUDENT_ID_TITLE      = "Student ID:";
	public static final String STUDENT_ID_UNUNIQUE_ERROR = "Please re-enter your Student ID information with an unique value.";
	public static final String STUDENT_ID_UNUNIQUE_ERROR_AT_SAVE = "Please press back button to re-enter your Student ID information with an unique value.";
    //END- TABE-BAUM 060: Unique Student ID
	
	public static final String STUDENT_ASSIGNMENT_ERROR = "Your school system does not allow students to be associated with more than one group (class) for testing. Please choose only one.";
	public static final String DEFAULT_STUDENT_ID_LABEL = "Student ID";
    
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
