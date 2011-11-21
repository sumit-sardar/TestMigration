package com.ctb.testSessionInfo.utils; 

import com.ctb.testSessionInfo.dto.Message;
import com.ctb.testSessionInfo.dto.PasswordInformation;
import com.ctb.util.userManagement.CTBConstants;

public class UserPasswordUtils {
	
	 public static String getSelectedUserName(String userId){
		 
		 return null;
	 }
	
	 public static String getRequiredPasswordField(PasswordInformation passwordInfo) {
	     
	        String requiredFields = null;
	        int requiredFieldCount = 0;  
	         
	        String oldPassword = passwordInfo.getOldPassword();
	        String newPassword = passwordInfo.getNewPassword();
	        String confirmPassword = passwordInfo.getConfirmPassword();

	        if (oldPassword.length() == 0 ) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_OLD_PASSWORD, requiredFieldCount, requiredFields);       
	        }
	        
	        if (newPassword.length() == 0 ) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_NEW_PASSWORD, requiredFieldCount, requiredFields);       
	        }
	         
	        if (confirmPassword.length() == 0) {
	            requiredFieldCount += 1;            
	            requiredFields = Message.buildErrorString(Message.FIELD_CONFIRM_PASSWORD, requiredFieldCount, requiredFields);       
	        }

	        return requiredFields;
	    }
	 
	 public static boolean isPasswordDifferent(String existingPassword, String providedPassword) {
  
	        if (! existingPassword.equals(providedPassword)) {
	            return true;
	        }
	    
	        return false;
    }

	 public static boolean isNewAndConfirmPasswordDifferent (PasswordInformation passwordInfo) {
		  
	        String newPassword = passwordInfo.getNewPassword().trim();
	        String confirmPassword = passwordInfo.getConfirmPassword().trim();
	    
	        if (! newPassword.equals(confirmPassword)) {
	            return true;
	        }
	    
	        return false;
 }
	 
	 public static String verifyPasswordInfo(PasswordInformation passwordInfo) {
		String invalidCharFields = null;
		int invalidCharFieldCount = 0;

		if (!(validPasswordString(CTBConstants.ALPHA_ARRAY, passwordInfo
				.getNewPassword())
				&& validPasswordString(CTBConstants.NUM_ARRAY, passwordInfo
						.getNewPassword())
				&& !validPasswordString(CTBConstants.DEX_SPECIAL_CHAR_ARRAY,
						passwordInfo.getNewPassword()) && passwordInfo
				.getNewPassword().length() >= 8)) {
			invalidCharFieldCount += 1;
			invalidCharFields = buildErrorString(Message.FIELD_NEW_PASSWORD,invalidCharFieldCount, invalidCharFields);
		}

		if (!(validPasswordString(CTBConstants.ALPHA_ARRAY, passwordInfo
				.getConfirmPassword())
				&& validPasswordString(CTBConstants.NUM_ARRAY, passwordInfo
						.getConfirmPassword())
				&& !validPasswordString(CTBConstants.DEX_SPECIAL_CHAR_ARRAY,
						passwordInfo.getConfirmPassword()) && passwordInfo
				.getConfirmPassword().length() >= 8)) {
			invalidCharFieldCount += 1;
			invalidCharFields = buildErrorString(Message.FIELD_CONFIRM_PASSWORD, invalidCharFieldCount,invalidCharFields);
		}

	   return invalidCharFields;

	}
	 
	   private static boolean validPasswordString(String charArray, String password) {

		boolean verified = false;
		int j = 0;

		while (!verified && (j < password.length())) {
			if (charArray.indexOf(String.valueOf(password.charAt(j))) != -1) {
				verified = true;
			}
			j++;
		}

		if (password.indexOf(" ") != -1) {
			verified = true;
		}

		return verified;
	}
	   
	    public static String buildErrorString(String field, int count, String str) {
	    	
	    	
		String result = str !=null ? str : "";

		if (count == 1) {
			result += field;
		} else {
			result += (", " + field);
		}
		return result;
	}

}
