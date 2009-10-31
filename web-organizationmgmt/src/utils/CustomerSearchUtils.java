package utils; 

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Customer;
import com.ctb.bean.testAdmin.CustomerData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeCategory;
import com.ctb.control.customerManagement.CustomerManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.widgets.bean.PagerSummary;
import dto.CustomerProfileInformation;
import dto.Level;
import dto.Message;
import java.util.ArrayList;
import java.util.List;
import manageCustomer.ManageCustomerController.ManageCustomerForm;

public class CustomerSearchUtils 
{ 
    /**
     * buildcustomerPagerSummary
     */    
    public static PagerSummary buildUserPagerSummary(CustomerData customerData, 
                                                Integer pageRequested) {
        PagerSummary pagerSummary = new PagerSummary();
        pagerSummary.setCurrentPage(pageRequested);                
        pagerSummary.setTotalObjects(customerData.getTotalCount());
        pagerSummary.setTotalPages(customerData.getFilteredPages());
        pagerSummary.setTotalFilteredObjects(customerData.getFilteredCount());  
                  
        return pagerSummary;
    }
    
    /*
     * Put all the customer detals in the list
    */
    public static List buildCustomerList (CustomerData customerData) {
        
        List customerList = new ArrayList();
        if (customerData != null){
           
            Customer[] customers = customerData.getCustomers();
           
            if ( customers != null) {
               
                for ( int i = 0; i < customers.length; i++ ) {
                    Customer customer = customers[i];
           
                    if ( customer != null && customer.getCustomerId() != null) {
           
                        CustomerProfileInformation customerDetails =
                                        new CustomerProfileInformation(customer);
                        customerList.add(customerDetails);                                        
                    }
                }
            }
        }
        return customerList;
    }
    
    /**
     * getUserProfileInformation
     */    
    public static CustomerProfileInformation getCustomerProfileInformation(
                                        CustomerManagement customerManagement, 
                                        String loginUserName, 
                                        Customer selectedCustomer) 
                                        throws CTBBusinessException {    
        CustomerProfileInformation customerProfileInfo = 
                                              new CustomerProfileInformation();         
        Customer customer = customerManagement.getCustomer(loginUserName, 
                                                           selectedCustomer);
        customerProfileInfo = new CustomerProfileInformation(customer);   
        
        return customerProfileInfo;
    }
    
     /**
     * getCustomerProfileInformation
     */    
    public static CustomerProfileInformation getCustomerProfileInformation(
                                                Integer customerId, 
                                                List customerList){    
        CustomerProfileInformation cpi = new CustomerProfileInformation();
        for ( int i = 0 ; i < customerList.size() ; i++) {
             cpi = (CustomerProfileInformation) customerList.get(i);
            if (cpi.getId().equals(customerId)) {
                
                return cpi; 
            
            }
        }          
        return cpi;
    }
    
     /**
     * fetch customer from list
     * @param customerId Integer
     * @param customerList List
     * @return CustomerProfileInformation
     */
    
     public static CustomerProfileInformation getCustomerNameFromList
                                    (Integer customerId, List customerList) {
        CustomerProfileInformation customer = new CustomerProfileInformation();
        for ( int i = 0; i < customerList.size(); i++ ) {
            customer = (CustomerProfileInformation) customerList.get(i);
            if ( customer.getId().equals(customerId) ) {
               
                return customer;
            
            } 
        }
        return customer;
    }
    
    /*
     * Searchs all customers if no criteria has given 
    */
    public static CustomerData searchAllCustomers(
                                           CustomerManagement customerManagement,
                                           String userName,
                                           PageParams page,
                                           SortParams sort) 
                                           throws CTBBusinessException {
        
        CustomerData customerData = customerManagement.getCustomers(userName,
                                                                    null,    
                                                                    page,
                                                                    sort);
        return customerData;
    }
    
    /*
     *  Searches customer basesd on search criteria
    */
    
    public static CustomerData searchCustomersByCriteria(
                                           CustomerManagement customerManagement,
                                           String userName,
                                           FilterParams filter,
                                           PageParams page,
                                           SortParams sort) 
                                           throws CTBBusinessException {
    
        CustomerData customerData = customerManagement.getCustomers(userName,
                                                                    filter,    
                                                                    page,
                                                                    sort);
        return customerData;
    }
    
    /*
     * setFrameworkDetailsFromList
    */
    
    public static OrgNodeCategory[] setFrameworkDetailsFromList(List frameWork){
        
        OrgNodeCategory[] orgNodeCategories = new OrgNodeCategory[frameWork.size()];
        
        for ( int i = 0; i < frameWork.size() ; i++ ) {
            Level level = new Level();
            if ( frameWork.get(i) != null ) {
                
                int levelcount = i;
                level = (Level)frameWork.get(i);
                OrgNodeCategory orgNodeCategory = new OrgNodeCategory ();
                orgNodeCategory.setOrgNodeCategoryId(level.getId());
                orgNodeCategory.setCategoryName(level.getName());
                orgNodeCategory.setCategoryLevel(new Integer(++levelcount));    
                orgNodeCategories[i] = orgNodeCategory;
            
            }
        }
       return orgNodeCategories;  
    }
    
      /**
     * add customer to list
     * @param customerProfile CustomerProfileInformation
     * @param customerList List
     * @return none
     */
     public static void addCustomerInCustomerList(
                                              CustomerProfileInformation customerProfile, 
                                              List customerList) {
        customerList.add(customerProfile.createClone());
    }
    
    
     /**
     * get Top node for customer
     * @param customerManagement CustomerManagement
     * @param form ManageCustomerForm
     * @return Node
     */
     
    public static Node getTopNodeForCustomer (CustomerManagement customerManagement,
                                              ManageCustomerForm form,
                                              String title){
      
      Node topNode = new Node();  
      try {
         topNode = customerManagement.getTopOrgNodeForCustomer(
                                              form.getSelectedCustomerId());
      } catch ( Exception be ) {
            be.printStackTrace();
            String msg = MessageResourceBundle.getMessage("CustomerManagement.Failed");                                        
            form.setMessage(title, msg, Message.ERROR);
            return null;
        }
      return topNode;
    }
}
    


