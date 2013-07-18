package com.ctb.control.licensing; 

import java.sql.SQLException;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfiguration;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.bean.testAdmin.LicenseNodeData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeLicenseInfo;
import com.ctb.bean.testAdmin.TestProduct;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.licensing.LicenseCreationException;
import com.ctb.exception.licensing.LicenseUpdationException;
import com.ctb.exception.licensing.OrgLicenseDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.OASLogger;
import com.ctb.util.licensing.CTBConstants;


/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class LicensingImpl implements Licensing
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Customer customers;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.License license;
   
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;
    
    static final long serialVersionUID = 1L;

    /**
     * @common:operation
     */
    public int reserveLicense(Integer testRosterId) throws CTBBusinessException
    {
        return 0;
    }
    
    /**
     * @common:operation
     */
    public int releaseLicense(Integer testRosterId) throws CTBBusinessException
    {
        return 0;
    }
    
    /**
     * @common:operation
     */
    public int retrieveLicense(Integer testRosterId) throws CTBBusinessException
    {
        return 0;
    }

    /**
     * @common:operation
     */
    public OrgNodeLicenseInfo getLicenseQuantitiesByOrg(String userName, Integer orgNodeId, Integer productId, String subtestFlag) throws CTBBusinessException
    {
        try {
            validator.validateNode(userName, orgNodeId, "LicesingImpl.getLicenseQuantitiesByOrg");
        } catch (ValidationException ve) {
            throw ve;
        }
        try{
            OrgNodeLicenseInfo orgNodeLicenseInfo = new OrgNodeLicenseInfo();
            Integer reservedCount = null;
            Integer consumedCount = null;
            
            orgNodeLicenseInfo.setOrgNodeId(orgNodeId);
            
            if(subtestFlag.equals(CTBConstants.TRUE_INITIAL)){
                reservedCount = license.getReservedQuantityForOrgBySubTest(orgNodeId, productId);
                consumedCount = license.getConsumedQuantityforOrgBySubTest(orgNodeId, productId);
                
                orgNodeLicenseInfo.setLicReserved(reservedCount);
                orgNodeLicenseInfo.setLicUsed(consumedCount);
            }
            else if(subtestFlag.equals(CTBConstants.FALSE_INITIAL)){
                reservedCount = license.getReservedQuantityForOrgByRoster(orgNodeId, productId);
                consumedCount = license.getConsumedQuantityForOrgByRoster(orgNodeId, productId);
                orgNodeLicenseInfo.setLicReserved(reservedCount);
                orgNodeLicenseInfo.setLicUsed(consumedCount);  
            }
            return orgNodeLicenseInfo;
        }
        catch(SQLException se){
            OrgLicenseDataNotFoundException lde = new OrgLicenseDataNotFoundException("LicensingImpl:getLicenseQuantitiesByOrg failed: " + se.getMessage());
            throw lde;
        }
    }
    
 // START- TABE BAUM 10: For retriving  License Data from orgnodeid and productid
    /**
     * @common:operation
     */
    public OrgNodeLicenseInfo getLicenseQuantitiesByOrgNodeIdAndProductId(String userName, Integer orgNodeId, Integer productId, String subtestFlag) throws CTBBusinessException
    {
      
        	CustomerLicense [] cls=null;
        	CustomerLicense [] cl=null;
            OrgNodeLicenseInfo orgNodeLicenseInfo = new OrgNodeLicenseInfo();
            Integer reservedCount = new Integer(0);
            Integer consumedCount =  new Integer(0);
            Integer availableCount =  new Integer(0);
            orgNodeLicenseInfo.setOrgNodeId(orgNodeId);
            
            	try{
            		
                	cls = license.getOrgnodeLicenseDetails(orgNodeId,productId);
                	cl = license.getTotalConsumedReservedQuantityByAncestorOrgNode(orgNodeId,productId);
                	
                }catch (SQLException e)
                     {
                         e.printStackTrace();
                     }
                if(cls != null && cl != null && cls.length > 0 && cl.length > 0) {
                availableCount = cls[0].getAvailable();
                if(cl[0].getConsumedLicense() != null && !(cl[0].getConsumedLicense().equals("")))
                	consumedCount = cl[0].getConsumedLicense();
                if(cl[0].getReservedLicense() != null && !(cl[0].getReservedLicense().equals("")))
                	reservedCount = cl[0].getReservedLicense();
                
                }
                
                orgNodeLicenseInfo.setLicPurchased(availableCount);
                orgNodeLicenseInfo.setLicReserved(reservedCount);
                orgNodeLicenseInfo.setLicUsed(consumedCount);
               
                
            return orgNodeLicenseInfo;
        }
        
   // END -TABE BAUM 10: For retriving  License Data from orgnodeid and productid
    
     /**
     * This method is resposible to retrive customer license details by
     * passing userName and productId
     * @common:operation
     * @param String - userName
     * @param Integer - productId
     * @return CustomerLicense[]
     * @throws CTBBusinessException
     */
     
    public CustomerLicense[] getCustomerLicenseData(String userName, Integer productId) 
            throws CTBBusinessException {
         try {   
            
             Customer customer = users.getCustomer(userName);  
             Integer customerId = customer.getCustomerId();
                
             CustomerLicense[] customerLicense  = null;  
             validator.validateCustomer(userName, customerId, 
                        "LicensingImpl.getCustomerLicenseData");      
            
            if ( productId != null && productId.intValue() != 0 ) {
                        
                validator.validateProduct(userName, productId, 
                        "LicensingImpl.getCustomerLicenseData" );
                        
                customerLicense =  this.license
                        .getProductLicenseDetails(customerId,productId);
                        
                
            } else  {
                
                customerLicense =  this.license.getCustomerLicenseDetails(customerId);
            }
            
            return customerLicense;  
                      
        } catch (SQLException e ) {
            
            OrgLicenseDataNotFoundException lde = 
                    new OrgLicenseDataNotFoundException("platformlicence.getCustomerLicenseData.E001");
                    
            throw lde;
            
        }
        
    }
    
    //START - TABE BAUM 10: For retriving  orgNode License Data
    
    public CustomerLicense[] getCustomerOrgNodeLicenseData(String userName, Integer productId) 
    throws CTBBusinessException {
	 try {   
		 
		 Customer customer = users.getCustomer(userName);  
         Integer customerId = customer.getCustomerId();
	     CustomerLicense[] customerLicense  = null;  
	    
	    if ( productId != null && productId.intValue() != 0 ) {
	                
	        validator.validateProduct(userName, productId, 
	                "LicensingImpl.getCustomerLicenseData" );
	                
	        customerLicense =  this.license
	                .getSelectedProductLicenseDetails(userName,productId, customerId);
	                
	        
	    } else  {
	        
	        customerLicense =  this.license.getUserOrgNodeLicenseDetails(userName);        
	    }
	    // For MQC 66803 :Rapid registration
	    if(customerLicense!=null && customerLicense.length>0){
	    	for( CustomerLicense license : customerLicense) {
	    		CustomerLicense[] cLicense  =this.license.isLicenseAvailable(userName);
	    		license.setIsLicenseAvailable(false);
	    		for( CustomerLicense lic : cLicense) {
	    			if(lic.getProductId().equals(license.getProductId())) {
	    				license.setIsLicenseAvailable(lic.isLicenseAvailable());
	    				break;
	    			} 
	    		}
	    	}
	    }
	    return customerLicense;  
	              
	} catch (SQLException e ) {
	    
	    OrgLicenseDataNotFoundException lde = 
	            new OrgLicenseDataNotFoundException("platformlicence.getCustomerLicenseData.E001");
	            
	    throw lde;
	    
	}
	
	}

    // END-TABE BAUM 10: For retriving  orgNode License Data
    
    /**
     * This method is resposible to retrive customer license details by
     * passing customerId and productId
     * @common:operation
     * @param Integer - customerId
     * @param Integer - productId
     * @return CustomerLicense
     * @throws CTBBusinessException
     */
     
    public CustomerLicense getCustomerLicenses (Integer customerId) 
            throws CTBBusinessException {
                
         try {   
            
                             
             CustomerLicense []customerLicense  = null;  
             CustomerLicense [] cl=null;
             Customer customer = customers.getCustomerDetails(customerId);
             String customerName = customer.getCustomerName();
             TestProduct product = this.license.getParentProductId(customerId)[0]; 
             Integer productId =  product.getProductId();     
             String productName = product.getProductName();
              //CustomerLicense change
             Node customerTopNode = orgNode.getTopOrgNodeForCustomer(customerId); 
             Integer orgNodeId = customerTopNode.getOrgNodeId();
            if ( productId != null && productId.intValue() != 0 ) {
                        
                customerLicense =  this.license
                        .getProductLicenseForCustomer(orgNodeId,customerId,productId);
                cl = license.getTotalConsumedReservedQuantityByAncestorOrgNode(orgNodeId,productId); 
              } 
            
                if (customerLicense != null && customerLicense.length > 0 ) {
                	 if (cl != null && cl.length > 0 ) {
                		 customerLicense[0].setConsumedLicense(cl[0].getConsumedLicense());
                		 customerLicense[0].setReservedLicense(cl[0].getReservedLicense());
                	 }
                	  customerLicense[0].setCustomerName(customerName);
                	 return customerLicense[0];
                
            } else {
                
                CustomerLicense customerLic = new CustomerLicense ();
                customerLic.setCustomerId(customerId);
                customerLic.setProductId(productId);
                customerLic.setProductName(productName);
                customerLic.setCustomerName(customerName);
                return customerLic;
                
            }
                    
        } catch (SQLException e ) {
            
            OrgLicenseDataNotFoundException lde = 
                    new OrgLicenseDataNotFoundException("platformlicence.getCustomerLicenses.E0014");
                    
            throw lde;
            
        }
        
    }
    
    /**
     * This method is resposible to save or update customer license details.
     * If the available license value has been changed from UI then
     * corrensponding EMAIL_NOTIFY_FLAG will be changed to 'T' otherwise will be 
     * unchanged and will be added the available license into LICENSE_AFTER_LAST_PURCHASE. 
     * @common:operation
     * @param CustomerLicense - customerLicense
     * @return boolean
     * @throws CTBBusinessException
     */
      //CustomerLicense change
    public boolean saveOrUpdateCustomerLicenses (CustomerLicense customerLicense) 
            throws CTBBusinessException {
    	try{
    		
    	Node customerTopNode = orgNode.getTopOrgNodeForCustomer(customerLicense.getCustomerId()); 
        Integer orgNodeId = customerTopNode.getOrgNodeId();
    	
            if (isCustomerLicenseExist(customerLicense.getCustomerId(),
                customerLicense.getProductId(),orgNodeId)) {
            
                 return updateCustomerLicenses (customerLicense,orgNodeId);
            
              } else {
            
                 return addCustomerLicenses (customerLicense,orgNodeId);
            
                    } 
          }catch (SQLException e ) {
            
            OrgLicenseDataNotFoundException lde = 
                    new OrgLicenseDataNotFoundException("platformlicence.getTopOrgNodeForCustomer");
                    
            throw lde;
            
        }
         
        
    }
    
    /**
     * This method is resposible to check the existance of customer license 
     * by passing customerId and productId
     * @return boolean
     * @param Integer - customerId
     * @param Integer - productId
     * @throws CTBBusinessException 
     */
     //CustomerLicense change
    private boolean  isCustomerLicenseExist (Integer customerId, Integer productId,Integer orgNodeId)
            throws CTBBusinessException {
         try {
            
            return this.license.isCustomerLicenseExist (customerId, productId, orgNodeId);
            
         } catch (SQLException se) {
            
            OrgLicenseDataNotFoundException lde = 
                    new OrgLicenseDataNotFoundException("platformlicence.isCustomerLicenseExist.E0012");
                    
            throw lde;
                     
         }       
                
    }
    /**
     * This method is resposible to check the existance of customer license 
     * by passing customerId and productId
     * @return boolean
     * @param Integer - customerId
     * @param Integer - productId
     * @throws CTBBusinessException 
     */
    
    private boolean  isCustomerOrgnodeLicenseExist (LicenseNodeData licenseNodeData)
            throws CTBBusinessException {
         try {
            
            return this.license.isCustomerOrgNodeLicenseExist (licenseNodeData);
            
         } catch (SQLException se) {
            
            OrgLicenseDataNotFoundException lde = 
                    new OrgLicenseDataNotFoundException("platformlicence.isCustomerLicenseExist.E0012");
                    
            throw lde;
                     
         }       
                
    }
    
    /**
     * This method is resposible to update the customer license details by 
     * passing CustomerLicense
     * @return boolean
     * @param CustomerLicense - customerLicense
     * @throws CTBBusinessException
     */
     //CustomerLicense change
    private boolean updateCustomerLicenses (CustomerLicense customerLicense,Integer orgNodeId) 
            throws CTBBusinessException {
        
        try {
            this.license.updateCustomerLicensewithAvailableChange(customerLicense,orgNodeId);
            // can be used in case when avaliable and consumed needed to be updated
           /* if (customerLicense.getAvailableLicenseChange()) {
                
                this.license.updateCustomerLicensewithAvailableChange(customerLicense);
                
            } else {
                
                this.license.updateCustomerLicensewithoutAvailableChange(customerLicense);
            }*/
           
         } catch (SQLException se) {
            
            LicenseUpdationException lue = 
                    new LicenseUpdationException("platformlicence.updateCustomerLicenses.E0014");
                    
            throw lue;
                     
         }    
         
         return true;
        
    }
    
    /**
     * This method is resposible to insert the customer license details by 
     * passing CustomerLicense
     * @return boolean
     * @param CustomerLicense - customerLicense
     * @throws CTBBusinessException
     */
    //CustomerLicense change
    private boolean addCustomerLicenses (CustomerLicense customerLicense, Integer orgNodeId)
            throws CTBBusinessException {
                
        try {
            this.license.addCustomerLicense(customerLicense, orgNodeId);
            
         } catch (SQLException se) {
            
            LicenseCreationException lce = 
                    new LicenseCreationException("platformlicence.addCustomerLicenses.E0014");
                    
            throw lce;
            
         }
         
         return true;    
                
    }
    
    
 // START - TABE BAUM 10: For updating the edited available license field value in manage license page and Inserting license details into database for a particular organization who's entry is not there in the database table
    
        public boolean saveOrUpdateOrgNodeLicenseDetail (LicenseNodeData [] licenseNodeData) 
    throws CTBBusinessException {
        	
    	try{
    		for(int i =0; i < licenseNodeData.length ; i++ ) {
    			String isSubtestModel ="T";
            	if(licenseNodeData[i].getSubtestModel().equals("Session")) {
            		isSubtestModel = "F";
            	}
            	licenseNodeData[i].setSubtestModel(isSubtestModel);

	    		if (isCustomerOrgnodeLicenseExist (licenseNodeData[i])) {
	    	        
	    	    
	    		this.license.updateAvailableLicenseChange(licenseNodeData[i]);
	    
	    		} else {
	    
	    		this.license.addOrgNodeLicenses(licenseNodeData[i]);
	    
	    		}
    		}
    	}catch (SQLException se) {
          
          LicenseUpdationException lue = 
                  new LicenseUpdationException("platformlicence.updateCustomerLicenses.E0014");
                  
          throw lue;
                   
       }   
    	
    return true;
}

   public  boolean addCustomerProductLicense(CustomerLicense customerLicense) throws com.ctb.exception.CTBBusinessException {
        
        try {
            this.license.addCustomerProductLicense(customerLicense);
            
         } catch (SQLException se) {
            
            LicenseCreationException lce = 
                    new LicenseCreationException("platformlicence.addCustomerLicenses.E0014");
                    
            throw lce;
            
         }
         
         return true;  
    }
   
   public Integer checkMultipleFrameWorkProduct(Integer productId) throws com.ctb.exception.CTBBusinessException {
	   Integer frameworkParentProductId =  null;
	   try {
          
		   frameworkParentProductId =  this.license.getFrameworkParentProductId(productId);
           
        } catch (SQLException se) {
           
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.getParentMultipleFrameworkException.E0014");
                   
           throw lce;
           
        }
        
        return frameworkParentProductId;   
	   
   }
   
   public CustomerLicense[] getCustomerProductLicenses(Integer customerId) throws CTBBusinessException {
	   try {   
		     CustomerLicense[] customerLicense  = license.getCustomerProductLicenses(customerId);  
		  
		    return customerLicense;  
		              
		} catch (SQLException e ) {
		    
		    OrgLicenseDataNotFoundException lde = 
		            new OrgLicenseDataNotFoundException("platformlicence.getCustomerLicenseData.E001");
		            
		    throw lde;
		    
		}
   }
   public boolean updateCustomerProductLicense(CustomerLicense customerLicense) throws CTBBusinessException {
	   try {
           this.license.updateCustomerProductLicense(customerLicense);
           
        } catch (SQLException se) {
           
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.updateCustomerProductLicense.E0014");
                   
           throw lce;
           
        }
        
        return true;    
   }
     // END - TABE BAUM 10: For updating the edited available license field value in manage license page and Inserting license details into database for a particular organization who's entry is not there in the database table
    
