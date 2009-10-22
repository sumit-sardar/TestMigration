package com.ctb.util.userManagement; 

import com.ctb.bean.testAdmin.User;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * UserUtils.java
 * @author Tata Consultency Services
 * 
 */ 


public class UserUtils 
{
    static final long serialVersionUID = 1L;
    
	/**
	 * Generates a usernames of format
     * <first-name>_<mi>_<last-name>_mmdd
	 * @param student
	 * @return String
	 */
    public static String generateBasicUsername(User user, String suffix)
    {
        String firstName = convertForUserName( user.getFirstName() );
        String lastName = convertForUserName (user.getLastName() );
        String middleName = convertForUserName (user.getMiddleName() );
        firstName = firstName.length() < 11?firstName:firstName.substring(0,10);
        firstName = firstName.endsWith("_")?firstName:firstName + "_";
        lastName = lastName.length() < 11?lastName:lastName.substring(0,10);
        //lastName = lastName.endsWith("_")?lastName:lastName + "_";
        String middleInitial = middleName.length() > 0?
                middleName.substring(0,1):"";
               
        return new String( firstName.toLowerCase() +
                (middleInitial.length()>0?
                   middleInitial.toLowerCase() + "_":"")
                   + lastName.toLowerCase() 
                         )+suffix;
    }
    
    
    /**
	 * Generates a usernames of format
     * <first-name>_<mi>_<last-name>_mmdd
	 * @param student
	 * @return String
	 */
    public static String generateEscapeUsername(User user)
    {
        String firstName = convertForUserName( user.getFirstName() );
        String lastName = convertForUserName (user.getLastName() );
        String middleName = convertForUserName (user.getMiddleName() );
        firstName = firstName.length() < 11?firstName:firstName.substring(0,10);
        
        firstName = firstName.endsWith("_")?firstName:firstName + "\\_";
        lastName = lastName.length() < 11?lastName:lastName.substring(0,10);
        //lastName = lastName.endsWith("_")?lastName:lastName + "_";
        String middleInitial = middleName.length() > 0?
                middleName.substring(0,1):"";
               
        String usrName =  new String( firstName.toLowerCase() +
                (middleInitial.length()>0?
                   middleInitial.toLowerCase() + "\\_":"")
                   + lastName.toLowerCase() 
                         );
    
        return usrName;
    }
    
    
     public static String generateFormatedUsername(User user, String suffix)
    {
        String firstName = convertForUserName( user.getFirstName() );
        String lastName = convertForUserName (user.getLastName() );
        String middleName = convertForUserName (user.getMiddleName() );
        firstName = firstName.length() < 11?firstName:firstName.substring(0,10);
        firstName = firstName.endsWith("_")?firstName:firstName + "_";
        lastName = lastName.length() < 11?lastName:lastName.substring(0,10);
        //lastName = lastName.endsWith("_")?lastName:lastName + "_";
        String middleInitial = middleName.length() > 0?
                middleName.substring(0,1):"";
               
        return new String( firstName.toLowerCase() +
                (middleInitial.length()>0?
                   middleInitial.toLowerCase() + "_":"")
                   + lastName.toLowerCase() 
                         )+suffix;
    }

    private static String convertForUserName( String inString_ )
    {
    	if ( inString_ == null )
    		return "";
		inString_ = inString_.trim();
		char prev_c = '*';
        StringBuffer sb = new StringBuffer();
        for( int i=0; i < inString_.length(); ++i )
        {
            char c = inString_.charAt( i );
            if (( c >= 'a' && c <= 'z' ) || 
				( c >= 'A' && c <= 'Z' ) ||
				( c >= '0' && c <= '9' ))  
                				
            {
                sb.append( c );
				prev_c = c;
            } 
            else 
			if (( c == ' ' ) && (prev_c != '_'))
            {
                sb.append( '_' );
				prev_c = '_';
            }
        }
        return sb.toString();
    }

        
} 
