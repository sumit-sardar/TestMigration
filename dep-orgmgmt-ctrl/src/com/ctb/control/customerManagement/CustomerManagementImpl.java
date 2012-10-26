package com.ctb.control.customerManagement; 

import com.bea.control.*;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Address;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerConfig;
import com.ctb.bean.testAdmin.CustomerData;
import com.ctb.bean.testAdmin.CustomerEmail;
import com.ctb.bean.testAdmin.FindCustomer;
import com.ctb.bean.testAdmin.FindCustomerData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.CustomerDataRetrivalException;
import com.ctb.exception.customerManagement.CustomerCreationException;
import com.ctb.exception.customerManagement.CustomerUpdationException;
import com.ctb.exception.customerManagement.CustomerDataNotFoundException;
import com.ctb.exception.frameWorkManagement.FrameWorkCreationException;
import com.ctb.exception.frameWorkManagement.FrameWorkDeletionException;
import com.ctb.exception.frameWorkManagement.FrameWorkUpdationException;
import com.ctb.exception.organizationManagement.OrgDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.CTBConstants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import com.ctb.util.SimpleCache;
import java.util.List;
import java.util.ListIterator;
import org.apache.beehive.controls.api.bean.ControlImplementation;
/**
 * @author Tata Consultancy Services
 */

/**
 * @editor-info:code-gen control-interface="true"
 */

