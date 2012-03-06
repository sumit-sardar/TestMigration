package utils; 
 
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
   

    // get License
    public static final String TEST_SESSION_TITLE  = "No Test Sessions";
    public static final String INVALID_CHARS  = "Please re-enter your string without these characters: !, @, #, $, %, ^";
    public static final String FIND_TEST_SESSION_TITLE      = "Find Test Session";
   
    public static final String REQUIRED_TEXT            = "Please enter/select this value to continue.";
    
    
         
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

} 
