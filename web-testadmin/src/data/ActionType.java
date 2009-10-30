package data; 

public class ActionType implements java.io.Serializable  
{ 
    static final long serialVersionUID = 1L;

    public static final String SCHEDULE_TEST = "schedule";
    public static final String VIEW_TEST     = "view";
    public static final String EDIT_TEST     = "edit";
    public static final String COPY_TEST     = "copy";
    public static final String DEFAULT       = "default";

    public ActionType() 
    {
    }    

} 
