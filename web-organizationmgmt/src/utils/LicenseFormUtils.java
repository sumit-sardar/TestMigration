package utils; 

import dto.Message;
import manageCustomer.ManageCustomerController.ManageCustomerForm;

public class LicenseFormUtils 
{ 
     public static boolean verifyLicenseInformation(ManageCustomerForm form)	    
    {                    
        
        if ( isRequiredfieldMissing(form) ) {
            
            return false;
            
        }
         if (isInvalidLicInfo(form)){
            
            return false;
            
        }
       
        return true;
    }
    
    
     public static boolean isRequiredfieldMissing(ManageCustomerForm form )
    {
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
       
        
        String avaliableLicense = form.getLicenseNode().getAvailable();
        if ( "".equals(avaliableLicense.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_AVALIABLE, requiredFieldCount, requiredFields);       
        }
                       
        if (requiredFieldCount > 0) {
            
            requiredFields += ("<br/>" + Message.REQUIRED_LICENSE_TEXT);
            form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
            
            return true;
        }
        return false;
    }
    
     public static boolean isInvalidLicInfo(ManageCustomerForm form)
    {
                
        String invalidCharFields = verifyLicInfo(form);
        String invalidString = "";                        
        if ( invalidCharFields.length() > 0 ) {
            
            invalidString =invalidCharFields + ("<br/>" + Message.INVALID_LICENSE_VALUE )  ;
            
        }																									   
                   
        if ( invalidString != null && invalidString.length() > 0 ) {    
            
            form.setMessage(Message.INVALID_LICENSE_TEXT, invalidString, Message.ERROR);
            return true;
        
        } 
        return false;   
    }
  
    public static String verifyLicInfo(ManageCustomerForm form)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validNameStringForLic(form.getLicenseNode().getAvailable())) {
        
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_LICENSE_AVALIABLE,
                    invalidCharFieldCount, invalidCharFields);       
        
        }
        
        if (form.getLicenseNode().getConsumed() != null) {
            
            if ( !WebUtils.validNameStringForLic(form.getLicenseNode().getConsumed())) {
                
                invalidCharFieldCount += 1;            
                invalidCharFields = buildErrorString(Message.FIELD_LICENSE_CONSUME,
                        invalidCharFieldCount, invalidCharFields);       
            
            }
        }
            
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
    
    
    
} 
