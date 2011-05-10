package utils; 

import dto.CustomerContactInformation;
import dto.CustomerProfileInformation;
import dto.Level;
import dto.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import manageCustomer.ManageCustomerController.ManageCustomerForm;

public class CustomerFormUtils 
{ 
    /**
     * verify customer information, checks for invalid char
     * @param customerProfile CustomerProfileInformation to be modified.
     * @return String
     *
     */
    public static String verifyFindCustomerInfo (CustomerProfileInformation customerProfile) {
        
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validCustomerNameString(customerProfile.getName()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CUSTOMER_NAME, 
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }
        
        if ( !WebUtils.validCustomerNameString(customerProfile.getCode()) ) {
        
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CUSTOMER_ID, 
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }

        return invalidCharFields;
    }
   
    /**
     * built error string for validation message
     * @param field String.
     * @param count int.
     * @param str String.
     * @return String
     */ 
    public static String buildErrorString(String field, int count, String str) {
        String result = str;
        
        if ( count == 1 ) {
            
            result += field;
        
        }
        else {
            
            result += (", " + field);            
        
        }        
        return result;
    }
    
    /**
     *verify customer info for validation message
     * @param form ManageCustomerForm.
     * @param customerId Integer.
     * @return boolean
     *
     */ 
     public static boolean verifyCustomerInformation(ManageCustomerForm form, 
                                                     Integer customerId){                    
        
        if ( isRequiredfieldMissing(form,customerId) ) {
        
            return false;
        
        }
        //LLO-099 MDR Validation
        if ( verifyMinLength(form) ) {
            
            return false;
        
        }
                
        if ( isInvalidUserInfo(form) ){
        
            return false;
        
        }           									   
                          
        if ( isInvalidUserBillingContact(form) ){
        
            return false;
        
        }
        
        if ( isInvalidUserMailingContact(form) ){
        
            return false;
        
        }
        return true;
    }
    
    /**
     * check for required field
     * @param form ManageCustomerForm.
     * @param customerId Integer.
     * @return String
     */
     public static boolean isRequiredfieldMissing(ManageCustomerForm form,
                                                  Integer customerId) {
                                                    
        CustomerProfileInformation customerProfile = form.getCustomerProfile();
        
        // check for required fields
        String requiredFields = "";
        int requiredFieldCount = 0;
        
        // for add customer
        if( customerId == null || customerId.intValue() == 0 ){
            
            //type is mandetory
            String type = customerProfile.getCustomerTypeId().trim();
            if ( type== null || type.length() == 0 ) {
                
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_CUSTOMER_TYPE,
                                         requiredFieldCount, requiredFields);       
            }
        }
        
