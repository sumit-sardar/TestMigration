package com.ctb.lexington.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.ctb.lexington.data.StudentVO;

/**
 * Provides utility methods for text formatting and parsing.
 *
 * @author  <a href="mailto:john.m.shields@accenture.com">John M. Shields</a>
 * @version
 * $Id$
 */
public class FormatUtils
{

    /**
     * Set's the phone format pattern, example: (123)456-7890x123
     */
    public static final String PHONE_FORMAT = "({0}){1}-{2}x{3}";
    
    public static final String PHONE_FORMAT_SHORT = "({0}){1}-{2}";
    
    //private MessageFormat phoneFormat = new MessageFormat( PHONE_FORMAT );

    /**
     * Set's the fax format pattern, example: (123)456-7890
     */
    public static final String FAX_FORMAT = "({0}){1}-{2}";
    
    /**
     * Set's the zipcode format pattern, example: 12345-6789
     */
    public static final String ZIPCODE_FORMAT = "{0}-{1}";
    
    //private MessageFormat zipcodeFormat = new MessageFormat( ZIPCODE_FORMAT );
    
    public static final String USERNAME_FORMAT = "{0}_{1}_{2}";
    
    /**
     * Returns a formatted phone nubmer, example: (123)456-7890x123
     *
     * @return Example: (123)456-7890x123
     */
    public static String formatPhone( String areaCode_,
                                      String coc_,
                                      String line_,
                                      String extension_ )
    {        
        String formattedPhone;
        
        if (extension_.equals(""))
        {
            Object[] args = new Object[]
            {
                areaCode_,
                coc_,
                line_,
            };
            
            formattedPhone = MessageFormat.format( PHONE_FORMAT_SHORT, args );
        }
        else
        {
            
            // Create argument array
            Object[] args = new Object[]
            {
                areaCode_,
                coc_,
                line_,
                extension_
            };
            
            formattedPhone = MessageFormat.format( PHONE_FORMAT, args );
        }

        return formattedPhone;
    }
    
    /**
     * Returns a formatted zipcode, example: 12345-1234
     *
     * @return Example: 12345-1234
     */
    public static String formatZipcode( String postalCode_,
                                        String postalCodeExt_ )
    {
        // create arg array
        Object[] args = new Object[]
        {
            postalCode_,
            postalCodeExt_
        };
        
        // format
        return MessageFormat.format( ZIPCODE_FORMAT, args );
    }
    
    public static String generateRandomPassword(int passwordLength_)
    {
        String alphaNumArray = CTBConstants.ALPHA_ARRAY + CTBConstants.NUM_ARRAY;
        String password = "";
        int index = 0;

        Random rnd = new Random();

        boolean validPassword = false;

        while(!validPassword)
        {
            password = "";
            for(int i = 0; i < passwordLength_; i++)
            {
                index = rnd.nextInt();

                if(index<0)
                {
                    index = index * -1;
                }

                // make sure the index is a value within the length of our array
                if(index!=0)
                {
                    index = index % alphaNumArray.length();
                }

                password = password.concat(String.valueOf(alphaNumArray.charAt(index)));
            }


            if(verifyContainsCharFrom(CTBConstants.ALPHA_ARRAY,password) && verifyContainsCharFrom(CTBConstants.NUM_ARRAY,password))
            {
                    validPassword = true;
            }
        }
        return password;
    }
    
