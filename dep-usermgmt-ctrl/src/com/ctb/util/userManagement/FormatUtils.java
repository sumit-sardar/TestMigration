package com.ctb.util.userManagement; 


import java.security.MessageDigest;
import java.util.Random;

/** 
 * FormatUtils.java
 * @author Tata Consultency Services
 * 
 */ 

public class FormatUtils 
{ 
    
    
    public static String generateRandomPassword(int passwordLength)
    {
        String alphaNumArray = CTBConstants.ALPHA_ARRAY + CTBConstants.NUM_ARRAY;
        String password = "";
        int index = 0;

        Random rnd = new Random();

        boolean validPassword = false;

        while(!validPassword) {
            password = "";
            for(int i = 0; i < passwordLength; i++) {
                index = rnd.nextInt();

                if(index < 0) {
                    index = index * -1;
                }

                // make sure the index is a value within the length of our array
                if(index != 0) {
                    index = index % alphaNumArray.length();
                }

                password = password.concat(String.valueOf(alphaNumArray.charAt(index)));
            }


            if(verifyContainsCharFrom(CTBConstants.ALPHA_ARRAY,password) 
                && verifyContainsCharFrom(CTBConstants.NUM_ARRAY,password)) {
                    validPassword = true;
            }
        }
        return password;
    }
    
    private static boolean verifyContainsCharFrom(String charArray,String password) {
        boolean verified = false;
        int j = 0;

        while(!verified && (j < password.length())) {
            if(charArray.indexOf(String.valueOf(password.charAt(j))) != -1) {
                    verified = true;
            }
            j++;
        }
        return verified;
    }
    
    public static String encodePassword(String password) throws Exception{
        MessageDigest md;
        StringBuffer retval = new StringBuffer( "" );
        byte[] hash = new byte[]{};
        try {
            md = MessageDigest.getInstance("MD5");
            md.update( password.getBytes() );
            hash = md.digest();
        }
        catch (Exception e ) {
            throw e;
        }
        for (int i = 0; i < hash.length; ++i ) {
            if (((int) hash[i] & 0xff) < 0x10) {
                retval.append("0");
            }
            retval.append(Long.toString((int) hash[i] & 0xff, 16));
       }

        return retval.toString();
    }
} 
