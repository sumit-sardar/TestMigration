package com.ctb.control.dataExportManagement;

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.dataExportManagement.CustomerConfiguration;
import com.ctb.bean.dataExportManagement.CustomerConfigurationValue;
import com.ctb.bean.dataExportManagement.ManageJob;
import com.ctb.bean.dataExportManagement.ManageJobData;
import com.ctb.bean.dataExportManagement.ManageStudent;
import com.ctb.bean.dataExportManagement.ManageStudentData;
import com.ctb.bean.dataExportManagement.ManageTestSession;
import com.ctb.bean.dataExportManagement.ManageTestSessionData;
import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.dataExportManagement.CustomerConfigurationDataNotFoundException;
import com.ctb.exception.dataExportManagement.CustomerReportDataNotFoundException;
import com.ctb.exception.dataExportManagement.JobDataNotFoundException;
import com.ctb.exception.dataExportManagement.StudentDataNotFoundException;
import com.ctb.exception.dataExportManagement.UserDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.DateUtils;
import java.util.Date;
import java.util.Hashtable;

/**
 * @author John_Wang
 *
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class DataExportManagementImpl implements DataExportManagement
{ 
	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.CustomerReportBridge reportBridge;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.TestRoster testRosters;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNodeStudent orgNodeStudents;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Students students;

	

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.OrgNode orgNode;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.Users users;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.validation.Validator validator;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.DataExportManagement dataExportManagement;

	static final long serialVersionUID = 1L;

	private static final int CTB_CUSTOMER_ID =2;
	private String findInColumn = "ona.ancestor_org_node_id in ";

	
	/**
	 * Get customer configuration for the specified customer.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @return CustomerConfiguration []
	 * @throws CTBBusinessException
	 */
	public CustomerConfiguration [] getCustomerConfigurations(String userName, Integer customerId) throws CTBBusinessException
	{
		validator.validateCustomer(userName, customerId, "DataExportManagementImpl.getCustomerConfigurations");
		try {
			CustomerConfiguration [] customerConfigurations = dataExportManagement.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = dataExportManagement.getCustomerConfigurations(CTB_CUSTOMER_ID);
			}

			if (customerConfigurations != null && customerConfigurations.length > 0) 
			{
				for (int i = 0; i < customerConfigurations.length; i++) {
					CustomerConfiguration cutomerConfig = customerConfigurations[i];
					CustomerConfigurationValue [] customerConfigurationValues 
					= dataExportManagement.getCustomerConfigurationValues(cutomerConfig.getId().intValue());
					cutomerConfig.setCustomerConfigurationValues(customerConfigurationValues);              
				}
			}
			return customerConfigurations;
		} catch (SQLException se) {
			CustomerConfigurationDataNotFoundException tee = new CustomerConfigurationDataNotFoundException("StudentManagementImpl: getCustomerConfigurations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}


	}

	
	/**
	 * Get user information including full name and system id for the specified user name.
	 * If the specified user lies withing the requesting user's visible hierarchy,
	 * all fields are returned - if not, only the first, last, and middle names
	 * of the specified user are returned. Each user object contains a Customer object,
	 * which contains information about the user's customer, including a flag, hideAccommodations,
	 * which indicates whether accommodation-related UI elements should be hidden for
	 * the specified user.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param detailUserName - identifies the user whose information is desired
	 * @return User
	 * @throws CTBBusinessException
	 */
	public User getUserDetails(String userName, String detailUserName) throws CTBBusinessException{
		boolean hasPerms = true;
		try {
			validator.validateUser(userName, detailUserName, "DataExportManagementImpl.getUserDetails");
		} catch (ValidationException ve) {
			hasPerms = false;
		}
		try {
			User user = users.getUserDetails(detailUserName);
			user.setCustomer(users.getCustomer(detailUserName));
			user.setRole(users.getRole(detailUserName));
			if(!hasPerms) {
				User secureUser = new User();
				secureUser.setFirstName(user.getFirstName());
				secureUser.setLastName(user.getLastName());
				secureUser.setMiddleName(user.getMiddleName());
				secureUser.setUserId(user.getUserId());
				secureUser.setUserName(user.getUserName());
				secureUser.setCustomer(user.getCustomer());
				secureUser.setRole(user.getRole());
				return secureUser;
			} else {
				return user;
			}
		} catch (SQLException se) {
			CTBBusinessException cbe = new UserDataNotFoundException("DataExportManagementImpl: getUserDetails: " + se.getMessage());
			cbe.setStackTrace(se.getStackTrace());
			throw cbe;
		}  
	}
	

	/**
	
	/**
	
	/**
	 * Retrieves the no of reports  available to a user's customer
	 * @common:operation
	 * @param userName - identifies the user
	 * @param customerId - identifies the customer
	 * @return boolean value 
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public boolean userHasReports(String userName,Integer customerId) throws CTBBusinessException{
		validator.validate(userName, customerId, "userHasReports");
		try{            
			Integer noOfReports = reportBridge.getCustomerReports(customerId);            
			if(noOfReports.intValue() > 0){
				return true;
			}
			return false;
		}catch(SQLException se){
			CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("DataExportManagementImpl: userHasReports: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}
	/**
	 * New method added for CR - GA2011CR001
	 * Get customer configuration value for the specified customer configuration.
	 * @common:operation
	 * @param configId - identifies the customerconfiguration whose information is desired
	 * @return CustomerConfigurationValue []
	 * @throws CTBBusinessException
	 */
	public CustomerConfigurationValue [] getCustomerConfigurationsValue( Integer configId) throws CTBBusinessException
	{	
		try {
			CustomerConfigurationValue [] customerConfigurationValues 
			= dataExportManagement.getCustomerConfigurationValues(configId);
			return customerConfigurationValues;
		} catch (SQLException se) {
			CustomerConfigurationDataNotFoundException tee = new CustomerConfigurationDataNotFoundException("DataExportManagementImpl: getCustomerConfigurations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}
	
	

	@Override
	public ManageStudentData getIncompleteRosterUnexportedStudents(Integer customerId, FilterParams filter, PageParams page,	SortParams sort) throws CTBBusinessException{
		Integer scheduledStudentCount = 0;
		Integer notTakenStudentCount = 0;
		Integer notCompletedStudentCount = 0;
		ManageStudentData std = new ManageStudentData();
		ManageStudent [] students = null;
		Integer pageSize = null;
		if(page != null) {
			pageSize = new Integer(page.getPageSize());
		}
        
        try {
			students = dataExportManagement.getIncompleteRosterUnexportedStudents(customerId);
			
			// for setting the count of different status in student data
			for (int i = 0; i <students.length; i++ ){
				ManageStudent student = (ManageStudent)students[i];
				String completionStatus = student.getTestCompletionStatus();
				
				if (completionStatus.equals("SC"))
					scheduledStudentCount++;
				else if (completionStatus.equals("NT"))
					notTakenStudentCount++;
				else
					notCompletedStudentCount++;
			}
			std.setScheduledStudentCount(scheduledStudentCount);
			std.setNotTakenStudentCount(notTakenStudentCount);
			std.setNotCompletedStudentCount(notCompletedStudentCount);
			
			
			
			std.setManageStudents(students, pageSize);
			
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);
		} catch (SQLException e) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("DataExportManagementImpl: findStudentsByCustomerId: " + e.getMessage());
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
       

		
	
        return std;
	}

	
	@Override
	public ManageStudentData getAllUnscoredUnexportedStudents(Integer customerId, FilterParams filter, PageParams page,	SortParams sort) throws CTBBusinessException{
		
		ManageStudentData std = new ManageStudentData();
		ManageStudent [] students = null;
		Integer pageSize = null;
		if(page != null) {
			pageSize = new Integer(page.getPageSize());
		}
        
        try {
			students = dataExportManagement.getAllUnscoredUnexportedStudents(customerId);
			
			std.setManageStudents(students, pageSize);
			/*students = std.getManageStudents();
			
			 std.setManageStudents(students, pageSize);*/
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);
		} catch (SQLException e) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("DataExportManagementImpl: findStudentsByCustomerId: " + e.getMessage());
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}      		
	
        return std;
	}
	
	@Override
	public ManageJobData getDataExportJobStatus(Integer userId, FilterParams filter, PageParams page,	SortParams sort) throws CTBBusinessException{
		
		ManageJobData std = new ManageJobData();
		ManageJob [] jobs = null;
		Integer pageSize = null;
		if(page != null) {
			pageSize = new Integer(page.getPageSize());
		}
        
        try {
        	jobs = dataExportManagement.getDataExportJobStatus(userId);
			
			std.setManageJobs(jobs, pageSize);
			/*students = std.getManageStudents();
			
			 std.setManageStudents(students, pageSize);*/
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);
		} catch (SQLException e) {
			JobDataNotFoundException tee = new JobDataNotFoundException("DataExportManagementImpl: findStudentsByCustomerId: " + e.getMessage());
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
       

		
	
        return std;
	}

	@Override
	public ManageTestSessionData getTestSessionsWithUnexportedStudents(Integer customerId, FilterParams filter, PageParams page,	SortParams sort) throws CTBBusinessException{
		
		ManageTestSessionData mtsd = new ManageTestSessionData();
		ManageTestSession[] testSessions = null;
		Integer toBeExportedValue = new Integer(0);
		Integer completeValue = new Integer(0); 
		Integer notTakenValue = new Integer(0);
		Integer incompleteValue = new Integer(0);
		Integer scheduledValue = new Integer(0);
		Integer studentStopValue = new Integer(0);
		Integer systemStopValue = new Integer(0);
		Integer endedSystemStopValue = new Integer(0);
		Integer pageSize = null;
		Integer totalExportedStudentCount =  new Integer(0);
		Integer scheduledStudentCount = new Integer(0);
		Integer notTakenStudentCount = new Integer(0);
		Integer notCompletedStudentCount = new Integer(0);
		
		
		if(page != null) {
			pageSize = new Integer(page.getPageSize());
		}
        
       try {
        	testSessions = dataExportManagement.getTestSessionForExportWithStudents(customerId);
        	
			for (int i = 0; i <testSessions.length; i++ ){
				
				toBeExportedValue = new Integer(0);
				completeValue = new Integer(0); 
				notTakenValue = new Integer(0);
				incompleteValue = new Integer(0);
				scheduledValue = new Integer(0);
				studentStopValue = new Integer(0);
				systemStopValue = new Integer(0);
				endedSystemStopValue = new Integer(0);
				 completeValue = dataExportManagement.getCompletedSubtestUnexportedStudentsForTestSession(customerId,testSessions[i].getTestAdminId()); 
				
				if(testSessions[i].getStatus().equals("PA")){
					Integer systemStopCountFromIncompleteCount = dataExportManagement.getSystemStopCountFromInCompleteForTestSession(customerId, testSessions[i].getTestAdminId());
				    notTakenValue = dataExportManagement.getNotTakenSubtestUnexportedStudentsForTestSession(customerId, testSessions[i].getTestAdminId());
				    incompleteValue = dataExportManagement.getInCompleteSubtestUnexportedStudentsForTestSession(customerId, testSessions[i].getTestAdminId());
				    endedSystemStopValue = incompleteValue - systemStopCountFromIncompleteCount;
				    toBeExportedValue = completeValue + incompleteValue - systemStopCountFromIncompleteCount;
				    notTakenStudentCount = notTakenStudentCount +  notTakenValue;
				}else{
					scheduledValue = dataExportManagement.getScheduledSubtestUnexportedStudentsForTestSession(customerId, testSessions[i].getTestAdminId());
					studentStopValue = dataExportManagement.getStudentStopSubtestUnexportedStudentsForTestSession(customerId, testSessions[i].getTestAdminId());
				    systemStopValue = dataExportManagement.getSystemStopSubtestUnexportedStudentsForTestSession(customerId, testSessions[i].getTestAdminId());
					
					toBeExportedValue = completeValue + studentStopValue;
					totalExportedStudentCount = totalExportedStudentCount + toBeExportedValue;
					scheduledStudentCount = scheduledStudentCount + scheduledValue;
				}
				notCompletedStudentCount  = notCompletedStudentCount + systemStopValue + endedSystemStopValue;
				testSessions[i].setComplete(completeValue);
				testSessions[i].setScheduled(scheduledValue);
				testSessions[i].setNotTaken(notTakenValue);
				testSessions[i].setIncomplete(incompleteValue);
				testSessions[i].setStudentStop(studentStopValue);
				testSessions[i].setSystemStop(systemStopValue);
				testSessions[i].setToBeExported(toBeExportedValue);
					
			}
			mtsd.setTotalExportedStudentCount(totalExportedStudentCount);
			mtsd.setScheduledStudentCount(scheduledStudentCount);
			mtsd.setNotTakenStudentCount(notTakenStudentCount);
			mtsd.setNotCompletedStudentCount(notCompletedStudentCount);
			mtsd.setManageTestSessions(testSessions, pageSize);
			testSessions = mtsd.getManageTestSessions();
			
			mtsd.setManageTestSessions(testSessions, pageSize);
			if(filter != null) mtsd.applyFiltering(filter);
			if(sort != null) mtsd.applySorting(sort);
			if(page != null) mtsd.applyPaging(page);
		} 
         catch (SQLException e) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("DataExportManagementImpl: findStudentsByCustomerId: " + e.getMessage());
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
       

        return mtsd;
	}
} 

	
	

