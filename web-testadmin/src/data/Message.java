package data; 
 
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
    public static final String LICENSE_TITLE  = "Licenses Exceeded";
   
    public static final int MAX_LICENSE = 20;
    public static final int MIN_LICENSE = 5;
    public static final String LOW_LICENSE_COLOR = "RED";
    public static final String MEDIUM_LICENSE_COLOR = "YELLOW";
    public static final String HIGH_LICENSE_COLOR = "GREEN";
     public static final int MAX_LICENSE_PERCENT = 100;
    public static final int MIN_LICENSE_PERCENT = 0;
    //START - Added for Deferred Defect 64306
    public static final String INSUFFICENT_LICENSE_QUANTITY = "There are insufficient licenses available to schedule the number of tests requested.";
    //END - Added for Deferred Defect 64306
    
    
         
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