    /**
     * Generates the two basic Usernames of format
     * <first-name>_<last-name> and <first-name>_<mi>_<last-name>
     */
    public static String[] generateBasicUsernames( String firstName_,
                                                   String middleName_,
                                                   String lastName_)
    {
        String userNames[] = new String[2];
        firstName_ = convertForUserName(firstName_);
        lastName_ = convertForUserName(lastName_);
        middleName_ = convertForUserName(middleName_);
        if ( firstName_.length() > 12 )
        {
            firstName_ = firstName_.substring( 0, 12 );
            
        }
        if (firstName_.endsWith("_"))
                firstName_ = firstName_.substring(0, (firstName_.length() - 1));
        
        if ( lastName_.length() > 12 )
        {
            lastName_ = lastName_.substring( 0, 12 );
            
        }
        userNames[0] = firstName_.toLowerCase() + "_" 
                            + lastName_.toLowerCase();
        if (!(middleName_.equals("")))
        {
            if (middleName_.endsWith("_"))
                middleName_ = middleName_.substring(0, (middleName_.length() - 1));
            userNames[1] = firstName_.toLowerCase() + "_" 
                       + middleName_.toLowerCase().substring( 0, 1 ) + "_"
                        + lastName_.toLowerCase() ;
        }
        else userNames[1] = firstName_.toLowerCase() + "_" 
                            + lastName_.toLowerCase();
        return userNames;
    }
    
    
    /**
     * Generates the two basic student Usernames of format
     * <first-name>_<last-name>_mmdd and <first-name>_<mi>_<last-name>_mmdd
     */
    public static String[] generateBasicStudentUsernames( StudentVO student_ )
    {
        String firstName = convertForUserName( student_.getFirstName() );
        String lastName = convertForUserName (student_.getLastName() );
        String middleName = convertForUserName (student_.getMiddleName() );
        firstName = firstName.length() < 11?firstName:firstName.substring(0,10);
        firstName = firstName.endsWith("_")?firstName:firstName + "_";
        lastName = lastName.length() < 11?lastName:lastName.substring(0,10);
        lastName = lastName.endsWith("_")?lastName:lastName + "_";
        String middleInitial = middleName.length() > 0?
                middleName.substring(0,1):"";
        Date birthdate = (student_.getBirthdate() != null) ? student_.getBirthdate().getTime() : new Date();
        
        String dobDay = 
            (new SimpleDateFormat( "d")).format( birthdate );
        String dobMonth = 
            (new SimpleDateFormat( "M")).format( birthdate );
        return new String[] { 
            new String( firstName.toLowerCase() +
                lastName.toLowerCase() +
                        padWithZero( dobMonth ) +
                        padWithZero( dobDay ) ) ,
            new String( firstName.toLowerCase() +
                (middleInitial.length()>0?
                   middleInitial.toLowerCase() + "_":"")
                   + lastName.toLowerCase() +
                        padWithZero( dobMonth ) +
                        padWithZero( dobDay ) ) ,
        };
    }

    public static String convertForUserName( String inString_ )
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
			if (( c == ' ' ) && (prev_c != '_'))
            {
                sb.append( '_' );
				prev_c = '_';
            }
        }
        return sb.toString();
    }

    /*
    public static Object[] parsePhone(String phone_)
    {
    }
    
    public static Object[] parseZipcode(String zipcode_)
    {
    }
     */
    
    private static boolean verifyContainsCharFrom(String charArray_,String password_)
    {
        boolean verified = false;
        int j = 0;

        while(!verified && (j < password_.length()))
        {
            if(charArray_.indexOf(String.valueOf(password_.charAt(j))) != -1)
            {
                    verified = true;
            }
            j++;
        }
        return verified;
    }

    private static String padWithZero( String number_ )
    {
        if ( number_.length() < 2 )
        {
            number_ = "0" + number_;
        } 
        return number_;
    }

    public static String formatFileIndex( int index_ )
    {
        Integer index = new Integer(index_);
        String fileIndex = new String("fileIndex");
        fileIndex += index.toString();
        return fileIndex;
    }

    public static String formatFileIndexRemoved( int index_ )
    {
        Integer index = new Integer(index_);
        String fileIndex = new String("fileIndexRemoved");
        fileIndex += index.toString();
        return fileIndex;
    }

    public static String formatFileIndexTitle( int index_ )
    {
        Integer index = new Integer(index_);
        String fileIndexTitle =  new String("File ");
        fileIndexTitle += index.toString();
        fileIndexTitle += ":";
        return fileIndexTitle;
    }
    
	public static String formatName(String firstName, String middleName, String lastName){
	    firstName = firstName != null ? firstName : "";
	    middleName = middleName != null ? middleName : "";
	    lastName = lastName != null ? lastName : "";
		boolean hasFirstName = firstName.length() > 0;
		boolean hasMiddleName = middleName.length() > 0;
        String displayName = lastName;
        if(hasMiddleName || hasFirstName)
        	displayName = displayName + ", ";
        if(hasFirstName)
        	displayName = displayName + firstName + " ";
        if(hasMiddleName)
        	displayName = displayName + middleName;
        return displayName;
	}
    
}
 
