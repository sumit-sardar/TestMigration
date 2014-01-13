package com.ctb.control.testAdmin; 


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.ClassHierarchy;
import com.ctb.bean.testAdmin.CustomerTestResourceData;
import com.ctb.bean.testAdmin.ItemResponseData;
import com.ctb.exception.CTBBusinessException;

/**
 * Platform control provides functions related to test session
 * monitoring and status, including methods to obtain lists of
 * test sessions of interest to a particular user.
 * 
 * @author Nate_Cohen, John_Wang
 */
@ControlInterface()
public interface TestSessionStatus 
{ 

    /**
     * Retrieves a filtered, sorted, paged list of test sessions to which the
     * specified user is assigned as a proctor.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of test sessions to which the
     * specified user is assigned as a proctor
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestSessionData getProctorAssignmentsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves details for a particular test session
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session of interest
     * @return TestSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionDetails(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the list of currently active system broadcast messages
     * @param userName - identifies the user
     * @return BroadcastMessageData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.BroadcastMessageData getBroadcastMessages(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of test sessions created at the
     * specified org node
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.TestSessionData getRecommendedTestSessionsForOrgNode(String userName, Integer selectedProductId, Integer orgNodeId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;
    
    
    com.ctb.bean.testAdmin.RosterElement[] getTestRosterForStudentIdAndOrgNode(Integer studentId, Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of org nodes that are children of
     * the specified org node
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return NodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.NodeData getOrgNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of org nodes that are children of
     * the specified org node, plus a count of all active test sessions scheduled
     * at/below each node in the list.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return SessionNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionNodeData getSessionNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of org nodes at which the
     * specified user has a role defined
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return NodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.NodeData getTopNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of org nodes at which the
     * specified user has a role defined, plus a count of all active test
     * sessions scheduled at/below each node in the list.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return SessionNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionNodeData getTopSessionNodesForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    //START - TABE BAUM 020 Form Recommendation 
    com.ctb.bean.testAdmin.SessionNodeData getTopRecommendedSessionNodesForUser(java.lang.String userName,java.lang.Integer productId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    com.ctb.bean.testAdmin.SessionNodeData getRecommendedSessionNodesForParent(String userName, Integer orgNodeId, Integer productId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;
    //END - TABE BAUM 020 Form Recommendation 
    /**
     * Retrieves a filtered, sorted, paged list of roster elements for the
     * specified test session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return RosterElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.RosterElementData getRosterForTestSession(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    com.ctb.bean.testAdmin.RosterElementData getReportableRosterForTestSession(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    com.ctb.bean.testAdmin.RosterElementData getRosterForTestSessionWithShowRosterAccom(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    HashMap<Integer, ArrayList<ClassHierarchy>> buildOrgNodeIdMap(java.lang.String userName, java.lang.Integer test_admin_id) throws com.ctb.exception.CTBBusinessException;
    /**
     * Retrieves a roster elements by testRosterId
     * @param testRosterId - identifies the test roster
     * @return RosterElement
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.RosterElement getRoster(java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of test elements which comprise
     * the specified test session. (presumably 'TS'-type item sets)
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestElementData getTestElementsForTestSession(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Toggles the validation status of the specifed roster element
     * @param userName - identifies the user
     * @param testRosterId - identifies the test session roster element (student)
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    void toggleRosterValidationStatus(java.lang.String userName, java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of active tests, meaning tests
     * having scheduled sessions whose login window opens within the next 60
     * days. Each ActiveTest object contains a list of all subtests (TD item
     * sets) belonging to the test.
     * @param userName - identifies the user
     * @return ActiveTestData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ActiveTestData getActiveTestsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of active sessions for a test,
     * meaning sessions of the specified test whose login window opens within the
     * next 60 days, or any sessions thereafter with login windows opening less
     * than 60 days after the close of the login window of any previous session
     * of the specified test.
     * @param userName - identifies the user
     * @param itemSetId - identifies the test
     * @return ActiveSessionData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ActiveSessionData getActiveSessionsForTest(java.lang.String userName, java.lang.Integer itemSetId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

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
     * Retrieves a filtered, sorted, paged list of roster elements for the
     * specified test session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param orgNodeId - identifies the org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return RosterElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.RosterElementData getRosterForTestSessionAndOrgNode(java.lang.String userName, java.lang.Integer testAdminId, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of child org nodes of the specified org node, plus a
     * count of users who have roles anywhere at/below each org node in the list
     * (userCount). If testAdminId is not null, a count of users at or below each
     * node who are assigned as proctors to the specified session is also
     * attached to each node (proctorCount).
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return UserNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserNodeData getTopUserNodesForUser(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the student item set statuses for a session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @param studentId - identifies the student
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentSessionStatusesData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentSessionStatusData getStudentItemSetStatusesForRoster(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the student item set statuses for a session.
     * @param userName - identifies the user
     * @param parentItemSetId - identifies the parent item set
     * @param itemSetTyp - identifies the item set type
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestElementData getTestElementsForParent(java.lang.String userName, java.lang.Integer parentItemSetId, java.lang.String itemSetType, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get customer configuration for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return CustomerConfiguration []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerConfiguration[] getCustomerConfigurations(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the TestProduct for the given testAdminId
     * @userName - Identifies the user @ testAdminId - Identifies the test admin
     */
    
