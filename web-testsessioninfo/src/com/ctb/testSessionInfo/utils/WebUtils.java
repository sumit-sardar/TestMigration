package com.ctb.testSessionInfo.utils; 

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class WebUtils 
{ 
    public static String buildURI(HttpServletRequest req, String flow, String controller)
    {
        StringBuffer uri = new StringBuffer(req.getContextPath());
        uri.append("/" + flow);
        uri.append("/" + controller);
        return uri.toString();
    }
    
    public static boolean validEmail(String email)
    {        
        email = email.trim();
        boolean matchFound = true;
        if (email != null && email.length()>0) {
           //Pattern p = Pattern.compile(".+@.+\\.[a-z]+"); 
           Pattern p = Pattern.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");                  
           //Match the given string with the pattern
           Matcher m = p.matcher(email);
           //check whether match is found 
           matchFound = m.matches();
        }        
        return matchFound;        
    }
    
} 
