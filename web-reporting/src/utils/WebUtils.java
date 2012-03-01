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
    
    public static String verifyFindStudentInfo(String firstName, String lastName, String middleName,
            String studentNumber, String loginId, String studentIdLabelName)
		{
		String invalidCharFields = "";
		int invalidCharFieldCount = 0;
		int requiredFieldCount = 0;
		String requiredFields = "";
		
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
	            invalidCharFields = buildErrorString(studentIdLabelName, invalidCharFieldCount, invalidCharFields);      //Changed for # 65980 
	     }
		
        /* if (productName.equals(FilterSortPageUtils.FILTERTYPE_ANY_TESTNAME)) {
             requiredFieldCount += 1;            
             requiredFields = Message.buildErrorString("Test Name", requiredFieldCount, requiredFields);       
         }*/
         
		if (! validString(loginId) ) {
		invalidCharFieldCount += 1;            
		invalidCharFields = buildErrorString("Login ID", invalidCharFieldCount, invalidCharFields);       
		}
	
		return invalidCharFields;
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
