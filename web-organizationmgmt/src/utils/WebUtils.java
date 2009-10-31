package utils; 

/**
 *@author Tata Consultancy Services 
 * WebUtils class is used to valid customer and org  details 
 * such as Email, Phone number, Address,
 * valid character for text field details
 */

import javax.servlet.http.HttpServletRequest;
import java.util.regex.*;


public class WebUtils 
{ 
    public static String buildURI(HttpServletRequest req, String flow, String controller) {
        StringBuffer uri = new StringBuffer(req.getContextPath());
        uri.append("/" + flow);
        uri.append("/" + controller);
        return uri.toString();
    }
    
    
    public static boolean requestHasInvalidParameters(String str) {
       
        String[] invalidChars = { "<script>", "javascript:"};
        
            for( int i=0; i<invalidChars.length; i++ ) {
                
                if ( str.indexOf(invalidChars[i]) != -1 ) {
                    
                    // Invalid character found in this parameter, set to invalid
                    return true;   
                                 
                }
                
            }
          
        return false;
    }
    
    public static boolean validEmail(String email) {
        
        email = email.trim();
        boolean matchFound = true;
        
        if ( email != null && email.length()>0 ) {
            
           Pattern p = Pattern.compile("[a-z|A-Z|0-9|_|\\-|.]+@[a-z|A-Z|0-9|_|\\-|.]+\\.[a-z|A-Z|0-9|_|\\-|.]+");                  
           //Match the given string with the pattern
           Matcher m = p.matcher(email);
           //check whether match is found 
           matchFound = m.matches();
           
        }         
        
        return matchFound;  
        
    }
    
    public static boolean invalidCharacter(char ch) {
        boolean invalid = false;
        
    	if ( ch == '!' ||
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

    public static boolean validNameCharacter(char ch) {
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
         // !, @, #, -, _, ', :, /, comma, period, and space will be allowed in these fields.
        boolean validChar = ((ch == '/') ||
                             (ch == '\\') ||
                             (ch == '-') ||
                             (ch == '\'') ||
                             (ch == '(') ||
                             (ch == ')') ||
                             (ch == '&') ||
                             (ch == '+') ||
                             (ch == ',') ||
                             (ch == '.') ||
                             (ch == ' '));
         
        return (zero_nine || A_Z || a_z || validChar);
    }
    
     public static boolean validCharacterForLic(char ch) {
       
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
         
        return (zero_nine);
    }
    
    
    public static boolean validCustomerInfoCharecter(char ch) {   
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        //A-Z, a-z, 0-9, and the symbols  /, \, -, ', (, ), &, +, comma, period, and space will be allowed in these fields.
        
        boolean validChar = ((ch == '/') ||
                             (ch == '\\') ||
                             (ch == '-') ||
                             (ch == '\'') ||
                             (ch == '(') ||
                             (ch == ')') ||
                             (ch == '&') ||
                             (ch == '+') ||
                             (ch == ',') ||
                             (ch == '.') ||
                             (ch == ' '));
                             
        return (zero_nine || A_Z || a_z || validChar) ;                            
        
    }
    
      
    public static boolean validNameCharacterForAddressCity(char ch) {
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
         //  /, \, -, ', (, ), &, +, comma, period, and space will be allowed in these fields.
        boolean validChar = ((ch == '/') ||      
                             (ch == '-') ||
                             (ch == '\\') ||
                             (ch == '\'') || 
                             (ch == '(') || 
                             (ch == ')') ||
                             (ch == '&') || 
                             (ch == '+') || 
                             (ch == ',') || 
                             (ch == '.') ||
                             (ch == ' '));
          
        
        return (zero_nine || A_Z || a_z || validChar);
    }
    
     public static boolean validNameCharacterForFramework(char ch) {
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
         //  #,/, \, -, ', (, ), &, +, comma, period, and space will be allowed in these fields.
        boolean validChar = ((ch == '#') ||
                             (ch == '/') ||      
                             (ch == '-') ||
                             (ch == '\\') ||
                             (ch == '\'') || 
                             (ch == '(') || 
                             (ch == ')') ||
                             (ch == '&') || 
                             (ch == '+') || 
                             (ch == ',') || 
                             (ch == '.') ||
                             (ch == ' '));
          
        
        return (zero_nine || A_Z || a_z || validChar);
    }

    

    public static boolean validTextCharacter(char ch) {
        boolean zero_nine = ((ch >= 48) && (ch <= 57));
        boolean A_Z = ((ch >= 65) && (ch <= 90));
        boolean a_z = ((ch >= 97) && (ch <= 122));
        boolean validChar = (ch == ' ');
        
        return (zero_nine || A_Z || a_z || validChar);
    }

    public static boolean validString(String str) {
        char[] characters = str.toCharArray();
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            
            if ( invalidCharacter(character) ) {
            
                return false;
                
            }
                
        }
         
        return !requestHasInvalidParameters(str);
    }

    public static boolean validNameString(String str) {   
        str = str.trim();
        char[] characters = str.toCharArray();
        for ( int i=0 ; i<characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validNameCharacter(character) ) {
            
                return false;
                
            }
                           
        }
        
        return !requestHasInvalidParameters(str);
    }
    
