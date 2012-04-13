package com.ctb.control.testAdmin; 



import java.util.List;

import com.bea.control.annotations.TransactionAttribute;
import com.bea.control.annotations.TransactionAttributeType;
import org.apache.beehive.controls.api.bean.ControlInterface;

/**
 * Platform control provides functions related to test session
 * scheduling, including methods to obtain lists of
 * products available for scheduling, tests within a product,
 * and subtests available in a test, methods for persisting test
 * session, roster, and student accomodation information, 
 * and support methods for browsing org, student and user hierarchies
 * 
 * @author Nate_Cohen, John_Wang
 */
@ControlInterface()
public interface ScheduleTest 
{ 

    /**
     * Retrieves a filtered, sorted, paged list of products which the specified
     * user is able to schedule tests for. Each product contains a list of the
     * unique set of levels of the tests within that product. and a list of the
     * unique set of grades of the tests within that product
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestProductData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestProductData getTestProductsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of products which the specified
     * user is able to schedule tests for. Each product contains a list of the
     * unique set of levels of the tests within that product. and a list of the
     * unique set of grades of the tests within that product
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestProductData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestProductData getTestCatalogForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a filtered, sorted, paged list of tests under the specified
     * product. Each testElement object also contains a
     * overrideFormAssigmentMethod field, which if populated (see
     * TestSession.FormAssignment constants) indicates that only the specified
     * assignment method should be available for scheduling, and a
     * overrideLoginStartDate field, which if populated indicates that no earlier
     * (than the override date) login start date can be chosen for sessions of
     * this test.
     * @param userName - identifies the user
     * @param productId - identifies the product
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestElementData getTestsForProduct(java.lang.String userName, java.lang.Integer productId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    
    
    


    /**
     * Retrieves a filtered, sorted, paged list of independantly schedulable
     * units of the specified test. Each element returned contains a randomly
     * generated "suggested" test access code for that unit.
     * @param userName - identifies the user
     * @param testItemSetId - identifies the test
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return TestElementData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TestElementData getSchedulableUnitsForTest(java.lang.String userName, java.lang.Integer testItemSetId, java.lang.Boolean generateAccessCodes, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    com.ctb.bean.testAdmin.TestElementData getSchedulableUnitsForTestWithBlankAccessCode(java.lang.String userName, java.lang.Integer testItemSetId, java.lang.Boolean generateAccessCodes, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    List<String> getFixedNoAccessCode(int totalAccessCode) throws com.ctb.exception.CTBBusinessException;
    /**
     * Retrieves a filtered, sorted, paged list of the unique student
     * grade/accommodation option sets of all students in orgs visible to the
     * current user.
     * @param userName - identifies the user
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentAccommodationsData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentAccommodationsData getAccommodationOptionsForUser(java.lang.String userName, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at the specified org
     * node.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentData getStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at the specified org
     * node. Each returned SchedulingStudent object also contains grade and
     * accommodations data, and an editable flag indicating whether or not the
     * student can be scheduled for the test corresponding to the provided
     * testItemSetId. If the editable flag is false, the editableCause field
     * contains a description of the reason the student is non-editable.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testItemSetId - identifies the TC item set of the test being scheduled
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SchedulingStudentData getSchedulingStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testItemSetId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at the specified org
     * node. Each returned SessionStudent object also contains grade and
     * accommodations data, and if testAdminId is not null, assigned form and
     * scheduled org node name are also included, and the editable flag indicates
     * ("T" or "F") whether or not the student can be removed from or otherwise
     * manipulated within the session. If the editable flag is false, the
     * editableCause field contains a description of the reason the student is
     * non-editable. Only students who are in the specified test admin are
     * returned by this call. If all students in the org node are desired, use
     * getSessionStudentsForOrgNode instead. If testAdminId is null, this call
     * behaves the same as getSchedulingStudentsForOrgNode() but returns
     * SessionStudent objects.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test session
     * @param testItemSetId - identifies the TC item set of the test being scheduled
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionStudentData getSessionStudentsForOrgNodeAndAdmin(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, java.lang.Integer testItemSetId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of students at the specified org
     * node. Each returned SessionStudent object also contains grade and
     * accommodations data, and if testAdminId is not null, assigned form and
     * scheduled org node name are also included, and the editable flag indicates
     * ("T" or "F") whether or not the student can be removed from or otherwise
     * manipulated within the session. If the editable flag is false, the
     * editableCause field contains a description of the reason the student is
     * non-editable. All students in the specified org node are always returned,
     * even when testAdminId is provided. If testAdminId is null, this call
     * behaves the same as getSchedulingStudentsForOrgNode() but returns
     * SessionStudent objects.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test session
     * @param testItemSetId - identifies the TC item set of the test being scheduled
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionStudentData getSessionStudentsForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, java.lang.Integer testItemSetId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of users at the specified org
     * node.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return UserData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserData getUsersForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of child org nodes of the specified org node
     * <br/><br/>Each node contains two counts: the number of students rostered
     * in the specified admin which are at or below the specified node
     * (rosterCount), and the number of students matching the grade and student
     * test accommodation filter criteria which are at or below the specified
     * node (studentCount). If no test admin id is specified, only the student
     * count is populated, not the roster count. <br/><br/>The filter params
     * passed to this call only affect the student count, not the set of org
     * nodes returned, whereas the sort and paging params affect the org node
     * list, as usual.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getStudentNodesForParentAndAdmin(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

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
    
    com.ctb.bean.testAdmin.StudentNodeData getTopStudentNodesForUserAndAdmin(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of child org nodes of the
     * specified org node <br/><br/>Each node contains a count of the number of
     * students rostered in the specified admin which are at or below the node
     * (rosterCount). Any nodes for which this count is 0 will be filtered out of
     * the result list.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getRosterNodesForParentAndAdmin(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of child org nodes of the
     * specified org node <br/><br/>Each node contains a count of the number of
     * students rostered in the specified admin which are at or below the node
     * (rosterCount). Any nodes for which this count is 0 will be filtered out of
     * the result list.
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getTestTicketNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
   
    
    com.ctb.bean.testAdmin.StudentNodeData getTestTicketNodesHaveStudentForParent(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of org nodes at which the
     * specified user has a role defined. <br/><br/>Each node contains a count of
     * the number of students rostered in the specified admin which are at or
     * below the node (rosterCount). Any nodes for which this count is 0 will be
     * filtered out of the result list.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getTopRosterNodesForUserAndAdmin(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged list of org nodes at which the
     * specified user, or the user who schduled the specified test admin, has a
     * role defined. <br/><br/>Each node contains a count of the number of
     * students rostered in the specified admin which are at or below the node
     * (rosterCount). Any nodes for which this count is 0 will be filtered out of
     * the result list.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test admin
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return StudentNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.StudentNodeData getTopTestTicketNodes(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a list of child org nodes of the specified org node, plus a
     * count of users who have roles anywhere at/below each org node in the list
     * (userCount). If testAdminId is not null, a count of users at or below each
     * node who are assigned as proctors to the specified session is also
     * attached to each node (proctorCount).
     * @param userName - identifies the user
     * @param orgNodeId - identifies the parent org node
     * @param testAdminId - identifies the test session
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return UserNodeData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.UserNodeData getUserNodesForParent(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

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
     * Creates a test session. newSession.getTestSession().getTestAdminId() must
     * be null.
     * @param userName - identifies the user
     * @param newSession - contains test session information and the student and proctor lists
     * @return the test admin id of the newly created session
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    java.lang.Integer createNewTestSession(java.lang.String userName, com.ctb.bean.testAdmin.ScheduledSession newSession) throws com.ctb.exception.CTBBusinessException;

    /**
     * Updates a test session. newSession.getTestSession().getTestAdminId() must
     * be a valid existing testAdminId - the corresponding test session is
     * updated with the new information.
     * @param userName - identifies the user
     * @param newSession - contains test session information and the student and proctor lists
     * @return the test admin id of the updated session
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    java.lang.Integer updateTestSession(java.lang.String userName, com.ctb.bean.testAdmin.ScheduledSession newSession) throws com.ctb.exception.CTBBusinessException;

    /**
     * checks each of the provided list of access codes against the database to
     * ensure they are not in use. Result array contains "valid" for each code
     * that is valid and a variety of other strings appended to each other for
     * each code that is already in use, or is otherwise invalid (eg. contains
     * special characters, <6 characters in length). If testAdminId parameter is
     * non-null, codes in use withing the corresponding test session are not
     * considered invalid (allows re-arranging of access codes within the
     * schedulable units of a session). Example 1, valid code returns: "valid"
     * Example 2, code is too short, contains bad chars, and is in use returns:
     * "shortbadcharsexists"
     * @param userName - identifies the user
     * @param accessCodes - array of proposed access codes
     * @param testAdminId - ignore existing codes within this session when checking for existence
     * @return array of validation results
     * @throws CTBBusinessException
     */
    
