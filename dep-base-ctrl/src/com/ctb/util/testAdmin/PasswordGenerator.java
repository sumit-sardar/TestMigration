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
            password = rwFactory.generateWordPaddedWithDigits(GENERATED_STUDENT_PASSWORD_LENGTH);

            if ((rwFactory.verifyNoVulgar(password)) || (rwFactory.verifyNotNegative(password)))
            {
                validPassword = true;
            }
        }

        return password;
    }
} 
