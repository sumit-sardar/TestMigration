package com.ctb.control.customerManagement; 

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.CustomerData;
import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface()
public interface CustomerManagement 
{ 

    /**
     * update customer record
     * @param loginName - identifies the login user
     * @param customer - contains the existing customer information
     * @return nothing
     * @throws CTBBusinessException
     */
    
    void updateCustomer(java.lang.String loginName, com.ctb.bean.testAdmin.Customer customer) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create new Customer Record
     * @param userName - identifies the login user
     * @param User - contains the new customer information
     * @return String - created customer's id
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Customer createCustomer(java.lang.String loginUserName, com.ctb.bean.testAdmin.Customer customer) throws com.ctb.exception.CTBBusinessException;

    /**
     * get all the customer viewable for the login user
     * @param userName - identifies the login user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @returns CustomerData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerData getCustomers(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * get all the customer viewable for the login user
     * @param userName - identifies the login user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @returns nothing
     * @throws CTBBusinessException
     */
    
    void updateFrameWork(java.lang.String loginUserName, com.ctb.bean.testAdmin.Customer customer) throws com.ctb.exception.CTBBusinessException;

    /**
     * get selected customer details
     * @param userName - identifies the login user
     * @param selectedCustomer - containes the selected customer data
     * @returns Customer
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Customer getCustomer(java.lang.String userName, com.ctb.bean.testAdmin.Customer selectedCustomer) throws com.ctb.exception.CTBBusinessException;

    /**
     * get states array for state dropdown menu
     * @throws CTBBusinessException
     * @returns USState[]
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.USState[] getStates() throws com.ctb.exception.CTBBusinessException;

    /**
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node getTopOrgNodeForCustomer(java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * get parent node for an organization of the customer
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Node getParentOrgNodeForCustomer(java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;
    

    /**
     * get parent node for an organization of the customer
     * @param customerId - identifies the customer
     * @throws CTBBusinessException
     * @return Node
     * @throws CTBBusinessException
     */
    
    void setSDSConfiguration(java.lang.Integer customerId, java.lang.String customerName) throws com.ctb.exception.CTBBusinessException;
} 
