package com.ctb.control.studentManagement; 

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.studentManagement.Address;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.CustomerDemographic;
import com.ctb.bean.studentManagement.CustomerDemographicValue;
import com.ctb.bean.studentManagement.CustomerProgramGoal;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicData;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.studentManagement.StudentOtherDetail;
import com.ctb.bean.studentManagement.StudentOtherDetailValue;
import com.ctb.bean.studentManagement.StudentProgramGoal;
import com.ctb.bean.studentManagement.StudentProgramGoalData;
import com.ctb.bean.studentManagement.StudentProgramGoalValue;
import com.ctb.bean.studentManagement.StudentWorkForceData;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeStudent;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.StudentNode;
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.USState;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.UserDataRetrivalException;
import com.ctb.exception.studentManagement.CustomerConfigurationDataNotFoundException;
import com.ctb.exception.studentManagement.CustomerDemographicDataNotFoundException;
import com.ctb.exception.studentManagement.CustomerReportDataNotFoundException;
import com.ctb.exception.studentManagement.OrgNodeDataNotFoundException;
import com.ctb.exception.studentManagement.StudentDataCreationException;
import com.ctb.exception.studentManagement.StudentDataDeletionException;
import com.ctb.exception.studentManagement.StudentDataNotFoundException;
import com.ctb.exception.studentManagement.StudentDataUpdateException;
import com.ctb.exception.studentManagement.UserDataNotFoundException;
import com.ctb.exception.validation.ValidationException;
import com.ctb.util.DESUtils;
import com.ctb.util.MathUtils;
import com.ctb.util.SQLutils;
import com.ctb.util.SimpleCache;
import com.ctb.util.StudentEduAndInstrUtils;
import com.ctb.util.StudentWorkForceUtils;
import com.ctb.util.studentManagement.DeleteStudentStatus;
import com.ctb.util.studentManagement.DynamicSQLUtils;
import com.ctb.util.studentManagement.StudentUtils;

