package com.ctb.control.studentManagement; 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.request.FilterParams.FilterParam;
import com.ctb.bean.studentManagement.CustomerConfiguration;
import com.ctb.bean.studentManagement.CustomerConfigurationValue;
import com.ctb.bean.studentManagement.CustomerDemographic;
import com.ctb.bean.studentManagement.CustomerDemographicValue;
import com.ctb.bean.studentManagement.ItemResponseData;
import com.ctb.bean.studentManagement.ManageBulkStudentData;
import com.ctb.bean.studentManagement.ManageStudent;
import com.ctb.bean.studentManagement.ManageStudentData;
import com.ctb.bean.studentManagement.MusicFiles;
import com.ctb.bean.studentManagement.OrganizationNode;
import com.ctb.bean.studentManagement.OrganizationNodeData;
import com.ctb.bean.studentManagement.StudentDemographic;
import com.ctb.bean.studentManagement.StudentDemographicData;
import com.ctb.bean.studentManagement.StudentDemographicValue;
import com.ctb.bean.testAdmin.StudentReportIrsScore;
import com.ctb.bean.studentManagement.StudentScoreReport;
import com.ctb.bean.testAdmin.CustomerReport;
import com.ctb.bean.testAdmin.CustomerReportData;
import com.ctb.bean.testAdmin.Node;
import com.ctb.bean.testAdmin.OrgNodeStudent;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentAccommodations;
import com.ctb.bean.testAdmin.StudentAccommodationsData;
import com.ctb.bean.testAdmin.StudentDemoGraphics;
import com.ctb.bean.testAdmin.StudentDemographicDataBean;
import com.ctb.bean.testAdmin.StudentNode;
import com.ctb.bean.testAdmin.StudentNodeData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.User;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.studentManagement.CustomerConfigurationDataNotFoundException;
import com.ctb.exception.studentManagement.CustomerContentAreasNotFoundException;
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
import com.ctb.util.studentManagement.DeleteStudentStatus;
import com.ctb.util.studentManagement.DynamicSQLUtils;
import com.ctb.util.studentManagement.StudentUtils;

