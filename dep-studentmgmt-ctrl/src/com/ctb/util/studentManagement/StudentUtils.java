package com.ctb.util.studentManagement; 

import com.ctb.bean.testAdmin.Student;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * StudentUtils.java
 * @author John_Wang
 */ 


public class StudentUtils 
{
    static final long serialVersionUID = 1L;
    
	/**
	 * Generates a student usernames of format
     * <first-name>-<mi>-<last-name>-mmdd
	 * @param student
	 * @return String
	 */
    public static String generateBasicStudentUsername(Student student, String suffix)
    {
        String firstName = convertForUserName( student.getFirstName() );
        String lastName = convertForUserName (student.getLastName() );
        String middleName = convertForUserName (student.getMiddleName() );
        firstName = firstName.length() < 11?firstName:firstName.substring(0,10);
        firstName = firstName.endsWith("-")?firstName:firstName + "-";
        lastName = lastName.length() < 11?lastName:lastName.substring(0,10);
        lastName = lastName.endsWith("-")?lastName:lastName + "-";
        String middleInitial = middleName.length() > 0?
                middleName.substring(0,1):"";
        Date birthdate = (student.getBirthdate() != null) ? student.getBirthdate() : new Date();
        
        String dobDay = 
            (new SimpleDateFormat( "d")).format( birthdate );
        String dobMonth = 
            (new SimpleDateFormat( "M")).format( birthdate );
        return new String( firstName.toUpperCase() +
                (middleInitial.length()>0?
                   middleInitial.toUpperCase() + "-":"")
                   + lastName.toUpperCase() +
                        padWithZero( dobMonth ) +
                        padWithZero( dobDay ) )+suffix;   //ISTEP2010CR005 - to generate uppercase for student login id
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
				( c >= '0' && c <= '9' ) )				
            {
                sb.append( c );
				prev_c = c;
            } 
            else 
			if (( c == ' ' ) && (prev_c != '-'))
            {
                sb.append( '-' );
				prev_c = '-';
            }
        }
        return sb.toString();
    }

    private static String padWithZero( String number_ )
    {
        if ( number_.length() < 2 )
        {
            number_ = "0" + number_;
        } 
        return number_;
    }

    
} 
