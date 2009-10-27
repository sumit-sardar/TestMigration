package utils; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            ch == '^' ||
            ch == '&' ||
            ch == ')' ||
            ch == '(' ||
            ch == '+') {
           invalid = true; 
        }
       return invalid;
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

    public static boolean validString(String str)
    {
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (invalidCharacter(character))
                return false;
        }
         
        return !requestHasInvalidParameters(str);
    }

    public static boolean validNameString(String str)
    {
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validNameCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }

    public static boolean validTextString(String str)
    {
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (! validTextCharacter(character))
                return false;
        }
        return !requestHasInvalidParameters(str);
    }
    
    public static String verifyCreateStudentName(String firstName, String lastName, String middleName)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("First Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Last Name", invalidCharFieldCount, invalidCharFields);       
        }

        if (! validNameString(middleName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Middle Name", invalidCharFieldCount, invalidCharFields);       
        }
    
        return invalidCharFields;
    }

    public static String verifyCreateStudentNumber(String studentNumber, String studentSecondNumber)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (studentNumber != null) {
            if (! validTextString(studentNumber) ) {
                invalidCharFieldCount += 1;            
                invalidCharFields = buildErrorString("Student ID", invalidCharFieldCount, invalidCharFields);       
            }
        }
        if (studentSecondNumber != null) {
            if (! validTextString(studentSecondNumber) ) {
                invalidCharFieldCount += 1;            
                invalidCharFields = buildErrorString("Student ID 2", invalidCharFieldCount, invalidCharFields);       
            }
        }
            
        return invalidCharFields;
    }

    public static String verifyFindStudentInfo(String firstName, String lastName, String middleName,
                                                 String studentNumber, String loginId)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("First Name", invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Last Name", invalidCharFieldCount, invalidCharFields);       
        }

        if (! validNameString(middleName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Middle Name", invalidCharFieldCount, invalidCharFields);       
        }

        if (! validString(studentNumber) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Student ID", invalidCharFieldCount, invalidCharFields);       
        }
    
        if (! validString(loginId) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString("Login ID", invalidCharFieldCount, invalidCharFields);       
        }
        
        return invalidCharFields;
    }
    
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