/**
 * @author John_Wang
 *
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation()
public class StudentManagementImpl implements StudentManagement, Serializable
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
	private com.ctb.control.db.TestAdmin testAdmins;

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
	private com.ctb.control.db.StudentAccommodation accommodation;

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
	@Control()
	private com.ctb.control.db.Addresses addresses;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.validation.Validator validator;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.StudentManagement studentManagement;

	static final long serialVersionUID = 1L;

	private static final int CTB_CUSTOMER_ID =2;
	private String findInColumn = "ona.ancestor_org_node_id in ";

	/**
	 * Get student profile for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student whose information is desired
	 * @return Student
	 * @throws CTBBusinessException
	 */
	public Student getStudentProfile(String userName, Integer studentId) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentProfile");

		try {
			Student student = studentManagement.getStudent(studentId.intValue());
			return student;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentProfile: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Get manage student object for the specified student with the array of assinged org ndoes populated.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student whose information is desired
	 * @return ManageStudent
	 * @throws CTBBusinessException
	 */
	public ManageStudent getManageStudent(String userName, Integer studentId) throws CTBBusinessException
	{
		try {
			validator.validateStudent(userName, studentId, "StudentManagementImpl.getManageStudent");
		} catch (ValidationException ve) {
			//validate student if student is not across organization
			validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getManageStudent");
		}
		try {
			ManageStudent student = studentManagement.getManageStudent(studentId.intValue());

			OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(studentId.intValue(), userName);
			Address studentContact = studentManagement.getStudentContact(studentId.intValue());

			student.setOrganizationNodes(orgNodes);
			student.setAddress(studentContact);
			return student;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getManageStudent: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Get student accommodations for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student whose information is desired
	 * @return StudentAccommodations
	 * @throws CTBBusinessException
	 */
	public StudentAccommodations getStudentAccommodations(String userName, Integer studentId) throws CTBBusinessException
	{

		//change for CA-ABE student intake
		if (studentId !=null) {

			try {
				validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentAccommodations");
			} catch (ValidationException ve) {
				//validate student if student is not across organization
				validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentAccommodations");
			}

		}
		try {
			StudentAccommodations studentAccommo = accommodation.getStudentAccommodations(studentId);
			return studentAccommo;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentAccommodations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	/**
	 * Create student accommodations for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentAccommodations - contains the student's accommodations information
	 * @throws CTBBusinessException
	 */
	public void createStudentAccommodations(String userName, StudentAccommodations studentAccommodations) throws CTBBusinessException
	{
		Integer studentId = studentAccommodations.getStudentId();
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentAccommodations");

		try {
			accommodation.createStudentAccommodations(studentAccommodations);
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentAccommodations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    

	/**
	 * Update student accommodations for the specified student.
	 * If the student had no accommodations, new accommodations record will be created.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentAccommodations - contains the student's accommodations information
	 * @throws CTBBusinessException
	 */
	public void updateStudentAccommodations(String userName, StudentAccommodations studentAccommodations) throws CTBBusinessException
	{
		Integer studentId = studentAccommodations.getStudentId();
		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudentAccommodations");

		try {
			StudentAccommodations accommo = studentManagement.getStudentAccommodations(studentId);
			if (accommo == null)
				accommodation.createStudentAccommodations(studentAccommodations);
			else    
				accommodation.updateStudentAccommodations(studentAccommodations);
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: updateStudentAccommodations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    

	/**
	 * Delete student accommodations for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student to be deleted
	 * @throws CTBBusinessException
	 */
	public void deleteStudentAccommodations(String userName, Integer studentId) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudentAccommodations");

		try {
			accommodation.deleteStudentAccommodations(studentId);
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: updateStudentAccommodations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    

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
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getCustomerConfigurations");
		try {
			CustomerConfiguration [] customerConfigurations = studentManagement.getCustomerConfigurations(customerId.intValue());
			if (customerConfigurations == null || customerConfigurations.length == 0) {
				customerConfigurations = studentManagement.getCustomerConfigurations(CTB_CUSTOMER_ID);
			}

			if (customerConfigurations != null && customerConfigurations.length > 0) 
			{
				for (int i = 0; i < customerConfigurations.length; i++) {
					CustomerConfiguration cutomerConfig = customerConfigurations[i];
					CustomerConfigurationValue [] customerConfigurationValues 
					= studentManagement.getCustomerConfigurationValues(cutomerConfig.getId().intValue());
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
	 * Get grades for the specified customer.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @return String []
	 * @throws CTBBusinessException
	 */
	public String [] getGradesForCustomer(String userName, Integer customerId) throws CTBBusinessException
	{
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getGradesForCustomer");
		try {
			String [] grades = null;
			CustomerConfigurationValue [] customerConfigurationValues = studentManagement.getCustomerConfigurationValuesForGrades(customerId.intValue());
			if (customerConfigurationValues == null || customerConfigurationValues.length == 0) {
				customerConfigurationValues = studentManagement.getCustomerConfigurationValuesForGrades(CTB_CUSTOMER_ID);
			}
			if (customerConfigurationValues != null && customerConfigurationValues.length > 0) 
			{
				grades = new String[customerConfigurationValues.length];
				for (int i = 0; i < customerConfigurationValues.length; i++) {
					grades[i] = customerConfigurationValues[i].getCustomerConfigurationValue();

				}
			}
			return grades;
		} catch (SQLException se) {
			CustomerConfigurationDataNotFoundException tee = new CustomerConfigurationDataNotFoundException("StudentManagementImpl: getGradesForCustomer: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Get customer demographic for the specified customer.
	 ** @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return CustomerDemographic []
	 * @throws CTBBusinessException
	 */
	public CustomerDemographic [] getCustomerDemographics(String userName, Integer customerId, boolean returnInvisible) throws CTBBusinessException
	{
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getCustomerDemographics");
		try {
			CustomerDemographic [] customerDemographics;
			if (returnInvisible) 
				customerDemographics = studentManagement.getCustomerDemographics(customerId.intValue());
			else
				customerDemographics = studentManagement.getVisibleCustomerDemographics(customerId.intValue());

			if (customerDemographics == null || customerDemographics.length == 0) {
				if (returnInvisible) 
					customerDemographics = studentManagement.getCustomerDemographics(CTB_CUSTOMER_ID);
				else
					customerDemographics = studentManagement.getVisibleCustomerDemographics(CTB_CUSTOMER_ID);

			}

			if (customerDemographics != null && customerDemographics.length > 0) 
			{
				for (int i = 0; i < customerDemographics.length; i++) {
					CustomerDemographicValue [] customerDemographicValues;
					if (returnInvisible) 
						customerDemographicValues = studentManagement.getCustomerDemographicValues(customerDemographics[i].getId().intValue());
					else
						customerDemographicValues = studentManagement.getVisibleCustomerDemographicValues(customerDemographics[i].getId().intValue());

					customerDemographics[i].setCustomerDemographicValues(customerDemographicValues);
				}
			}
			return customerDemographics;
		} catch (SQLException se) {
			CustomerDemographicDataNotFoundException tee = new CustomerDemographicDataNotFoundException("StudentManagementImpl: getCustomerDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Get student demographic for the specified customer and student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param studentId - identifies the student whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return StudentDemographic []
	 * @throws CTBBusinessException
	 */
	public StudentDemographic [] getStudentDemographics(String userName, Integer customerId, Integer studentId, boolean returnInvisible) throws CTBBusinessException
	{   
		//change for CA-ABE student intake
		if (studentId !=null) {
			
			try {
				validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentDemographics");
			} catch (ValidationException ve) {
				//validate student if student is not across organization
				validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentDemographics");
			}

		}

		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getStudentDemographics");
		try {
			CustomerDemographic [] customerDemographics;
			if (returnInvisible) 
				customerDemographics = studentManagement.getCustomerDemographics(customerId.intValue());
			else
				customerDemographics = studentManagement.getVisibleCustomerDemographics(customerId.intValue());

			if (customerDemographics == null || customerDemographics.length == 0) {
				if (returnInvisible) 
					customerDemographics = studentManagement.getCustomerDemographics(CTB_CUSTOMER_ID);
				else
					customerDemographics = studentManagement.getVisibleCustomerDemographics(CTB_CUSTOMER_ID);

			}

			StudentDemographic [] studentDemographics = null;

			if (customerDemographics != null && customerDemographics.length > 0) 
			{
				studentDemographics = new StudentDemographic[customerDemographics.length];
				for (int i = 0; i < customerDemographics.length; i++) {
					studentDemographics[i] = new StudentDemographic(customerDemographics[i]);
					studentDemographics[i].setStudentId(studentId);
					StudentDemographicValue [] studentDemographicValues;
					if (returnInvisible) 
						studentDemographicValues = studentManagement.getStudentDemographicValues(studentDemographics[i].getId().intValue(), studentId == null? -1: studentId.intValue());
					else
						studentDemographicValues = studentManagement.getVisibleStudentDemographicValues(studentDemographics[i].getId().intValue(), studentId == null? -1: studentId.intValue());

					studentDemographics[i].setStudentDemographicValues(studentDemographicValues);
				}
			}
			return studentDemographics;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Create student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void createStudentDemographics(String userName, Integer studentId, StudentDemographic [] studentDemographics) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentDemographics");

		try {
			Integer count = studentManagement.getCountStudentDemographicDataForStudent(studentId);
			if (count.intValue() > 0) 
				throw new StudentDataCreationException("StudentManagementImpl: createStudentDemographics: Student with id '" + studentId+ "' has existing demographic data.");
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentDemographicDataId(studentManagement.getNextPKForStudentDemographicData());                        
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setCreatedBy(userId);
						studentDemographicData.setCreatedDateTime(now);
						studentManagement.createStudentDemographicData(studentDemographicData);
					}
				}
			}
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    


	/**
	 * Create student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void createStudentDemographicsUpload(User user, Integer studentId, StudentDemographic [] studentDemographics) throws CTBBusinessException
	{
		validator.validateStudent(user.getUserName(), studentId, "StudentManagementImpl.createStudentDemographics");

		try {
			Integer count = studentManagement.getCountStudentDemographicDataForStudent(studentId);
			if (count.intValue() > 0) 
				throw new StudentDataCreationException("StudentManagementImpl: createStudentDemographics: Student with id '" + studentId+ "' has existing demographic data.");
			//User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentDemographicDataId(studentManagement.getNextPKForStudentDemographicData());                        
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setCreatedBy(userId);
						studentDemographicData.setCreatedDateTime(now);
						studentManagement.createStudentDemographicData(studentDemographicData);
					}
				}
			}
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    

	/**
	 * Update student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void updateStudentDemographics(String userName, Integer studentId, StudentDemographic [] studentDemographics) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudentDemographics");

		try {
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			// delete all visible for student then insert new ones
			studentManagement.deleteVisibleStudentDemographicDataForStudent(studentId);

			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				boolean isSingle = false;
				if ("SINGLE".equals(studentDemographics[i].getValueCardinality())) {
					boolean foundSelectedValue = false;
					for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) 
						if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) 
							foundSelectedValue = true;                 

					if (foundSelectedValue)  
						studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					else {
						boolean foundInvisibleValue = false;
						StudentDemographicValue [] oldStudentDemographicValues = studentManagement.getStudentDemographicValues(studentDemographics[i].getId().intValue(), studentId.intValue());
						for (int j=0; oldStudentDemographicValues!=null && j<oldStudentDemographicValues.length; j++) 
							if (oldStudentDemographicValues[j] != null && "true".equals(oldStudentDemographicValues[j].getSelectedFlag()) && "F".equals(oldStudentDemographicValues[j].getVisible())) 
								foundInvisibleValue = true; 

						if (!foundInvisibleValue)
							studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					}                                              
				}
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentDemographicDataId(studentManagement.getNextPKForStudentDemographicData());                        
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setCreatedBy(userId);
						studentDemographicData.setCreatedDateTime(now);
						studentManagement.createStudentDemographicData(studentDemographicData);
					}
				}
			}
		} catch (SQLException se) {
			StudentDataUpdateException tee = new StudentDataUpdateException("StudentManagementImpl: updateStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}   


	/**
	 * For Import/Export Application
	 * Update student demographic data for the specified student.
	 * @common:operation
	 * @param user - identifies the calling user details
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void updateStudentDemographicsUpload(User user, Integer studentId, StudentDemographic [] studentDemographics) throws CTBBusinessException
	{
		validator.validateStudent(user.getUserName(), studentId, "StudentManagementImpl.updateStudentDemographics");

		try {
			//User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			// delete all visible for student then insert new ones
			studentManagement.deleteVisibleStudentDemographicDataForStudent(studentId);

			for (int i=0; studentDemographics!= null && i<studentDemographics.length; i++) {
				StudentDemographicValue [] studentDemographicValues = studentDemographics[i].getStudentDemographicValues();
				boolean isSingle = false;
				if ("SINGLE".equals(studentDemographics[i].getValueCardinality())) {
					boolean foundSelectedValue = false;
					for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) 
						if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) 
							foundSelectedValue = true;                 

					if (foundSelectedValue)  
						studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					else {
						boolean foundInvisibleValue = false;
						StudentDemographicValue [] oldStudentDemographicValues = studentManagement.getStudentDemographicValues(studentDemographics[i].getId().intValue(), studentId.intValue());
						for (int j=0; oldStudentDemographicValues!=null && j<oldStudentDemographicValues.length; j++) 
							if (oldStudentDemographicValues[j] != null && "true".equals(oldStudentDemographicValues[j].getSelectedFlag()) && "F".equals(oldStudentDemographicValues[j].getVisible())) 
								foundInvisibleValue = true; 

						if (!foundInvisibleValue)
							studentManagement.deleteStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographics[i].getId());
					}                                              
				}
				for (int j=0; studentDemographicValues!=null && j<studentDemographicValues.length; j++) {
					if (studentDemographicValues[j] != null && "true".equals(studentDemographicValues[j].getSelectedFlag())) {
						StudentDemographicData studentDemographicData = new StudentDemographicData();
						studentDemographicData.setStudentDemographicDataId(studentManagement.getNextPKForStudentDemographicData());                        
						studentDemographicData.setStudentId(studentId);
						studentDemographicData.setCustomerDemographicId(studentDemographics[i].getId());
						studentDemographicData.setValueName(studentDemographicValues[j].getValueName());
						studentDemographicData.setCreatedBy(userId);
						studentDemographicData.setCreatedDateTime(now);
						studentManagement.createStudentDemographicData(studentDemographicData);
					}
				}
			}
		} catch (SQLException se) {
			StudentDataUpdateException tee = new StudentDataUpdateException("StudentManagementImpl: updateStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}   

	/**
	 * Delete student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student
	 * @throws CTBBusinessException
	 */
	public void deleteStudentDemographics(String userName, Integer studentId) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.deleteStudentDemographics");

		try {
			studentManagement.deleteStudentDemographicDataForStudent(studentId);
		} catch (SQLException se) {
			StudentDataDeletionException tee = new StudentDataDeletionException("StudentManagementImpl: deleteStudentDemographics: " + se.getMessage());
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
			validator.validateUser(userName, detailUserName, "StudentManagementImpl.getUserDetails");
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
			CTBBusinessException cbe = new UserDataNotFoundException("StudentManagementImpl: getUserDetails: " + se.getMessage());
			cbe.setStackTrace(se.getStackTrace());
			throw cbe;
		}  
	}


	/**
	 * Retrieves a sorted, filtered, paged list of students at and below user's top org node(s).
	 * @common:operation
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findStudentsAtAndBelowUserTopNodes(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		try {
			ManageStudentData std = new ManageStudentData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}

			ManageStudent [] students = studentManagement.getStudentsAtAndBelowTopOrgNodes(userName);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(students[i].getId().intValue(), userName);
					students[i].setOrganizationNodes(orgNodes);
				}
			}
			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsAtAndBelowTopOrgNodes: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	/**
	 * Retrieves a sorted, filtered, paged list of students at and below specified org node(s).
	 * If orgNodeIds is null or empty, use user's top org node(s).
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeIds - identifies the org nodes
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findStudentsAtAndBelowOrgNode(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.findStudentsAtAndBelowOrgNodes");
		try {
			ManageStudentData std = new ManageStudentData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}

			ManageStudent [] students = studentManagement.getStudentsAtAndBelowOrgNode(orgNodeId);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowOrgNode(students[i].getId().intValue(), orgNodeId);
					students[i].setOrganizationNodes(orgNodes);
				}
			}
			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsAtAndBelowTopOrgNodes: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}


	/**
	 * Retrieves a sorted, filtered, paged list of students at and below user's top org node(s).
	 * The SQL's where clause is dynamically generated on based on filter passed in.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findStudentsAtAndBelowTopOrgNodesWithDynamicSQL(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		try {
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}

			Integer totalCount = null;
			String searchCriteria = "";
			if (filter != null) {
				searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter);
				filter.setFilterParams(new FilterParam[0]);
				totalCount = studentManagement.getStudentCountAtAndBelowUserTopNodes(userName);
			}
			String orderByClause = "";
			if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
				sort = null;
			}
			searchCriteria = searchCriteria + orderByClause;

			ManageStudent [] students = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteria(userName, searchCriteria);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(students[i].getId().intValue(), userName);
					students[i].setOrganizationNodes(orgNodes);
				}
			}


			if (totalCount != null) {
				std.setTotalCount(totalCount);
				if (page == null)
					std.setTotalPages(new Integer(1));
				else 
					std.setTotalPages(MathUtils.intDiv(totalCount, new Integer(page.getPageSize())));
			}


			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsAtAndBelowTopOrgNodesWithDynamicSQL: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}


	/**
	 * Retrieves a sorted, filtered, paged list of students at and below user's top org node(s).
	 * The SQL's where clause is dynamically generated on based on filter passed in.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findCAABEStudentsAtAndBelowTopOrgNodesWithDynamicSQL(String userName, Integer customerId,FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		try {
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}

			Integer totalCount = null;
			String searchCriteria = "";
			if (filter != null) {
				searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter);
				filter.setFilterParams(new FilterParam[0]);
				totalCount = studentManagement.getAcrossOrgStudentCount(userName,customerId);
				//totalCount=32;
			}
			String orderByClause = "";
			if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorterABEStudent(sort);                
				sort = null;
			}
			searchCriteria = searchCriteria ;
			orderByClause = searchCriteria + orderByClause;
			//ca-abe
			ManageStudent [] students = studentManagement.getCAABEStudentsAtAndBelowUserTopNodeWithSearchCriteria(userName, searchCriteria,customerId,orderByClause);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);
			students = std.getManageStudents();

			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForCAABEStudentAtAndBelowUserTopNodes(students[i].getId().intValue(), userName,customerId);
					students[i].setOrganizationNodes(orgNodes);
				}
			}

			//getThe orgnodes which are not visible to login user



			OrganizationNode [] accrossOrgNodes = studentManagement.getInvisibleAssignedOrganizationNodesForCAABEStudentOtherpNodes(userName,customerId);

			if (students != null && students.length > 0) {

				for (int i=0; i <students.length; i++) {

					if (students[i] != null ) {

						OrganizationNode [] stuAssignedorgNodes = students[i].getOrganizationNodes();

						if (stuAssignedorgNodes != null && stuAssignedorgNodes.length > 0) {

							for (int j=0; j <stuAssignedorgNodes.length; j++) {

								for (int k=0; k < accrossOrgNodes.length; k++) {

									if (stuAssignedorgNodes[j].getOrgNodeId().intValue() ==
										accrossOrgNodes[k].getOrgNodeId().intValue()) {
										stuAssignedorgNodes[j].setUserHierarchy("false");
										break;
									}

								}
							}


						}
					}
				}
			}

			//set the permisssion for deletion
			//String deletePermission="true";
			for (int i=0; i <students.length; i++) {
				if (students[i] != null ) {
					OrganizationNode [] stuAssignedorgNodes = students[i].getOrganizationNodes();
					String deletePermissionOfStudent="false";
					if (stuAssignedorgNodes != null && stuAssignedorgNodes.length > 0) {

						for (int j=0; j <stuAssignedorgNodes.length; j++) {
							if (stuAssignedorgNodes[j].getUserHierarchy().equals("true")) {
								deletePermissionOfStudent="true";
								break;
							}
						}
					}
					students[i].setDeletePermission(deletePermissionOfStudent);
				}
			}


			if (totalCount != null) {
				std.setTotalCount(totalCount);
				if (page == null)
					std.setTotalPages(new Integer(1));
				else 
					std.setTotalPages(MathUtils.intDiv(totalCount, new Integer(page.getPageSize())));
			}


			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsAtAndBelowTopOrgNodesWithDynamicSQL: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}


	/**
	 * Retrieves a sorted, filtered, paged list of students at and below specified org node(s).
	 * If orgNodeIds is null or empty, use user's top org node(s).
	 * The SQL's where clause is dynamically generated on based on filter passed in.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeIds - identifies the org nodes
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findStudentsAtAndBelowOrgNodeWithDynamicSQL(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.findStudents");
		try {
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}

			Integer totalCount = null;
			String searchCriteria = "";
			if (filter != null) {
				searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter);
				filter.setFilterParams(new FilterParam[0]);
				totalCount = studentManagement.getStudentCountAtAndBelowOrgNode(orgNodeId);
			}
			String orderByClause = "";
			if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
				sort = null;
			}
			searchCriteria = searchCriteria + orderByClause;


			ManageStudent [] students = studentManagement.getStudentsAtAndBelowOrgNodeWithSearchCriteria(orgNodeId, searchCriteria);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowOrgNode(students[i].getId().intValue(), orgNodeId);
					students[i].setOrganizationNodes(orgNodes);
				}
			}


			if (totalCount != null) {
				std.setTotalCount(totalCount);
				if (page == null)
					std.setTotalPages(new Integer(1));
				else 
					std.setTotalPages(MathUtils.intDiv(totalCount, new Integer(page.getPageSize())));
			}


			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsAtAndBelowOrgNodesWithDynamicSQL: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}



	/**
	 * Retrieves a sorted, filtered, paged list of students at the specified org node.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeId - identifies the org nodes
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return ManageStudentData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public ManageStudentData findStudentsForOrgNode(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.findStudentsForOrgNode");
		try {
			ManageStudentData std = new ManageStudentData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			Integer [] orgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);
			ManageStudent [] students = studentManagement.getStudentsForOrgNode(userName, orgNodeId);
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowOrgNode(students[i].getId().intValue(), orgNodeId);
					students[i].setOrganizationNodes(orgNodes);
				}
			}
			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsForOrgNode: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	/**
	 * Retrieves a list of child org nodes of the specified org node
	 * <br/><br/>Each node contains a count: the number of students 
	 * which are at or below the specified node (studentCount).
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeId - identifies the parent org node
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return OrganizationNodeData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public OrganizationNodeData getOrganizationNodesForParent(String userName, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.getOrganizationNodesForParent");
		try {
			OrganizationNodeData ond = new OrganizationNodeData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			StudentNode [] nodes = null;
			StudentNodeData snd = (StudentNodeData) SimpleCache.checkCache5min("STUDENT_NODES_FOR_ORG", String.valueOf(orgNodeId));
			if(snd == null) {
				nodes = orgNode.getStudentNodesForParent(orgNodeId);
				snd = new StudentNodeData();
				snd.setStudentNodes(nodes, new Integer(1000));
				SimpleCache.cacheResult("STUDENT_NODES_FOR_ORG", String.valueOf(orgNodeId), snd);
			} else {
				nodes = (StudentNode []) snd.getStudentNodes();
			}
			OrganizationNode [] orgNodes = new OrganizationNode[nodes.length];
			for(int i=0;i<nodes.length;i++) {
				orgNodes[i] = new OrganizationNode(nodes[i]);                     
			}
			ond.setOrganizationNodes(orgNodes, pageSize);
			if(filter != null) ond.applyFiltering(filter);
			if(sort != null) ond.applySorting(sort);
			if(page != null) ond.applyPaging(page);
			orgNodes = ond.getOrganizationNodes();

//			User user = getUserDetails(userName, userName);
//			Integer customerId = user.getCustomer().getCustomerId();
			Integer customerId = orgNode.getOrgNodeById(orgNodeId).getCustomerId();
			Integer bottomOrgNodeCategoryId = studentManagement.getBottomOrgNodeCategoryIdForCustomer(customerId);

			for(int i=0;i<orgNodes.length && orgNodes[i] != null;i++) {
//				StudentAccommodationsData accommData = new StudentAccommodationsData();
//				accommData.setStudentAccommodations(accommodation.getStudentAccommodationsByAncestorNode(orgNodes[i].getOrgNodeId()), null);
//				orgNodes[i].setStudentCount(accommData.getFilteredCount());
				Integer [] orgNodeIds = new Integer[1];
				orgNodeIds[0] = orgNodes[i].getOrgNodeId();

				if (bottomOrgNodeCategoryId.intValue() > 4 && bottomOrgNodeCategoryId.equals(orgNodes[i].getOrgNodeCategoryId())) 
					orgNodes[i].setBottomLevelNodeFlag("true");
				else 
					orgNodes[i].setBottomLevelNodeFlag("false");

				SimpleCache.clearCurrentUserCacheValue("STUDENT_NODES_FOR_ORG",String.valueOf(orgNodes[i].getOrgNodeId()));    
			}
			return ond;
		} catch (SQLException se) {
			OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("StudentManagementImpl: getOrganizationNodesForParent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}


	/**
	 * Retrieves a list of top org nodes of the user.
	 * <br/><br/>Each node contains a count: the number of students 
	 * which are at or below the specified node (studentCount).
	 * @common:operation
	 * @param userName - identifies the user
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return OrganizationNodeData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public OrganizationNodeData getTopOrganizationNodesForUser(String userName, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException
	{
		try {
			OrganizationNodeData ond = new OrganizationNodeData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			StudentNode [] nodes = orgNode.getTopStudentNodesForUser(userName);
			OrganizationNode [] orgNodes = new OrganizationNode[nodes.length];
			User user = getUserDetails(userName, userName);
			Integer customerId = user.getCustomer().getCustomerId();
			for(int i=0;i<nodes.length;i++) {
				orgNodes[i] = new OrganizationNode(nodes[i]);  
				customerId =  orgNodes[i].getCustomerId();                  
			}
			ond.setOrganizationNodes(orgNodes, pageSize);
			if(filter != null) ond.applyFiltering(filter);
			if(sort != null) ond.applySorting(sort);
			if(page != null) ond.applyPaging(page);
			orgNodes = ond.getOrganizationNodes();
			Integer bottomOrgNodeCategoryId = studentManagement.getBottomOrgNodeCategoryIdForCustomer(customerId);

			for(int i=0;i<orgNodes.length && orgNodes[i] != null;i++) {
//				StudentAccommodationsData accommData = new StudentAccommodationsData();
//				accommData.setStudentAccommodations(accommodation.getStudentAccommodationsByAncestorNode(orgNodes[i].getOrgNodeId()), null);
//				orgNodes[i].setStudentCount(accommData.getFilteredCount());
				Integer [] orgNodeIds = new Integer[1];
				orgNodeIds[0] = orgNodes[i].getOrgNodeId();

				if (bottomOrgNodeCategoryId.intValue() > 4 && bottomOrgNodeCategoryId.equals(orgNodes[i].getOrgNodeCategoryId())) 
					orgNodes[i].setBottomLevelNodeFlag("true");
				else 
					orgNodes[i].setBottomLevelNodeFlag("false");

				SimpleCache.clearCurrentUserCacheValue("STUDENT_NODES_FOR_ORG",String.valueOf(orgNodes[i].getOrgNodeId()));
			}
			return ond;
		} catch (SQLException se) {
			OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("StudentManagementImpl: getTopOrganizationNodesForUser: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}




	/**
	 * Retrieves a list of ancestor org nodes of the specified org node
	 * <br/><br/>studentCount and childCount not populated for this call.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeId - identifies the parent org node
	 * @return OrganizationNode []
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public OrganizationNode [] getAncestorOrganizationNodesForOrgNode(String userName, Integer orgNodeId) throws CTBBusinessException
	{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.getAncestorOrganizationNodesForOrgNode");
		try {
			Integer [] topOrgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);
			String findInColumn = "ona1.ancestor_org_node_id in ";
			//OrganizationNode [] orgNodes = studentManagement.getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(orgNodeId, topOrgNodeIds);
			OrganizationNode [] orgNodes = studentManagement.getAncestorOrganizationNodesForOrgNodeAtAndBelowTopOrgNodes(orgNodeId, SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
			return orgNodes;
		} catch (SQLException se) {
			OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("StudentManagementImpl: getAncestorOrganizationNodesForOrgNode: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}



	/**
	 * Create new student record.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param manageStudent - contains the new student information
	 * @return student id
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public Integer createNewStudent(String userName, ManageStudent manageStudent) throws CTBBusinessException
	{
		OrganizationNode [] organizationNodes = manageStudent.getOrganizationNodes();
		for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) 
			validator.validateNode(userName, organizationNodes[i].getOrgNodeId(), "StudentManagementImpl.createNewStudent");

		try {
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Student student = new Student();
			student.setActivationStatus("AC");
			Integer newStudentId = students.getNextPK();
			//Changes for CA_ABE
			//START
			Integer cloneStudentId = newStudentId;
			student.setStudentId(cloneStudentId);
			student.setFirstName(manageStudent.getFirstName());
			student.setMiddleName(manageStudent.getMiddleName());
			student.setLastName(manageStudent.getLastName());
			student.setBirthdate(manageStudent.getBirthDate());
			student.setGender(manageStudent.getGender());
			student.setGrade(manageStudent.getGrade());
			student.setInstructorFirstName(manageStudent.getInstructorFirstName());
			student.setInstructorLastName(manageStudent.getInstructorLastName());
			student.setIsPBAFormSigned(manageStudent.getIsPBAFormSigned());
			student.setIsSSN(manageStudent.getIsSSN());
			student.setVisibleAcrossOrganization(manageStudent.getVisibleAcrossOrganization());

			student.setExtPin1(manageStudent.getStudentIdNumber());
			student.setExtPin2(manageStudent.getStudentIdNumber2());
			student.setCreatedBy(userId);
			student.setCreatedDateTime(new Date());
			student.setCustomerId(user.getCustomer().getCustomerId());

			//GACRCT2010CR007 - changed to generate 4 digit sequence number when provide student  date of birth is null.
			String studentLoginIdSequence = "";
			if(manageStudent.getBirthDate() == null)
				studentLoginIdSequence = students.getStudentLoginIdSequence();

			String studentUserName = generateUniqueStudentUserName(student, studentLoginIdSequence);
			student.setUserName(studentUserName);

			students.createNewStudent(student);
			if(manageStudent.getAddress() != null){
				Address address = manageStudent.getAddress();
				address.setStudentId(cloneStudentId);
				Integer newAddressId = studentManagement.getNextPKForStudentContact();
				address.setAddressId(newAddressId);
				address.setCreatedBy(userId);
				address.setCreatedDateTime(new Date());
				studentManagement.createNewStudentContactInformation(address);

			}
			//END
			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				Node node = orgNode.getOrgNodeById(organizationNodes[i].getOrgNodeId());                
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(userId);
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(node.getCustomerId());
				orgNodeStudent.setDataImportHistoryId(node.getDataImportHistoryId());
				orgNodeStudent.setOrgNodeId(node.getOrgNodeId());
				orgNodeStudent.setStudentId(student.getStudentId());
				orgNodeStudents.createOrgNodeStudent(orgNodeStudent);                                
			}
			return newStudentId;

		} catch (SQLException se) {
			StudentDataCreationException one = new StudentDataCreationException("StudentManagementImpl: createNewStudent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}


	/**
	 * Create new student record for import/export application
	 * @common:operation
	 * @param user - identifies the login user details
	 * @param manageStudent - contains the new student information
	 * @return student id
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public Student createStudentUpload(User user, ManageStudent manageStudent) throws CTBBusinessException
	{
		OrganizationNode [] organizationNodes = manageStudent.getOrganizationNodes();
		for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) 
			validator.validateNode(user.getUserName(), organizationNodes[i].getOrgNodeId(), "StudentManagementImpl.createNewStudent");

		try {
			//User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Student student = new Student();
			student.setActivationStatus("AC");
			Integer newStudentId = students.getNextPK();
			student.setStudentId(newStudentId);
			student.setFirstName(manageStudent.getFirstName());
			student.setMiddleName(manageStudent.getMiddleName());
			student.setLastName(manageStudent.getLastName());
			student.setBirthdate(manageStudent.getBirthDate());
			student.setGender(manageStudent.getGender());
			student.setGrade(manageStudent.getGrade());

			student.setExtPin1(manageStudent.getStudentIdNumber());
			student.setExtPin2(manageStudent.getStudentIdNumber2());
			student.setCreatedBy(userId);
			student.setCreatedDateTime(new Date());
			student.setCustomerId(user.getCustomer().getCustomerId());

			//GACRCT2010CR007 - changed to generate 4 digit sequence number when provide student  date of birth is null.
			String studentLoginIdSequence = "";
			if(manageStudent.getBirthDate() == null)
				studentLoginIdSequence = students.getStudentLoginIdSequence();

			String studentUserName = generateUniqueStudentUserName(student, studentLoginIdSequence);
			student.setUserName(studentUserName);
			students.createNewStudent(student);


			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				Node node = orgNode.getOrgNodeById(organizationNodes[i].getOrgNodeId());                
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(userId);
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(node.getCustomerId());
				orgNodeStudent.setDataImportHistoryId(node.getDataImportHistoryId());
				orgNodeStudent.setOrgNodeId(node.getOrgNodeId());
				orgNodeStudent.setStudentId(student.getStudentId());
				orgNodeStudents.createOrgNodeStudent(orgNodeStudent);                                
			}
			return student;

		} catch (SQLException se) {
			StudentDataCreationException one = new StudentDataCreationException("StudentManagementImpl: createNewStudent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}



	/**
	 * Update student record.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param manageStudent - contains the updated student information
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public void updateStudent(String userName, ManageStudent manageStudent) throws CTBBusinessException
	{	

		Integer studentId = manageStudent.getId();
		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudent");
		OrganizationNode [] organizationNodes = manageStudent.getOrganizationNodes();
		for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) 
			validator.validateNode(userName, organizationNodes[i].getOrgNodeId(), "StudentManagementImpl.updateStudent");

		try {
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Integer [] topOrgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);

			studentManagement.updateStudent(manageStudent, userId, new Date());

			Hashtable newOrgNodeHash = new Hashtable();

			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				newOrgNodeHash.put(organizationNodes[i].getOrgNodeId(), organizationNodes[i]);
			}

			com.ctb.bean.testAdmin.OrgNodeStudent [] orgNodeStus = orgNodeStudents.getOrgNodeStudentForStudentAtAndBelowOrgNodes(studentId, SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
			for (int i=0; orgNodeStus!=null && i< orgNodeStus.length; i++) {
				com.ctb.bean.testAdmin.OrgNodeStudent oldOrgNodeInDB = orgNodeStus[i];
				boolean foundInNewOrgNodes = newOrgNodeHash.containsKey(oldOrgNodeInDB.getOrgNodeId());
				if (foundInNewOrgNodes) { //activate 
					orgNodeStudents.activateOrgNodeStudentForStudentAndOrgNode(oldOrgNodeInDB.getStudentId(), oldOrgNodeInDB.getOrgNodeId());    
					//remove from hash so that the remaining will be new org nodes
					newOrgNodeHash.remove(oldOrgNodeInDB.getOrgNodeId());                              
				}
				else { //delete or deactivate
					Integer rosterCount = testRosters.getRosterCountForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					if (rosterCount.intValue() >0) {
						orgNodeStudents.deactivateOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
					else {
						orgNodeStudents.deleteOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
				}
			}

			//insert new org nodes remaining in hash                
			Iterator iterator = newOrgNodeHash.values().iterator();
			while (iterator.hasNext()) {
				OrganizationNode newOrganizationNode = (OrganizationNode) iterator.next();
				Node node = orgNode.getOrgNodeById(newOrganizationNode.getOrgNodeId());                
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(userId);
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(node.getCustomerId());
				orgNodeStudent.setDataImportHistoryId(node.getDataImportHistoryId());
				orgNodeStudent.setOrgNodeId(node.getOrgNodeId());
				orgNodeStudent.setStudentId(studentId);
				orgNodeStudents.createOrgNodeStudent(orgNodeStudent);                                
			}

		} catch (SQLException se) {
			StudentDataUpdateException one = new StudentDataUpdateException("StudentManagementImpl: updateStudent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}



	/**
	 * Update student record for import/export.
	 * @common:operation
	 * @param user - identifies the login user details
	 * @param manageStudent - contains the updated student information
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public void updateStudentUpload(User user, ManageStudent manageStudent) throws CTBBusinessException
	{
		Integer studentId = manageStudent.getId();
		validator.validateStudent(user.getUserName(), studentId, "StudentManagementImpl.updateStudent");
		OrganizationNode [] organizationNodes = manageStudent.getOrganizationNodes();
		for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) 
			validator.validateNode(user.getUserName(), organizationNodes[i].getOrgNodeId(), "StudentManagementImpl.updateStudent");

		try {
			//User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Integer [] topOrgNodeIds = studentManagement.getTopOrgNodeIdsForUser(user.getUserName());

			studentManagement.updateStudent(manageStudent, userId, new Date());

			Hashtable newOrgNodeHash = new Hashtable();

			for (int i=0; organizationNodes!=null && i< organizationNodes.length; i++) {
				newOrgNodeHash.put(organizationNodes[i].getOrgNodeId(), organizationNodes[i]);
			}

			com.ctb.bean.testAdmin.OrgNodeStudent [] orgNodeStus = orgNodeStudents.getOrgNodeStudentForStudentAtAndBelowOrgNodes(studentId, SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
			for (int i=0; orgNodeStus!=null && i< orgNodeStus.length; i++) {
				com.ctb.bean.testAdmin.OrgNodeStudent oldOrgNodeInDB = orgNodeStus[i];
				boolean foundInNewOrgNodes = newOrgNodeHash.containsKey(oldOrgNodeInDB.getOrgNodeId());
				if (foundInNewOrgNodes) { //activate 
					orgNodeStudents.activateOrgNodeStudentForStudentAndOrgNode(oldOrgNodeInDB.getStudentId(), oldOrgNodeInDB.getOrgNodeId());    
					//remove from hash so that the remaining will be new org nodes
					newOrgNodeHash.remove(oldOrgNodeInDB.getOrgNodeId());                              
				}
				else { //delete or deactivate
					Integer rosterCount = testRosters.getRosterCountForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					if (rosterCount.intValue() >0) {
						orgNodeStudents.deactivateOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
					else {
						orgNodeStudents.deleteOrgNodeStudentForStudentAndOrgNode(studentId, oldOrgNodeInDB.getOrgNodeId());
					}
				}
			}

			//insert new org nodes remaining in hash                
			Iterator iterator = newOrgNodeHash.values().iterator();
			while (iterator.hasNext()) {
				OrganizationNode newOrganizationNode = (OrganizationNode) iterator.next();
				Node node = orgNode.getOrgNodeById(newOrganizationNode.getOrgNodeId());                
				OrgNodeStudent orgNodeStudent = new OrgNodeStudent();
				orgNodeStudent.setActivationStatus("AC");
				orgNodeStudent.setCreatedBy(userId);
				orgNodeStudent.setCreatedDateTime(new Date());
				orgNodeStudent.setCustomerId(node.getCustomerId());
				orgNodeStudent.setDataImportHistoryId(node.getDataImportHistoryId());
				orgNodeStudent.setOrgNodeId(node.getOrgNodeId());
				orgNodeStudent.setStudentId(studentId);
				orgNodeStudents.createOrgNodeStudent(orgNodeStudent);                                
			}

		} catch (SQLException se) {
			StudentDataUpdateException one = new StudentDataUpdateException("StudentManagementImpl: updateStudent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}

	/**
	 * Delete student record.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param studentId - identifies the student to be deleted
	 * @return DeleteStudentStatus
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public DeleteStudentStatus deleteStudent(String userName, Integer studentId) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.deleteStudent");
		try {

			RosterElement [] rosters = studentManagement.getRosterElementsForStudent(studentId);

			int deleteStatus = 0;	// 0: do not touch student (default)
			// 1: remove/inactivate student
			// 2: fully delete student

			if (rosters == null || rosters.length == 0)
			{
				// first rule: if no test_rosters, then fully delete this student
				deleteStatus = 2;
			}
			else
			{
				// second rule: if test_rosters but only in SC or NT, then
				// fully delete this student
				boolean onlySC_NT = true;
				for (int i=0; i<rosters.length; i++)
				{
					if ( !"SC".equals(rosters[i].getTestCompletionStatus()) && !"NT".equals(rosters[i].getTestCompletionStatus()))
					{
						onlySC_NT = false;
						break;
					}
					//if has some responses
					if (studentManagement.getResponseCountForRoster(rosters[i].getTestRosterId()).intValue() > 0) 
					{
						onlySC_NT = false;
						break;
					}
				}
				if ( onlySC_NT )
				{
					deleteStatus = 2;
				}

				if ( deleteStatus == 0 )
				{
					// third rule: if test_rosters but in
					// IP, IS, IN, IC, SP, CO and test_admins are PA then
					// remove/invalidate this student
					deleteStatus = 1;

					for (int i=0; i<rosters.length; i++)
					{
						//if ( !"SC".equals(rosters[i].getTestCompletionStatus()) && !"NT".equals(rosters[i].getTestCompletionStatus()))
						//if has some responses
						if ((!"SC".equals(rosters[i].getTestCompletionStatus()) && !"NT".equals(rosters[i].getTestCompletionStatus()))
								|| studentManagement.getResponseCountForRoster(rosters[i].getTestRosterId()).intValue() > 0) 
						{
							TestSession session =testAdmins.getTestAdminDetails(rosters[i].getTestAdminId());
							if (!session.getTestAdminStatus().equals("PA"))
							{
								deleteStatus = 0;	// no touch
								break;
							}
						}
					}
				}
			}

			// end of checking, now perform action based on the deleteStatus
			if ( deleteStatus == 0 )
			{
				// do not delete this student
				CTBBusinessException be = new CTBBusinessException( 
						"You cannot delete this student.  " +
				"Student is associated with test administrations.");
				throw be;
			}
			// Now we can either fully delete or inactivate this student

			Integer [] topOrgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);

			if (deleteStatus == 2) {
				studentManagement.deleteStudentItemSetStatusesForRoster(studentId,SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
				studentManagement.deleteRostersByStudentId(studentId,SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));  
				studentManagement.deleteStudentTutorialStatus(studentId);
			}


			com.ctb.bean.testAdmin.OrgNodeStudent [] orgNodeStus = orgNodeStudents.getOrgNodeStudentForStudentAtAndBelowOrgNodes(studentId,SQLutils.generateSQLCriteria(findInColumn,topOrgNodeIds));
			for (int i=0; orgNodeStus!=null && i< orgNodeStus.length; i++) {
				Integer orgNodeId = orgNodeStus[i].getOrgNodeId();
				Integer rosterCount = testRosters.getRosterCountForStudentAndOrgNode(studentId, orgNodeId);
				if (rosterCount.intValue() >0) {
					orgNodeStudents.deactivateOrgNodeStudentForStudentAndOrgNode(studentId, orgNodeId);
				}
				else {
					orgNodeStudents.deleteOrgNodeStudentForStudentAndOrgNode(studentId, orgNodeId);
				}
			}
			orgNodeStus = orgNodeStudents.getOrgNodeStudentForStudent(studentId);

			boolean canDeactivate = true;
			for (int i=0; i<orgNodeStus.length; i++) {
				if ("AC".equals(orgNodeStus[i].getActivationStatus())) {
					canDeactivate = false;
					break;
				}
			}
			DeleteStudentStatus status;
			if (orgNodeStus != null && orgNodeStus.length >0) {
				if (canDeactivate)
					students.deactivateStudent(studentId);
				status = DeleteStudentStatus.INACTIVATED;
			}
			else {
				studentManagement.deleteStudentDemographicDataForStudent(studentId);                
				accommodation.deleteStudentAccommodations(studentId);
				//ca-abe change -student address deletion
				studentManagement.deleteStudentContactInformationDataForStudent(studentId);   
				//student workforce section deletion
				studentManagement.deleteStudentAdditionalDataForStudent(studentId,"Supplement data for Workforce Student");
				//student prg-goal deletion
				studentManagement.deleteStudentPrgGoalDataForStudent(studentId);   
				//student edu_instru deletion
				studentManagement.deleteStudentAdditionalDataForStudent(studentId,"Education And Instruction");
				students.deleteStudent(studentId);
				status = DeleteStudentStatus.DELETED;
			}   

			return status;      
		} catch (SQLException se) {
			StudentDataDeletionException one = new StudentDataDeletionException("StudentManagementImpl: deleteStudent("+studentId+"): " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}

	/**
	 * Create student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentDemographicData - contains the student's demographic information
	 * @return studentDemographicDataId
	 * @throws CTBBusinessException
	 */
	public Integer createStudentDemographicData(String userName, StudentDemographicData studentDemographicData) throws CTBBusinessException
	{
		Integer studentId = studentDemographicData.getStudentId();
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentDemographicData");

		try {
			Integer count = studentManagement.getCountStudentDemographicDataForStudentAndCustomerDemographic(studentId, studentDemographicData.getCustomerDemographicId());
			if (count.intValue() > 0) 
				throw new StudentDataCreationException("StudentManagementImpl: createStudentDemographicData: Student with id '" + studentId+ "' has existing demographic data '"+studentDemographicData.getCustomerDemographicId()+"'.");
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Integer studentDemographicDataId = studentManagement.getNextPKForStudentDemographicData();            
			studentDemographicData.setStudentDemographicDataId(studentDemographicDataId);                        
			studentDemographicData.setCreatedBy(userId);
			studentDemographicData.setCreatedDateTime(new Date());
			studentManagement.createStudentDemographicData(studentDemographicData);
			return studentDemographicDataId;
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentDemographicData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    


	/**
	 * Update student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentDemographicData  - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void updateStudentDemographicData(String userName, StudentDemographicData studentDemographicData) throws CTBBusinessException
	{
		Integer studentId = studentDemographicData.getStudentId();
		validator.validateStudent(userName, studentId, "StudentManagementImpl.updateStudentDemographicData");

		try {
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			studentDemographicData.setUpdatedBy(userId);
			studentDemographicData.setUpdatedDateTime(new Date());
			studentManagement.updateStudentDemographicData(studentDemographicData);
		} catch (SQLException se) {
			StudentDataUpdateException tee = new StudentDataUpdateException("StudentManagementImpl: updateStudentDemographicData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	} 

	/**
	 * Get student demographic data for the specified studentDemographicDataId.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentDemographicDataId - identifies the student demographic data
	 * @return StudentDemographicData
	 * @throws CTBBusinessException
	 */
	public StudentDemographicData getStudentDemographicData(String userName, Integer studentDemographicDataId) throws CTBBusinessException
	{
		try {
			StudentDemographicData sdd = studentManagement.getStudentDemographicData(studentDemographicDataId);
			Integer studentId = sdd.getStudentId();
			if (studentId !=null) {
				try {
					validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentDemographicData");
				} catch (ValidationException ve) {
					//validate student if student is not across organization
					validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentDemographicData");
				}
			}
			return sdd;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}


	/**
	 * Delete student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student
	 * @throws CTBBusinessException
	 */
	public void deleteStudentDemographicDataForStudent(String userName, Integer studentId) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.deleteStudentDemographicDataForStudent");

		try {
			studentManagement.deleteStudentDemographicDataForStudent(studentId);
		} catch (SQLException se) {
			StudentDataDeletionException tee = new StudentDataDeletionException("StudentManagementImpl: deleteStudentDemographicDataForStudent: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}    

	/**
	 * Delete student demographic data for the specified studentDemographicDataId.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentDemographicDataId - identifies the student demographic data
	 * @throws CTBBusinessException
	 */
	public void deleteStudentDemographicData(String userName, Integer studentDemographicDataId) throws CTBBusinessException
	{
		//validator.validateStudent(userName, studentId, "StudentManagementImpl.deleteStudentDemographicData");

		try {
			studentManagement.deleteStudentDemographicData(studentDemographicDataId);
		} catch (SQLException se) {
			StudentDataDeletionException tee = new StudentDataDeletionException("StudentManagementImpl: deleteStudentDemographicData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}   

	/**
	 * @common:operation
	 */
	public OrganizationNode[]  getStudentsOrganizationUpload(String userName, Integer studentId) throws Exception 
	{

		validator.validateStudent(userName, studentId, "StudentManagementImpl.getManageStudent");

		try {
			OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(studentId.intValue(), userName);
			return orgNodes;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getManageStudent: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Retrieves the set of online reports available to a user's customer
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeId - identifies the org node
	 * @param  programId - identifies the program
	 * @return CustomerReportData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public CustomerReportData getCustomerReportData(String userName,Integer orgNodeId, Integer programId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException {
		try {
			validator.validate(userName, null, "testAdmin.getCustomerReportData");
			CustomerReportData crd = new CustomerReportData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
//			Integer [] topOrgNodeIds = orgNode.getTopOrgNodeIdsForUser(userName);
//			Integer orgNodeId = topOrgNodeIds[0];
			CustomerReport [] cr = reportBridge.getReportAssignmentsForUser(userName, programId, orgNodeId);
			for (int i=0; i < cr.length; i++) {
				String reportURL = cr[i].getReportUrl();
//				String encryptedProgramId = DESUtils.encrypt(String.valueOf(cr[i].getActiveProgramId()), cr[i].getSystemKey());
				String encryptedProgramId = DESUtils.encrypt(String.valueOf(programId), cr[i].getSystemKey());
//				String paramsPlainText = "NodeInstanceId="+cr[i].getOrgNodeId()
//				+"&LevelId="+cr[i].getCategoryLevel()+"&Timestamp="+(new Date()).toString();
				String paramsPlainText = "NodeInstanceId="+orgNodeId
				+"&LevelId="+cr[i].getCategoryLevel()+"&Timestamp="+(new Date()).toString();
				String encryptedParams = DESUtils.encrypt(paramsPlainText, cr[i].getCustomerKey());
				reportURL = reportURL +"?sys="+encryptedProgramId+"&parms="+encryptedParams;
				cr[i].setReportUrl(reportURL);
			}
			crd.setCustomerReports(cr, pageSize);
			if(filter != null) crd.applyFiltering(filter);
			if(sort != null) crd.applySorting(sort);
			if(page != null) crd.applyPaging(page);
			return crd;
		} catch (SQLException se) {
			CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("StudentManagementImpl: getCustomerReportData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

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
			CustomerReportDataNotFoundException tee = new CustomerReportDataNotFoundException("StudentManagementImpl: userHasReports: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	private String generateUniqueStudentUserName(Student student, String studentLoginIdSequence) throws SQLException{

		String userName = StudentUtils.generateBasicStudentUsername(student, "", studentLoginIdSequence);
		int count = 0;
		while (studentManagement.isExistingStudentUserName(userName)) {
			count++;
			String suffix = "-"+count;
			userName = StudentUtils.generateBasicStudentUsername(student, suffix, studentLoginIdSequence);
		}
		return userName;

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
			= studentManagement.getCustomerConfigurationValues(configId);
			return customerConfigurationValues;
		} catch (SQLException se) {
			CustomerConfigurationDataNotFoundException tee = new CustomerConfigurationDataNotFoundException("StudentManagementImpl: getCustomerConfigurations: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	/**CHANGES for CA_ABE
	 * retrieve States and places it in cache.
	 * @common:operation
	 * @param void
	 * @return USState[]
	 * @throws CTBBusinessException
	 */
	public USState[] getStates() throws CTBBusinessException {

		try {

			String key = "STATES";
			USState[] states = (USState[]) SimpleCache.checkCache("usStateArray", key, "manageStudent");
			if (states == null) {
				states = addresses.getStates();
				SimpleCache.cacheResult("usStateArray", key, states, "manageStudent");
			}

			return states;
		} catch(SQLException se) {
			OrgNodeDataNotFoundException dataNotfound = 
				new OrgNodeDataNotFoundException
				("StudentManagement.Failed");
			dataNotfound.setStackTrace(se.getStackTrace());
			throw dataNotfound;
		} catch (Exception e) {
			UserDataRetrivalException dataRetrivalException = 
				new UserDataRetrivalException
				("StudentManagement.Failed");
			dataRetrivalException.setStackTrace(e.getStackTrace());
			throw dataRetrivalException;
		}

	}

	/**
	 * Get student demographic for the specified customer and student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param studentId - identifies the student whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return StudentDemographic []
	 * @throws CTBBusinessException
	 */
	public StudentProgramGoal [] getStudentProgramGoals(String userName, Integer customerId, Integer studentId, boolean returnInvisible) throws CTBBusinessException
	{
		if (studentId !=null) {
			try {
				validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentProgramGoals");
			} catch (ValidationException ve) {
				//validate student if student is not across organization
				validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentProgramGoals");
			}
		}
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getStudentProgramGoals");
		try {
			CustomerProgramGoal [] customerProgramGoals;

			customerProgramGoals = studentManagement.getCustomerProgramGoals(customerId.intValue());



			StudentProgramGoal [] studentProgramGoal = null;

			if (customerProgramGoals != null && customerProgramGoals.length > 0) 
			{
				studentProgramGoal = new StudentProgramGoal[customerProgramGoals.length];
				for (int i = 0; i < customerProgramGoals.length; i++) {
					studentProgramGoal[i] = new StudentProgramGoal(customerProgramGoals[i]);
					studentProgramGoal[i].setStudentId(studentId);
					StudentProgramGoalValue [] studentProgramGoalValues;
					studentProgramGoalValues = studentManagement.getStudentProgramGoalValues(studentProgramGoal[i].getCustomerPrgGoalId().intValue(), studentId == null? -1: studentId.intValue());
					if (customerProgramGoals[i].getLabelName().equals("Provider Use") && studentId != null) {
						studentProgramGoalValues = studentManagement.getStudentProGProvider(studentProgramGoal[i].getCustomerPrgGoalId().intValue(), studentId.intValue());
					}
					studentProgramGoal[i].setStudentProgramGoalValues(studentProgramGoalValues);
				}
				//retrieve customer provider use value

			}
			return studentProgramGoal;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}


	/**
	 * Create student demographic data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void createStudentProgAndGoals(String userName, Integer studentId, StudentProgramGoal [] studentProgramGoals) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentDemographics");

		try {
			Integer count = studentManagement.getCountStudentPrgGoalDataForStudent(studentId);
			if (count.intValue() > 0) 
				throw new StudentDataCreationException("StudentManagementImpl: createStudentProgAndGoals: Student with id '" + studentId+ "' has existing demographic data.");
			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			for (int i=0; studentProgramGoals!= null && i<studentProgramGoals.length; i++) {
				StudentProgramGoalValue [] studentProgramGoalValues = studentProgramGoals[i].getStudentProgramGoalValues();
				for (int j=0; studentProgramGoalValues!=null && j<studentProgramGoalValues.length; j++) {
					if (studentProgramGoalValues[j] != null && "true".equals(studentProgramGoalValues[j].getSelectedFlag())) {
						StudentProgramGoalData studentProgramGoalData = new StudentProgramGoalData();
						studentProgramGoalData.setStudentProgramGoalDataId(studentManagement.getNextPKForStudentPrgGoalData());                        
						studentProgramGoalData.setStudentId(studentId);
						studentProgramGoalData.setCustomerPrgGoalId(studentProgramGoals[i].getCustomerPrgGoalId());
						studentProgramGoalData.setValueName(studentProgramGoalValues[j].getValueName());
						studentProgramGoalData.setCreatedBy(userId);
						studentProgramGoalData.setCreatedDateTime(now);
						studentManagement.createStudentProgramGoalData(studentProgramGoalData);
					}
				}
			}
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}


	/**
	 * Get student workforce for the specified customer and student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param studentId - identifies the student whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return StudentDemographic []
	 * @throws CTBBusinessException
	 */
	public StudentOtherDetail [] getStudentWorkFoceDetails(String userName, Integer customerId, Integer studentId, boolean returnInvisible) throws CTBBusinessException
	{
		if (studentId !=null) {
			try {
				validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentWorkFoceDetails");
			} catch (ValidationException ve) {
				//validate student if student is not across organization
				validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentWorkFoceDetails");
			}
		}
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getStudentWorkFoceDetails");
		try {

			StudentOtherDetail [] studentOtherDetails = null;
			Map workfroceOptions = StudentWorkForceUtils.getWorkforceList();

			studentOtherDetails = new StudentOtherDetail[workfroceOptions.size()];
			Iterator iterate = workfroceOptions.keySet().iterator();

			int i=0;
			while (iterate.hasNext()) {
				String paramName = (String)iterate.next();
				List parameterOptions= (List)workfroceOptions.get(paramName);
				String[] stgpworkforceoptions = null;
				if(parameterOptions != null && parameterOptions.size() > 0){
					stgpworkforceoptions = new String[parameterOptions.size()];
					for(int k= 0 ; k < parameterOptions.size(); k++  ) {
						stgpworkforceoptions[k]= (String)parameterOptions.get(k);
					}

				} else {
					stgpworkforceoptions = new String[1];
					stgpworkforceoptions[0]="";
				}


				String[] paramsTyes = paramName.split("_");
				StudentOtherDetail stuOtherDetail = new StudentOtherDetail();
				stuOtherDetail.setSectionName("Supplement data for Workforce Student");
				stuOtherDetail.setLabelName(paramsTyes[0]);
				stuOtherDetail.setSortOrder(i);
				stuOtherDetail.setValueCardinality(paramsTyes[1]);
				StudentOtherDetailValue[] studentOtherDetailValues = new StudentOtherDetailValue[stgpworkforceoptions.length];
				int j = 0;
				for(String stgpworkforce: stgpworkforceoptions ) {

					StudentOtherDetailValue stprggoalval = new StudentOtherDetailValue();
					stprggoalval.setValueName(stgpworkforce);
					stprggoalval.setLabelName(paramsTyes[0]);
					stprggoalval.setVisible("false");
					stprggoalval.setSelectedFlag("false");
					studentOtherDetailValues[j] = stprggoalval;
					j++;
				}


				stuOtherDetail.setStudentOtherDetailValues(studentOtherDetailValues);
				studentOtherDetails[i]= stuOtherDetail;


				i++;
			}



			if (studentId !=null) {
				//retrieve from database and set the selected flag


				StudentWorkForceData [] studentOtherDetailValuesDB = studentManagement.getStudentWorkforceDetails(studentId,"Supplement data for Workforce Student"); 
				if(studentOtherDetailValuesDB != null){

					for(int k=0 ; k < studentOtherDetailValuesDB.length ; k++ ) {

						for(int m=0 ; m < studentOtherDetails.length ; m++ ) {
							if (studentOtherDetailValuesDB[k].getLabelName().
									equals(studentOtherDetails[m].getLabelName())) { 

								StudentOtherDetailValue [] studentOtherDetailValues = studentOtherDetails[m].getStudentOtherDetailValues();
								for(int l=0 ; l < studentOtherDetailValues.length ; l++ ) {

									if (studentOtherDetailValuesDB[k].getLabelName().
											equals(studentOtherDetailValues[l].getLabelName())) { 
										if (studentOtherDetailValuesDB[k].getValueName()!= null
												&& studentOtherDetailValues[l].getValueName() != null
												&&(studentOtherDetailValuesDB[k].getValueName().
														equals(studentOtherDetailValues[l].getValueName()))) {
											studentOtherDetailValues[l].setSelectedFlag("true");
											studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
											break;
										}

									}

									if (studentOtherDetailValuesDB[k].getLabelName() != null
											&& studentOtherDetailValues[l].getLabelName() != null
											&& studentOtherDetailValuesDB[k].getLabelName().equals("Scheduled Work Hours Per Week")
											&& studentOtherDetailValues[l].getLabelName().equals("Scheduled Work Hours Per Week")) {
										if (studentOtherDetailValuesDB[k].getValueName()!= null) {
											studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
										} else {
											studentOtherDetailValues[l].setValueName("");
										}
										studentOtherDetailValues[l].setSelectedFlag("true");
										break;
									}

									if (studentOtherDetailValuesDB[k].getLabelName().equals("Provider Use") &&
											studentOtherDetailValues[l].getLabelName().equals("Provider Use")) {
										if (studentOtherDetailValuesDB[k].getValueName()!= null) {
											studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
										} else {
											studentOtherDetailValues[l].setValueName("");
										}
										studentOtherDetailValues[l].setSelectedFlag("true");
										break;
									}
									if (studentOtherDetailValuesDB[k].getLabelName().equals("Hourly Wage") &&
											studentOtherDetailValues[l].getLabelName().equals("Hourly Wage")) {
										if (studentOtherDetailValuesDB[k].getValueName()!= null) {
											studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
										} else {
											studentOtherDetailValues[l].setValueName("");
										}
										studentOtherDetailValues[l].setSelectedFlag("true");
										break;
									}


								}
								break;
							}
						}
					}
				}
			}






			return studentOtherDetails;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentDemographics: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Create student WorkForce data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void createStudentWorkForceData(String userName, Integer studentId, StudentOtherDetail [] studentOtherDetail) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentWorkForceData");

		try {
			/*Integer count = studentManagement.getCountStudentPrgGoalDataForStudent(studentId);
            if (count.intValue() > 0) 
                throw new StudentDataCreationException("StudentManagementImpl: createStudentProgAndGoals: Student with id '" + studentId+ "' has existing demographic data.");
			 */ User user = getUserDetails(userName, userName);
			 Integer userId = user.getUserId();
			 Date now = new Date();
			 for (int i=0; studentOtherDetail!= null && i<studentOtherDetail.length; i++) {
				 StudentOtherDetailValue [] studentOtherDetailValues = studentOtherDetail[i].getStudentOtherDetailValues();
				 for (int j=0; studentOtherDetailValues!=null && j<studentOtherDetailValues.length; j++) {
					 if (studentOtherDetailValues[j] != null && "true".equals(studentOtherDetailValues[j].getSelectedFlag())) {
						 StudentWorkForceData studentWorkForceData = new StudentWorkForceData();
						 Integer studentAdditionalId = studentManagement.getNextPKForStudentOtherData();
						 studentWorkForceData.setStudentAdditionalDataId(studentAdditionalId);                        
						 studentWorkForceData.setStudentId(studentId);
						 studentWorkForceData.setSectionName(studentOtherDetail[i].getSectionName());
						 studentWorkForceData.setValueName(studentOtherDetailValues[j].getValueName());
						 studentWorkForceData.setLabelName(studentOtherDetail[i].getLabelName());
						 studentWorkForceData.setCreatedBy(userId);
						 studentWorkForceData.setCreatedDateTime(now);
						 studentManagement.createStudentWorkforceData(studentWorkForceData);
					 }

				 }
			 }
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentWorkForceData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}

	/**
	 * Get student demographic for the specified customer and student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param studentId - identifies the student whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return StudentDemographic []
	 * @throws CTBBusinessException
	 */
	public StudentOtherDetail [] getStudentEduAndInstr(String userName, Integer customerId, Integer studentId, boolean returnInvisible) throws CTBBusinessException
	{
		if (studentId !=null) {
			try {
				validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentEduAndInstr");
			} catch (ValidationException ve) {
				//validate student if student is not across organization
				validator.validateStudentAcrossOrg(userName, studentId, "StudentManagementImpl.getStudentEduAndInstr");
			}
		}

		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getStudentEduAndInstr");
		try {

			StudentOtherDetail [] studentOtherDetails = null;
			Map eduAndInstrOptions = StudentEduAndInstrUtils.getEduAndInstrList();

			studentOtherDetails = new StudentOtherDetail[eduAndInstrOptions.size()];
			Iterator iterate = eduAndInstrOptions.keySet().iterator();

			int i=0;
			while (iterate.hasNext()) {
				String paramName = (String)iterate.next();
				String[] paramsTyes = paramName.split("_");
				List parameterOptions= (List)eduAndInstrOptions.get(paramName);
				String[] stgpeduAndInstroptions = null;
				String tempStgpeduAndInstroptions = "";
				if(paramsTyes[0].equals("Skill Level")){
					if(parameterOptions != null && parameterOptions.size()==1){
						stgpeduAndInstroptions = new String[5];
						HashMap skillMap = (HashMap)parameterOptions.get(0);
						Iterator iterator = skillMap.keySet().iterator();
						int k=0;
						while (iterator.hasNext()) { 
							String skillName = (String)iterator.next();
							String mapvalue[]= (String[])skillMap.get(skillName);
							tempStgpeduAndInstroptions = skillName+",";
							for(int skis= 0 ; skis < mapvalue.length; skis++  ) {
								tempStgpeduAndInstroptions = tempStgpeduAndInstroptions +(String)mapvalue[skis];
								if(skis != mapvalue.length-1)
									tempStgpeduAndInstroptions = tempStgpeduAndInstroptions+",";
							}
							stgpeduAndInstroptions[k] = tempStgpeduAndInstroptions;
							k++;
						}

					}


				} else {

					if(parameterOptions != null && parameterOptions.size() > 0){
						stgpeduAndInstroptions = new String[parameterOptions.size()];
						for(int k= 0 ; k < parameterOptions.size(); k++  ) {
							stgpeduAndInstroptions[k]= (String)parameterOptions.get(k);
						}

					} else {
						stgpeduAndInstroptions = new String[1];
						stgpeduAndInstroptions[0]="";
					}


				}



				StudentOtherDetail stuOtherDetail = new StudentOtherDetail();
				stuOtherDetail.setSectionName("Education And Instruction");
				stuOtherDetail.setLabelName(paramsTyes[0]);
				stuOtherDetail.setSortOrder(i);
				stuOtherDetail.setValueCardinality(paramsTyes[1]);
				stuOtherDetail.setVisible("true");
				StudentOtherDetailValue[] studentotherDetailValues = new StudentOtherDetailValue[stgpeduAndInstroptions.length];
				int j = 0;
				for(String stgpeduAndInstr: stgpeduAndInstroptions ) {

					StudentOtherDetailValue steduAndInstrval = new StudentOtherDetailValue();
					steduAndInstrval.setValueName(stgpeduAndInstr);
					steduAndInstrval.setVisible("true");
					if(stuOtherDetail.getLabelName().equals("Earned the above outside the U.S.")) {
						if(stgpeduAndInstroptions[1].equals("No"))
							steduAndInstrval.setSelectedFlag("true");
					}
					else {
						steduAndInstrval.setSelectedFlag("false");
					}
					studentotherDetailValues[j] = steduAndInstrval;
					j++;
				}

				if(stuOtherDetail.getLabelName().equals("Instructional Program")) {
					stuOtherDetail.setMultipleAllowedFlag("true");
				} else {
					stuOtherDetail.setMultipleAllowedFlag("false");
				}
				stuOtherDetail.setStudentOtherDetailValues(studentotherDetailValues);
				studentOtherDetails[i]= stuOtherDetail;


				i++;
			}

			if (studentId !=null) {
				//retrieve from database and set the selected flag
				StudentWorkForceData [] studentOtherDetailValuesDB;

				studentOtherDetailValuesDB = studentManagement.getStudentWorkforceDetails(studentId,"Education And Instruction"); 


				if(studentOtherDetailValuesDB != null){

					for(int j=0 ; j < studentOtherDetails.length ; j++ ){
						StudentOtherDetailValue [] studentOtherDetailValues = studentOtherDetails[j].getStudentOtherDetailValues();

						for(int k=0 ; k < studentOtherDetailValuesDB.length ; k++ ) {

							if(studentOtherDetailValuesDB[k].getLabelName().equals("Date of Entry into this Class") 
									&& studentOtherDetails[j].getLabelName().equals("Date of Entry into this Class")){
								for(int l=0; l < studentOtherDetailValues.length; l++){
									if(studentOtherDetailValues[l].getValueName().equals(studentOtherDetailValuesDB[k].getValueName())) {
										studentOtherDetailValues[l].setValueCode(studentOtherDetailValuesDB[k].getValue());
										break;
									}
								}

							}
							

							if(studentOtherDetailValuesDB[k].getLabelName().equals("Skill Level") 
									&& studentOtherDetails[j].getLabelName().equals("Skill Level")){
								for(int l=0; l < studentOtherDetailValues.length; l++){
									if(studentOtherDetailValues[l].getValueName().equals(studentOtherDetailValuesDB[k].getValueName())) {
										studentOtherDetailValues[l].setValueCode(studentOtherDetailValuesDB[k].getValue());
										break;
									}
								}

							}

							if(studentOtherDetailValuesDB[k].getLabelName().equals("Instructional Program") 
									&& studentOtherDetails[j].getLabelName().equals("Instructional Program")){
								for(int l=0; l < studentOtherDetailValues.length; l++){
									if(studentOtherDetailValues[l].getValueName().equals(studentOtherDetailValuesDB[k].getValueName())) {
										studentOtherDetailValues[l].setSelectedFlag("true");
										studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
										break;
									}
								}

							}


							if (studentOtherDetailValuesDB[k].getLabelName().equals("Class Number") &&
									studentOtherDetails[j].getLabelName().equals("Class Number")) {
								if (studentOtherDetailValuesDB[k].getValueName()!= null) {
									studentOtherDetailValues[0].setValueName(studentOtherDetailValuesDB[k].getValueName());
								} else {
									studentOtherDetailValues[0].setValueName("");
								}
								studentOtherDetailValues[0].setSelectedFlag("true");
							}

							if (studentOtherDetailValuesDB[k].getLabelName().
									equals(studentOtherDetails[j].getLabelName())) {

								for(int l=0; l < studentOtherDetailValues.length; l++){
									if (studentOtherDetailValuesDB[k].getValueName().
											equals(studentOtherDetailValues[l].getValueName())) {
										studentOtherDetailValues[l].setSelectedFlag("true");
										studentOtherDetailValues[l].setValueName(studentOtherDetailValuesDB[k].getValueName());
									}
								}
							}
						}

					}

				}
			}





			return studentOtherDetails;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentEduAndInstr: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}

	}

	/**
	 * Create student WorkForce data for the specified student.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param studentId - identifies the student 
	 * @param studentDemographics [] - contains the student's demographic information
	 * @throws CTBBusinessException
	 */
	public void createStudentEducationInstructionData(String userName, Integer studentId, StudentOtherDetail [] studentOtherDetail) throws CTBBusinessException
	{
		validator.validateStudent(userName, studentId, "StudentManagementImpl.createStudentWorkForceData");

		try {

			User user = getUserDetails(userName, userName);
			Integer userId = user.getUserId();
			Date now = new Date();
			for (int i=0; studentOtherDetail!= null && i<studentOtherDetail.length; i++) {
				StudentOtherDetailValue [] studentOtherDetailValues = studentOtherDetail[i].getStudentOtherDetailValues();
				for (int j=0; studentOtherDetailValues!=null && j<studentOtherDetailValues.length; j++) {
					if (studentOtherDetailValues[j] != null && "true".equals(studentOtherDetailValues[j].getSelectedFlag())) {
						StudentWorkForceData studentWorkForceData = new StudentWorkForceData();
						Integer studentAdditionalId = studentManagement.getNextPKForStudentOtherData();
						studentWorkForceData.setStudentAdditionalDataId(studentAdditionalId);                        
						studentWorkForceData.setStudentId(studentId);
						studentWorkForceData.setSectionName(studentOtherDetail[i].getSectionName());
						studentWorkForceData.setValueName(studentOtherDetailValues[j].getValueName());
						studentWorkForceData.setValue(studentOtherDetailValues[j].getValueCode());
						studentWorkForceData.setLabelName(studentOtherDetail[i].getLabelName());
						studentWorkForceData.setCreatedBy(userId);
						studentWorkForceData.setCreatedDateTime(now);
						studentManagement.createStudentEducationInstructionData(studentWorkForceData);
					}

				}
			}
		} catch (SQLException se) {
			StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: createStudentWorkForceData: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	}


	//END-  added for CA-ABE


} 
