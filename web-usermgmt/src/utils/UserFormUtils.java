package utils; 

import java.util.HashMap;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.User;
import java.util.List;
import manageUser.ManageUserController.ManageUserForm;
import com.ctb.control.userManagement.UserManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.util.userManagement.CTBConstants;
import dto.Message;
import dto.PasswordInformation;
import dto.PathNode;
import dto.UserProfileInformation;
import java.util.Iterator;


public class UserFormUtils 
{ 

    public static boolean invalidPrimaryPhoneFormat(UserProfileInformation userProfile)
    {
        if ((! validNumber(userProfile.getUserContact().getPrimaryPhone1())) ||
            (! validNumber(userProfile.getUserContact().getPrimaryPhone2())) ||
            (! validNumber(userProfile.getUserContact().getPrimaryPhone3())) ||
            (! validNumber(userProfile.getUserContact().getPrimaryPhone4()))) {
            return true;       
        }
                
        return false;
    }
    
    public static boolean invalidSecondaryPhoneFormat(UserProfileInformation userProfile)
    {
        if ((! validNumber(userProfile.getUserContact().getSecondaryPhone1())) ||
            (! validNumber(userProfile.getUserContact().getSecondaryPhone2())) ||
            (! validNumber(userProfile.getUserContact().getSecondaryPhone3())) ||
            (! validNumber(userProfile.getUserContact().getSecondaryPhone4()))) {
            return true;       
        }
                
        return false;
    }
    
    public static boolean invalidFaxNumberFormat(UserProfileInformation userProfile)
    {
        if ((! validNumber(userProfile.getUserContact().getFaxNumber1()) ||
            (! validNumber(userProfile.getUserContact().getFaxNumber2())) ||
            (! validNumber(userProfile.getUserContact().getFaxNumber3())))) {
            return true;       
        }
                
        return false;
    }

    public static String verifyUserAddressInfo(UserProfileInformation userProfile)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validAddressString(userProfile.getUserContact().getAddressLine1()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_ADDRESS_LINE1, invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! WebUtils.validAddressString(userProfile.getUserContact().getAddressLine2()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_ADDRESS_LINE2, invalidCharFieldCount, invalidCharFields);       
        }
                
