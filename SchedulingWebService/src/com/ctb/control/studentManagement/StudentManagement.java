
package com.ctb.control.studentManagement;


import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.exception.CTBBusinessException;

/** 
 *
 * @author John_Wang
 */ 
@ControlInterface()
public interface StudentManagement 
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

    /**
     * Get grades for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return String []
     * @throws CTBBusinessException
     */
   
    java.lang.String[] getGradesForCustomer(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at the specified org
     * node.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org nodes
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of child org nodes of the specified org node
     * <br/><br/>Each node contains a count: the number of students which are at
     * or below the specified node (studentCount).
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return OrganizationNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.OrganizationNodeData getOrganizationNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get student profile for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student whose information is desired
     * @return Student
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Student getStudentProfile(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of top org nodes of the user. <br/><br/>Each node
     * contains a count: the number of students which are at or below the
     * specified node (studentCount).
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return OrganizationNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.OrganizationNodeData getTopOrganizationNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get student accommodations for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student whose information is desired
     * @return StudentAccommodations
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentAccommodations getStudentAccommodations(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create new student record.
     * @param userName - identifies the user
     * @param manageStudent - contains the new student information
     * @return student id
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.Student createNewStudent(java.lang.String userName, com.ctb.bean.studentManagement.ManageStudent manageStudent) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * <br/><br/>studentCount and childCount not populated for this call.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @return OrganizationNode []
     * @throws com.ctb.exception.CTBBusinessException
     */

    com.ctb.bean.studentManagement.OrganizationNode[] getAncestorOrganizationNodesForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update student record.
     * @param userName - identifies the user
     * @param manageStudent - contains the updated student information
     * @throws com.ctb.exception.CTBBusinessException
     */
   
    void updateStudent(java.lang.String userName, com.ctb.bean.studentManagement.ManageStudent manageStudent) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete student record.
     * @param userName - identifies the user
     * @param studentId - identifies the student to be deleted
     * @return DeleteStudentStatus
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.util.studentManagement.DeleteStudentStatus deleteStudent(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create student accommodations for the specified student.
     * @param userName - identifies the calling user
     * @param studentAccommodations - contains the student's accommodations information
     * @throws CTBBusinessException
     */
   
    void createStudentAccommodations(java.lang.String userName, com.ctb.bean.testAdmin.StudentAccommodations studentAccommodations) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete student accommodations for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student to be deleted
     * @throws CTBBusinessException
     */
    
    void deleteStudentAccommodations(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentDemographicData - contains the student's demographic information
     * @throws CTBBusinessException
     */
    
    void updateStudentDemographicData(java.lang.String userName, com.ctb.bean.studentManagement.StudentDemographicData studentDemographicData) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student
     * @param studentDemographics [] - contains the student's demographic information
     * @throws CTBBusinessException
     */
    
    void createStudentDemographics(java.lang.String userName, java.lang.Integer studentId, com.ctb.bean.studentManagement.StudentDemographic[] studentDemographics) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student
     * @throws CTBBusinessException
     */
    
    void deleteStudentDemographics(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get customer configuration for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return CustomerConfiguration []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.CustomerConfiguration[] getCustomerConfigurations(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get customer demographic for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @param returnInvisible - indicates whether to return invisible data/values
     * @return CustomerDemographic []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.CustomerDemographic[] getCustomerDemographics(java.lang.String userName, java.lang.Integer customerId, boolean returnInvisible) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get manage student object for the specified student with the array of
     * assinged org ndoes populated.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student whose information is desired
     * @return ManageStudent
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudent getManageStudent(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentDemographicData - contains the student's demographic information
     * @return studentDemographicDataId
     * @throws CTBBusinessException
     */
    
    java.lang.Integer createStudentDemographicData(java.lang.String userName, com.ctb.bean.studentManagement.StudentDemographicData studentDemographicData) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get student demographic data for the specified studentDemographicDataId.
     * @param userName - identifies the calling user
     * @param studentDemographicDataId - identifies the student demographic data
     * @return StudentDemographicData
     * @throws CTBBusinessException
     */
   
    com.ctb.bean.studentManagement.StudentDemographicData getStudentDemographicData(java.lang.String userName, java.lang.Integer studentDemographicDataId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student
     * @throws CTBBusinessException
     */
    
    void deleteStudentDemographicDataForStudent(java.lang.String userName, java.lang.Integer studentId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete student demographic data for the specified
     * studentDemographicDataId.
     * @param userName - identifies the calling user
     * @param studentDemographicDataId - identifies the student demographic data
     * @throws CTBBusinessException
     */
    
    void deleteStudentDemographicData(java.lang.String userName, java.lang.Integer studentDemographicDataId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get student demographic for the specified customer and student.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @param studentId - identifies the student whose information is desired
     * @param returnInvisible - indicates whether to return invisible data/values
     * @return StudentDemographic []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.StudentDemographic[] getStudentDemographics(java.lang.String userName, java.lang.Integer customerId, java.lang.Integer studentId, boolean returnInvisible) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at and below user's
     * top org node(s). The SQL's where clause is dynamically generated on based
     * on filter passed in.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsAtAndBelowTopOrgNodesWithDynamicSQL(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at and below user's for scoring
     * top org node(s). The SQL's where clause is dynamically generated on based
     * on filter passed in.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsAtAndBelowTopOrgNodesWithDynamicSQLForScoring(java.lang.String userName, java.lang.Integer productId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update student accommodations for the specified student. If the student
     * had no accommodations, new accommodations record will be created.
     * @param userName - identifies the calling user
     * @param studentAccommodations - contains the student's accommodations information
     * @throws CTBBusinessException
     */
    
    void updateStudentAccommodations(java.lang.String userName, com.ctb.bean.testAdmin.StudentAccommodations studentAccommodations) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param programId - identifies the program
     * @return CustomerReportData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerReportData getCustomerReportData(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer programId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the no of reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return boolean value
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    boolean userHasReports(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at and below
     * specified org node(s). If orgNodeIds is null or empty, use user's top org
     * node(s). The SQL's where clause is dynamically generated on based on
     * filter passed in.
     * @param userName - identifies the user
     * @param orgNodeIds - identifies the org nodes
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsAtAndBelowOrgNodeWithDynamicSQL(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at and below
     * specified org node(s). If orgNodeIds is null or empty, use user's top org
     * node(s).
     * @param userName - identifies the user
     * @param orgNodeIds - identifies the org nodes
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsAtAndBelowOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at and below user's
     * top org node(s).
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return ManageStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.ManageStudentData findStudentsAtAndBelowUserTopNodes(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student
     * @param studentDemographics [] - contains the student's demographic information
     * @throws CTBBusinessException
     */
    
    void updateStudentDemographics(java.lang.String userName, java.lang.Integer studentId, com.ctb.bean.studentManagement.StudentDemographic[] studentDemographics) throws com.ctb.exception.CTBBusinessException;

    /**
     * Update student record for import/export.
     * @param user - identifies the login user details
     * @param manageStudent - contains the updated student information
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    void updateStudentUpload(com.ctb.bean.testAdmin.User user, com.ctb.bean.studentManagement.ManageStudent manageStudent) throws com.ctb.exception.CTBBusinessException;

    /**
     * For Import/Export Application Update student demographic data for the
     * specified student.
     * @param user - identifies the calling user details
     * @param studentId - identifies the student
     * @param studentDemographics [] - contains the student's demographic information
     * @throws CTBBusinessException
     */
    
    void updateStudentDemographicsUpload(com.ctb.bean.testAdmin.User user, java.lang.Integer studentId, com.ctb.bean.studentManagement.StudentDemographic[] studentDemographics) throws com.ctb.exception.CTBBusinessException;

    /**
     * Create student demographic data for the specified student.
     * @param userName - identifies the calling user
     * @param studentId - identifies the student
     * @param studentDemographics [] - contains the student's demographic information
     * @throws CTBBusinessException
     */
    
    void createStudentDemographicsUpload(com.ctb.bean.testAdmin.User user, java.lang.Integer studentId, com.ctb.bean.studentManagement.StudentDemographic[] studentDemographics) throws com.ctb.exception.CTBBusinessException;

    
    com.ctb.bean.studentManagement.OrganizationNode[] getStudentsOrganizationUpload(java.lang.String userName, java.lang.Integer studentId) throws java.lang.Exception;

    /**
     * Create new student record for import/export application
     * @param user - identifies the login user details
     * @param manageStudent - contains the new student information
     * @return student id
     * @throws com.ctb.exception.CTBBusinessException
     */
	
    com.ctb.bean.testAdmin.Student createStudentUpload(com.ctb.bean.testAdmin.User user, com.ctb.bean.studentManagement.ManageStudent manageStudent) throws com.ctb.exception.CTBBusinessException;
    
    /**
     * New method added for CR - GA2011CR001
     * Get customer configuration value for the specified customer configuration.
     * @common:operation
     * @param configId - identifies the customerconfiguration whose information is desired
     * @return CustomerConfigurationValue []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.studentManagement.CustomerConfigurationValue[] getCustomerConfigurationsValue(java.lang.Integer configId) throws com.ctb.exception.CTBBusinessException;
    
	//bulk accommodation 
	com.ctb.bean.studentManagement.ManageBulkStudentData findBulkStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.FilterParams demoFilter,com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	//bulk Accommodation
    com.ctb.bean.studentManagement.CustomerDemographic[] getCustomerDemographics(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of org nodes at which the specified user has a role
     * defined. <br/><br/>Each node contains two counts: the number of students
     * rostered in the specified admin which are at or below the node
     * (rosterCount), and the number of students matching the grade and student
     * test accommodation filter criteria which are at or below the node
     * (studentCount). If no test admin id is specified, only the student count
     * is populated, not the roster count. <br/><br/>The filter params passed to
     * this call only affect the student count, not the set of org nodes
     * returned, whereas the sort and paging params affect the org node list, as
     * usual.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getTopStudentNodesForBulkAccommodationUserAndAdmin(java.lang.String userName, java.lang.Integer customerId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.FilterParams demoFilter,com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    com.ctb.bean.testAdmin.StudentNodeData getStudentNodesForBulkAccomUserParentAndAdmin(String userName, java.lang.Integer customerId,Integer orgNodeId, FilterParams filter,FilterParams demoFilter,PageParams page, SortParams sort)throws com.ctb.exception.CTBBusinessException;
    void updateBulkStudentAccommodations(String userName,  com.ctb.bean.testAdmin.StudentAccommodations studentAccommodations,Integer[] studentId) throws  com.ctb.exception.CTBBusinessException;
    
    /***
     * Temporary Change
     * Retrieve Cr Response
     * 
     */
    
    com.ctb.bean.studentManagement.ItemResponseData getStudentCrResponse(Integer testRosterId,String itemId) throws com.ctb.exception.CTBBusinessException;
    
    StudentSessionStatus[] getStudentMostResentSessionDetail(Integer studentId) throws  com.ctb.exception.CTBBusinessException;
    
    
    // Added for Auditory Calming
    
    com.ctb.bean.studentManagement.MusicFiles[] getMusicFiles() throws com.ctb.exception.CTBBusinessException;
    
    //Added for TABE-BAUM 028
    String hasMultipleAccessCode(int testAdminId) throws com.ctb.exception.CTBBusinessException;
   
   //Added for TABE-BAUM 060: Unique Student ID
	boolean validateUniqueStudentId(Boolean isCreateNew, Integer customerId , Integer studentId, String studentIDNumber)throws com.ctb.exception.CTBBusinessException;

    
	com.ctb.bean.testAdmin.UserNodeData OrgNodehierarchy(java.lang.String userName, Integer associatedNodeId) throws com.ctb.exception.CTBBusinessException;
    
	com.ctb.bean.testAdmin.UserNodeData getTopUserNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	com.ctb.bean.studentManagement.ManageStudentData findStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	java.lang.Integer getLeafNodeCategoryId(String userName, Integer customerId) throws com.ctb.exception.CTBBusinessException;
	
	//Bulk Move
	com.ctb.bean.studentManagement.ManageStudentData getBulkMoveStudent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	void updateBulkMoveOperation(String userName, Integer orgId, Integer[] studentIds) throws com.ctb.exception.CTBBusinessException;
	
	com.ctb.bean.studentManagement.ManageStudentData getStudentsMinimalInfoForSelectedOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	java.lang.Integer getStudentsCountForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;
	
	//Out Of School
	com.ctb.bean.studentManagement.ManageStudentData getOOSStudent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
	
	void updateOOSOperation(Integer[] updatedOOSData) throws com.ctb.exception.CTBBusinessException;
	
	com.ctb.bean.studentManagement.ManageStudentData getStudentsMinimalInfoForScoring(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

	com.ctb.bean.studentManagement.ManageStudentData getAllCompletedStudentForOrgNode(String userName, Integer treeOrgNodeId)throws com.ctb.exception.CTBBusinessException;

	java.lang.Integer getCompletedStudentCountForOrgNode(String userName,	Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

	com.ctb.bean.studentManagement.StudentScoreReport getStudentReport(java.lang.Integer testRosterId, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
	
} 	