        String customerName = customerProfile.getName().trim();
        if ( customerName.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_CUSTOMER_NAME,
                                     requiredFieldCount, requiredFields);       
        }
                
            
              
            //State is mandetory
        String state = customerProfile.getStateId().trim();
        if ( state == null || state.length() == 0 ) {
                
                requiredFieldCount += 1;            
                requiredFields = Message.buildErrorString(Message.FIELD_STATE, 
                                        requiredFieldCount, requiredFields);       
        }
        //START- Changed For LASLINK Product
        if(customerProfile.getCustomerTypeId().equals("LasLink Customer") ||  customerProfile.getCustomerType().equals("LasLink Customer")) {
	        String mDRNumber = customerProfile.getMdrNumber().trim();
	        if ( mDRNumber == null || mDRNumber.length() == 0 ) {
	                
	                requiredFieldCount += 1;            
	                requiredFields = Message.buildErrorString(Message.FIELD_MDRNUMBER, 
	                                        requiredFieldCount, requiredFields);       
	        }
        }
        //END- Changed For LASLINK Product
        String ctbContact = customerProfile.getCtbContact().trim();
        if ( ctbContact.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_CTB_CONTACT, 
                                    requiredFieldCount, requiredFields);       
        }
        
        String ctbContactEmail = customerProfile.getCtbContactEmail().trim();
        if ( ctbContactEmail.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_CTB_CONTACT_EMAIL, 
                                    requiredFieldCount, requiredFields);       
        
        }
        
        String customerContact = customerProfile.getCustomerContact().trim();
        if ( customerContact.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(Message.FIELD_CUSTOMER_CONTACT,
                                     requiredFieldCount, requiredFields);       
        }
        
        String customerContactEmail = customerProfile.
                                            getCustomerContactEmail().trim();
        if ( customerContactEmail.length() == 0 ) {
            
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(
                                        Message.FIELD_CUSTOMER_CONTACT_EMAIL,
                                        requiredFieldCount, requiredFields);       
        }
        
        String customerContactPhone1 = customerProfile.getConatctPhone1().trim();
        String customerContactPhone2 = customerProfile.getConatctPhone2().trim();
        String customerContactPhone3 = customerProfile.getConatctPhone3().trim();
        String customerContactPhone4 = customerProfile.getConatctPhone4().trim();
        
        if ( customerContactPhone1.length() == 0 
              && customerContactPhone2.length() == 0
              && customerContactPhone3.length() == 0
              && customerContactPhone4.length() == 0) {
                
            requiredFieldCount += 1;            
            requiredFields = Message.buildErrorString(
                                    Message.FIELD_CUSTOMER_CONTACT_PHONE,
                                    requiredFieldCount, requiredFields);       
       
        }
                     
        if ( requiredFieldCount > 0 ) {
            if ( requiredFieldCount == 1 ) {
                
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT);
                form.setMessage(Message.MISSING_REQUIRED_FIELD, 
                                requiredFields, Message.ERROR);
            
            } else {
              
                requiredFields += ("<br/>" + Message.REQUIRED_TEXT_MULTIPLE);
                form.setMessage(Message.MISSING_REQUIRED_FIELDS, 
                                requiredFields, Message.ERROR);
            
            }
            return true;
        }
        return false;
    }
     
     /**
	  * LLO-099 MDR Validation
      * check for minlength
      * @param form ManageCustomerForm.
      * @param customerId Integer.
      * @return String
      */
      public static boolean verifyMinLength(ManageCustomerForm form) {
    	  CustomerProfileInformation customerProfile = form.getCustomerProfile();
    	  String invalidCharFields = "";
    	  int invalidCharFieldCount = 0;
    	  String invalidString = "";      
    			
    	  	if(customerProfile.getCustomerTypeId().equals("LasLink Customer") || customerProfile.getCustomerType().equals("LasLink Customer")) {
    		        //START- Changed For LASLINK Product
    		            if ((customerProfile.getMdrNumber().length()> 0 ) && customerProfile.getMdrNumber().length() < (new Integer(8)).intValue()) {
    			            
    			            invalidCharFieldCount += 1;            
    			            invalidCharFields = buildErrorString(Message.FIELD_MDRNUMBER, 
    			                                                 invalidCharFieldCount,
    			                                                 invalidCharFields);       
    			        
    			        }
    		      
    		        //END- Changed For LASLINK Product
    			
    		        if ( invalidCharFields.length() > 0) {
    		            
    		            invalidString = invalidCharFields + ("<br/>" 
    		                            + Message.INVALID__MINLENGTH_FORMAT);
    		            
    		        }	
    		        if ( invalidString != null && invalidString.length() > 0 ) {
    		            
    		            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString,
    		                                    Message.ERROR);
    		            return true;
    		            
    		        }
    	  	}
    		        return false;   
    		}
    
    /**
     * check for valid field
     * @param form ManageCustomerForm.
     * @returns boolean
     */
     public static boolean isInvalidUserInfo(ManageCustomerForm form){
        CustomerProfileInformation customerProfile = form.getCustomerProfile();
        
        String invalidCharFields = verifyCustomerInfo(customerProfile);
        String invalidNumberField = veryfyCustomerPhone(customerProfile);
        String invalidString = "";                        
        if ( invalidCharFields.length() > 0) {
            
            invalidString = invalidCharFields + ("<br/>" 
                            + Message.INVALID_NAME_CHARS);
            
        }	
        if( invalidNumberField != null && invalidNumberField.length() > 0 ){
            
            if( invalidString != null && invalidString.length() > 0 ){
            
                invalidString += ("<br/>");
            
            }
            invalidString += invalidNumberField ;
            invalidNumberField += ", ";
        }																								   
        
        String ctbEmail = customerProfile.getCtbContactEmail();
        String customerEmail = customerProfile.getCustomerContactEmail();
         
        boolean validCTBEmail = WebUtils.validEmail(ctbEmail);  
        boolean validCustomerEmail = WebUtils.validEmail(customerEmail);
        
        if ( !validCTBEmail || !validCustomerEmail ) {
            if( invalidString != null && invalidString.length() > 0 ){
                
                invalidString += ("<br/>");
               
            }
            if ( !validCTBEmail ) {
                
                 invalidString += Message.FIELD_CTB_CONTACT_EMAIL;
                 
            }
            if ( !validCTBEmail && !validCustomerEmail ) {
                
                invalidString += ", ";
                
            }
            
            if ( !validCustomerEmail ) {
                
                invalidString += Message.FIELD_CUSTOMER_CONTACT_EMAIL;
                
            }
            
            invalidString += ("<br/>" + Message.INVALID_EMAIL);
        }
   
            
        if ( invalidString != null && invalidString.length() > 0 ) {
            
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString,
                                    Message.ERROR);
            return true;
            
        } 
        return false;   
    }
    
    /**
     * verify customer info
     * @param customerProfile CustomerProfileInformation.
     * @return String
     */
     public static String verifyCustomerInfo(
                          CustomerProfileInformation customerProfile){
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validCustomerNameString(customerProfile.getName()) ) {
           
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CUSTOMER_NAME, 
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }
        
        if ( !WebUtils.validCustomerNameString(customerProfile.getCode()) ) {
           
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CUSTOMER_ID, 
                                                 invalidCharFieldCount,
                                                 invalidCharFields);       
        
        }
        if ( !WebUtils.validCustomerNameString(customerProfile.getCtbContact()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CTB_CONTACT, 
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }
                    
        if ( !WebUtils.validCustomerNameString(customerProfile.getCustomerContact()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_CUSTOMER_CONTACT, 
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }
                                    
        return invalidCharFields;
    }
    
    /**
     * verify customer phone info
     * @param customerProfile CustomerProfileInformation.
     * @return String
     */
    
    public static String veryfyCustomerPhone(
                                CustomerProfileInformation customerProfile){
     // this section is for the validation of  Phone 
        String invalidString = "";
        String invalidNumFields = "";   
        
        if ( isInvalidPhone(customerProfile) ){
        
            if ( invalidNumFields != null && invalidNumFields.length() > 0 ) {
        
                invalidNumFields += ", ";
        
            }
        
            invalidNumFields += Message.FIELD_CUSTOMER_CONTACT_PHONE;
        }
        
        if (invalidNumFields != null && invalidNumFields.length() > 0 ) {
            if ( invalidString != null && invalidString.length() > 0 ){
            
                invalidString += ("<br/>");
           
            }
            
            invalidString += invalidNumFields + ("<br/>" 
                            + Message.INVALID_NUMBER_FORMAT);
        }
        
       return invalidString;
    }
    
     /**
     * verify phone info
     * @param customerProfile CustomerProfileInformation.
     * @return boolean
     */
    public static boolean isInvalidPhone(
                                CustomerProfileInformation customerProfile) {
        boolean invalidPhonelength = invalidPhoneLength(customerProfile);
       
        if ( invalidPhonelength ) {
            
            return true;
        
        }
        
        boolean invalidPhoneFormat  = invalidPhoneFormat(customerProfile);
       
        if ( invalidPhoneFormat ) {
            
            return true;
        
        }
        
        return false;
    }
    
    /**
     * verify phone length
     * @param customerProfile CustomerProfileInformation.
     * @return boolean
     */ 
   public static boolean invalidPhoneLength(
                                CustomerProfileInformation customerProfile) {    
                                         
        if ( customerProfile.getConatctPhone1().trim().length() > 0 
            || customerProfile.getConatctPhone2().trim().length() > 0 
            || customerProfile.getConatctPhone3().trim().length() > 0 ) {
                        
            if ( customerProfile.getConatctPhone1().trim().length() < 3
                || customerProfile.getConatctPhone2().trim().length() < 3
                || customerProfile.getConatctPhone3().trim().length() < 4 ) { 
               
                return true;
            }
        }

        if ( customerProfile.getConatctPhone1().trim().length() == 0 
            && customerProfile.getConatctPhone2().trim().length() == 0 
            && customerProfile.getConatctPhone3().trim().length() == 0 
            && customerProfile.getConatctPhone4().trim().length() > 0 ) { 
           
            return true;
        }
        
        return false;
     } 
     
     /**
     * verify phone format
     * @param customerProfile CustomerProfileInformation.
     * @return boolean
     */ 
     public static boolean invalidPhoneFormat(
                                CustomerProfileInformation customerProfile) {
                                    
        if ( (! validNumber(customerProfile.getConatctPhone1())) 
            || (! validNumber(customerProfile.getConatctPhone2())) 
            || (! validNumber(customerProfile.getConatctPhone3())) 
            || (! validNumber(customerProfile.getConatctPhone4())) ) {
           
            return true;       
        }
                
        return false;
    }
    
     /**
     * verify number
     * @param str String.
     * @return boolean
     */ 
     
      public static boolean validNumber(String str){
        str = str.trim();
        char[] characters = str.toCharArray();
        
        for ( int i = 0 ; i < characters.length ; i++ ) {
            char character = characters[i];
            
            if ( !((character >= 48) && (character <= 57)) ) {
            
                return false;
            }
        } 
        return true;
    }
    
     /**
     * verify biiling information
     * @param form ManageCustomerForm.
     * @return boolean
     */ 
    public static boolean isInvalidUserBillingContact(ManageCustomerForm form) {
        CustomerProfileInformation customerProfile = form.getCustomerProfile();
        // this section is for the validation of address line 1 & 2 
        String invalidCharFields = verifyUserBillingAddressInfo(customerProfile);
        String invalidString = "";                        
       
        if ( invalidCharFields.length() > 0 ) {
            
            invalidString = invalidCharFields + ("<br/>" 
                            + Message.INVALID_ADDR_CHARS);
        
        }	
        
        // this section is for the validation of City
        invalidCharFields = verifyUserBillingCityInfo(customerProfile);
        if ( invalidCharFields != null && invalidCharFields.length() > 0 ) {
        
            if(invalidString!=null && invalidString.length()>0){
        
                invalidString += ("<br/>");
        
            }
            invalidString += invalidCharFields + ("<br/>" 
                             + Message.INVALID_CITY_CHARS);
        }																									   
         
        // this section is for the validation of Zip  
        String invalidNumFields = "";
        if ( isInvalidZip(customerProfile.getBillingContact()) ) {
        
            invalidNumFields += Message.FIELD_BILLING_ZIP;
        
        }
        
        if ( invalidNumFields != null && invalidNumFields.length()>0 ) {
        
            if ( invalidString!=null && invalidString.length()>0 ){
        
                invalidString += ("<br/>");
        
            }
            invalidString += invalidNumFields + ("<br/>" 
                            + Message.INVALID_NUMBER_FORMAT);
        }
                         
        if ( invalidString!=null && invalidString.length() > 0 ) {    
        
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString,
                            Message.ERROR);
            return true;
            
        } 
        
        return false;   
    }
    /**
     * verify mailing information
     * @param form ManageCustomerForm.
     * @return boolean
     */ 
    public static boolean isInvalidUserMailingContact(ManageCustomerForm form) {
        CustomerProfileInformation customerProfile = form.getCustomerProfile();
        // this section is for the validation of address line 1 & 2 
        String invalidCharFields = verifyUserMailingAddressInfo(customerProfile);
        String invalidString = "";                        
        
        if ( invalidCharFields.length() > 0 ) {
         
            invalidString = invalidCharFields + ("<br/>"
                            + Message.INVALID_ADDR_CHARS);
        
        }	
        
        // this section is for the validation of City
        invalidCharFields = verifyUserMailingCityInfo(customerProfile);
        
        if ( invalidCharFields != null && invalidCharFields.length() > 0 ) {
            
            if(invalidString!=null && invalidString.length()>0){
            
                invalidString += ("<br/>");
            
            }
            
            invalidString += invalidCharFields + ("<br/>" 
                             + Message.INVALID_CITY_CHARS);
        }																									   
         
        // this section is for the validation of Zip  
        String invalidNumFields = "";
        
        if (isInvalidZip(customerProfile.getMailingContact())) {
            
            invalidNumFields += Message.FIELD_MAILING_ZIP;
        
        }
                               
        if ( invalidNumFields != null && invalidNumFields.length() > 0 ) {
            
            if(invalidString!=null && invalidString.length() > 0){
                
                invalidString += ("<br/>");
           
            }
            
            invalidString += invalidNumFields + ("<br/>" 
                            + Message.INVALID_NUMBER_FORMAT);
        }
                         
        if ( invalidString!=null && invalidString.length() > 0 ) {    
            
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString,
                            Message.ERROR);
            return true;
            
        } 
        
        return false;   
    }    
    
    /**
     * verify biiling information
     * @param customerProfile CustomerProfileInformation.
     * @return boolean
     */ 
    public static String verifyUserBillingAddressInfo(
                                CustomerProfileInformation customerProfile){
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validAddressString(customerProfile.getBillingContact().
                                                 getAddressLine1()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_BILLING_ADDRESS_LINE1,
                                                 invalidCharFieldCount, 
                                                 invalidCharFields);       
        
        }
        
        if ( !WebUtils.validAddressString(customerProfile.getBillingContact().
                                                 getAddressLine2()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_BILLING_ADDRESS_LINE2, 
                                                 invalidCharFieldCount,
                                                 invalidCharFields);       
        
        }
        
        if ( !WebUtils.validAddressString(customerProfile.getBillingContact().
                                                 getAddressLine3()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_BILLING_ADDRESS_LINE3, 
                                                invalidCharFieldCount, 
                                                invalidCharFields);       
        
        }
                
        return invalidCharFields;
    }
   
   /**
     * verify mailing information
     * @param customerProfile CustomerProfileInformation.
     * @return boolean
     */
    
    public static String verifyUserMailingAddressInfo(
                                         CustomerProfileInformation customerProfile) {
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validAddressString(customerProfile.getMailingContact().
                                                 getAddressLine1()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MAILING_ADDRESS_LINE1,
                                     invalidCharFieldCount, invalidCharFields);       
        
        }
        
        if (! WebUtils.validAddressString(customerProfile.getMailingContact().
                                                 getAddressLine2()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MAILING_ADDRESS_LINE2, 
                                        invalidCharFieldCount, invalidCharFields);       
        
        }
        
        if (! WebUtils.validAddressString(customerProfile.getMailingContact().
                                                getAddressLine3()) ) {
           
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MAILING_ADDRESS_LINE3, 
                                        invalidCharFieldCount, invalidCharFields);       
        
        }
                
        return invalidCharFields;
    }
    
    /**
     * verify biiling city information
     * @param customerProfile CustomerProfileInformation.
     * @return String
     */
    
    public static String verifyUserBillingCityInfo(
                                        CustomerProfileInformation customerProfile){
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if (! WebUtils.validNameStringForCity(customerProfile.
                                                    getBillingContact().getCity()) ) {
            
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_BILLING_CITY, 
                                        invalidCharFieldCount, invalidCharFields);       
        
        }
          
        return invalidCharFields;
    }
    
    /**
     * verify mailing information
     * @param customerProfile CustomerProfileInformation.
     * @return String
     */
    
    public static String verifyUserMailingCityInfo(
                                        CustomerProfileInformation customerProfile){
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;

        if ( !WebUtils.validNameStringForCity(customerProfile.
                                            getMailingContact().getCity()) ) {
           
            invalidCharFieldCount += 1;            
            invalidCharFields = buildErrorString(Message.FIELD_MAILING_CITY, 
                                        invalidCharFieldCount, invalidCharFields);       
        }
          
        return invalidCharFields;
    }
    
    /**
     * verify zip information
     * @param contact CustomerContactInformation.
     * @return boolean
     */
     
    public static boolean isInvalidZip(CustomerContactInformation contact) {
        boolean invalidZiplength = invalidZipLength(contact);
        
        if ( invalidZiplength ) {
            
            return true;
            
        }
        
        boolean invalidZipFormat  = invalidZipFormat(contact);
        
        if ( invalidZipFormat ) {
            
            return true;
            
        }
        
        return false;
    }
    
    /**
     * verify zip length
     * @param contact CustomerContactInformation.
     * @return boolean
     */
    
    public static boolean invalidZipLength(CustomerContactInformation contact) {     
      
        if ( contact.getZipCode1().trim().length() > 0 
            || contact.getZipCode2().trim().length() > 0 ) {
                
            if ( contact.getZipCode1().trim().length() < 5 ) {
                  return true;
            }
            
        }
        return false;
    } 
  
   /**
     * verify  valid zip 
     * @param contact CustomerContactInformation.
     * @return boolean
     */
      
   public static boolean invalidZipFormat(CustomerContactInformation contact) {     
       
        if ( (! validNumber(contact.getZipCode1())) ||
             (! validNumber(contact.getZipCode2())) ) {                    
       
            return true;
       
        }
        return false;
    }
    
    
    /**
     * framewok validation
     * @param form ManageCustomerForm.
     * @param frameWork List
     * @return boolean
     */
    
    public static boolean verifyFramewok(ManageCustomerForm form, ArrayList frameWork) {

        if ( frameWork == null || frameWork.size() == 0) {
           
            form.setMessage(Message.MISSING_REQUIRED_FIELD, 
                            Message.ADD_FRAMEWORK_ERROR, Message.ERROR);
            return false;
        
        }
        
        if ( isInvalidFrameworkInfo(frameWork, form) ) {
            
            return false;
        
        }
                
        if ( !isDuplicateFramworkInfo(frameWork, form) ){
        
            return false;
        
        }   
       
        return true;
       
    }
    
    /**
     * check for duplicate framewok 
     * @param form ManageCustomerForm.
     * @param frameWork List
     * @return boolean
     */
    
	private static boolean isDuplicateFramworkInfo(ArrayList frameWork, 
                                                   ManageCustomerForm form){ 
           
        ArrayList tempFramework = new ArrayList();
        String invalidString = "";
        tempFramework = (ArrayList)frameWork.clone();
        
        for( int i = 0 ; i < frameWork.size() ; i ++ ) {
            
            for( int j = tempFramework.size() - 1; j  > i  ; j--) {
                
                Level level = (Level)frameWork.get(i);
                Level tempLevel = (Level)tempFramework.get(j);
               
                if( level.getName().equalsIgnoreCase(tempLevel.getName()) ) {
                
                    invalidString = (Message.DUPLICATE_FRAMEWORK_ENTRY);
                    form.setMessage(Message.INVALID_FORMAT_TITLE, 
                                invalidString, Message.ERROR);
                
                    return false; 
                    
                } 
            }
        }
        
        return true;
    }   
    
    /**
     * framewok validation info
     * @param form ManageCustomerForm.
     * @param frameWork List
     * @return boolean
     */
    
    private static boolean isInvalidFrameworkInfo(List frameWork, 
                                                  ManageCustomerForm form) {
        String invalidCharFields = verifyFrmeworkInfo(frameWork);
        String invalidString = "";                        
        if ( invalidCharFields.length() > 0 ) {
            
            invalidString = invalidCharFields + ("<br/>" 
                            + Message.INVALID_FRAMEWORK_CHARS);
            form.setMessage(Message.INVALID_FORMAT_TITLE, invalidString, 
                            Message.ERROR);
            return true;
        
        }
        return false;	
    }
    
    /**
     * framewok validation info
     * @param frameWork List
     * @return String
     */
    
     public static String verifyFrmeworkInfo(List frameWork){
        String invalidCharFields = "";
        int invalidCharFieldCount = 0;
        
        for (int i = 0; i < frameWork.size(); i++) {
           Level level = (Level)frameWork.get(i); 
           if ( !WebUtils.validFrameworkString(level.getName()) ){
            
                invalidCharFieldCount += 1;            
                invalidCharFields = buildErrorString(Message.FIELD_FRAMEWORK, 
                                                     invalidCharFieldCount,
                                                     invalidCharFields);       
                break;
            }
        }
        
        return invalidCharFields;
    }
    
} 