//   public CustomerLicense getLASCustomerTopNodeData (Integer customerId, Integer productId) throws CTBBusinessException {
//	   try {   
//		     CustomerLicense customerLicense  = null;
//		     customerLicense = license.getLASCustomerTopNodeData(customerId, productId);  
//		  
//		    return customerLicense;  
//		              
//		} catch (SQLException e ) {
//		    
//		    OrgLicenseDataNotFoundException lde = 
//		            new OrgLicenseDataNotFoundException("platformlicence.getLASCustomerTopNodeData.E001");
//		            
//		    throw lde;
//		    
//		}
//	   
//   }
   
   public boolean updateLASCustomerTopNodeLicense (CustomerLicense customerLicense)throws CTBBusinessException {
	   
	   try {
           this.license.updateLASCustomerTopNodeLicense(customerLicense.getOrgNodeId(),customerLicense.getProductId(),customerLicense.getCustomerId(), customerLicense.getAvailable(), customerLicense.getLicenseAfterLastPurchase());

	   } catch (SQLException se) {
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.updateLASCustomerTopNodeLicense.E0014");
           throw lce;
        }
        return true; 
   }
   
   public boolean addLASCustomerTopNodeLicense (CustomerLicense customerLicense)throws CTBBusinessException {
	   
	   try {
           this.license.addLASCustomerTopNodeLicense(customerLicense);
	   } catch (SQLException se) {
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.addLASCustomerTopNodeLicense.E0014");
           throw lce;
        }
        return true; 
   }
   
   public int getTopNodeId(Integer customerId) throws CTBBusinessException{
	   Integer orgNodeId = null;
	   try {
		   orgNodeId = this.license.getTopNodeId(customerId);

	   } catch (SQLException se) {
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.getTopNodeId");
           throw lce;
        }
	   return orgNodeId;
   }
   
   public CustomerLicense getTopOrgnodeLicenseDetails(Integer orgNodeId, Integer productId) throws CTBBusinessException {
	   try {   
		     CustomerLicense customerLicense  = null;
		     customerLicense = this.license.getTopOrgnodeLicenseDetails(orgNodeId, productId);  
		  
		    return customerLicense;  
		              
		} catch (SQLException e ) {
		    
		    OrgLicenseDataNotFoundException lde = 
		            new OrgLicenseDataNotFoundException("platformlicence.getTopOrgnodeLicenseDetails.E001");
		            
		    throw lde;
		    
		}
   }
   
   public boolean addEditOrgnodeOrderLicense (CustomerLicense customerLicense)throws CTBBusinessException {
	   
	   Integer orderIndexId = null;
	   Integer orgOrderLicense = null;
	   Integer availableOrgOrderLicense = null;
	   boolean result = false;
	   
	   try {
		   orderIndexId = this.license.getOrderIndexDetails(customerLicense);
		   customerLicense.setOrderIndex(orderIndexId);
		   orgOrderLicense = this.license.getOrgOrderLicenseDetails(customerLicense);
		   if (orderIndexId != null) {
			   if (orgOrderLicense == null) {
				   this.license.addOrgnodeOrderLicense(customerLicense);
				   result = true;
			   }	
			   else {
				   	availableOrgOrderLicense = this.license.getAvailableOrgOrderLicense(customerLicense);
				   	if(availableOrgOrderLicense != null)
				   		availableOrgOrderLicense = availableOrgOrderLicense + customerLicense.getBalanceLicense();
				   	customerLicense.setAvailable(availableOrgOrderLicense);
				   	this.license.updateOrgnodeOrderLicense(customerLicense);
				    result = true;
			   }
		   }
		   else 
			   result = false;
		   
	   } catch (SQLException se) {
           LicenseCreationException lce = 
                   new LicenseCreationException("platformlicence.addEditOrgnodeOrderLicense");
           throw lce;
        }
        return result ; 
   }
   
   /**
    * This is a generic method to send mail. It retrieves the content of the body
    * from database. value should be an empty string even If for some email_type, 
    * there is no replacement in the body. Caller should ensure that to_address 
    * is not null. This method suppresses any exception occured. 
    * 
    */
   public void sendMail(Integer customerId, String to, Integer emailType, String orderNumber, String licenseQuantity, 
		   String purchaseDate, String expiryDate) {
       try {
           CustomerEmail emailData = new CustomerEmail();
           boolean isLaslinkCustomer = false;
           if(customerId != null){ 
             emailData = users.getCustomerEmailByCustomerId(customerId, emailType);
           }
           /*else if (orgNodeId != null){
             emailData = users.getCustomerEmailByOrgId(orgNodeId, emailType);
           }*/
           CustomerConfiguration [] cc = users.getCustomerConfigurations(emailData.getCustomerId());
           for (int i = 0; i < cc.length; i++) {
				if(cc[i].getCustomerConfigurationName().equals("LASLINK_Customer") ||
						cc[i].getCustomerConfigurationName().equals("LL_Customer")){
					isLaslinkCustomer = true;
				}
			}
           String content = emailData.getEmailBodyStr().replaceAll(
                               CTBConstants.EMAIL_CONTENT_PLACEHOLDER_ORDERNO, orderNumber);
           content = content.replaceAll(
        		   			   CTBConstants.EMAIL_CONTENT_PLACEHOLDER_LICENSEQTY, licenseQuantity);
           content = content.replaceAll(
        		   			   CTBConstants.EMAIL_CONTENT_PLACEHOLDER_PURCHASEDATE, purchaseDate );
           content = content.replaceAll(
        		   			   CTBConstants.EMAIL_CONTENT_PLACEHOLDER_EXPIRYDATE, expiryDate);
                               
           InitialContext ic = new InitialContext();
           
           //the properties were configured in WebLogic through the console
           Session session = (Session) ic.lookup("UserManagementMail");
           
           //contruct the actual message
           Message msg =  new MimeMessage(session);
           String replyTo = emailData.getReplyTo();
           if(replyTo == null || replyTo.length() < 1) {
               replyTo = CTBConstants.EMAIL_FROM;
           }
           if(isLaslinkCustomer)
           	msg.setFrom(new InternetAddress(replyTo,CTBConstants.EMAIL_FROM_ALIAS_LASLINKS));
           else
           	msg.setFrom(new InternetAddress(replyTo));
           
           //emailTo could be a comma separated list of addresses
           msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
           msg.setSubject(emailData.getSubject());
           msg.setText(content);
           msg.setSentDate(new Date());
           
           //send the message
           Transport.send(msg);
           
       } catch (Exception e) {
           e.printStackTrace();
           OASLogger.getLogger("Licensing").error(e.getMessage());
           OASLogger.getLogger("Licensing").error("sendMail failed for emailType: " + emailType);
       }
   }
}