    com.ctb.bean.testAdmin.TestProduct getProductForTestAdmin(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of Customer SDS names and
     * corresponding tokens for the provided user's customer.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return CustomerSDSData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerSDSData getCustomerSDSListForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of programs available to a user's customer
     * @param userName - identifies the user
     * @return ProgramData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ProgramData getProgramsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the no of reports available to a user's customer
     * @param userName - identifies the user
     * @param customerId - identifies the customer
     * @return boolean value
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    boolean userHasReports(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param orgNodeId - identifies the org node
     * @param programId - identifies the program
     * @return CustomerReportData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerReportData getCustomerReportData(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer programId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    com.ctb.bean.testAdmin.CustomerReportData getTASCReportData(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer programId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of ancestor org nodes of the specified org node
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @return OrganizationNode []
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.OrganizationNode[] getAncestorOrganizationNodesForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

    /**
     * To check if a user's customer is configured to allow subtest invalidation
     * @param userName - identifies the user
     * @return Boolean
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    java.lang.Boolean allowSubtestInvalidation(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    
    java.lang.Boolean hasProgramStatusConfig(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    java.lang.Boolean hasUploadDownloadConfig(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    
    /**
     * Toggles the validation status of the specifed subtests
     * @param userName - identifies the user
     * @param testRosterId - identifies the test session roster element (student)
     * @param itemSetIds - identifies the TD item sets (subtests)
     * @throws com.ctb.exception.CTBBusinessException
     */
    void toggleSubtestValidationStatus(java.lang.String userName, java.lang.Integer testRosterId, java.lang.Integer[] itemSetIds, java.lang.String status) throws com.ctb.exception.CTBBusinessException;
    
    void toggleSubtestValidationStatus(java.lang.String userName, java.lang.Integer testRosterId, java.lang.String[] itemSetIds, java.lang.String status) throws com.ctb.exception.CTBBusinessException;
    
    //added for Process Scores  button 
    void rescoreStudent(java.lang.Integer testRosterId)throws com.ctb.exception.CTBBusinessException;
    /**
     * Toggle the customer flag status for the specified test roster.
     * @param testRosterId - identifies the test roster
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    void toggleCustomerFlagStatus(java.lang.String userName, java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Toggle the customer flag status for the specified subtests.
     * @param testRosterId - identifies the test session roster element (student)
     * @param itemSetIds - identifies the TD item sets (subtests)
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    void toggleSubtestCustomerFlagStatus(java.lang.String userName, java.lang.Integer testRosterId, java.lang.Integer[] itemSetIds) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the list of all currently active broadcast messages for account
     * manager
     * @param userName - identifies the user
     * @return BroadcastMessageData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.BroadcastMessageData getBroadcastMessagesForActManager(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param testRosterId - identifies the test roster
     * @return String
     * @throws com.ctb.exception.CTBBusinessException
     */
	
    java.lang.String getIndividualReportUrl(java.lang.String userName, java.lang.Integer testRosterId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param testRosterId - identifies the test roster
     * @return String
     * @throws com.ctb.exception.CTBBusinessException
     */
	
    java.lang.String getIndividualReportUrlForSession(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    /**
     * Retrieves the set of online reports available to a user's customer
     * @param userName - identifies the user
     * @param testRosterIds - list of the test rosters
     * @return String
     * @throws com.ctb.exception.CTBBusinessException
     */
	
    java.lang.String getIndividualReportUrl(java.lang.String userName, java.lang.Integer[] testRosterIds, java.lang.String fileName, java.lang.String fileType, java.lang.String email) throws com.ctb.exception.CTBBusinessException;
    
    /**
     * New method added for CR - GA2011CR001
     * Get customer configuration value for the specified customer configuration.
     * @common:operation
     * @param configId - identifies the customerconfiguration whose information is desired
     * @return CustomerConfigurationValue []
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.CustomerConfigurationValue[] getCustomerConfigurationsValue(java.lang.Integer configId) throws com.ctb.exception.CTBBusinessException;
    
    
    /**   
     * ISTEP CR032 -Download Test
     * @param userName
     * @param filter
     * @param page
     * @param sort
     * @return
     * @throws com.ctb.exception.CTBBusinessException
     */
    CustomerTestResourceData getCustomerTestResources(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    

    /**   
     * @param userName
     * @throws com.ctb.exception.CTBBusinessException
     */
    java.lang.String getReportParams(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
    
    
    /**   
     * @throws com.ctb.exception.CTBBusinessException
     */
    java.lang.String getReportOpenAPI_URL(java.lang.String reportName) throws com.ctb.exception.CTBBusinessException;
    
    /**   
     * @param userName
     * @param resourceTypeCode
     * @throws com.ctb.exception.CTBBusinessException
     */
    String getParentResourceUriForUser(java.lang.String userName, java.lang.String resourceTypeCode) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionsForUserHome(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort, Integer userId) throws com.ctb.exception.CTBBusinessException;
    
    public void updateDonotScore(Integer testRosterId, String dnsStatus, Integer userName) throws CTBBusinessException;
    
    com.ctb.bean.testAdmin.TestSessionData getTestSessionsForStudentScoring(java.lang.String userName, java.lang.Integer userId, java.lang.Integer orgNodeId) throws com.ctb.exception.CTBBusinessException;

	 com.ctb.bean.testAdmin.TestSessionData getCurrentFutureTestAdminsForOrgNode(String userName,Integer userId, Integer treeOrgNodeId) throws CTBBusinessException;
	
	com.ctb.bean.testAdmin.TestSessionData getRecommendedTestSessionsForOrgNodeWithStudentStatus(String userName,  Integer userId, Integer selectedProductId, Integer orgNodeId,  Integer studentId, FilterParams filter, PageParams page, SortParams sort) throws CTBBusinessException;

	com.ctb.bean.testAdmin.TestSessionData getCurrentFutureTestAdminsForOrgNodeWithStudentStatus( String userName, Integer userId, Integer treeOrgNodeId,	Integer studentId) throws CTBBusinessException;

	List getInvalidationReasonList() throws com.ctb.exception.CTBBusinessException;

	List<String> getRosterFormList(java.lang.String testAdminId) throws com.ctb.exception.CTBBusinessException;
	
	void updateRosterForm(java.lang.String userName, java.lang.Integer testRosterId, java.lang.String assignedForm) throws com.ctb.exception.CTBBusinessException;
	
	String saveStudentResponseFromClicker(ItemResponseData itemResponseData) throws com.ctb.exception.CTBBusinessException;
	
	boolean saveStudentResponseInBatch(ArrayList<ItemResponseData> itemResponseData) throws com.ctb.exception.CTBBusinessException;
	
	void updateCompletionStatusForRoster(Integer rosterId) throws com.ctb.exception.CTBBusinessException;
	
} 