    java.lang.String[] validateAccessCodes(java.lang.String userName, java.lang.String[] accessCodes, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

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
     * Retrieves a paged, sorted, filtered list of students that are in the given
     * test session..
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @return SessionStudentData
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionStudentData getSessionStudents(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves all information pertaining to a test session, including the
     * schedulable unit manifest, the roster, the proctor list, and a count of
     * students who have logged into the session. The returned ScheduledSession
     * object can be edited and passed back in via the updateTestSession or
     * createNewTestSession methods to persist changes or create a copy if the
     * copyable flag is 'T'. The copyable flag will be set to 'T' if the
     * requesting user has a non-proctor role, the test is active and distributed
     * to the requesting user's top node, and the requesting user has the
     * scheduling node as one of their top nodes. Otherwise the flag will be set
     * to 'F'. The ScheduledSession.testSession object also contains a
     * overrideFormAssigmentMethod field, which if populated (see
     * TestSession.FormAssignment constants) indicates that only the specified
     * assignment method should be available for scheduling, and a
     * overrideLoginStartDate field, which if populated indicates that no earlier
     * (than the override date) login start date can be chosen for the session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @return ScheduledSession
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ScheduledSession getScheduledSession(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Deletes all information pertaining to a test session, including the
     * schedulable unit manifest, the roster, and the proctor list. An exception
     * will be thrown if any roster elements are in a state which prevents
     * deletion (non-SC completion status).
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @throws CTBBusinessException
     */
    
    void deleteTestSession(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Get grades for the specified customer.
     * @param userName - identifies the calling user
     * @param customerId - identifies the customer whose information is desired
     * @return String []
     * @throws CTBBusinessException
     */
    
    java.lang.String[] getGradesForCustomer(java.lang.String userName, java.lang.Integer customerId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves all information pertaining to a test session, including the
     * schedulable unit manifest, the proctor list. The returned ScheduledSession
     * object can be edited and passed back in via the updateTestSession or
     * createNewTestSession methods to persist changes or create a copy if the
     * copyable flag is 'T'. The copyable flag will be set to 'T' if the
     * requesting user has a non-proctor role, the test is active and distributed
     * to the requesting user's top node, and the requesting user has the
     * scheduling node as one of their top nodes. Otherwise the flag will be set
     * to 'F'. The ScheduledSession.testSession object also contains a
     * overrideFormAssigmentMethod field, which if populated (see
     * TestSession.FormAssignment constants) indicates that only the specified
     * assignment method should be available for scheduling, and a
     * overrideLoginStartDate field, which if populated indicates that no earlier
     * (than the override date) login start date can be chosen for the session.
     * @param userName - identifies the user
     * @param testAdminId - identifies the test session
     * @return ScheduledSession
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.ScheduledSession getTestSessionDataWithoutRoster(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the manifest for the given roster
     * @userName - Identifies the user @ rosterId - Identifies the test roster id
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     */
    
    com.ctb.bean.testAdmin.StudentManifestData getManifestForRoster(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

    /**
     * Adds a new roster element to the specified session. The scheduling student
     * contains the manifest (collection of subtests) selected for the student.
     * This will be used by the rapid registration process to add a student to an
     * existing session. This call should update license usage quantities
     * appropriately, as described above in the ‘Product licensure/usage’
     * section.
     * @userName - - identifies the user
     * @SchedulingStudent -
     * @testAdminId - Identifies the test session
     */
    
    com.ctb.bean.testAdmin.RosterElement addStudentToSession(java.lang.String userName, com.ctb.bean.testAdmin.SessionStudent sessionStudent, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Adjusts the manifest as specified for a roster element/student already
     * part of an existing session. This will be used in the edit test session UI
     * to persist changes to the manifest or subtest selection for a particular
     * student within an existing test session. This call should fail if the
     * roster element is not in an editable completion state (eg. Not SC)
     * @param userName - identifies the user
     * @param StudentManifestData - identifies StudentManifest info
     * @return RosterElement - roster
     * @throws CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.RosterElement updateManifestForRoster(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer stdentOrgNodeId, java.lang.Integer testAdminId, com.ctb.bean.testAdmin.StudentManifestData studentManifestData) throws com.ctb.exception.CTBBusinessException;

    /**
     * Delete the Student Item set Status for a roster and the roster for a
     * student.
     * @param userName - identifies the user
     * @param studentId - identifies the student
     * @param testAdminId - identifies the test session
     * @throws CTBBusinessException
     */
    
    void deleteAddedStudentFromSession(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves the TestProduct for the given testAdminId
     * @userName - Identifies the user @ testAdminId - Identifies the test admin
     */
    
    com.ctb.bean.testAdmin.TestProduct getProductForTestAdmin(java.lang.String userName, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves an array of TABERecommendedLevel for a student based on his
     * prior locator tests' recommended levels.
     * @param userName - identifies the user
     * @param studentId - identifies the student
     * @param testItemSetId - identifies the test
     * @param locatorItemSetId - identifies the locator test
     * @return TABERecommendedLevel []
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.TABERecommendedLevel[] getTABERecommendedLevelForStudent(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer testItemSetId, java.lang.Integer locatorItemSetId) throws com.ctb.exception.CTBBusinessException;

    /**
     * Retrieves a sorted, filtered, paged subset of students in the provided
     * list of students who have been scheduled for any administration of the
     * test corresponding to the provided testItemSetId. The resulting list can
     * be used to prevent scheduling of these students in additional sessions of
     * the same test.
     * @param userName - identifies the user
     * @param studentList - a list of students to check
     * @param testItemSetId - identifies the test (TC item_set_id) to check against
     * @param testAdminId - identifies the current test admin id for scheduling (excluded for restriction)
     * @param filter - filtering params
     * @param page - paging params
     * @param sort - sorting params
     * @return SessionStudentData
     * @throws com.ctb.exception.CTBBusinessException
     */
    
    com.ctb.bean.testAdmin.SessionStudentData getRestrictedStudentsForTest(java.lang.String userName, com.ctb.bean.testAdmin.SessionStudent[] studentList, java.lang.Integer testItemSetId, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;

	
    java.lang.String isTestAdminExists(java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.ScheduledSession getScheduledSessionDetails(String userName, Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.ScheduledSession getScheduledStudentsMinimalInfoDetails(String userName, Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.ScheduledSession getScheduledProctorsMinimalInfoDetails(String userName, Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.SessionStudentData getSessionStudentsMinimalInfoForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, java.lang.Integer testAdminId, java.lang.Integer testItemSetId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.UserData getUsersMinimalInfoForOrgNode(java.lang.String userName, java.lang.Integer orgNodeId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.TestElement getTestElementMinInfoById(Integer customerId, Integer itemsetIdTC) throws  com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.StudentNodeData getTopTestTicketNodesForPrintTT(java.lang.String userName, java.lang.Integer testAdminId, com.ctb.bean.request.FilterParams filter, com.ctb.bean.request.PageParams page, com.ctb.bean.request.SortParams sort) throws com.ctb.exception.CTBBusinessException;
    
    com.ctb.bean.testAdmin.ScheduledStudentDetailsWithManifest getScheduledStudentsManifestDetails(java.lang.String userName, java.lang.Integer studentId, java.lang.Integer testAdminId) throws com.ctb.exception.CTBBusinessException;
    
    java.lang.String[] getTestCatalogForUserForScoring(java.lang.String userName) throws com.ctb.exception.CTBBusinessException;
} 
