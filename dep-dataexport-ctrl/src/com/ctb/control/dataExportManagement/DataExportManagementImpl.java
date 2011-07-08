package com.ctb.control.dataExportManagement;

import java.sql.SQLException;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.JmsConnectionException.CustomJMSConnectionException;
import com.ctb.exception.dataExportManagement.CustomerReportDataNotFoundException;
import com.ctb.exception.dataExportManagement.UserDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.jmsutils.ExportDataJMSUtil;

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
	

	public void initGenerateReportTask(String userName, Integer customerId)
			throws CTBBusinessException {
		System.out.println("Hi initGenerateReportTask  .....  ");
		ExportDataJMSUtil util  = new ExportDataJMSUtil();
		try {
			util.initGenerateReportTask( userName, customerId);
		} catch (CustomJMSConnectionException e) {
			e.printStackTrace();
		}
		System.out.println("Bye initGenerateReportTask  .....  ");
	}

	
	
} 
