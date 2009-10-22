package com.ctb.util.testAdmin; 

import com.ctb.exception.CTBBusinessException;
import com.ctb.util.RandomWordGenerator;

public class AccessCodeGenerator 
{ 
    static final long serialVersionUID = 1L;
    // the length of access codes
	public static final int GENERATED_ACCESS_CODE_LENGTH                = 10;
	public static final int MAX_ACCESS_CODE_LENGTH                      = 32;
	public static final int MIN_ACCESS_CODE_LENGTH                      = 6;
    
    /**
     * Generates an access code for a test administration.
     *
     * @return String representing generated access code.
     * @throws CTBBusinessException
     */
    public static String generateAccessCode() throws CTBBusinessException {
        String accessCode = null;
    
        RandomWordGenerator rwFactory = new RandomWordGenerator(RandomWordGenerator.Dictionary.US_DICTIONARY);

        accessCode = rwFactory.generateWordPaddedWithDigits(GENERATED_ACCESS_CODE_LENGTH);

        return accessCode;
    }
} 
