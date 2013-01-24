package utils; 

import dto.LASLicenseNode;
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
    
    public static boolean verifyLASLicenseInformation(ManageCustomerForm form)	    
    {                    
        
        if ( isLASRequiredfieldMissing(form) ) {
            
            return false;
            
        }
        if (isLASInvalidLicInfo(form)){
            
            return false;
            
        }
       
        return true;
    }
    
    public static boolean isLASInvalidLicInfo(ManageCustomerForm form)
    {
          
        String invalidCharFields = verifyLASLicInfo(form);
        String invalidString = "";                        
        if ( invalidCharFields.length() > 0 ) {
            
            invalidString =invalidCharFields + ("<br/>" + Message.INVALID_LICENSE_VALUE )  ;
            
        }																									   
                   
        if ( invalidString != null && invalidString.length() > 0 ) {    
            
            form.setMessage(Message.INVALID_LICENSE_TEXT, invalidString, Message.ERROR);
            return true;
        
        }
        boolean purchaseDate = false;
        boolean expiryDate = false;
        int validateResult = DateUtils.validateDateString(form.getLASLicenseNode().getPurchaseDate());
        if (validateResult != DateUtils.DATE_VALID)
        	purchaseDate = true;
        validateResult = DateUtils.validateDateString(form.getLASLicenseNode().getExpiryDate());
        if (validateResult != DateUtils.DATE_VALID)
        	expiryDate = true;
        if (purchaseDate || expiryDate)
        {
        	form.setMessage(Message.INVALID_LICENSE_DATE_FORMAT, invalidString, Message.ERROR);
            return true;
        }
        return false;   
    }
  
    public static String verifyLASLicInfo(ManageCustomerForm form)
    {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validNameStringForLic(form.getLASLicenseNode().getLicenseQuantity())) {
        
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_LICENSE_QUANTITY, invalidCharFieldCount, invalidCharFields);       
        
        }
        
        return invalidCharFields;
    }
    
    public static boolean isLASRequiredfieldMissing(ManageCustomerForm form )
    {
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
        String licenseQuantity = form.getLASLicenseNode().getLicenseQuantity();
        if ( "".equals(licenseQuantity.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_QUANTITY, requiredFieldCount, requiredFields);       
        }
        String PO = form.getLASLicenseNode().getPurchaseOrder();
        if ( "".equals(PO.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_PO, requiredFieldCount, requiredFields);       
        }
        String PDate = form.getLASLicenseNode().getPurchaseDate();
        if ( "".equals(PDate.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_PURCHASE_DATE, requiredFieldCount, requiredFields);       
        }
        String ExpDate = form.getLASLicenseNode().getExpiryDate();
        if ( "".equals(ExpDate.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_EXPIRY_DATE, requiredFieldCount, requiredFields);       
        }

        if (requiredFieldCount > 0) {
            
            requiredFields += ("<br/>" + Message.REQUIRED_LICENSE_TEXT);
            form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
            
            return true;
        }
        return false;
    }
    
    public static boolean  verifyLASLicenseEditInformation (ManageCustomerForm form, LASLicenseNode node )
    {
    	if (isLASRequiredEditFieldMissing(form, node)) {
            
            return false;
            
        }
        if (isLASInvalidLicInfoEdit(form, node)){
            
            return false;
            
        }
        
        return true;
    }
    
    public static boolean isLASRequiredEditFieldMissing(ManageCustomerForm form, LASLicenseNode node)
    {
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
        String licenseQuantity = node.getLicenseQuantity();
        if ( "".equals(licenseQuantity.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_AVALIABLE, requiredFieldCount, requiredFields);       
        }
        
        String ExpDate = node.getExpiryDate();
        if ( "".equals(ExpDate.trim())) {
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_LICENSE_EXPIRY_DATE, requiredFieldCount, requiredFields);       
        }

        if (requiredFieldCount > 0) {
            
            requiredFields += ("<br/>" + Message.REQUIRED_LICENSE_TEXT);
            form.setMessage(Message.MISSING_REQUIRED_FIELD, requiredFields, Message.ERROR);
            
            return true;
        }
        return false;
    }
    
    public static boolean isLASInvalidLicInfoEdit(ManageCustomerForm form, LASLicenseNode node)
    {
          
        //String invalidCharFields = verifyLASLicInfo(form);
        String invalidString = "";                        
//        if ( invalidCharFields.length() > 0 ) {
//            
//            invalidString =invalidCharFields + ("<br/>" + Message.INVALID_LICENSE_VALUE )  ;
//            
//        }																									   
//                   
//        if ( invalidString != null && invalidString.length() > 0 ) {    
//            
//            form.setMessage(Message.INVALID_LICENSE_TEXT, invalidString, Message.ERROR);
//            return true;
//        
//        }
        boolean expiryDate = false;
        int validateResult = DateUtils.validateDateString(node.getExpiryDate());
        if (validateResult != DateUtils.DATE_VALID)
        	expiryDate = true;
        
        if (expiryDate)
        {
        	form.setMessage(Message.INVALID_LICENSE_DATE_FORMAT, invalidString, Message.ERROR);
            return true;
        }
        return false;   
    }
    
    
} 
