package utils; 

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.*;


public class WebUtils 
{ 

    public static String buildURI(HttpServletRequest req, String flow, String controller)
    {
        StringBuffer uri = new StringBuffer(req.getContextPath());
        uri.append("/" + flow);
        uri.append("/" + controller);
        return uri.toString();
    }
    
    public static void writeResponseContent(HttpServletResponse res, String content)
    {
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        
        try {
            res.getWriter().write(content);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean requestHasInvalidParameters(String str)
    {       
        String[] invalidChars = { "<script>", "javascript:"};
        
        for( int i=0; i<invalidChars.length; i++ ) {
            if ( str.indexOf(invalidChars[i]) != -1 ) {
                return true;    // Invalid character found                
            }
        }          
        return false;
    }
    
    public static boolean invalidCharacter(char ch)
    {
        boolean invalid = false;
    	if (ch == '!' ||
            ch == '@' ||
            ch == '#' ||
            ch == '$' ||
            ch == '%' ||
            ch == '^' ||
            ch == '&' ||
            ch == ')' ||
            ch == '(' ||
            ch == '+') {
           invalid = true; 
        }
       return invalid;
    }
    
    public static boolean validAddressCharacter(char ch)
    {
        boolean valid = validNameCharacter(ch);
    	if (ch == '#')
           valid = true; 
    
        return valid;
    }

    public static boolean validNameCharacter(char ch)
    {
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        boolean validChar = ((ch == '/') || 
                             (ch == '\'') || 
                             (ch == '-') || 
                             (ch == '\\') || 
                             (ch == '.') || 
                             (ch == '(') || 
                             (ch == ')') || 
                             (ch == '&') || 
                             (ch == '+') || 
                             (ch == ',') || 
                             (ch == ' '));
        
        return (zero_nine || A_Z || a_z || validChar);
    }

    public static boolean validTextCharacter(char ch)
    {
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean validChar = (ch == ' ');
        
        return (zero_nine || A_Z || a_z || validChar);
    }
    
    public static boolean validLoginCharacter(char ch)
    {
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean validChar = (ch == '_');
        
        return (zero_nine || A_Z || a_z || validChar);
    }

    public static boolean validString(String str)
    {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validLoginCharacter(character))
                return false;
        }
         
        return !requestHasInvalidParameters(str);
    }

    public static boolean validNameString(String str)
    {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validNameCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    public static boolean validAddressString(String str)
    {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validAddressCharacter(character) )
                return false;
        }
        return !requestHasInvalidParameters(str);
    }

    public static boolean validLoginID(String loginId)
    {      
        return validString(loginId);
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
    
    public static boolean validTextString(String str)
    {
        str = str.trim();
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validTextCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
} 
