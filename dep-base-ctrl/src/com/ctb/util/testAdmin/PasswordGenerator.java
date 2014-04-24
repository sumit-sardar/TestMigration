package com.ctb.util.testAdmin; 

import com.ctb.exception.CTBBusinessException;
import com.ctb.util.RandomWordGenerator;

public class PasswordGenerator 
{ 
    static final long serialVersionUID = 1L;
	public static final int GENERATED_STUDENT_PASSWORD_LENGTH           = 6;
    
    /**
     * Generates a password for a test roster.
     *
     * @return String representing generated password.
     * @throws CTBBusinessException
     */
    public static String generatePassword() throws CTBBusinessException {
        String password = null;
        boolean validPassword = false;

        RandomWordGenerator rwFactory = new RandomWordGenerator(RandomWordGenerator.Dictionary.US_DICTIONARY);

        while (!validPassword)
        {   
        	 //ISTEP2010CR005 - to generate uppercase for test roster password
            password = rwFactory.generateWordPaddedWithDigits(GENERATED_STUDENT_PASSWORD_LENGTH).toUpperCase();

            if ((rwFactory.verifyNoVulgar(password)) || (rwFactory.verifyNotNegative(password)))
            {
                validPassword = true;
            }
        }

        return password;
    }
    
    /**
     * Generates a password for a test roster according to the specified maximum and minimum length
     * However it is assumed that minimum length cannot be less than 4 and maximum length cannot be  more than 10
     * @return String representing generated password.
     * @throws CTBBusinessException
     */
    public static String generatePasswordRecommendedLength(Integer maxLength,Integer minLength) throws CTBBusinessException {
        String password = null;
        boolean validPassword = false;
        RandomWordGenerator rwFactory = new RandomWordGenerator(RandomWordGenerator.Dictionary.US_DICTIONARY);

        while (!validPassword)
        {   
        	password = rwFactory.generateWordPaddedWithDigitsRecommendedLength(maxLength,minLength).toUpperCase();

            if ((rwFactory.verifyNoVulgar(password)) || (rwFactory.verifyNotNegative(password)))
            {
                validPassword = true;
            }
        }

        return password;
    }
    
    public static String generatePasswordForWV(String fName, String lName, Integer productId) throws CTBBusinessException{
    	String password = null;
    	
    	fName=fName.replaceAll("[^a-zA-Z0-9]+","");
    	if(fName.length()==0) {
    		fName="0";
    	}
    	lName=lName.replaceAll("[^a-zA-Z0-9]+","");
    	    	
    	while(lName.length() < 4) {
    		lName += "0";
		}
    	
		password = fName.substring(0,1)+ lName.substring(0,4);
		password = password.toUpperCase();
		
		/*
		 * Block added on 21 March,14 to change password of WV based on Test type.
		 * Practice test password =  First name (1st letter) + last Name (first  4 letter’s) + Character “P”
		 * Breach test password = First name (1st letter) + last Name (first  4 letter’s) + Character “B”
		 * Operational test password = First name (1st letter) + last Name (first  4 letter’s) 
		 * */
		if (productId == 5501)
			password = password+"B";
		else if(productId == 5503)
			password = password+"P";
		
    	return password;
    }
} 
