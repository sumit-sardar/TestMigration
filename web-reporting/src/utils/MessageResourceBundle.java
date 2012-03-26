package utils; 

import java.util.ResourceBundle;

public class MessageResourceBundle 
{ 
    private final static String RESOURCE_BUNDLE_FILE = "ErrorMessages";
    private static ResourceBundle rb;
    static {
        rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE);
    }
    
    public static String getMessage(String key) {
        return getBundle().getString(key);
    }

    public static String getMessage(String key, String arg0) {
        String msg = getBundle().getString(key);
        msg = StringUtils.replace(msg, "{0}", arg0);
        return msg;
    }

    public static String getMessage(String key, String arg0, String arg1) {
        String msg = getBundle().getString(key);
        msg = StringUtils.replace(msg, "{0}", arg0);
        msg = StringUtils.replace(msg, "{1}", arg1);
        return msg;
    }
    
    private static ResourceBundle getBundle() {
        if (rb == null)
            rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE);
        return rb;
    }
} 