     public static boolean validNameStringForLic(String str) {   
        str = str.trim();
        char[] characters = str.toCharArray();
        for ( int i=0 ; i<characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validCharacterForLic(character)) {
            
                return false;
                
            }
                           
        }
        
        return !requestHasInvalidParameters(str);
    }
    
    
    public static boolean validCustomerNameString(String str) {
        char[] characters = str.toCharArray();
        for( int i = 0; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validCustomerInfoCharecter(character) ) {
                
                return false;
                
            }
        }
         return !requestHasInvalidParameters(str);
    }
    
    public static boolean validNameStringForCity(String str) {
        char[] characters = str.toCharArray();
        for ( int i=0 ; i <characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validNameCharacterForAddressCity(character) ) {
            
                return false;
                
            }
            
                
        }
        return !requestHasInvalidParameters(str);
    }

    public static boolean validTextString(String str) {
        char[] characters = str.toCharArray();
        for ( int i=0 ; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validTextCharacter(character) ) {
                
                return false;
                
            }
        }
        return !requestHasInvalidParameters(str);
    }

    
    public static String buildErrorString(String field, int count, String str) {
        String result = str;
        if ( count == 1 ) {
            
            result += field;
            
        } else {
            
            result += (", " + field);
                       
        }        
        return result;
    }
    
   public static String verifyPhoneFax(String PhoneFax1, String PhoneFax2, String PhoneFax3, String PhoneFax4, String FiledName) {
      String invalidCharFields = "";
      int invalidCharFieldCount = 0;
      
      if ( !validNumber(PhoneFax1) ) {
        
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString(FiledName+"1", invalidCharFieldCount, invalidCharFields);       
        
     }
     
     if ( !validNumber(PhoneFax2) ) {
        
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString(FiledName+"2", invalidCharFieldCount, invalidCharFields);  
             
     }
     
     if ( !PhoneFax3.equals("") && !validNumber(PhoneFax3) ) {
        
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString(FiledName+"3", invalidCharFieldCount, invalidCharFields);       
        
     }
     
     if (!PhoneFax4.equals("") && !validNumber(PhoneFax4) ) {
        invalidCharFieldCount += 1;            
        invalidCharFields = buildErrorString(FiledName+"4", invalidCharFieldCount, invalidCharFields); 
              
     }
     
      return invalidCharFields;
      
    }
    
    public static boolean validNumber(String str) {
        char[] characters = str.toCharArray();
        for ( int i=0 ; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( !( (character >= 48) && (character <= 57) ) ) {
                
                return false;
                
            } 
            
        } 
       return true;
    }
    
     public static boolean validAddressString(String str) {
        str = str.trim();
        char[] characters = str.toCharArray();
        for ( int i=0 ; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validAddressCharacter(character) ) {
                
                return false;
                
            }
        }
        return !requestHasInvalidParameters(str);
    }
    
     
    public static boolean validAddressCharacter(char ch) {
        boolean valid = validNameCharacterForAddressCity(ch);
        
    	if ( ch == '#' ) {
            
           valid = true; 
           
        }
    
        return valid;
    }
    
    public static boolean validFrameworkString(String str) {
        str = str.trim();
        char[] characters = str.toCharArray();
        for ( int i=0 ; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( ! validNameCharacterForFramework(character) ) {
                
                return false;
                
            }
        }
        return !requestHasInvalidParameters(str);
    }
} 
