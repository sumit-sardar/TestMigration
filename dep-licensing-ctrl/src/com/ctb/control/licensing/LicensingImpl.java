package com.ctb.control.licensing; 

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.testAdmin.Customer;
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

    
     // END - TABE BAUM 10: For updating the edited available license field value in manage license page and Inserting license details into database for a particular organization who's entry is not there in the database table
     
} 
  