        return invalidCharFields;
    }
    
    public static String verifyUserCityInfo(UserProfileInformation userProfile)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validNameString(userProfile.getUserContact().getCity()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CITY, invalidCharFieldCount, invalidCharFields);       
        }
        
          
        return invalidCharFields;
    }
    
    public static boolean invalidZipLength(UserProfileInformation userProfile) 
    {     
        if (userProfile.getUserContact().getZipCode1().trim().length() > 0 
            || userProfile.getUserContact().getZipCode2().trim().length() > 0) {
                
            if(userProfile.getUserContact().getZipCode1().trim().length() < 5 ) {
            //    || (userProfile.getUserContact().getZipCode2().trim().length() != 0 
            //    && userProfile.getUserContact().getZipCode2().trim().length() < 5 )) {
                        
                return true;
            }
            
        }
        return false;
    }
    
    public static boolean invalidZipFormat(UserProfileInformation userProfile) 
    {     
        if ( (! validNumber(userProfile.getUserContact().getZipCode1())) ||
             (! validNumber(userProfile.getUserContact().getZipCode2())) ) {                    
            return true;
        }
        return false;
    }
    
     public static boolean invalidPrimaryPhoneLength(UserProfileInformation userProfile) 
     {         
        if (userProfile.getUserContact().getPrimaryPhone1().trim().length() > 0 ||
            userProfile.getUserContact().getPrimaryPhone2().trim().length() > 0 ||
            userProfile.getUserContact().getPrimaryPhone3().trim().length() > 0) {
                        
            if (userProfile.getUserContact().getPrimaryPhone1().trim().length() < 3 ||
                userProfile.getUserContact().getPrimaryPhone2().trim().length() < 3 ||
                userProfile.getUserContact().getPrimaryPhone3().trim().length() < 4) { 
                return true;
            }
        }

        if (userProfile.getUserContact().getPrimaryPhone1().trim().length() == 0 && 
            userProfile.getUserContact().getPrimaryPhone2().trim().length() == 0 && 
            userProfile.getUserContact().getPrimaryPhone3().trim().length() == 0 && 
            userProfile.getUserContact().getPrimaryPhone4().trim().length() > 0) { 
            return true;
        }
        
        return false;
     }
     
     public static boolean invalidSecondaryPhoneLength(UserProfileInformation userProfile) 
     {         
        if (userProfile.getUserContact().getSecondaryPhone1().trim().length() > 0 ||
            userProfile.getUserContact().getSecondaryPhone2().trim().length() > 0 ||
            userProfile.getUserContact().getSecondaryPhone3().trim().length() > 0) {
                            
                if (userProfile.getUserContact().getSecondaryPhone1().trim().length() < 3 ||
                    userProfile.getUserContact().getSecondaryPhone2().trim().length() < 3 ||
                    userProfile.getUserContact().getSecondaryPhone3().trim().length() < 4) { 
                return true;
            }
        }

        if (userProfile.getUserContact().getSecondaryPhone1().trim().length() == 0 && 
            userProfile.getUserContact().getSecondaryPhone2().trim().length() == 0 && 
            userProfile.getUserContact().getSecondaryPhone3().trim().length() == 0 && 
            userProfile.getUserContact().getSecondaryPhone4().trim().length() > 0) { 
            return true;
        }
        
        return false;
     }
     
     public static boolean invalidFaxNumberLength(UserProfileInformation userProfile) 
     {         
        if (userProfile.getUserContact().getFaxNumber1().trim().length() > 0 ||
            userProfile.getUserContact().getFaxNumber2().trim().length() > 0 ||
            userProfile.getUserContact().getFaxNumber3().trim().length() > 0) {
                    
            if (userProfile.getUserContact().getFaxNumber1().trim().length() < 3 ||
                userProfile.getUserContact().getFaxNumber2().trim().length() < 3 ||
                userProfile.getUserContact().getFaxNumber3().trim().length() < 4 ) {                           
                return true;
            } 
        }
        
        return false;
     }

    public static String verifyUserInfo(UserProfileInformation userProfile)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validNameString(userProfile.getFirstName()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_FIRST_NAME, invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! WebUtils.validNameString(userProfile.getMiddleName()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MIDDLE_NAME, invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! WebUtils.validNameString(userProfile.getLastName()) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_LAST_NAME, invalidCharFieldCount, invalidCharFields);       
        }
            
        return invalidCharFields;
    }
    
    public static String verifyFindUserInfo(String firstName, String lastName)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validNameString(firstName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_FIRST_NAME, invalidCharFieldCount, invalidCharFields);       
        }
        
        if (! WebUtils.validNameString(lastName) ) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_LAST_NAME, invalidCharFieldCount, invalidCharFields);       
        }

        return invalidCharFields;
    }
    
    public static String buildErrorString(String field, int count, String str)
    {
        String result = str;
        
        if (count == 1) {
            result += field;
        }
        else {
            result += (", " + field);            
        }        
        return result;
    }
    
    public static boolean validNumber(String str)
    {
        str = str.trim();
        char[] characters = str.toCharArray();
        
        for (int i=0 ; i<characters.length ; i++) {
            char character = characters[i];
            if (!((character >= 48) && (character <= 57))) {
                return false;
            }
        } 
        return true;
    }
    
  
    
   public static String verifyPasswordInfo(PasswordInformation passwordInfo, 
                                           boolean isLoginUser)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;
        
        /*if (isLoginUser) {
            
            if (! (validPasswordString(CTBConstants.ALPHA_ARRAY, passwordInfo.getOldPassword()) 
                && validPasswordString(CTBConstants.NUM_ARRAY, passwordInfo.getOldPassword()))) {
                    invalidCharFieldCount += 1;            
                    invalidCharFields = buildErrorString(Message.FIELD_OLD_PASSWORD, invalidCharFieldCount, invalidCharFields);       
            }
            
        }*/
     
      /* Changed/Added for DEx Phase 2 on 30-Apr-09 by TCS -- Start*/
        
            if (! (validPasswordString(CTBConstants.ALPHA_ARRAY, passwordInfo.getNewPassword()) 
                && validPasswordString(CTBConstants.NUM_ARRAY, passwordInfo.getNewPassword())
                && !validPasswordString(CTBConstants.DEX_SPECIAL_CHAR_ARRAY, passwordInfo.getNewPassword())
                && passwordInfo.getNewPassword().length() >= 8)) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_NEW_PASSWORD, invalidCharFieldCount, invalidCharFields);
            }
        
            if (! (validPasswordString(CTBConstants.ALPHA_ARRAY, passwordInfo.getConfirmPassword()) 
                && validPasswordString(CTBConstants.NUM_ARRAY, passwordInfo.getConfirmPassword())
                && !validPasswordString(CTBConstants.DEX_SPECIAL_CHAR_ARRAY, passwordInfo.getConfirmPassword())
                && passwordInfo.getConfirmPassword().length() >= 8)) {
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CONFIRM_PASSWORD, invalidCharFieldCount, invalidCharFields);       
            }
      
        /* Changed/Added for DEx Phase 2 on 30-Apr-09 by TCS -- End*/
         
     return invalidCharFields;
    
    }
   
   private static boolean validPasswordString(String charArray, String password) 
   {
           
        boolean verified = false;
        int j = 0;
        
        while (!verified && (j < password.length())) {
            if(charArray.indexOf(String.valueOf(password.charAt(j))) != -1) {
                    verified = true;
            }
            j++;
        }
    
        if(password.indexOf(" ") != -1) {
                verified = true;
            }    
       
        return verified;
    } 
    
    public static boolean isRequiredfieldMissing(ManageUserForm form, List selectedOrgNodes, boolean isLoginUser, String userName)
    {
        UserProfileInformation userProfile = form.getUserProfile();
        
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
        String firstName = userProfile.getFirstName().trim();
        if ( firstName.length() == 0 ) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_FIRST_NAME, requiredFieldCount, requiredFields);       
        }
                
        String lastName = userProfile.getLastName().trim();
        if ( lastName.length() == 0 ) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LAST_NAME, requiredFieldCount, requiredFields);       
        }
        
        //Time Zone is mandetory
        String timeZone = userProfile.getTimeZone().trim();
        if ( timeZone== null || timeZone.length() == 0 ) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_TIME_ZONE, requiredFieldCount, requiredFields);       
        }
        
        // for add user
        if(userName == null || "".equals(userName)){
            
            //Role is mandetory
            String role = userProfile.getRoleId().trim();
            if ( role==null || role.length() == 0 ) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_ROLE, requiredFieldCount, requiredFields);       
            }
        }
        
        if(!isLoginUser){							 
             if ( selectedOrgNodes.size() == 0 ) {
                requiredFieldCount += 1;      
                requiredFields = Message.buildErrorString(Message.FIELD_ORG_ASSIGNMENT, requiredFieldCount, requiredFields);       
            }   
        }
        
        if (requiredFieldCount > 0) {
            if (requiredFieldCount == 1) {
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
                form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
            }
            else {
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
                form.setMessage(Message.MISSING_REQUIRED_FIELDS, requiredFields, Message.ERROR);
            }
            return true;
        }
        return false;
    }
    
    public static boolean isInvalidUserInfo(ManageUserForm form)
    {
        UserProfileInformation userProfile = form.getUserProfile();
        
        String invalidCharFields = verifyUserInfo(userProfile);
        String invalidString = "";                        
        if (invalidCharFields.length() > 0) {
            invalidString = invalidCharFields + ("<br/>" + Message.INVALID_NAME_CHARS);
        }																									   
        String email = userProfile.getEmail(); 
        boolean validEmail = WebUtils.validEmail(email);  
       
        if (!validEmail) {
            if(invalidString!=null && invalidString.length()>0){
               invalidString += ("<br/>");
            }
            invalidString += Message.FIELD_EMAIL + ("<br/>" + Message.INVALID_EMAIL);
        } 
            
        if (invalidString!=null && invalidString.length() > 0 ) {    
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString, Message.ERROR);
            return true;
        } 
        return false;   
    }
        
    public static boolean isInvalidUserContact(ManageUserForm form)
    {
        UserProfileInformation userProfile = form.getUserProfile();
        
        // this section is for the validation of address line 1 & 2 
        String invalidCharFields = verifyUserAddressInfo(userProfile);
        String invalidString = "";                        
        if (invalidCharFields.length() > 0) {
            invalidString = invalidCharFields + ("<br/>" + Message.INVALID_ADDRESS_CHARS);
        }	
        
        // this section is for the validation of City
        invalidCharFields = verifyUserCityInfo(userProfile);
        if (invalidCharFields != null && invalidCharFields.length() > 0) {
            if(invalidString!=null && invalidString.length()>0){
               invalidString += ("<br/>");
            }
            invalidString += invalidCharFields + ("<br/>" + Message.INVALID_CITY_CHARS);
        }																									   
         
        // this section is for the validation of Zip  
        String invalidNumFields = "";
        if (isInvalidZip(userProfile)) {
            invalidNumFields += Message.FIELD_ZIP;
        }
        
        // this section is for the validation of Primary Phone 
        if (isInvalidPrimaryPhone(userProfile)){
            if(invalidNumFields!=null && invalidNumFields.length()>0) {
               invalidNumFields += ", ";
            }
            invalidNumFields += Message.FIELD_PRIMARY_PHONE;
        }
        
        if (isInvalidSecondaryPhone(userProfile)){
            if(invalidNumFields!=null && invalidNumFields.length()>0){
               invalidNumFields += ", ";
            }
            invalidNumFields += Message.FIELD_SECONDARY_PHONE;
        }
        
        if (isInvalidFaxNumber(userProfile)){
            if(invalidNumFields!=null && invalidNumFields.length()>0){
               invalidNumFields += ", ";
            }
            invalidNumFields += Message.FIELD_FAX_NUMBER;
        }
               
        if (invalidNumFields != null && invalidNumFields.length()>0 ) {
            if(invalidString!=null && invalidString.length()>0){
               invalidString += ("<br/>");
            }
            invalidString += invalidNumFields + ("<br/>" + Message.INVALID_NUMBER_FORMAT);
        }
                         
        if (invalidString!=null && invalidString.length() > 0 ) {    
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString, Message.ERROR);
            return true;
        } 
        
        return false;   
    }
    
    public static boolean isRequiredPasswordFieldMissing(ManageUserForm form, PasswordInformation passwordInfo, boolean isLoginUser) {
     
        String requiredFields = "";
        int requiredFieldCount = 0;  
         
        String oldPassword = passwordInfo.getOldPassword();
        String newPassword = passwordInfo.getNewPassword();
        String confirmPassword = passwordInfo.getConfirmPassword();
        String hintQuestion = passwordInfo.getHintQuestionId().trim();
        String hintAnswer = passwordInfo.getHintAnswer().trim();
         
        if (isLoginUser) {
            if ( oldPassword.length() == 0 ) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_OLD_PASSWORD, requiredFieldCount, requiredFields);       
            }
        }
        if (newPassword.length() == 0 ) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_NEW_PASSWORD, requiredFieldCount, requiredFields);       
        }
         
        if (confirmPassword.length() == 0) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_CONFIRM_PASSWORD, requiredFieldCount, requiredFields);       
        }
        if (isLoginUser) {
            if ( hintQuestion == null || hintQuestion.length() == 0 ) {
                    requiredFieldCount += 1;            
                    requiredFields = Message.buildErrorString(Message.FIELD_HINT_QUESTION, requiredFieldCount, requiredFields);       
            }
         
            if( hintAnswer.length() == 0) {
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_HINT_ANSWER, requiredFieldCount, requiredFields);       

            }
            
        }
                  
        
         
        if (requiredFieldCount > 0) {
            if (requiredFieldCount == 1) {
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
                form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
            }
            else {
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
                form.setMessage(Message.MISSING_REQUIRED_FIELDS, requiredFields, Message.ERROR);
            }
            return true;
        } 
                                                                                                 
        return false;
    }
     
        
    public static boolean verifyUserPassword(ManageUserForm form, String selectedUserName,
                                             PasswordInformation passwordInfo, 
                                             UserManagement userManagement,
                                             boolean isLoginUser) {
        
        if( isRequiredPasswordFieldMissing(form, passwordInfo, isLoginUser) ) {
            return false;
        }
        
        if( isInvalidPasswordInformation(form, passwordInfo, isLoginUser) ) {
            return false;
        }
        
        if (isLoginUser) {            
//            if ( isOldNewPasswordSame (form, passwordInfo) ) {
//                return false;
//            }
        }
       
        if ( isNewAndConfirmPasswordDifferent(form, passwordInfo) ) {
            return false;
        } 
        
        
        return true; 
    }

    public static boolean isOldNewPasswordSame (ManageUserForm form, PasswordInformation passwordInfo) {
    
        String oldPassword = passwordInfo.getOldPassword();
        String newPassword = passwordInfo.getNewPassword();
        String confirmPassword = passwordInfo.getConfirmPassword();
        
        if (oldPassword.equals(newPassword) || oldPassword.equals(confirmPassword)) {
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.OLD_NEW_PASSWORD_MATCH, Message.ERROR);
            return true;
        }
        
        return false;
    } 
        
    public static boolean isInvalidPasswordInformation (ManageUserForm form, 
                                                        PasswordInformation passwordInfo, 
                                                        boolean isLoginUser) {
       
        String invalidCharFields = verifyPasswordInfo(passwordInfo, isLoginUser);
        String invalidString = "";                        
         /* Changed/Added for DEx Phase 2 on 30-Apr-09 by TCS -- Start*/
        if (invalidCharFields.length() > 0) {
       
            invalidString = invalidCharFields + ("<br/>" + Message.INVALID_DEX_PASSWORD);
          
        }
         /* Changed/Added for DEx Phase 2 on 30-Apr-09 by TCS -- Start*/
        
        if (invalidString!=null && invalidString.length() > 0 ) {    
            form.setMessage(Message.INVALID_CHARS_TITLE, invalidString, Message.ERROR);
            return true;
        } 
        
        return false;
    }
        
    public static boolean isNewAndConfirmPasswordDifferent (ManageUserForm form, PasswordInformation passwordInfo) {
    
        String newPassword = passwordInfo.getNewPassword().trim();
        String confirmPassword = passwordInfo.getConfirmPassword().trim();
    
        if (! newPassword.equals(confirmPassword)) {
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.PASSWORD_MISMATCH, Message.ERROR);
            return true;
        }
    
        return false;
    }
        
    public static boolean isOldPasswordWrong(ManageUserForm form, String selectedUserName, PasswordInformation passwordInfo, UserManagement userManagement) {
                                                     
        String oldPassword = passwordInfo.getOldPassword().trim();
    
        try {
            //if (! userManagement.verifyOldPassword(userId, oldPassword)) {
              //  form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.WRONG_PASSWORD, Message.ERROR);
                return true;
            //}    
        } 
        catch (Exception e) {}
        return false;
    }
        
    public static boolean isRepeatedPassword (ManageUserForm form, String selectedUserName, PasswordInformation passwordInfo, UserManagement userManagement) {
    
        boolean verified = false;
        String newPassword = passwordInfo.getNewPassword().trim();
/*    
        try {
            //verified = userManagement.isRepeatedPassword(userId, newPassword);
        } 
        catch (Exception e) {}*/
    
        if(verified) {
            form.setMessage(Message.CHANGE_PASSWORD_TITLE, Message.REPEATED_PASSWORD, Message.ERROR);
            return true;
        }
    
        return false;
    }        
        
    public static boolean verifyUserInformation(ManageUserForm form, List selectedOrgNodes, boolean isLoginUser, String userName, User user)	    
    {                    
        
        if ( isRequiredfieldMissing(form, selectedOrgNodes, isLoginUser, userName) ) {
            return false;
        }
                
        if (isInvalidUserInfo(form)){
            return false;
        }           									   
        
        if (user.getRole().getRoleId().intValue() 
                == (new Integer(form.getUserProfile().getRoleId()).intValue()) &&
            user.getUserId().intValue() != form.getUserProfile().getUserId().intValue()) {
            if ( !verifyAdminCreationPermission
                    (form, user.getOrganizationNodes(), selectedOrgNodes) ) {
                return false;
            }        
        }
          
        if (isInvalidUserContact(form)){
            return false;
        }
        return true;
    }
        
    public static boolean isInvalidZip(UserProfileInformation userProfile) 
    {
        boolean invalidZiplength = invalidZipLength(userProfile);
        if (invalidZiplength) {
            return true;
        }
        
        boolean invalidZipFormat  = invalidZipFormat(userProfile);
        if (invalidZipFormat) {
            return true;
        }
        
        return false;
    } 
    
    public static boolean isInvalidPrimaryPhone(UserProfileInformation userProfile) 
    {
        boolean invalidPhonelength = invalidPrimaryPhoneLength(userProfile);
        if (invalidPhonelength) {
            return true;
        }
        
        boolean invalidPhoneFormat  = invalidPrimaryPhoneFormat(userProfile);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isInvalidSecondaryPhone(UserProfileInformation userProfile) 
    {
        boolean invalidPhonelength = invalidSecondaryPhoneLength(userProfile);
        if (invalidPhonelength) {
            return true;
        }
        
        boolean invalidPhoneFormat  = invalidSecondaryPhoneFormat(userProfile);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isInvalidFaxNumber(UserProfileInformation userProfile) 
    {
        boolean invalidPhonelength = invalidFaxNumberLength(userProfile);
        if (invalidPhonelength) {
            return true;
        }
        
        boolean invalidPhoneFormat  = invalidFaxNumberFormat(userProfile);
        if (invalidPhoneFormat) {
            return true;
        }
        
        return false;
    }
    
    public static boolean verifyAdminCreationPermission (ManageUserForm form, Node []loginUserNodes, List selectedUserNodes)
    {
        String requiredFields = "";
        for (int i = 0; i < loginUserNodes.length; i++) {
            
            Integer loginUserNodeId = loginUserNodes[i].getOrgNodeId();
            
            for (int j = 0; j < selectedUserNodes.size(); j++) {
                
                PathNode pathNode = (PathNode)selectedUserNodes.get(j);
                Integer selectedUserNodeId = pathNode.getId();
                
                if (loginUserNodeId.intValue() == selectedUserNodeId.intValue()) {
                    requiredFields += ("<br/>" + Message.ADMIN_CREATION_ERROR);
                    form.setMessage(Message.ADMIN_CREATION_TITLE, requiredFields, Message.ERROR);
                    return false;
                }
                
            }
           
        }
        return true;
    }
    

    /* START- Added for Deferred Defect 62758  
	 * User can't be associated with different organizations across different customers
	*/
	public static boolean verifyUserCreationPermission (ManageUserForm form, List selectedOrgNodes) {

		String requiredFields = "";
		for (int i = 0; i < selectedOrgNodes.size(); i++) {

			PathNode selectedPathNode = (PathNode)selectedOrgNodes.get(i);

			for (int j = 0; j < selectedOrgNodes.size() && i!=j; j++) {
				
				PathNode pathNode = (PathNode)selectedOrgNodes.get(j);
				//System.out.println("pathNodeList.getCustomerId().intValue()" + selectedPathNode.getCustomerId().intValue());
				//System.out.println("pathNode.getCustomerId().intValue()" + pathNode.getCustomerId().intValue());
				if (pathNode != null ) {

					if ( selectedPathNode.getCustomerId().intValue() != pathNode.getCustomerId().intValue() ) {

						requiredFields += ("<br/>" + Message.USER_CREATION_ERROR);
						form.setMessage(Message.USER_CREATION_TITLE, requiredFields, Message.ERROR);
						return false;
					}
				}
			}
		}
		return true;
	}
	//END- Added for Deferred Defect 62758  
} 