/**
 * @author John_Wang
 *
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class StudentManagementImpl implements StudentManagement
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
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.validation.Validator validator;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.db.StudentManagement studentManagement;
	
	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.ImmediateReportingIrs immediateReportingIrs;

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
		validator.validateStudent(userName, studentId, "StudentManagementImpl.getManageStudent");

		try {
			ManageStudent student = studentManagement.getManageStudent(studentId.intValue());
			OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(studentId.intValue(), userName);
			student.setOrganizationNodes(orgNodes);
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
		validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentAccommodations");

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
			
			studentManagement.setRosterUpdateFlag(studentId);
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
	 * Get customer demographic for the specified customer.
	 ** @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @param returnInvisible - indicates whether to return invisible data/values
	 * @return CustomerDemographic []
	 * @throws CTBBusinessException
	 */
	public CustomerDemographic [] getCustomerDemographics(String userName, Integer customerId) throws CTBBusinessException
	{
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getCustomerDemographics");
		try {
			CustomerDemographic [] customerDemographics;

			customerDemographics = studentManagement.getCustomerDemographicsBasedOnCardinality(customerId.intValue(), "SINGLE");


			if (customerDemographics == null || customerDemographics.length == 0) {

				customerDemographics = studentManagement.getCustomerDemographicsBasedOnCardinality(CTB_CUSTOMER_ID, "SINGLE");

			}

			if (customerDemographics != null && customerDemographics.length > 0) 
			{
				for (int i = 0; i < customerDemographics.length; i++) {
					CustomerDemographicValue [] customerDemographicValues;

					customerDemographicValues = studentManagement.getCustomerDemographicValues(customerDemographics[i].getId().intValue());

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
		if (studentId !=null)
			validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentDemographics");
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
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());  // Changes for code entry in student_demographic_data table
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
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());
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
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());
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
						studentDemographicData.setValue(studentDemographicValues[j].getValueCode());
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

	public ManageStudentData findStudentsAtAndBelowTopOrgNodesWithDynamicSQLForScoring(String userName, Integer catalogId, FilterParams filter, PageParams page, SortParams sort ) throws CTBBusinessException
	{
		try {
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			FilterParams statusFilter = new FilterParams();
			statusFilter.setFilterParams(new FilterParam[0]);
			
			if (filter != null) {
				FilterParam [] filterParams = filter.getFilterParams();
				for (int i = 0; i < filterParams.length; i++) {
			            FilterParam filterParam = filterParams[i];
			            if (filterParam != null) {
			                String fieldName = filterParam.getField();
			                if(fieldName.equals("ScoringStatus")) {
			                	statusFilter.setFilterParams(filterParams);
			                	break;
			                }
			            }
				}
			}
			Integer totalCount = null;
			String searchCriteria = "";
			if (filter != null) {
				searchCriteria = DynamicSQLUtils.generateWhereClauseForFilter(filter);
				filter.setFilterParams(new FilterParam[0]);
				//totalCount = studentManagement.getStudentCountAtAndBelowUserTopNodes(userName);
				ManageStudent [] studentTotalCount = null;
				studentTotalCount = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaForScoring(userName, catalogId, "");
				totalCount = studentTotalCount.length;
			}
			String orderByClause = "";
			/*if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
				sort = null;
			}*/
			
			//searchCriteria = searchCriteria + orderByClause;
			ManageStudent [] students = null;
            
           students = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaForScoring(userName, catalogId,searchCriteria);
           for(ManageStudent student : students) {
        	   student.setScoringStatus( studentManagement.getScoringStatus(student.getRosterId(), student.getItemSetIdTC()));
           }
			
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			if(statusFilter != null) std.applyFiltering(statusFilter);
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
			//START- (LLO82) StudentManagement Changes For LasLink product
			if(manageStudent.getTestPurpose() != null) {
				student.setTestPurpose(manageStudent.getTestPurpose());
			}
			//END- (LLO82) StudentManagement Changes For LasLink product
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
			validator.validateStudent(userName, studentId, "StudentManagementImpl.getStudentDemographics");
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

	//bulk accommodation
	public ManageBulkStudentData findBulkStudentsForOrgNode(String userName, Integer orgNodeId, FilterParams filter, FilterParams demoFilter,
			PageParams page, SortParams sort) throws CTBBusinessException
			{
		validator.validateNode(userName, orgNodeId, "StudentManagementImpl.findStudentsForOrgNode");
		try {

			Integer customerId = users.getCustomer(userName).getCustomerId();
			ManageBulkStudentData std = new ManageBulkStudentData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			//Integer [] orgNodeIds = studentManagement.getTopOrgNodeIdsForUser(userName);
			SessionStudent [] students = studentManagement.getBulkStudentsForOrgNode(userName, orgNodeId);
			std.setManageStudents(students, pageSize);
			//START- change for TotalStudent Count display
			int totalStudents = 0;
			if( std.getManageStudents() != null && std.getManageStudents().length > 0){
				totalStudents = std.getTotalCount();
			} 
			//END- change for TotalStudent Count display
			if(filter != null) std.applyFiltering(filter);
			if(demoFilter != null && demoFilter.getFilterParams().length > 0 &&
					std!= null &&  std.getManageStudents() != null && std.getManageStudents().length > 0){
				Integer[] selectedStudentid	= new Integer[std.getManageStudents().length];
				List<SessionStudent> filteredStudents = new ArrayList<SessionStudent>();
				HashMap <Integer,SessionStudent> studentMapData = new HashMap<Integer,SessionStudent>();
				for(int i = 0 ; i < std.getManageStudents().length ; i++){
					selectedStudentid[i]=  std.getManageStudents()[i].getStudentId();
					studentMapData.put(selectedStudentid[i], std.getManageStudents()[i]);

				}
				
				//Start -Inclause changes
				int inClauselimit = 999;
				
				
				List sdgvsGlobal = new ArrayList();
				
				int loopCounters = selectedStudentid.length / inClauselimit;
				if((selectedStudentid.length % inClauselimit) > 0){
					loopCounters = loopCounters + 1;
				}
				for(int counter=0; counter<loopCounters; counter++){
					Integer[] newselectedStudentid = null;
					String searchbyStudentIds="";
					if((counter+1)!=loopCounters){
						newselectedStudentid = new Integer [inClauselimit];
						System.arraycopy(selectedStudentid, (counter*inClauselimit) , newselectedStudentid, 0, inClauselimit);
					} else {
						int count = selectedStudentid.length % inClauselimit;
						newselectedStudentid = new Integer [count];
						System.arraycopy(selectedStudentid, ((loopCounters-1)*inClauselimit) , newselectedStudentid, 0, count);
					}
					searchbyStudentIds = SQLutils.generateSQLCriteria("std.student_id in  ",newselectedStudentid);
					StudentDemoGraphics[] sdgvs = this.studentManagement.getStudentDemoValues(searchbyStudentIds,customerId);
					sdgvsGlobal.add(sdgvs);
				}
				for(int j = 0 ; j < sdgvsGlobal.size() ; j++){
					StudentDemoGraphics[] sdgvse = (StudentDemoGraphics[])sdgvsGlobal.get(j);
					for(int k = 0 ; k < sdgvse.length ; k++) {
						Integer studentid =  sdgvse[k].getStudentId();
						if(studentMapData.containsKey(studentid)) {
							SessionStudent sst = studentMapData.get(studentid);
							String[] values = new String[2];
							values[0] = sdgvse[k].getLabelName();
							values[1] = sdgvse[k].getValueName();
							sst.setValueMap(values);
						}
					}
				}
				
				//end -Inclause changes
				for(int i = 0 ; i < selectedStudentid.length ; i++){
					SessionStudent sst = studentMapData.get(selectedStudentid[i]);
					filteredStudents.add(sst);
				}

				std.setManageStudents((SessionStudent[]) filteredStudents.toArray(new SessionStudent[0]), pageSize);
				std.applyDemoFiltering(demoFilter);
			} 

			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);
			//START- change for TotalStudent Count display
			std.setTotalCount(totalStudents);
			//END- change for TotalStudent Count display
			//students = std.getManageStudents();
			return std;
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsForOrgNode: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
		catch (Exception se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findStudentsForOrgNode: " + se.getMessage());
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

	/**
	 * Bulk Accommodation
	 * Retrieves a list of org nodes at which the specified user has a role defined.
	 * <br/><br/>Each node contains two counts: the number of students rostered
	 * in the specified admin which are at or below the node (rosterCount),
	 * and the number of students matching the grade and student test
	 * accommodation filter criteria which are at or below the node (studentCount).
	 * If no test admin id is specified, only the student count is populated, not
	 * the roster count.
	 * <br/><br/>The filter params passed to this call only affect the student
	 * count, not the set of org nodes returned, whereas the sort and paging params
	 * affect the org node list, as usual.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param testAdminId - identifies the test admin
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return StudentNodeData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public StudentNodeData getTopStudentNodesForBulkAccommodationUserAndAdmin(String userName, Integer customerId, FilterParams filter,FilterParams demoFilter, PageParams page, SortParams sort) throws CTBBusinessException
	{

		try {
			StudentNodeData ond = new StudentNodeData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			Node [] nodes = orgNode.getTopNodesForUser(userName);
			StudentNode [] studentNodes = new StudentNode [nodes.length];
			for(int i=0;i<nodes.length;i++) {
				studentNodes[i] = new StudentNode(nodes[i]);                     
			}
			ond.setStudentNodes(studentNodes, pageSize);
			if(sort != null) ond.applySorting(sort);
			if(page != null) ond.applyPaging(page);
			studentNodes = ond.getStudentNodes();

			for(int i=0;i<studentNodes.length && studentNodes[i] != null;i++) {
				//filter student accommodation and grades
				//StudentAccommodationsData cachedAccommData = (StudentAccommodationsData) SimpleCache.checkCache5min("STUDENT_ACCOMMODATIONS_BY_ORG", String.valueOf(studentNodes[i].getOrgNodeId()));
				StudentAccommodationsData cachedAccommData = null;
				if(cachedAccommData == null) {
					StudentAccommodations[] accommData = accommodation.getStudentAccommodationsByAncestorNode(studentNodes[i].getOrgNodeId());
					HashMap accommsByOrg = new HashMap(studentNodes.length);
					for(int j=0;j<accommData.length;j++) {
						StudentAccommodations sa = accommData[j];
						ArrayList thisOrgAccoms = (ArrayList) accommsByOrg.get(sa.getOrgNodeId());
						if(thisOrgAccoms == null) thisOrgAccoms = new ArrayList();
						thisOrgAccoms.add(sa);
						accommsByOrg.put(sa.getOrgNodeId(), thisOrgAccoms);
					}
					Object[] keys = accommsByOrg.keySet().toArray();
					for(int j=0;j<keys.length;j++) {
						ArrayList accomms = (ArrayList) accommsByOrg.get(keys[j]);
						cachedAccommData = new StudentAccommodationsData();
						cachedAccommData.setStudentAccommodations((StudentAccommodations[]) accomms.toArray(new StudentAccommodations[0]), null);
						SimpleCache.cacheResult("STUDENT_ACCOMMODATIONS_BY_ORG_BULK_ACCOMMODATION", String.valueOf((Integer)keys[j]), cachedAccommData);
					}
					cachedAccommData = (StudentAccommodationsData) SimpleCache.checkCache("STUDENT_ACCOMMODATIONS_BY_ORG_BULK_ACCOMMODATION", String.valueOf(studentNodes[i].getOrgNodeId()));
				}
				StudentAccommodationsData newAccommData = new StudentAccommodationsData();
				if(cachedAccommData != null && cachedAccommData.getStudentAccommodations() != null) {
					StudentAccommodations[] saArray = new StudentAccommodations[cachedAccommData.getStudentAccommodations().length];
					for(int j=0;j<saArray.length;j++) {
						saArray[j] = cachedAccommData.getStudentAccommodations()[j];
					}
					newAccommData.setStudentAccommodations(saArray, null);
				}

				//filter demographic
				StudentDemographicDataBean newDemoData = new StudentDemographicDataBean();
				ArrayList<Integer> demosByOrgStudent = new ArrayList();

				//StudentDemographicDataBean cachedDemoData = (StudentDemographicDataBean) SimpleCache.checkCache5min("STUDENT_DEMOGRAPHICS_BY_ORG", String.valueOf(studentNodes[i].getOrgNodeId()));
				StudentDemographicDataBean cachedDemoData = null;
				if(cachedDemoData == null && demoFilter!= null && demoFilter.getFilterParams().length > 0) {
					StudentDemoGraphics[] studentDemographicData = studentManagement.getStudentDemographicDataValues(studentNodes[i].getOrgNodeId(),customerId);
					HashMap<Integer , StudentDemoGraphics>  thisOrgDemos = new HashMap(studentNodes.length);

					for(StudentDemoGraphics stdDemo:studentDemographicData ) {
						StudentDemoGraphics thisStudDemos = thisOrgDemos.get(stdDemo.getStudentId());
						if(thisStudDemos != null) {								


							String[] demoValueMap = new String[2];
							demoValueMap[0] = stdDemo.getLabelName();
							demoValueMap[1] = stdDemo.getValueName();
							thisStudDemos.setValueMap(demoValueMap);
						} else {
							String[] demoValueMap = new String[2];
							demoValueMap[0] = stdDemo.getLabelName();
							demoValueMap[1] = stdDemo.getValueName();
							stdDemo.setValueMap(demoValueMap);
							thisOrgDemos.put(stdDemo.getStudentId(),stdDemo);

						}
					}
					Object[] keys = thisOrgDemos.keySet().toArray();
					ArrayList demographs = new ArrayList();
					cachedDemoData = new StudentDemographicDataBean();

					for(int k=0;k<keys.length;k++) {
						StudentDemoGraphics stg = (StudentDemoGraphics)thisOrgDemos.get(keys[k]);
						demographs.add(stg);
					}
					cachedDemoData.setStudentDemographics((StudentDemoGraphics[]) demographs.toArray(new StudentDemoGraphics[0]), null);

				}

				if(cachedDemoData != null && cachedDemoData.getStudentDemographics() != null) {
					StudentDemoGraphics[] saArray = new StudentDemoGraphics[cachedDemoData.getStudentDemographics().length];
					for(int j=0;j<saArray.length;j++) {

						saArray[j] = cachedDemoData.getStudentDemographics()[j];
					}
					newDemoData.setStudentDemographics(saArray, null);
				}
				if(newDemoData.getStudentDemographics() != null && newDemoData.getStudentDemographics().length > 0) {
					if(demoFilter != null) 
						newDemoData.applyDemoFiltering(demoFilter);

					for(int j=0 ; j < newDemoData.getStudentDemographics().length ; j++) {
						demosByOrgStudent.add(newDemoData.getStudentDemographics()[j].getStudentId());
					}

				} 





				ArrayList<Integer> accommoByOrgStudent = new ArrayList();
				if(newAccommData.getStudentAccommodations() != null && newAccommData.getStudentAccommodations().length > 0) {
					if(filter != null) 
						newAccommData.applyFiltering(filter);
					//int filteredCount=newAccommData.getFilteredCount();
					int filteredCount = 0;
					if(demoFilter != null && demoFilter.getFilterParams().length > 0) {
						if(demosByOrgStudent != null && demosByOrgStudent.size() > 0) {

							for(int k=0 ; k < newAccommData.getStudentAccommodations().length ; k++) {
								if( demosByOrgStudent.contains(newAccommData.getStudentAccommodations()[k].getStudentId())){

									filteredCount = filteredCount +1;
									demosByOrgStudent.remove(newAccommData.getStudentAccommodations()[k].getStudentId());
								}
							}
							studentNodes[i].setStudentCount(filteredCount);
						} 

						else {
							studentNodes[i].setStudentCount(0);
						}

					} else {
						studentNodes[i].setStudentCount(newAccommData.getFilteredCount());
					}
				} else if(newDemoData.getStudentDemographics() != null && newDemoData.getStudentDemographics().length > 0) {
					studentNodes[i].setStudentCount(newDemoData.getFilteredCount());
				} else {
					studentNodes[i].setStudentCount(0);
				}

			} 
			//}
			return ond;
		} catch (SQLException se) {
			OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException("ScheduleTestImpl: getTopStudentNodesForUserAndAdmin: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}

	/**
	 * Bulk Accommodation
	 * Retrieves a list of child org nodes of the specified org node
	 * <br/><br/>Each node contains two counts: the number of students rostered
	 * in the specified admin which are at or below the specified node (rosterCount),
	 * and the number of students matching the grade and student test
	 * accommodation filter criteria which are at or below the specified node (studentCount).
	 * If no test admin id is specified, only the student count is populated, not
	 * the roster count.
	 * <br/><br/>The filter params passed to this call only affect the student
	 * count, not the set of org nodes returned, whereas the sort and paging params
	 * affect the org node list, as usual.
	 * @common:operation
	 * @param userName - identifies the user
	 * @param orgNodeId - identifies the parent org node
	 * @param testAdminId - identifies the test admin
	 * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return StudentNodeData
	 * @throws com.ctb.exception.CTBBusinessException
	 */
	public StudentNodeData getStudentNodesForBulkAccomUserParentAndAdmin(String userName, 
			Integer customerId,Integer orgNodeId, FilterParams filter,FilterParams demoFilter,
			PageParams page, SortParams sort)
	throws CTBBusinessException {

		validator.validateNode(userName, orgNodeId, "StudentManagement.getStudentNodesForBulkAccomUserParentAndAdmin");
		try {
			StudentNodeData ond = new StudentNodeData();
			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			Node [] nodes = orgNode.getOrgNodesByParent(orgNodeId);
			StudentNode [] studentNodes = new StudentNode [nodes.length];
			for(int i=0;i<nodes.length;i++) {
				studentNodes[i] = new StudentNode(nodes[i]);                     
			}
			ond.setStudentNodes(studentNodes, pageSize);
			if(sort != null) ond.applySorting(sort);
			if(page != null) ond.applyPaging(page);
			studentNodes = ond.getStudentNodes();

			for(int i=0;i<studentNodes.length && studentNodes[i] != null;i++) {   
				/*StudentAccommodationsData cachedAccommData = (StudentAccommodationsData) SimpleCache.
				checkCache("STUDENT_ACCOMMODATIONS_BY_ORG", String.valueOf(studentNodes[i].getOrgNodeId()));
				// copy cached data to new object before filtering
				 
*/				StudentAccommodationsData cachedAccommData = null;
				StudentAccommodationsData newAccommData = new StudentAccommodationsData();
				if(cachedAccommData == null) {
					StudentAccommodations[] accommData = accommodation.getStudentAccommodationsByAncestorNode(studentNodes[i].getOrgNodeId());
					HashMap accommsByOrg = new HashMap(studentNodes.length);
					for(int j=0;j<accommData.length;j++) {
						StudentAccommodations sa = accommData[j];
						ArrayList thisOrgAccoms = (ArrayList) accommsByOrg.get(sa.getOrgNodeId());
						if(thisOrgAccoms == null) thisOrgAccoms = new ArrayList();
						thisOrgAccoms.add(sa);
						accommsByOrg.put(sa.getOrgNodeId(), thisOrgAccoms);
					}
					Object[] keys = accommsByOrg.keySet().toArray();
					for(int j=0;j<keys.length;j++) {
						ArrayList accomms = (ArrayList) accommsByOrg.get(keys[j]);
						cachedAccommData = new StudentAccommodationsData();
						cachedAccommData.setStudentAccommodations((StudentAccommodations[]) accomms.toArray(new StudentAccommodations[0]), null);
						SimpleCache.cacheResult("STUDENT_ACCOMMODATIONS_BY_ORG_BULK_ACCOMMODATION", String.valueOf((Integer)keys[j]), cachedAccommData);
					}
					cachedAccommData = (StudentAccommodationsData) SimpleCache.checkCache("STUDENT_ACCOMMODATIONS_BY_ORG_BULK_ACCOMMODATION", String.valueOf(studentNodes[i].getOrgNodeId()));
				}
				
				

				StudentDemographicDataBean newDemoData = new StudentDemographicDataBean();
				ArrayList<Integer> demosByOrgStudent = new ArrayList();

				//StudentDemographicDataBean cachedDemoData = (StudentDemographicDataBean) SimpleCache.checkCache5min("STUDENT_DEMOGRAPHICS_BY_ORG", String.valueOf(studentNodes[i].getOrgNodeId()));
				StudentDemographicDataBean cachedDemoData = null;
				if(cachedDemoData == null && demoFilter!= null && demoFilter.getFilterParams().length > 0) {
					StudentDemoGraphics[] studentDemographicData = studentManagement.getStudentDemographicDataValues(studentNodes[i].getOrgNodeId(),customerId);
					HashMap<Integer , StudentDemoGraphics>  thisOrgDemos = new HashMap(studentNodes.length);

					for(StudentDemoGraphics stdDemo:studentDemographicData ) {
						StudentDemoGraphics thisStudDemos = thisOrgDemos.get(stdDemo.getStudentId());
						if(thisStudDemos != null) {								


							String[] demoValueMap = new String[2];
							demoValueMap[0] = stdDemo.getLabelName();
							demoValueMap[1] = stdDemo.getValueName();
							thisStudDemos.setValueMap(demoValueMap);
						} else {
							String[] demoValueMap = new String[2];
							demoValueMap[0] = stdDemo.getLabelName();
							demoValueMap[1] = stdDemo.getValueName();
							stdDemo.setValueMap(demoValueMap);
							thisOrgDemos.put(stdDemo.getStudentId(),stdDemo);

						}
					}
					Object[] keys = thisOrgDemos.keySet().toArray();
					ArrayList demographs = new ArrayList();
					cachedDemoData = new StudentDemographicDataBean();

					for(int k=0;k<keys.length;k++) {
						StudentDemoGraphics stg = (StudentDemoGraphics)thisOrgDemos.get(keys[k]);
						demographs.add(stg);
					}
					cachedDemoData.setStudentDemographics((StudentDemoGraphics[]) demographs.toArray(new StudentDemoGraphics[0]), null);

				}

				if(cachedDemoData != null && cachedDemoData.getStudentDemographics() != null) {
					StudentDemoGraphics[] saArray = new StudentDemoGraphics[cachedDemoData.getStudentDemographics().length];
					for(int j=0;j<saArray.length;j++) {

						saArray[j] = cachedDemoData.getStudentDemographics()[j];
					}
					newDemoData.setStudentDemographics(saArray, null);
				}
				if(newDemoData.getStudentDemographics() != null && newDemoData.getStudentDemographics().length > 0) {
					if(demoFilter != null) 
						newDemoData.applyDemoFiltering(demoFilter);

					for(int j=0 ; j < newDemoData.getStudentDemographics().length ; j++) {
						demosByOrgStudent.add(newDemoData.getStudentDemographics()[j].getStudentId());
					}

				} 



				if(cachedAccommData != null && cachedAccommData.getStudentAccommodations() != null) {
					StudentAccommodations[] saArray = new StudentAccommodations
					[cachedAccommData.getStudentAccommodations().length];
					for(int j=0;j<saArray.length;j++) {
						saArray[j] = cachedAccommData.getStudentAccommodations()[j];
					}
					newAccommData.setStudentAccommodations(saArray, null);
					if(filter != null) 
						newAccommData.applyFiltering(filter);
					if(demoFilter != null && demoFilter.getFilterParams().length > 0) {
						if(demosByOrgStudent != null && demosByOrgStudent.size() > 0 ){
							int filteredCount=0;
							for(int k=0 ; k < newAccommData.getStudentAccommodations().length ; k++) {
								if(demosByOrgStudent.contains(newAccommData.getStudentAccommodations()[k].getStudentId())){

									filteredCount = filteredCount + 1;
								}
							}
							studentNodes[i].setStudentCount(filteredCount);
						} else {

							studentNodes[i].setStudentCount(0);
						}
					}else {
						studentNodes[i].setStudentCount(newAccommData.getFilteredCount());
					}



				}  else  if(demosByOrgStudent != null && demosByOrgStudent.size() > 0 ){
					studentNodes[i].setStudentCount(newDemoData.getFilteredCount());
				}else {
					studentNodes[i].setStudentCount(0);
				}

			}
			return ond;
		} catch (SQLException se) {
			OrgNodeDataNotFoundException one = new OrgNodeDataNotFoundException(
					"ScheduleTestImpl: getStudentNodesForParent: " + se.getMessage());
			one.setStackTrace(se.getStackTrace());
			throw one;
		}
	}

	public void updateBulkStudentAccommodations(String userName, StudentAccommodations studentAccommodations,Integer[] studentId) throws CTBBusinessException
	{

		validator.validateUser(userName,userName, "StudentManagementImpl.updateStudentAccommodations");

		try {

			if (studentAccommodations != null)	{	
				
				StringBuffer strBuffer = new StringBuffer();

				if(studentAccommodations.getScreenMagnifier()!= null) {
					strBuffer.append("SCREEN_MAGNIFIER='"+ studentAccommodations.getScreenMagnifier()+"',");
				}
				
				if(studentAccommodations.getCalculator()!= null) {
					strBuffer.append("CALCULATOR='" + studentAccommodations.getCalculator()+"',");
				}
				if(studentAccommodations.getHighlighter()!= null) {
					strBuffer.append("HIGHLIGHTER='" + studentAccommodations.getHighlighter()+"',");
				}
				
				if(studentAccommodations.getScreenReader()!= null) {
					strBuffer.append("SCREEN_READER='"+studentAccommodations.getScreenReader()+"',");
				}
				if(studentAccommodations.getTestPause()!= null) {
					strBuffer.append("TEST_PAUSE='"+studentAccommodations.getTestPause()+"',");
				}
				if(studentAccommodations.getUntimedTest()!= null) {
					strBuffer.append("UNTIMED_TEST='"+studentAccommodations.getUntimedTest()+"',");
				}
				//Change for defect -# 65698 
				if(studentAccommodations.getColorFont() != null && studentAccommodations.getColorFont().equals("T")) {
					if(studentAccommodations.getAnswerBackgroundColor()!= null) {
						strBuffer.append("ANSWER_BACKGROUND_COLOR='" +studentAccommodations.getAnswerBackgroundColor()+"',");
					}
					if(studentAccommodations.getAnswerFontColor()!= null) {
						strBuffer.append("ANSWER_FONT_COLOR='" + studentAccommodations.getAnswerFontColor()+"',");
					}
					if(studentAccommodations.getAnswerFontSize()!= null) {
						strBuffer.append("ANSWER_FONT_SIZE='" + studentAccommodations.getAnswerFontSize()+"',");
					}
					if(studentAccommodations.getQuestionBackgroundColor()!= null) {
						strBuffer.append("QUESTION_BACKGROUND_COLOR='" +studentAccommodations.getQuestionBackgroundColor()+"',");
					}
					if(studentAccommodations.getQuestionFontColor()!= null) {
						strBuffer.append("QUESTION_FONT_COLOR='" + studentAccommodations.getQuestionFontColor()+"',");
					}
					if(studentAccommodations.getQuestionFontSize()!= null) {
						strBuffer.append("QUESTION_FONT_SIZE='" + studentAccommodations.getQuestionFontSize() + "',");
					}
				}
				//Change for defect -# 65698 
				if(studentAccommodations.getColorFont() != null && studentAccommodations.getColorFont().equals("F")) {
					strBuffer.append("ANSWER_BACKGROUND_COLOR=null" +",");
					strBuffer.append("ANSWER_FONT_COLOR=null" +",");
					strBuffer.append("ANSWER_FONT_SIZE=null" +",");
					strBuffer.append("QUESTION_BACKGROUND_COLOR=null" +",");
					strBuffer.append("QUESTION_FONT_COLOR=null" +",");
					strBuffer.append("QUESTION_FONT_SIZE=null" +",");
					
				}
				
				
				
				
				if (strBuffer != null && strBuffer.length() > 0) {
					String sqlUpdateStr = strBuffer.substring(0, strBuffer.length() - 1);
					//System.out.println("sqlUpdateStr.."+ sqlUpdateStr);
					
						//Start -Inclause changes
						int inClauselimit = 999;
						int loopCounters = studentId.length / inClauselimit;
						if((studentId.length % inClauselimit) > 0){
							loopCounters = loopCounters + 1;
						}
						for(int counter=0; counter<loopCounters; counter++){
							Integer[] newStudentid = null;
							String searchbyStudentIds="";
							if((counter+1)!=loopCounters){
								newStudentid = new Integer [inClauselimit];
								System.arraycopy(studentId, (counter*inClauselimit) , newStudentid, 0, inClauselimit);
							} else {
								int count = studentId.length % inClauselimit;
								newStudentid = new Integer [count];
								System.arraycopy(studentId, ((loopCounters-1)*inClauselimit) , newStudentid, 0, count);
								
							}
							searchbyStudentIds = SQLutils.generateSQLCriteria("student_id in  ",newStudentid);
							accommodation.updateBulkStudentAccommodations(searchbyStudentIds,sqlUpdateStr);
						}
						//END -Inclause changes
				}
			}
			
		
	} catch (SQLException se) {
		StudentDataCreationException tee = new StudentDataCreationException("StudentManagementImpl: updateStudentAccommodations: " + se.getMessage());
		tee.setStackTrace(se.getStackTrace());
		throw tee;
	}
}    
	// Temporary change for audio CR RESPONSE
	public ItemResponseData getStudentCrResponse(Integer testRosterId,String itemId) throws CTBBusinessException
	{
	
		try{
			ItemResponseData itemResponse = null;
			
			itemResponse = studentManagement.getCrResponse(testRosterId,itemId);
			return itemResponse;
		}
		catch(Exception e){
			
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentEduAndInstr: " );
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
	}
	//START- TABE BAUM 20 Fprm Recommendation
	public StudentSessionStatus[] getStudentMostResentSessionDetail(Integer studentId) throws CTBBusinessException{
		
		try{
			StudentSessionStatus [] StudentSessionStatus = null;
			
			StudentSessionStatus = studentManagement.getStudentMostResentSessionDetail(studentId);
			
			return StudentSessionStatus;
		}
		catch(Exception e){
			
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentMostResentSessionDetail: " );
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
		
	}
		//END- TABE BAUM 20 Form Recommendation
	
	
	//Added for Auditory Calming
	public MusicFiles[] getMusicFiles() throws CTBBusinessException {
		
		try{
			MusicFiles [] musicFileList = null;
			
			musicFileList = studentManagement.getMusicFilesList();
			
			return musicFileList;
		}
		catch(Exception e){
			
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getMusicFiles: " );
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
	}
	
	// Added for TABE BAUM - 028
	public String hasMultipleAccessCode(int testAdminId) throws CTBBusinessException {
		
		try{
			String hasbreak = studentManagement.hasMultipleAccessCode(testAdminId);
			
			return hasbreak;
		}
		catch(Exception e){
			
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: hasMultipleAccessCode: " );
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
	}


	/* Added for TABE-BAUM 060: Unique Student ID
	 * @see com.ctb.control.studentManagement.StudentManagement#validateUniqueStudentId(java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public boolean validateUniqueStudentId(Boolean isCreateNew, Integer customerId ,
					Integer studentId, String studentIDNumber) throws CTBBusinessException {
		
		boolean isIDUnique = false;
		Integer totalStudent = 0;
		String searchCriteria = "";
		//System.out.println("isCreateNew:"+isCreateNew+"customerId:"+customerId+"studentIDNumber:"+studentIDNumber+"studentId:"+studentId);
		if (!isCreateNew){
			searchCriteria = " and student_id <> "+ studentId;
		}

		try{
			totalStudent = studentManagement.validateUniqueStudentId(customerId, studentIDNumber.trim(), searchCriteria);
			if( totalStudent==0){
				isIDUnique = true;
			} 
			
		}
		catch(Exception e){
			
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: validateUniqueStudentId: " );
			tee.setStackTrace(e.getStackTrace());
			throw tee;
		}
		return isIDUnique;
	}
	
	/**
	 * Get grades for the specified customer.
	 * @common:operation
	 * @param userName - identifies the calling user
	 * @param customerId - identifies the customer whose information is desired
	 * @return String []
	 * @throws CTBBusinessException
	 */
	public ScorableItem [] getContentAreaForCatalog(String userName, Integer customerId, Integer catalogId) throws CTBBusinessException
	{
		validator.validateCustomer(userName, customerId, "StudentManagementImpl.getContentAreaForCatalog");
		try {
			ScorableItem [] contentAreas = null;
			contentAreas = studentManagement.getContentAreasForCatalog(catalogId.intValue());
			
			return contentAreas;
		} catch (SQLException se) {
			CustomerContentAreasNotFoundException tee = new CustomerContentAreasNotFoundException("StudentManagementImpl: getContentAreaForCustomer: " + se.getMessage());
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

	public ManageStudentData findStudentsAtAndBelowTopOrgNodesWithDynamicSQLForReporting(String userName, Integer catalogId, FilterParams filter, PageParams page, SortParams sort ) throws CTBBusinessException
	{
		try {
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			/*FilterParams statusFilter = new FilterParams();
			statusFilter.setFilterParams(new FilterParam[0]);*/
			Integer contentAreaFiltered = 0;
			if (filter != null) {
				FilterParam [] filterParams = filter.getFilterParams();
				for (int i = 0; i < filterParams.length; i++) {
			            FilterParam filterParam = filterParams[i];
			            if (filterParam != null) {
			                String fieldName = filterParam.getField();
			                if(fieldName.equals("completedContentAreaId")) {
			                	String [] tempVal = (String [])filterParam.getArgument();
			                	contentAreaFiltered = Integer.parseInt(tempVal[0]);
			                	/*statusFilter.setFilterParams(filterParams);*/
			                	break;
			                }
			            }
				}
			}
			Integer totalCount = null;
			String searchCriteria = "";
			if (filter != null) {
				searchCriteria = DynamicSQLUtils.generateWhereClauseForFilterReporting(filter);
				filter.setFilterParams(new FilterParam[0]);
				//totalCount = studentManagement.getStudentCountAtAndBelowUserTopNodes(userName);
				ManageStudent [] studentTotalCount = null;
				if(contentAreaFiltered == 0)
					studentTotalCount = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaForReporting(userName, catalogId, "");
				else
					studentTotalCount = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaReWithCA(userName, catalogId, "", contentAreaFiltered);
				totalCount = studentTotalCount.length;
			}
			String orderByClause = "";
			/*if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
				sort = null;
			}*/
			
			//searchCriteria = searchCriteria + orderByClause;
			ManageStudent [] students = null;
			String contentAreaNameFilDisp = "";
			if(contentAreaFiltered == 0)
				students = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaForReporting(userName, catalogId,searchCriteria);
			else {
				students = studentManagement.getStudentsAtAndBelowUserTopNodeWithSearchCriteriaReWithCA(userName, catalogId,searchCriteria, contentAreaFiltered);
				contentAreaNameFilDisp = studentManagement.getContentAreaNameFromId(contentAreaFiltered);
			}
           for(ManageStudent student : students) {
        	   //student.setScoringStatus( studentManagement.getScoringStatus(student.getRosterId(), student.getItemSetIdTC()));
        	   String plValue = immediateReportingIrs.getProficiencyLevel(student.getId(), Integer.parseInt(student.getTestAdminId()));
        	   if(plValue == null || "".equals(plValue))
        		   plValue = "N/A";
        	   student.setProficiencyLevel(plValue);
        	   if(contentAreaFiltered > 0) {
        		   student.setContentAreaString(contentAreaNameFilDisp);
        	   }
           }
			
			std.setManageStudents(students, pageSize);
			if(filter != null) std.applyFiltering(filter);
			/*if(statusFilter != null) std.applyFiltering(statusFilter);*/
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();
			/*for (int i=0; i <students.length; i++) {
				if (students[i] != null) {
					OrganizationNode [] orgNodes = studentManagement.getAssignedOrganizationNodesForStudentAtAndBelowUserTopNodes(students[i].getId().intValue(), userName);
					students[i].setOrganizationNodes(orgNodes);
				}
			}*/


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
	
	public StudentScoreReport getStudentReport(Integer testRosterId, Integer testAdminId) throws CTBBusinessException {
		
		try {
			StudentScoreReport stuScrReport = new StudentScoreReport();
			StudentReportIrsScore[] stuScoreData = null;
			StudentReportIrsScore stuScoreDataComp = null;
			StudentReportIrsScore[] stuFinalScoreData = null;
			stuScrReport = studentManagement.getStudentDataForReport(testRosterId);
			stuScoreData = immediateReportingIrs.getScoreDataForReport(stuScrReport.getStudentId(), testAdminId);
			stuScoreDataComp = immediateReportingIrs.getScoreDataForReportComposite(stuScrReport.getStudentId(), testAdminId);
			stuFinalScoreData = new StudentReportIrsScore[stuScoreData.length + 1];
			if(stuScoreData != null) {
				for(int i = 0; i < stuScoreData.length; i++) {
					if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Listening")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 0);
					} else if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Speaking")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 1);
					} else if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Oral")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 2);
					} else if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Reading")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 3);
					} else if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Writing")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 4);
					} else if(stuScoreData[i].getContentAreaName().equalsIgnoreCase("Comprehension")) {
						setFinalScoreValues(stuFinalScoreData, stuScoreData[i], 5);
					}
				}
			}
			if(stuScoreDataComp != null)
				setFinalScoreValues(stuFinalScoreData, stuScoreDataComp, stuScoreData.length);
			
			stuScrReport.setStudentReportIrsScore(stuFinalScoreData);
			
			return stuScrReport;
			
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: getStudentReport: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
		
	}
	
	private void setFinalScoreValues(StudentReportIrsScore[] stuFinalScoreData, StudentReportIrsScore stuScoreDataTemp, Integer stuFinalScoreDataValue) {
		stuFinalScoreData[stuFinalScoreDataValue] = new StudentReportIrsScore();
		stuFinalScoreData[stuFinalScoreDataValue].setContentAreaName(stuScoreDataTemp.getContentAreaName());
		stuFinalScoreData[stuFinalScoreDataValue].setRawScore(stuScoreDataTemp.getRawScore());
		stuFinalScoreData[stuFinalScoreDataValue].setScaleScore(stuScoreDataTemp.getScaleScore());
		stuFinalScoreData[stuFinalScoreDataValue].setProficiencyLevel(stuScoreDataTemp.getProficiencyLevel());
		
	}
	
	@Override
	public ManageStudentData findAllScoredStudentBySessionAtAndBelowTopOrgNodes( String userName, Integer testAdminId, FilterParams filter,	PageParams page, SortParams sort) throws CTBBusinessException {

		try {
			validator.validateAdmin(userName, testAdminId, "StudentManagementImpl.findAllScoredStudentBySessionAtAndBelowTopOrgNodes");
			ManageStudentData std = new ManageStudentData();

			Integer pageSize = null;
			if(page != null) {
				pageSize = new Integer(page.getPageSize());
			}
			/*Integer contentAreaFiltered = 0;
			Integer totalCount = null;*/
			String orderByClause = "";
			
			/*if (sort != null) {
				orderByClause = DynamicSQLUtils.generateOrderByClauseForSorter(sort);                
				sort = null;
			}*/
			
			ManageStudent [] students = null;
			students = studentManagement.findAllScoredStudentBySessionAtAndBelowTopOrgNodes(userName, testAdminId,orderByClause);
			
           for(ManageStudent student : students) {
        	   String plValue = immediateReportingIrs.getProficiencyLevel(student.getId(), Integer.parseInt(student.getTestAdminId()));
        	   if(plValue == null || "".equals(plValue))
        		   plValue = "N/A";
        	   student.setProficiencyLevel(plValue);
           }
			
			std.setManageStudents(students, pageSize);
			if(sort != null) std.applySorting(sort);
			if(page != null) std.applyPaging(page);

			students = std.getManageStudents();


			return std;
		}catch (ValidationException ve){
			ve.printStackTrace();
			throw	new StudentDataNotFoundException("SessionAccess.Validation.Failed");
		} catch (SQLException se) {
			StudentDataNotFoundException tee = new StudentDataNotFoundException("StudentManagementImpl: findAllScoredStudentBySessionAtAndBelowTopOrgNodes: " + se.getMessage());
			tee.setStackTrace(se.getStackTrace());
			throw tee;
		}
	
	}
} 