@ControlImplementation(isTransient=true)
public class CustomerManagementImpl implements CustomerManagement
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Customer customers;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.organizationManagement.OrganizationManagement organizationManagement;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNode orgNode;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.validation.Validator validator;
  
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Users users;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.Addresses addresses;
   
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OrgNodeCategory orgNodeCate;
    
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.SDSAssignment sdsAssignment;
    

    static final long serialVersionUID = 1L;

    /**
     * update customer record
     * @common:operation
     * @param loginName - identifies the login user
     * @param customer - contains the existing customer information
     * @return nothing
     * @throws CTBBusinessException
     */
    public void updateCustomer(String loginName, Customer customer)
                                throws CTBBusinessException {   
        Integer loginUserId = null;
        Integer customerId = null;
        Integer billingAddressId = customer.getBillingAddressId();
        Integer mailingAddressId = customer.getMailingAddressId();
        Integer newBillingAddressId = null;
        Integer newMailingAddressId = null;
        StringBuffer customerNameBuff = new StringBuffer();
        String customerName = null;
        
        try {
            validator.validateUser(loginName, loginName,
                                     "CustomerManagementImpl.createCustomer");
        } catch (ValidationException ve ) {
            throw ve;
        }
        
        try {
            
            loginUserId = users.getUserDetails(loginName).getUserId();
                
                /*
                 * Update Customer  contact information for exting address.
                 * Address for both billingAddress and mailingaddress
                 *  needs to be updated before profile updation, 
                 * since we may need to update the address_id as well.
                 * 
                 */ 
                
                
            Address billingAddress = customer.getBillingAddress();
            
            if ( billingAddressId != null ) {
                
                billingAddress.setUpdatedBy(loginUserId);
                billingAddress.setUpdatedDateTime(new Date());
                addresses.updateAddress(billingAddress);
                    
            } else if ( billingAddress != null ) {
                     
                newBillingAddressId = addresses.getNextPK();
                billingAddress.setAddressId(newBillingAddressId);
                billingAddress.setCreatedBy(loginUserId);
                billingAddress.setCreatedDateTime(new Date());
                addresses.createAddress(billingAddress);
                customer.setBillingAddressId(newBillingAddressId);
                
            }
                
                
            Address mailingAddress = customer.getMailingAddress();
            if( mailingAddressId != null ) {
                
                mailingAddress.setUpdatedBy(loginUserId);
                mailingAddress.setUpdatedDateTime(new Date());
                addresses.updateAddress(mailingAddress);
                
            } else if ( mailingAddress != null ) {
                     
                newMailingAddressId = addresses.getNextPK();
                mailingAddress.setAddressId(newMailingAddressId);
                mailingAddress.setCreatedBy(loginUserId);
                mailingAddress.setCreatedDateTime(new Date());
                addresses.createAddress(mailingAddress);
                customer.setMailingAddressId(newMailingAddressId);
                
            }
            //Update Selected Customer
       /*     customerName = customer.getCustomerName();
            customerNameBuff.append(customerName.substring(0,1).toUpperCase());   
            customerNameBuff.append(customerName.substring(1,customerName.length()));
            customer.setCustomerName(customerNameBuff.toString());*/
            
            customer.setActivationStatus(CTBConstants.ACTIVATION_STATUS_ACTIVE); 
            customer.setUpdatedBy(loginUserId);
            customer.setUpdatedDateTime(new Date());
            customers.updateCustomer(customer);
            //START - Changes For LASLINK Product
            customers.updateCustomerTopNodeMdrNumber(customer);
            //END - Changes For LASLINK Product              
        }  catch (SQLException se) {
               CustomerUpdationException dataUpdationException = 
                                            new CustomerUpdationException(
                                                    "EditCustomer.Failed");
                                                    
                dataUpdationException.setStackTrace(se.getStackTrace());
                throw dataUpdationException;
        }  catch (Exception e) {
                CustomerUpdationException dataUpdationException = 
                                            new CustomerUpdationException(
                                                    "EditCustomer.Failed");
                dataUpdationException.setStackTrace(e.getStackTrace());
                throw dataUpdationException;
        }

              
    }

   /**
     * Create new Customer Record
     * @common:operation
     * @param userName - identifies the login user
     * @param User - contains the new customer information
     * @return String - created customer's id
     * @throws CTBBusinessException
     */
    public Customer createCustomer(String loginUserName, Customer customer)
                                         throws CTBBusinessException {
        Integer loginUserId = null;
        Integer customerId = null;
        StringBuffer customerNameBuff = new StringBuffer();
        String customerName = null;        
        Integer billingAddressId = null;
        Integer mailingAddressId = null;
        boolean uniqueProductType = false;        
        try {
            validator.validateUser(loginUserName, 
                                   loginUserName,
                                   "CustomerManagementImpl.createCustomer");
        } catch (ValidationException ve){
                 throw ve;
        }
       
        try{
            loginUserId = users.getUserDetails(loginUserName).getUserId();
                
            if ( customer.getBillingAddress() != null ) {
                        
                Address billingAddress = customer.getBillingAddress();
                billingAddressId = addresses.getNextPK();
                billingAddress.setAddressId(billingAddressId);
                billingAddress.setCreatedBy(loginUserId);
                billingAddress.setCreatedDateTime(new Date());
                addresses.createAddress(billingAddress);
                customer.setBillingAddressId(billingAddressId);
                    
            } 
                
            if( customer.getMailingAddress() != null ) {
                    
                Address mailingaddress = customer.getMailingAddress();
                mailingAddressId = addresses.getNextPK();
                mailingaddress.setAddressId(mailingAddressId);
                mailingaddress.setCreatedBy(loginUserId);
                mailingaddress.setCreatedDateTime(new Date());
                addresses.createAddress(mailingaddress);
                customer.setMailingAddressId(mailingAddressId);
            }
           
      /*      customerName = customer.getCustomerName();
            customerNameBuff.append(customerName.substring(0,1).toUpperCase());   
            customerNameBuff.append(customerName.substring(1,customerName.length()));
            customer.setCustomerName(customerNameBuff.toString());*/
           
            customerId = customers.getNextPk();
            customer.setCustomerId(customerId);
            customer.setBillingAddressId(billingAddressId); 
            customer.setMailingAddressId(mailingAddressId);
            customer.setActivationStatus(CTBConstants.ACTIVATION_STATUS_ACTIVE);
            customer.setCreatedBy(loginUserId);
            customer.setCreatedDateTime(new Date());
            customer.setAllowDataUpload(CTBConstants.F);
            customer.setHideAccommodations(CTBConstants.F);
            customer.setImportStudentEditable(CTBConstants.F);
            customer.setDemographicVisible(CTBConstants.T);
               
            customers.createCustomer(customer); 


                          
            //insert customer framework  
            createFrameWork(loginUserName,customer.getOrgNodeCategory(),customerId);
               
            //insert customer top node
            createTopOrgNode(loginUserName,customer);

            if(CTBConstants.TABE_CUSTOMER.equalsIgnoreCase
                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
                  
                customers.createTabeCustomerConfiguration(customerId);  
                                                  
            } else if(CTBConstants.TERRANOVA_CUSTOMER.equalsIgnoreCase
                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
               
                customers.createTerranovaCustomerConfiguration(customerId);
                    
            }
            //START - Changes for LASLINK PRODUCT 
            else if(CTBConstants.LASLINK_CUSTOMER.equalsIgnoreCase
                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
               
                
                // create customer configuration based on product type selected
                String[] selectedProducts = customer.getProductList();
                System.out.println(" ++++++++++++++ "+selectedProducts);
                List<String> tempProductList = Arrays.asList(selectedProducts);
                uniqueProductType = true;
                
                if(tempProductList.contains(CTBConstants.CUSTOMER_PRODUCT_ESPANOL)){
                	if(tempProductList.size() > 1){
                		uniqueProductType = false;                		
                	}
                	else
                		uniqueProductType  = true;
                }
                  
                if(uniqueProductType){
                	if(tempProductList.contains(CTBConstants.CUSTOMER_PRODUCT_ESPANOL))
                		customers.createLLEspanolCustomerConfiguration(customerId);
                	else
                		customers.createLasLinkCustomerConfiguration(customerId);   //For Form A&B type product
                }
                else{
                	//call both
                	customers.createLasLinkEspanolCustomerConfiguration(customerId);
                }
            }
            //END - Changes for LASLINK PRODUCT 
            
//            //START - Changes for LLESPANOL PRODUCT 
//            else if(CTBConstants.LLESPANOL_CUSTOMER.equalsIgnoreCase
//                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
//               
//                customers.createLLEspanolCustomerConfiguration(customerId);
//                    
//            }
//            //END - Changes for LLESPANOL PRODUCT 
            
          //START - Changes for TABE ADAPTIVE PRODUCT 
            else if(CTBConstants.TABE_ADAPTIVE_CUSTOMER.equalsIgnoreCase
                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
               
                customers.createTABEAdaptiveCustomerConfiguration(customerId);
                    
            }
            //END - Changes for TABE ADAPTIVE PRODUCT
            
            else if(CTBConstants.OTHER_CUSTOMER.equalsIgnoreCase
                    (customer.getCustomerConfiguration()[0].getCustomerConfigurationName())) {
                saveCustomerEmail(customer);
            }
               
                        
            
            return  customer;   
        
        } catch (SQLException se) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
        
    }
    /** get all the customer viewable for the login user
     * @common:operation
     * @param userName - identifies the login user
     * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
     * @returns CustomerData
     * @throws CTBBusinessException
     */
    public CustomerData getCustomers(String userName,
                                     FilterParams filter, 
                                     PageParams page,
                                     SortParams sort) throws CTBBusinessException{
        
        CustomerData customerData = new CustomerData();                                        
        Integer pageSize = null;
        if (page != null) {
            
            pageSize = new Integer(page.getPageSize());
                
        }
           
        try {
              validator.validateUser(userName, 
                                       userName,
                                      "CustomerManagementImpl.getCustomers");
        } catch (ValidationException ve) {            
                throw ve;            
        }
            
        try {
                return getCustomerData(userName,filter, page, sort);
            
        } catch (SQLException se) {
                CustomerDataNotFoundException dataNotfoundException = 
                                        new CustomerDataNotFoundException
                                                ("FindCustomer.Failed");
                dataNotfoundException.setStackTrace(se.getStackTrace());
                throw dataNotfoundException;
        } catch(CTBBusinessException e){
                CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("FindCustomer.Failed");
                dataRetrivalException.setStackTrace(e.getStackTrace());
                throw dataRetrivalException;
        } catch (Exception e) {
                CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("FindCustomer.Failed");
                dataRetrivalException.setStackTrace(e.getStackTrace());
                throw dataRetrivalException;
        }
    }
     
    /** get all the customer viewable for the login user
     * @common:operation
     * @param userName - identifies the login user
     * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
     * @returns nothing
     * @throws CTBBusinessException
     */
    public void updateFrameWork(String loginUserName, Customer customer)
            throws CTBBusinessException{
        
        try {
            validator.validateUser(loginUserName, 
                                   loginUserName,
                                   "CustomerManagementImpl.updateFrameWork");
        } catch (ValidationException ve) {            
             throw ve;            
        }
        
            //Update Selected Customer's Framwork
       
        List createFrameworkList = new ArrayList();
        List updateFrameworkList = new ArrayList();
        List deleteFrameworkList = new ArrayList();
        
        try {
            
            OrgNodeCategory []OrgNodeCFromUI = customer.getOrgNodeCategory();
            OrgNodeCategory []OrgNodeCFromDB = orgNodeCate.getOrgNodeCategories(
                                                            customer.getCustomerId());
                                                            
            List orgNodeCListFromUI = Arrays.asList(OrgNodeCFromUI);
            List orgNodeCListFromDB = Arrays.asList(OrgNodeCFromDB);
            //process frameWork
            for (int i = 0; i < orgNodeCListFromUI.size(); i++) {
                
                //if OrgNodeCategoryId is null, then OrgNodeCategory will be new 
                 
                if ((((OrgNodeCategory)orgNodeCListFromUI.get(i)).
                                getOrgNodeCategoryId().intValue() == 0)
                       || (((OrgNodeCategory)orgNodeCListFromUI.get(i)).
                                getOrgNodeCategoryId().intValue() 
                                == CTBConstants.DEFAULT_STATE_CATEGORY_ID)) {
                    
                         createFrameworkList.add(orgNodeCListFromUI.get(i));
                         continue;
                         
                }
                
                ListIterator iter = orgNodeCListFromDB.listIterator();
                
                while (iter.hasNext()) {
                    
                    OrgNodeCategory tempCategoryFromUI = 
                                (OrgNodeCategory)orgNodeCListFromUI.get(i);
                    OrgNodeCategory tempCategoryFromDB = 
                                (OrgNodeCategory)iter.next();
                    
                    /*  if orgNodeCategoryId from UI and orgNodeCategoryId 
                    *   from DB are same then OrgNodeCategory will be editable
                    */ 
                    
                    if ( tempCategoryFromUI.getOrgNodeCategoryId().intValue() 
                                == tempCategoryFromDB.getOrgNodeCategoryId().intValue()) {
                      
                          updateFrameworkList.add(tempCategoryFromUI);
                          
                    }
                }
            }
            
            //Delete OrganizationList
            
            for ( int i = 0; i < orgNodeCListFromDB.size(); i++ ) {
                
                OrgNodeCategory orgNodeCategoryDB = 
                                (OrgNodeCategory)orgNodeCListFromDB.get(i);
                
                boolean flag = false;
                for (int j = 0; j < updateFrameworkList.size(); j++ ) {
                    
                    OrgNodeCategory orgNodeCategoryUL = (OrgNodeCategory)
                                                            updateFrameworkList.get(j);
                    if (orgNodeCategoryDB.getOrgNodeCategoryId().intValue() 
                            == orgNodeCategoryUL.getOrgNodeCategoryId().intValue() ) {
                                
                        flag = true;
                        break;
                        
                    }
                }
                if (!flag) {
                    
                    deleteFrameworkList.add(orgNodeCategoryDB);
                }
                
                
            }
            
            if (createFrameworkList.size() > 0) {
                
                //createFrameWork Call
                createFrameWork(loginUserName,
                                    (OrgNodeCategory [])createFrameworkList.toArray(
                                    new OrgNodeCategory[0]), 
                                    customer.getCustomerId());
                                                                     
            }
            
            if (updateFrameworkList.size() > 0) {
                
               //modifyFrameWork Call 
               modifyFrameWork (loginUserName,
                                    (OrgNodeCategory [])updateFrameworkList.toArray(
                                    new OrgNodeCategory[0])); 
            }
            
            if (deleteFrameworkList.size () > 0) {
                
               //deleteFrameWork Call 
               deleteFrameWork(loginUserName,
                                (OrgNodeCategory [])deleteFrameworkList.toArray(
                                                            new OrgNodeCategory[0]));
            }
            
            
        } catch (SQLException se) {
            FrameWorkUpdationException updateException = 
                                        new FrameWorkUpdationException
                                                ("EditFrameWork.Failed");
                updateException.setStackTrace(se.getStackTrace());
                throw updateException;
            
        } catch (CTBBusinessException e) {
             FrameWorkUpdationException updateException = 
                                        new FrameWorkUpdationException
                                                ("EditFrameWork.Failed");
             updateException.setStackTrace(e.getStackTrace());
                throw updateException;
            
        } catch (Exception e) {
             FrameWorkUpdationException updateException = 
                                        new FrameWorkUpdationException
                                                ("EditFrameWork.Failed");
             updateException.setStackTrace(e.getStackTrace());
                throw updateException;
            
        }
    }
      

    /**
     * get selected customer details
     * @common:operation
     * @param userName - identifies the login user
     * @param selectedCustomer - containes the selected customer data
     * @returns Customer
     * @throws CTBBusinessException
     */
    public Customer getCustomer(String userName, Customer selectedCustomer)
                                                throws CTBBusinessException{
        
        String statePr = "";
        Integer billingAddressId = null;
        Integer mailingAddressId = null;
        Integer selectedCustomerId =  selectedCustomer.getCustomerId();
                      
        try {
            Customer customer = customers.getCustomerDetails(selectedCustomerId); 
            
            OrgNodeCategory orgNodeCategory[] = orgNodeCate.
                                                    getOrgNodeCategories(
                                                    selectedCustomerId);
            
            for( int i = 0; i<orgNodeCategory.length; i++ ){
                OrgNodeCategory orgNdcat = orgNodeCategory[i];
                
                if ( orgNdcat.getCategoryLevel().intValue() == 1 ){
                    
                    orgNdcat.setBeforeInsertable(Boolean.FALSE);
                    
                } else {
                    
                    orgNdcat.setBeforeInsertable(Boolean.TRUE);
                    
                }
                
                Node[] orgNode = orgNodeCate.getOrgNodeForCategory(orgNdcat.
                                                                getOrgNodeCategoryId());
				//START - Changes For LASLINK Product                                                
                if ( orgNdcat.getCategoryLevel().intValue() == 1 ){
                	
                	customer.setMdrNumber(orgNode[0].getMdrNumber() );
                    
                } 
				//END - Changes For LASLINK Product
                if( orgNode != null && orgNode.length > 0 ){
                    
                    orgNdcat.setDeletable(Boolean.FALSE);
                    
                } else {
                    
                    orgNdcat.setDeletable(Boolean.TRUE);
                }
                
                Student[] stu = orgNodeCate.getStudentForCategory(orgNdcat.
                                                                    getOrgNodeCategoryId());
                if ( stu != null && stu.length > 0 ){
                    
                    orgNdcat.setAfterInsertable(Boolean.FALSE);
                    
                } else {
                    
                    orgNdcat.setAfterInsertable(Boolean.TRUE);
                    
                }
                
            }
            
            customer.setOrgNodeCategory(orgNodeCategory);
            
            CustomerConfig[] customerConfigurations
                         =  customers.getCustomerConfigurations(selectedCustomerId);
                                    
            if ( customerConfigurations.length > 0 ) {
                
                if ( customerConfigurations[0].getCustomerConfigurationName().equals(
                                CTBConstants.DB_TABE_CUSTOMER)){
                                    
                    customerConfigurations[0].setCustomerConfigurationName(
                                CTBConstants.TABE_CUSTOMER);
                                                            
                }
                else if( customerConfigurations[0].getCustomerConfigurationName().equals(
                                CTBConstants.DB_TERRANOVA_CUSTOMER)){
                                    
                    customerConfigurations[0].setCustomerConfigurationName(
                                CTBConstants.TERRANOVA_CUSTOMER);
                                                            
                }
                //START - Changes For LASLINK Product
                else if( customerConfigurations[0].getCustomerConfigurationName().equals(
                        CTBConstants.DB_LASLINK_CUSTOMER)){
                            
                	customerConfigurations[0].setCustomerConfigurationName(
                        CTBConstants.LASLINK_CUSTOMER);
                                                    
       			 }
       			//END - Changes For LASLINK Product
                
                //START - Changes For LLESPANOL Product
                else if( customerConfigurations[0].getCustomerConfigurationName().equals(
                        CTBConstants.DB_LLESPANOL_CUSTOMER)){
                            
                	customerConfigurations[0].setCustomerConfigurationName(
                        CTBConstants.LLESPANOL_CUSTOMER);
                                                    
       			 }
       			//END - Changes For LLESPANOL Product
                
              //START - Changes For TABE ADAPTIVE Product
                else if( customerConfigurations[0].getCustomerConfigurationName().equals(
                        CTBConstants.DB_TABE_ADAPTIVE_CUSTOMER)){
                            
                	customerConfigurations[0].setCustomerConfigurationName(
                        CTBConstants.TABE_ADAPTIVE_CUSTOMER);
                                                    
       			 }
       			//END - Changes For TABE ADAPTIVE Product


                customer.setCustomerConfiguration(customerConfigurations);
            } 
            
            else {
                
                CustomerConfig custConfig = new CustomerConfig();
                custConfig.setCustomerConfigurationName(CTBConstants.OTHER_CUSTOMER);
                customerConfigurations = new CustomerConfig[1];
                customerConfigurations[0] = custConfig;
                customer.setCustomerConfiguration(customerConfigurations);
                
            }
            
            
            billingAddressId = customer.getBillingAddressId();
            mailingAddressId = customer.getMailingAddressId();
            
            statePr = customer.getStatePr();
            if (statePr != null || "".equals(statePr) ) {
                
                USState state = addresses.getState(statePr);
                customer.setState(state);
                
            }
            
            if ( billingAddressId != null ) {
                
                  //retrive selected customer Billing Address   
                  Address billingAddress = addresses.getAddress(billingAddressId);
                  customer.setBillingAddress(billingAddress);
                  
            }
            if ( mailingAddressId != null ) {
                
                //retrive selected customer Mailinging Address 
                  Address mailingAddress = addresses.getAddress(mailingAddressId);
                  customer.setMailingAddress(mailingAddress);
                  
            }
           //update for Products
           Integer[] productTestCatalogIds =  customers.getCustomerTestCatalogs(selectedCustomerId);
           ArrayList products = new ArrayList();          
           if(null != productTestCatalogIds && productTestCatalogIds.length > 0){
	           if(Arrays.asList(productTestCatalogIds).contains(13196)){
	        	   products.add(CTBConstants.CUSTOMER_PRODUCT_FORMA);
	           }
	           if(Arrays.asList(productTestCatalogIds).contains(13216)){
	        	   products.add(CTBConstants.CUSTOMER_PRODUCT_FORMB);
	           }
	           if(Arrays.asList(productTestCatalogIds).contains(13217)){
	        	   products.add(CTBConstants.CUSTOMER_PRODUCT_ESPANOL);
	           }
           }
           String[] customerSelectedProducts = new String[products.size()];
           int counter = 0;
           for(Object product : products){
        	   customerSelectedProducts[counter++] = (String)product;
           }
           customer.setProductList(customerSelectedProducts);
           return customer;
            
        } catch (SQLException se) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("ViewCustomer.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
            throw dataRetrivalException;
                        
        } catch (Exception e) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("ViewCustomer.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }  
    }
    
     /**
      * get states array for state dropdown menu
      * @common:operation
      * @throws CTBBusinessException
      * @returns USState[]
      * @throws CTBBusinessException
      */
    public USState[] getStates() throws CTBBusinessException{
        try {
            
            String key = CTBConstants.STATE_KEY;
            USState[] states = (USState[]) SimpleCache.checkCache(
                                    CTBConstants.US_STATE_ARRAY,
                                    key,
                                    CTBConstants.MANAGE_CUSTOMER);
            if ( states == null ) {
                
                states = addresses.getStates();
                SimpleCache.cacheResult(CTBConstants.US_STATE_ARRAY,
                                    key,
                                    states,
                                    CTBConstants.MANAGE_CUSTOMER);
                                        
            }
            
            return states;
        } catch(SQLException se) {
            OrgDataNotFoundException dataNotfound = 
                                        new OrgDataNotFoundException
                                                ("CustomerManagement.Failed");
            dataNotfound.setStackTrace(se.getStackTrace());
            throw dataNotfound;
        } catch (Exception e) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("CustomerManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }

    /**
     * @common:operation
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    public Node getTopOrgNodeForCustomer(Integer customerId) 
                                         throws CTBBusinessException {
        
         try {
            Node topNodeForCustomer = orgNode.getTopOrgNodeForCustomer(customerId);
            return topNodeForCustomer;
         } catch (SQLException se) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("CustomerManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
            throw dataRetrivalException;
                        
        } catch (Exception e) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("CustomerManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
    
    /**
     * get parent node for an organization of the customer
     * @common:operation
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    public Node getParentOrgNodeForCustomer(Integer customerId) 
                                            throws CTBBusinessException {
        
         try {
            Node parentNodeForCustomer = orgNode.
                                            getParentOrgNodeForCustomer(customerId);
            return parentNodeForCustomer;
         } catch (SQLException se) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("CustomerManagement.Failed");
            dataRetrivalException.setStackTrace(se.getStackTrace());
            throw dataRetrivalException;
                        
        } catch (Exception e) {
            CustomerDataRetrivalException dataRetrivalException = 
                                        new CustomerDataRetrivalException
                                                ("CustomerManagement.Failed");
            dataRetrivalException.setStackTrace(e.getStackTrace());
            throw dataRetrivalException;
        }
    }
    
    
    /**
     * get parent node for an organization of the customer
     * @common:operation
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    public void setSDSConfiguration(Integer customerId, String customerName) 
                                 throws CTBBusinessException {

        
         try {
                sdsAssignment.addCustomerToADS(customerId, customerName);
                sdsAssignment.configureSDSForCustomer(customerId);
           
         } catch (SQLException e) {
            FrameWorkCreationException dataCreationException =
                                        new FrameWorkCreationException
                                            ("CustomerManagement.Failed");
            dataCreationException.setStackTrace(e.getStackTrace()); 
            throw dataCreationException;

         } catch (Exception e) {
            FrameWorkCreationException dataCreationException =
                                        new FrameWorkCreationException
                                            ("CustomerManagement.Failed");
            dataCreationException.setStackTrace(e.getStackTrace()); 
            throw dataCreationException;     
         }
    }
    
    
     //////////////////////PRIVATE METHODS//////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////   
    
     /**
     * This is just a private method used by 
     * getCustomers 
     */
    private CustomerData getCustomerData(String userName,
                                        FilterParams filter, 
                                        PageParams page,
                                        SortParams sort)
                                        throws SQLException, CTBBusinessException {
                                            
        CustomerData customerData = new CustomerData();
        FindCustomerData findCustomerData = new FindCustomerData();
        User loginUser = users.getUserDetails(userName);
        Integer pageSize = page == null ? null : new Integer(page.getPageSize());
        Integer totalCount = null;
        boolean isStateFilter = false;
        String searchCriteria = "";
        FindCustomer[] findCustomers = null;
        String enableFlag = "TTTTT";
     
        findCustomers = customers.getCustomers(CTBConstants.MAX_CTB_CUSTOMER);
        findCustomerData.setCustomers(findCustomers, pageSize);
        
        if ( filter != null ) {
            
            findCustomerData.applyFiltering(filter);   
                     
        }
        if ( sort != null ) {
            
            findCustomerData.applySorting(sort);
            
        }
        if ( page != null ) {
            
            findCustomerData.applyPaging(page); 
            
        }
        
        findCustomers = findCustomerData.getCustomers();
        Customer[] customerArray = new Customer[findCustomers.length];
        for ( int i = 0; i < findCustomers.length; i++ ) {
				 
             if (findCustomers[i] != null) {
                
                customerArray[i] = new Customer(findCustomers[i]);
                customerArray[i].setEditable(enableFlag + findCustomers[i].
                    getAllowSubscription());
                
             }  
             
         }
        
        customerData.setCustomers(customerArray, pageSize);
        customerData.setFilteredCount(findCustomerData.getFilteredCount());
        customerData.setFilteredPages(findCustomerData.getFilteredPages());
        customerData.setTotalCount(findCustomerData.getTotalCount());
        customerData.setTotalPages(findCustomerData.getTotalPages());  
        
        for ( int i = 0; i < customerData.getCustomers().length; i++ ) {
            
            if (customerData.getCustomers()[i] == null 
                        || customerData.getCustomers()[i].getCustomerId() == null) {
                            
                continue;
                
            }
            
            CustomerConfig[] customerConfigurations = 
                                customers.getCustomerConfigurations(
                                customerData.getCustomers()[i].getCustomerId());
            customerData.getCustomers()[i].
                                setCustomerConfiguration(customerConfigurations);
           
                        
        }
                    
        
        return customerData;                                            
    }
    
     /**
     * This is just a private method used by 
     * createCustomer
     */
    private void createFrameWork(String loginUserName, 
                                 OrgNodeCategory orgNodeCategory[],
                                 Integer customerId) 
                                 throws CTBBusinessException {

        Integer loginUserId = null;

         try {
            loginUserId = users.getUserDetails(loginUserName).getUserId();
            for (int i = 0; i < orgNodeCategory.length; i++) {
                orgNodeCategory [i].setCustomerId(customerId);
                orgNodeCategory [i].setActivationStatus(
                                CTBConstants.ACTIVATION_STATUS_ACTIVE);
                orgNodeCategory [i].setIsGroup(CTBConstants.F);
                orgNodeCategory [i].setCreatedBy(loginUserId);
                orgNodeCategory [i].setCreatedDateTime(new Date());
                orgNodeCate.createOrgNodeCategory(orgNodeCategory [i]);

            }


         } catch (SQLException e) {
            FrameWorkCreationException dataCreationException =
                                        new FrameWorkCreationException
                                            ("CustomerManagement.Failed");
            dataCreationException.setStackTrace(e.getStackTrace());
            throw dataCreationException;                                            

         } catch (Exception e) {
            FrameWorkCreationException dataCreationException =
                                        new FrameWorkCreationException
                                            ("CustomerManagement.Failed");
            dataCreationException.setStackTrace(e.getStackTrace()); 
            throw dataCreationException;     
         }
    }
    
    /**
     * This is just a private method used by 
     * createCustomer
     */

    private void createTopOrgNode(String loginUserName, 
                                  Customer customer) 
                                  throws CTBBusinessException {

        Integer loginUserId = null;

        try {
            Integer userNode[] = orgNode.getTopOrgNodeIdsForUser(loginUserName);
           
            OrgNodeCategory orgNodeCategory[] = orgNodeCate.
                                                getOrgNodeCategories(
                                                customer.getCustomerId());
            OrgNodeCategory maxOrgNodeCategory = null;
            
            if ( orgNodeCategory != null && orgNodeCategory.length > 0 ) {
                
                maxOrgNodeCategory = orgNodeCategory[0];
                
                for (int i=1; i<orgNodeCategory.length; i++) {
                    
                    if ( maxOrgNodeCategory.getCategoryLevel().intValue() 
                            > orgNodeCategory[i-1].getCategoryLevel().intValue() ) {
                                
                        maxOrgNodeCategory = orgNodeCategory[i];
                        
                    }
                }
                
            }
           
           if ( userNode != null && userNode.length > 0 
                        && maxOrgNodeCategory != null ){
               
               Node topNode = new Node();
               topNode.setOrgNodeName(customer.getCustomerName());
               topNode.setOrgNodeCategoryId(maxOrgNodeCategory.getOrgNodeCategoryId());
               topNode.setParentOrgNodeId(userNode[0]);
               topNode.setCustomerId(customer.getCustomerId());
               //START - Changes for LASLINK PRODUCT 
               if(customer.getMdrNumber() != null ){
            	   topNode.setMdrNumber(customer.getMdrNumber());
               }
               //END - Changes for LASLINK PRODUCT 
               organizationManagement.createOrganization(loginUserName,topNode);
               
           }

         } catch (SQLException se) {
               CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "CustomerManagement.Failed");
               dateCreationException.setStackTrace(se.getStackTrace());
               throw dateCreationException;
         } catch (Exception e) {
               CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "CustomerManagement.Failed");
               dateCreationException.setStackTrace(e.getStackTrace());
               throw dateCreationException;
         }
    }

   /**
     * This is just a private method used by 
     * updateFrameWork
     */ 
    private void deleteFrameWork(String loginUserName, OrgNodeCategory orgNodeCategory[]) 
                                                throws CTBBusinessException {
                                                    
        Integer loginUserId = null;                                            
        try {
            loginUserId = users.getUserDetails(loginUserName).getUserId();
                for ( int i = 0; i < orgNodeCategory.length; i++ ) {
                    orgNodeCategory [i].setUpdatedBy(loginUserId);
                    orgNodeCate.deleteOrgNodeCategory(orgNodeCategory [i]);
                }
        } catch (Exception e) {
            FrameWorkDeletionException dataDeletionException =
                                        new FrameWorkDeletionException
                                            ("DeleteFramework.Failed");
            dataDeletionException.setStackTrace(e.getStackTrace());
            throw dataDeletionException;                                            
        }
    }

   /**
     * This is just a private method used by 
     * updateFrameWork
     */ 
    private void modifyFrameWork(String loginUserName, 
                                 OrgNodeCategory orgNodeCategory[]) 
                                 throws CTBBusinessException {
        Integer loginUserId = null;

         try {
            loginUserId = users.getUserDetails(loginUserName).getUserId();
            for (int i = 0; i < orgNodeCategory.length; i++) {

                orgNodeCategory [i].setActivationStatus(
                                    CTBConstants.ACTIVATION_STATUS_ACTIVE);
                orgNodeCategory [i].setUpdatedBy(loginUserId);
                orgNodeCategory [i].setUpdatedDateTime(new Date());
                orgNodeCate.editOrgNodeCategory(orgNodeCategory [i]);
            }
        } catch (Exception e) {
            FrameWorkUpdationException dataUpdationException =
                                        new FrameWorkUpdationException
                                            ("CustomerManagement.Failed");
            dataUpdationException.setStackTrace(e.getStackTrace());
            throw dataUpdationException;                                            
         }
    }

    /**
     * This is just a private method used by 
     * createCustomer
     */
    
     private void saveCustomerEmail(Customer customer) throws CTBBusinessException { 
                                       

        try {
            CustomerEmail  customerEmail = new CustomerEmail();
            customerEmail.setCustomerId(customer.getCustomerId());
            customerEmail.setCreatedDateTime(customer.getCreatedDateTime());
            customerEmail.setCreatedBy(customer.getCreatedBy());
            customerEmail.setContactPhone(customer.getContactPhone());
                   
                   
            CustomerEmail[] customerEmailList = customers.getDefaultEmails();
            for (int i=0; i<customerEmailList.length; i++){ 
                
                CustomerEmail customerEmailIns = customerEmailList[i];
                
                if(customerEmailIns.getEmailType().intValue() == 1){     
                    //For Welcome Type Email
                    customerEmail.setEmailBody(customerEmailIns.getEmailBody());
                    customerEmail.setEmailType(customerEmailIns.getEmailType());
                    customerEmail.setSubject(customerEmailIns.getSubject());
                    customerEmail.setReplyTo(customerEmailIns.getReplyTo());
                    
                    customers.saveCustomerEmail(customerEmail);
                }
                else if (customerEmailIns.getEmailType().intValue() == 2){ 
                    //For Password Type Email
                    customerEmail.setEmailBody(customerEmailIns.getEmailBody());
                    customerEmail.setEmailType(customerEmailIns.getEmailType());
                    customerEmail.setSubject(customerEmailIns.getSubject());
                    customerEmail.setReplyTo(customerEmailIns.getReplyTo());
                  
                    customers.saveCustomerEmail(customerEmail);
                }
                else if(customerEmailIns.getEmailType().intValue() == 3){    
                    //For Password Notification Type Email
                    customerEmail.setEmailBody(customerEmailIns.getEmailBody());
                    customerEmail.setEmailType(customerEmailIns.getEmailType());
                    customerEmail.setSubject(customerEmailIns.getSubject());
                    customerEmail.setReplyTo(customerEmailIns.getReplyTo());
                
                    customers.saveCustomerEmail(customerEmail);
                }
            
            }

        }catch (SQLException se) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
     }
     
     /**
     * This is just a private method used by 
     * createCustomer
     */
    
     private void saveStaticCustomerEmail(Customer customer) throws CTBBusinessException { 
                                       

        try {
            String content = "";
            String subject = "";
           
                     
           //For Welcome Type Email
            String  welcomecontent = "Welcome to the Online Assessment System (OAS),"+
                                         "provided by CTB/McGraw-Hill (www.ctb.com). " +
                                         "Your account has been set up and your username " +
                                         "is:  <#userid#>" + 
                                         "\n\nAccess OAS at the following URL: " +
                                          CTBConstants.OAS_PRODUCTION_URL +
                                         "\n\nFor security purposes, your password will be "+
                                         "sent to you in a separate email." +
                                         "\nPlease watch your inbox for this message." +
                                         "\nFor any questions about set up or access, "+
                                         "just call your Account Manager at" +
                                         "\n(888) 630-1102.";
                                                 
                                               
           
            CustomerEmail  customerEmail = new CustomerEmail();
            customerEmail.setCustomerId(customer.getCustomerId());
            customerEmail.setEmailBodyStr(welcomecontent);
            customerEmail.setEmailType(CTBConstants.EMAIL_TYPE_WELCOME);
            customerEmail.setSubject(CTBConstants.MAIL_LOGIN_SUBJECT);
            customerEmail.setCreatedDateTime(customer.getCreatedDateTime());
            customerEmail.setCreatedBy(customer.getCreatedBy());
            customerEmail.setContactPhone(customer.getContactPhone());
            
            //Insert data into customer_email_configuration
            
            customers.saveCustomerEmail(customerEmail);
            
            String passwordContent = "Your Online Assessment System (OAS) password is"+
                                     " <#password#> " +
                                     "\n\nYour username has been sent in a separate email.";
            
            
            
            customerEmail.setEmailBodyStr(passwordContent);
            customerEmail.setEmailType(CTBConstants.EMAIL_TYPE_PASSWORD);
            customerEmail.setSubject(CTBConstants.MAIL_PASSWORD_SUBJECT);
            customers.saveCustomerEmail(customerEmail);
            
            String passwordNotification = "Your Online Assessment System (OAS) "+
                                          "password has been changed." +
                                          "If you are unaware of this change and/or "+
                                          "have not authorized or requested it, "+
                                          "please contact your OAS Administrator.";
            
                            
            customerEmail.setEmailBodyStr(passwordNotification);
            customerEmail.setEmailType(CTBConstants.EMAIL_TYPE_NOTIFICATION);
            customerEmail.setSubject(CTBConstants.MAIL_PASSWORD_NOTIFICATION);
            customers.saveCustomerEmail(customerEmail);

        }catch (SQLException se) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
                                                
            dateCreationException.setStackTrace(se.getStackTrace());
            throw dateCreationException;
        } catch (Exception e) {
            CustomerCreationException dateCreationException = 
                                        new CustomerCreationException(
                                                "AddCustomer.Failed");
            dateCreationException.setStackTrace(e.getStackTrace());
            throw dateCreationException;
        }
     }
     
     
      
} 
