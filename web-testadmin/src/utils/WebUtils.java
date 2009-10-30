package utils; 

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

    public static boolean requestHasInvalidParameters(String str)
    {
        if (str == null) str = "";
       
        String[] invalidChars = { "<script>", "javascript:"};
        
            for( int i=0; i<invalidChars.length; i++ )
            {
                if ( str.indexOf(invalidChars[i]) != -1 )
                {
                    // Invalid character found in this parameter, set to invalid
                    return true;                
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
            ch == ':' ||
            ch == '<' ||
            ch == '>' ||
            ch == '(' ||
            ch == ')' ||
            ch == '^') {
           invalid = true; 
        }
       return invalid;
    }

    public static boolean validString(String str)
    {
        if (str == null) str = "";

        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (invalidCharacter(character))
                return false;
        }
         
        return !requestHasInvalidParameters(str);
    }

    public static boolean invalidSessionName(String str)
    {
        if (str == null) str = "";
            
        boolean hasKeyword = ( str.indexOf("script") >= 0 );
        boolean hasInvalidChar = false;        
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (invalidCharacter(character))
                hasInvalidChar = true;
        }
        
        return (hasKeyword && hasInvalidChar);
    }
    
} 
