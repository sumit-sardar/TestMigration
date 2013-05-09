package com.ctb.control.licensing; 


import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.testAdmin.CustomerLicense;
import com.ctb.exception.CTBBusinessException;

@ControlInterface()
public interface Licensing 
{ 
    /**
	 * reserveLicense - Decrements available license quantity (CUSTOMER_PRODUCT_LICENSE.AVAILABLE) and increments reserved license quantity (CUSTOMER_PRODUCT_LICENSE.RESERVED) for a customer. 
	 * Actual # of licenses reserved may vary according to the test manifest (student item set status records) for the student/session identified by the testRosterId argument.
	 * Reservation will fail if the necessary quantity exceeds the customer's current available license quantity.
	 * Reservation will succeed, with an actual quantity reserved of 0, if the customer is configured to have an infinite number (-1 available quantity) of licenses for the relevant product.
	 * @param testRosterId - identifies the student/session for which this license reservation is being made
	 * @return integer representing the actual # of licenses reserved for the student/session against the customer's available quantity. 
	 * If the customer is configured to have an infinite number of available licenses for the relevant product, return value will be 0.
	 * @throws com.ctb.exception.CTBBusinessException Thrown when customer's available license quantity for the relevant product is insufficient, or the customer has no license information configured for the relevant product.
	 */
    
    int reserveLicense(java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    /**
	 * releaseLicense - Increments available license quantity (CUSTOMER_PRODUCT_LICENSE.AVAILABLE) and decrements reserved license quantity (CUSTOMER_PRODUCT_LICENSE.RESERVED) for a customer. 
	 * Actual # of licenses released may vary according to the test manifest (student item set status records) for the student/session identified by the testRosterId argument.
	 * Release will succeed, with an actual quantity released of 0, if the customer is configured to have an infinite number (-1 available quantity) of licenses for the relevant product.
	 * Release will succeed, with an actual quantity released of 0, even if the quantity to be released exceeds the currently reserved quantity for the customer and relevant product.
	 * @param testRosterId - identifies the student/session for which this license release is being made
	 * @return integer representing the actual # of licenses released for the student/session. 
	 * If the customer is configured to have an infinite number of available licenses for the relevant product, return value will be 0.
	 * @throws com.ctb.exception.CTBBusinessException Thrown when the customer has no license information configured for the relevant product.
	 */
    
    int releaseLicense(java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    
    int retrieveLicense(java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;
    
    /**
	 * getLicenseQuantitiesByOrg - retrieves the consumed and reserved license counts for a particular org and product.  
	 * These counts represent the licence quantities of the specified product consumed/reserved by an org node and all its child nodes. 
	 * @param userName - identifies the user for which this license retrieval request is being made
	 * @param orgNodeId -  identifies the organization node for which this license retrieval request is being made 
	 * @param productId - identifies the product for which the license count retrieval request is being made 
     * @param subtestFlag - identifes whether the license configuration for the customer to which the org belongs is by sub test or test roster. the possible values are "T" and "F"
	 * @throws com.ctb.exception.CTBBusinessException Thrown when ther eis an error in retriveing the license quantities for the org.
	 */
    
    com.ctb.bean.testAdmin.OrgNodeLicenseInfo getLicenseQuantitiesByOrg(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer productId, java.lang.String subtestFlag) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method is resposible to save or update customer license details. If
     * the available license value has been changed from UI then corrensponding
     * EMAIL_NOTIFY_FLAG will be changed to 'T' otherwise will be unchanged and
     * will be added the available license into LICENSE_AFTER_LAST_PURCHASE.
     * @param CustomerLicense - customerLicense
     * @return boolean
     * @throws CTBBusinessException
     */
    
    boolean saveOrUpdateCustomerLicenses(com.ctb.bean.testAdmin.CustomerLicense customerLicense) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method is resposible to retrive customer license details by passing
     * customerId and productId
     * @param Integer - customerId
     * @param Integer - productId
     * @return CustomerLicense
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerLicense getCustomerLicenses(java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * This method is resposible to retrive customer license details by passing
     * userName and productId
     * @param String - userName
     * @param Integer - productId
     * @return CustomerLicense[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerLicense[] getCustomerLicenseData(java.lang.String userName, java.lang.Integer productId) throws com.ctb.exception.CTBBusinessException;
    
     // TABE BAUM 10: For retriving  orgNode License Data
    
    com.ctb.bean.testAdmin.CustomerLicense[] getCustomerOrgNodeLicenseData(java.lang.String userName, java.lang.Integer productId) throws com.ctb.exception.CTBBusinessException;
    
    // TABE BAUM 10: For retriving  License Data from orgnodeid and productid
    
    com.ctb.bean.testAdmin.OrgNodeLicenseInfo getLicenseQuantitiesByOrgNodeIdAndProductId(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer productId, java.lang.String subtestFlag) throws com.ctb.exception.CTBBusinessException;

   
    // TABE BAUM 10: For updating the edited available license field value in manage license page and Inserting license details into database for a particular organization who's entry is not there in the database table
   
    boolean saveOrUpdateOrgNodeLicenseDetail(com.ctb.bean.testAdmin.LicenseNodeData [] licenseNodeData) throws com.ctb.exception.CTBBusinessException;

    boolean addCustomerProductLicense(CustomerLicense customerLicense) throws com.ctb.exception.CTBBusinessException;
    
    boolean updateCustomerProductLicense(CustomerLicense customerLicense) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.CustomerLicense[] getCustomerProductLicenses(java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;
    
//    com.ctb.bean.testAdmin.CustomerLicense getLASCustomerTopNodeData (java.lang.Integer customerId, java.lang.Integer productId)throws com.ctb.exception.CTBBusinessException;
    
    boolean updateLASCustomerTopNodeLicense (CustomerLicense customerLicense)throws com.ctb.exception.CTBBusinessException;
    
    boolean addLASCustomerTopNodeLicense (CustomerLicense customerLicense)throws com.ctb.exception.CTBBusinessException;
    
    int getTopNodeId(java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.CustomerLicense getTopOrgnodeLicenseDetails(java.lang.Integer orgNodeId, java.lang.Integer productId) throws com.ctb.exception.CTBBusinessException;
    
    boolean addEditOrgnodeOrderLicense (CustomerLicense customerLicense)throws com.ctb.exception.CTBBusinessException;
    
    void sendMail(Integer customerId, String to, Integer emailType, String orderNumber, String licenseQuantity, String purchaseDate, String expiryDate); 
} 
