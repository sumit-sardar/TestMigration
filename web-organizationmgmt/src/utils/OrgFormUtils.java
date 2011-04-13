package utils; 

import com.ctb.bean.testAdmin.Role;
import com.ctb.control.db.Roles;
import dto.Message;
import java.sql.SQLException;
import manageOrganization.ManageOrganizationController.ManageOrganizationForm;


public class OrgFormUtils 
{ 

    
     public static boolean verifyOrgInformation(ManageOrganizationForm form)	    
    {                    
        
        if ( isRequiredfieldMissing(form) ) {
            
            return false;
            
        }
                
        if (isInvalidOrgInfo(form)){
            
            return false;
            
        }           									   
        
        return true;
    }
            
    public static String verifyOrgInfo(ManageOrganizationForm form)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validNameString(form.getSelectedOrgName()) ) {
        
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_ORG_NAME, invalidCharFieldCount, invalidCharFields);       
        
        }
        
        if ( !WebUtils.validNameString(form.getSelectedOrgNodeCode()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_ORGCODE_NAME, invalidCharFieldCount, invalidCharFields);       
        
        }
        //START- Changed For LASLINK Product
        if(form.getSelectedOrgMdrNumber() != null){
        if ( !WebUtils.validNameString(form.getSelectedOrgMdrNumber()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MDRNUMBER, invalidCharFieldCount, invalidCharFields);       
        
        }
        }
        //END- Changed For LASLINK Product
            
        return invalidCharFields;
    }
    
    
    
    public static String buildErrorString(String field, int count, String str)
    {
        String result = str;
        
        if ( count == 1 ) {
            
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
            if ( !((character >= 48) && (character <= 57)) ) {
                
                return false;
                
            }
        } 
        return true;
    }
    
  
    
    public static boolean isRequiredfieldMissing(ManageOrganizationForm form )
    {
        //Organization orgProfile = form.getOrganizationDetail();
        
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
        String orgName = form.getSelectedOrgName().trim();
        if ( orgName.length() == 0 ) {
            
            requiredFieldCount += 1;    
            form.setSelectedOrgName("");       
            requiredFields = Message.buildErrorString(Message.FIELD_ORG_NAME, requiredFieldCount, requiredFields);       
        
        }
        //START- Changed For LASLINK Product
        if(form.getSelectedOrgMdrNumber() != null){
        String mDRNumber = form.getSelectedOrgMdrNumber().trim();
        if ( mDRNumber == null || mDRNumber.length() == 0 ) {
                
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_MDRNUMBER, requiredFieldCount, requiredFields);       
        }
        }
        //END- Changed For LASLINK Product
        String orgType = form.getSelectedOrgNodeTypeId();
        if ( "".equals(orgType) || orgType.length() == 0 ) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LEVEL_NAME, requiredFieldCount, requiredFields);       
        }
                
        String parentOrgName = form.getSelectedOrgNodeName().trim();
        if ( parentOrgName.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_PARENT_NAME, requiredFieldCount, requiredFields);       
        
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
    
    public static boolean isInvalidOrgInfo(ManageOrganizationForm form)
    {
                
        String invalidCharFields = verifyOrgInfo(form);
        String invalidString = "";                        
        if ( invalidCharFields.length() > 0 ) {
            
            invalidString = invalidCharFields + ("<br/>" + Message.INVALID_NAME_CHARS_ORG);
            
        }																									   
                   
        if ( invalidString != null && invalidString.length() > 0 ) {    
            
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString, Message.ERROR);
            return true;
        
        } 
        return false;   
    }
  
  
   public static String getLoginUserRole(String userName, Roles roles ,
                                         ManageOrganizationForm form) {
                                            
    String userRole = null;
     try{
        
        Role role = roles.getActiveRoleForUser(userName);
        userRole = role.getRoleName();
     
     } catch (SQLException e) {
     
        e.printStackTrace();
        String msg = MessageResourceBundle.getMessage(e.getMessage());
        form.setMessage(Message.MANAGE_ORG_ERROR, msg, Message.ERROR);  
     }
     
     return userRole;
   }      
   
    
} 
