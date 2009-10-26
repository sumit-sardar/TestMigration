package utils;

import com.ctb.bean.testAdmin.User;
import java.text.MessageFormat;
import java.util.Random;


public class FormatUtils
{
	public static final String ALPHA_ARRAY  = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUM_ARRAY    = "1234567890";

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
    
    /**
     * Returns a formatted username
     *
     * @return Example: 12345-1234
     */
    public static String formatUserName( User user_ )
    {
        // create arg array
        Object[] args = new Object[]
        {
            user_.getFirstName().toLowerCase(),
            user_.getLastName().toLowerCase()
        };
        
        // format
        return MessageFormat.format( USERNAME_FORMAT, args );
    }
    
    public static String generateRandomPassword(int passwordLength_)
    {
        String alphaNumArray = ALPHA_ARRAY + NUM_ARRAY;
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


            if(verifyContainsCharFrom(ALPHA_ARRAY,password) && verifyContainsCharFrom(NUM_ARRAY,password))
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
 