/*
 * $Log$
 * Revision 1.1  2007/01/30 01:31:45  ncohen
 * port scoring to 4.x platform
 *
 * Revision 1.1  2006/02/23 20:48:30  ncohen
 * create new module for legacy OAS app
 *
 * Revision 1.14  2005/05/03 21:25:16  ncohen
 * replace HEAD with iknow-millbarge
 *
 * Revision 1.13.12.2.4.1.2.1  2005/04/13 00:21:04  ttruong
 * defects in Re-match student modules
 *
 * Revision 1.13.12.2.4.1  2005/03/09 04:12:21  ttruong
 * match student module
 *
 * Revision 1.13.12.2  2004/09/17 16:09:48  ttruong
 * set birthday = null and grade = Adult when add a student in Rapid Registration
 *
 * Revision 1.13.12.1  2004/08/17 22:02:09  binkley
 * Globally organize imports (removed 1,000+ warnings).
 *
 * Revision 1.13  2003/07/30 18:35:18  ttruong
 * add logic to verify the scan files
 *
 * Revision 1.12  2003/02/05 19:29:13  oasuser
 * merged from OASR3tmp to trunk
 *
 * Revision 1.11  2003/01/31 04:04:38  oasuser
 * merged from OASR3tmp to trunk.  Code not compiling, so java will be re-merged at a later date.
 *
 * Revision 1.10.6.5  2003/02/05 01:30:46  oasuser
 * merged from R0221 branch to OASR3tmp branch.
 *
 * Revision 1.10.6.4  2003/01/31 02:18:15  sprakash
 * TestletVO refactoring
 *
 * Revision 1.10.6.3  2003/01/30 22:31:15  oasuser
 * merged from R0221 to OASR3tmp.  Some files not currently compiling.
 *
 * Revision 1.10.6.2  2003/01/24 06:18:06  ttruong
 * Refactoring
 *
 * Revision 1.10.6.1  2003/01/22 01:47:57  sprakash
 * StudentBean to StudentVO updates and assert keyword fix
 *
 * Revision 1.10.4.2  2003/02/01 05:41:08  ttruong
 * defect 14040
 *
 * Revision 1.10.4.1  2003/01/28 19:15:31  ttruong
 * defect 9386
 *
 * Revision 1.10  2003/01/09 03:16:00  oasuser
 * merged from iknowR2-fixes branch as of 01-08 to trunk
 *
 * Revision 1.9.8.1  2002/12/31 18:18:02  ttruong
 * defect 13539.
 * Allow numbers when generateUserName()
 *
 * Revision 1.9  2002/09/05 20:27:20  oasuser
 * Clobbered trunk with code from ATSR1 branch.
 *
 * Revision 1.7.4.1  2002/08/22 18:31:59  jshields
 * Fixed control-M problems.
 *
 * Revision 1.7  2002/08/20 22:51:06  oasuser
 * Merged build-R1-20020813-161424
 *
 * Revision 1.6  2002/06/27 23:02:04  oasuser
 * Merged build-R1-20020627-1500 to trunk
 *
 * Revision 1.2.2.2  2002/06/05 16:46:11  adimayug
 * fixed defect 9386.
 * --Michael.
 *
 * Revision 1.2.2.1  2002/05/31 19:09:43  sprakash
 * modified generate basicUserNames to include code to generate user names.
 *
 * Revision 1.2  2002/05/21 18:38:52  oasuser
 * Committing to the trunk
 *
 * Revision 1.1.2.11  2002/05/15 03:31:44  jtsang
 * added short format
 *
 * Revision 1.1.2.10  2002/04/30 04:56:46  dfuller
 * Made a couple of changes to generateBasicStudentUsernames
 *
 * Revision 1.1.2.9  2002/04/24 19:19:52  adimayug
 * convertForUserName now a public method
 *
 * Revision 1.1.2.8  2002/04/18 23:17:16  dfuller
 * Added punctuation stripping to middle name and ensured spaces are
 * converted to underscores.
 *
 * Revision 1.1.2.7  2002/04/18 03:55:54  dfuller
 * changed handling of usernames to retain only alphanumeric for
 * first, middle, and last names
 *
 * Revision 1.1.2.6  2002/04/03 23:45:41  dfuller
 * fixed a date format problem.
 *
 * Revision 1.1.2.5  2002/04/03 21:38:26  dfuller
 * updated generateBasicStudentUsernames to fix some substring issues and
 * bad date formats.
 *
 * Revision 1.1.2.4  2002/04/02 17:19:47  dfuller
 * added generateBasicStudentUsernames method
 *
 * Revision 1.1.2.3  2002/03/27 17:57:36  jshields
 * Added FAX_FORMAT.
 *
 * Revision 1.1.2.2  2002/03/15 18:28:18  jshields
 * Added formatUserName and generateRandomPassword methods.
 *
 * Revision 1.1.2.1  2002/03/14 20:18:07  jshields
 * Adding utility for formatting text.
 *
 */
