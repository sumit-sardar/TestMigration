
package com.ctb.control.dataExportManagement;


import java.util.List;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.dataExportManagement.ManageJobData;
import com.ctb.bean.dataExportManagement.ManageStudentData;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ManageTestSessionData;
import com.ctb.exception.CTBBusinessException;

/** 
 *
 * @author John_Wang
 */ 
@ControlInterface()
public interface DataExportManagement 
{ 

    /**
     * Get user information including full name and system id for the specified
     * user name. If the specified user lies withing the requesting user's
     * visible hierarchy, all fields are returned - if not, only the first, last,
     * and middle names of the specified user are returned. Each user object
     * contains a Customer object, which contains information about the user's
     * customer, including a flag, hideAccommodations, which indicates whether
     * accommodation-related UI elements should be hidden for the specified user.
     * @param userName - identifies the calling user
     * @param detailUserName - identifies the user whose information is desired
     * @return User
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.User getUserDetails(java.lang.String userName, java.lang.String detailUserName) throws com.ctb.exception.CTBBusinessException;
 // Start - LLO- 118 - Change for Ematrix UI
    
    /**
     * Get customer configuration for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return CustomerConfiguration []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.dataExportManagement.CustomerConfiguration[] getCustomerConfigurations(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;


   /**
     * Retrieves the no of reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return boolean value
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    boolean userHasReports(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

  
    /**
     * New method added for CR - GA2011CR001
     * Get customer configuration value for the specified customer configuration.
     * @common:operation
     * @param configId - identifies the customerconfiguration whose information is desired
     * @return CustomerConfigurationValue []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.dataExportManagement.CustomerConfigurationValue[] getCustomerConfigurationsValue(java.lang.Integer configId) throws com.ctb.exception.CTBBusinessException;
    
	
	ManageStudentData getIncompleteRosterUnexportedStudents(Integer customerId, FilterParams filter, PageParams page,	SortParams sort)throws com.ctb.exception.CTBBusinessException;
	
	
	ManageStudentData getAllUnscoredUnexportedStudentsDetail(List toBeExportedStudentRosterList,Integer customerId, FilterParams filter, PageParams page,	SortParams sort)throws com.ctb.exception.CTBBusinessException;

	ManageJobData getDataExportJobStatus(Integer userId, FilterParams filter, PageParams page, SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	
	ManageTestSessionData getTestSessionsWithUnexportedStudents(Integer customerId, FilterParams filter, PageParams page, SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
	Integer getSubmitJobIdAndStartExport(Integer userId, Integer studentCount) throws com.ctb.exception.CTBBusinessException;
	 
	//End - LLO- 118 - Change for Ematrix UI
} 	


