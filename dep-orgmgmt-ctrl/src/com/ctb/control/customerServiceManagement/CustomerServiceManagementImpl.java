package com.ctb.control.customerServiceManagement;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.AuditFileReopenSubtest;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.ScheduleElement;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.customerServiceManagement.StudentDataNotFoundException;
import com.ctb.exception.validation.ValidationException;

@ControlImplementation()
public class CustomerServiceManagementImpl implements CustomerServiceManagement,Serializable {

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.Students students;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.TestRoster testRoster;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.TestAdminItemSet testAdminItemSet;


	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.TestAdmin testAdmin;

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.StudentItemSetStatus studentItemSetStatus;
	
	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	private com.ctb.control.validation.Validator validator;

	static final long serialVersionUID = 1L;

	/**
     * Retrieve Student details
     * below user's top org node(s). 
     * @common:operation
     * @param loginUserName - identifies the user
     * @param studentUserName - identifies the student
	 * @return student
	 * @throws CTBBusinessException
     */
		
	public Student getStudentDetail(String loginUserName,String studentUserName) throws CTBBusinessException {

		Student student = null;
		try {
			student = students.getStudentDetail(loginUserName, studentUserName) ;
		} catch(SQLException se){
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
		/*finally {
			if(student == null) {
				StudentDataNotFoundException studentDataNotFoundException = 
					new StudentDataNotFoundException
					("FindStudentforTestSession.Failed");
				throw studentDataNotFoundException;			}
		}*/
		return student;
	}

	/**
     * Retrieve test session details
     * below user's top org node(s). 
     * @common:operation
     * @param loginUserName - identifies the user
     * @param studentId - identifies the student
     * @param accessCode- identifies the testadmin
      * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return testSessionData
	 * @throws CTBBusinessException
     */

	public TestSessionData getStudentTestSessionData(String loginUserName,
			Integer studentId, 
			String  accessCode,
			FilterParams filter, 
			PageParams page, 
			SortParams sort) throws CTBBusinessException { 

		String searchCriteria = null;
		TestSession[] testSessions = null;
		TestSession[] filteredTestSessions = null;
		System.out.println("platform accessCode" + accessCode);
		searchCriteria = generateSearchCriteria(studentId,accessCode);
		boolean validationFlag = true;
		System.out.println("platform method" + searchCriteria);
		
		try {
			testSessions = testAdmin.getTestSessionData(searchCriteria);
			System.out.println("testSessions" + testSessions);
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 

		if (testSessions != null && testSessions.length > 0) {
			ArrayList testSessionList = new ArrayList();
			for(int i=0;i<testSessions.length;i++) {
				TestSession data = testSessions[i];
				try {
					// validate the testAdmin  to check for user scope
					validator.validateAdmin(loginUserName, data.getTestAdminId(),
					"CustomerServiceManagementImpl:validateTestSessionForReopen");
					testSessionList.add(data);
					} catch (ValidationException ve) {
	        		continue;
	        	}
	        }
			filteredTestSessions = new TestSession[testSessionList.size()];
			testSessionList.toArray(filteredTestSessions);
		} 

		TestSessionData testSessionData = new TestSessionData();
		Integer pageSize = null;
		if ( page != null ) {
			pageSize = new Integer(page.getPageSize());
		} 
		testSessionData.setTestSessions(filteredTestSessions, pageSize);
		if ( filter != null ) {
			testSessionData.applyFiltering(filter);
		}
		if( sort != null && testSessionData != null && filteredTestSessions != null ) {
			testSessionData.applySorting(sort);
		}
		if( page != null && testSessionData != null && filteredTestSessions != null ) {
			testSessionData.applyPaging(page);
		}

		return testSessionData;
	}
	
	/**
     * Retrieve subtest list for student
     * below user's top org node(s). 
     * @common:operation
     * @param rosterId - identifies the subtest
     * @param filter - filtering params
	 * @param page - paging params
	 * @param sort - sorting params
	 * @return studentSessionStatusData
	 * @throws CTBBusinessException
     */
		public StudentSessionStatusData getSubtestListForStudent(Integer rosterId,
			String testAccessCode,	
			FilterParams filter, 
			PageParams page, 
			SortParams sort)
	throws CTBBusinessException { 
		com.ctb.bean.testAdmin.StudentSessionStatus[] studentSessionStatus = null;
		String searchCriteria = null;
		searchCriteria = generateSearchCriteriaForRoster(testAccessCode);
		try {
			studentSessionStatus = studentItemSetStatus.getSubtestListForRoster(rosterId, searchCriteria);
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
	
		StudentSessionStatusData studentSessionStatusData = new StudentSessionStatusData();
		Integer pageSize = null;
		if ( page != null ) {
			pageSize = new Integer(page.getPageSize());
		} 
		studentSessionStatusData.setStudentSessionStatuses(studentSessionStatus, pageSize);
		if ( filter != null ) {
			studentSessionStatusData.applyFiltering(filter);
		}
		if( sort != null ) {
			studentSessionStatusData.applySorting(sort);
		}
		if( page != null ) {
			studentSessionStatusData.applyPaging(page);
		}

		return studentSessionStatusData;
	}
		
		/**
	     * Retrieve subtest list for test session
	     * below user's top org node(s). 
	     * @common:operation
	     * @param loginUserName - identifies the subtest
	     * @param accessCode - identifies the accesscode
	     * @param filter - filtering params
	     * @param page - paging params
	     * @param sort - sorting params
		 * @return scheduleElementData
		 * @throws CTBBusinessException
	     */
	
	public ScheduleElementData getSubTestListForTestSession(String loginUserName,
			String accessCode)
			throws CTBBusinessException { 
		ScheduleElement[] scheduleElements = null;
		ScheduleElementData scheduleElementData = null;
		
		try {
			scheduleElements = testAdminItemSet.getSubTestListForTestSession( accessCode.toUpperCase());
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
	
		if (scheduleElements != null && scheduleElements.length > 0) {
			ScheduleElement data = scheduleElements[0];
			try {
					// validate the testAdmin  to check for user scope
					validator.validateAdmin(loginUserName, data.getTestAdminId(),
					"CustomerServiceManagementImpl:validateTestSessionForReopen");
					scheduleElementData = new ScheduleElementData();
					scheduleElementData.setScheduleElements(scheduleElements, null);
			} catch (ValidationException ve) {
	        	
				throw ve;
	        }
	    }
					
		return scheduleElementData;
	}
	
	/**
     * Retrieve student list for a subtest
     * below user's top org node(s). 
     * @common:operation
     * @param testAdminId - identifies the testadmin
     * @param ItemSetId - identifies the itemset
	 * @return studentSessionStatusData
	 * @throws CTBBusinessException
     */
	public StudentSessionStatusData getStudentListForSubTest(Integer testAdminId, 
			Integer ItemSetId,
			FilterParams filter, 
			PageParams page, 
			SortParams sort)
			throws CTBBusinessException { 
		com.ctb.bean.testAdmin.StudentSessionStatus[] studentSessionStatus = null;
		try {
			studentSessionStatus = studentItemSetStatus.getRosterListForSubTest(testAdminId,ItemSetId);
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
	
		StudentSessionStatusData studentSessionStatusData = new StudentSessionStatusData();
		Integer pageSize = null;
		if ( page != null ) {
			pageSize = new Integer(page.getPageSize());
		} 
		studentSessionStatusData.setStudentSessionStatuses(studentSessionStatus, pageSize);
		if ( filter != null ) {
			studentSessionStatusData.applyFiltering(filter);
		}
		if( sort != null ) {
			studentSessionStatusData.applySorting(sort);
		}
		if( page != null ) {
			studentSessionStatusData.applyPaging(page);
		}

		return studentSessionStatusData;
	}
	
	/**
     * Reopen the subtest
     * below user's top org node(s). 
     * @common:operation
     * @param userId - identifies the user
     * @param testRosterId - identifies the test roster
     * @param studentId - identifies the student
     * @param customerId - identifies the customer
	 * @return studentSessionStatusData
	 * @throws CTBBusinessException
     */
	
	public void reopenSubtest(AuditFileReopenSubtest [] auditFileReopenSubtest)
			throws CTBBusinessException { 
		
		try {
			for(int i=0;i<auditFileReopenSubtest.length;i++) {
				AuditFileReopenSubtest reopenSubtestInfo = auditFileReopenSubtest[i];
				RosterElement rosterElement = testRoster.getRosterElement(reopenSubtestInfo.getTestRosterId());
				reopenSubtestInfo.setOldSRosterCompStatus(rosterElement.getTestCompletionStatus());
				testRoster.updateTestRosterForReopen(reopenSubtestInfo.getTestRosterId(),new Date(),reopenSubtestInfo.getCreatedBy());
				students.updateStudentActiveSessionFlag(reopenSubtestInfo.getStudentId(),new Date(),reopenSubtestInfo.getCreatedBy());
				studentItemSetStatus.updateStudentItemSetStatus(reopenSubtestInfo.getTestRosterId(),
						reopenSubtestInfo.getItemSetTDId());
				studentItemSetStatus.insertAuditRecordForReopenSubtestData(reopenSubtestInfo);
			}
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("FindStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
	
	}
	
/**
 * This is just a private method used to generate the search criteria
 * @param studentId
 * @param accessCode
 * @return
 */

	
	private String generateSearchCriteria (Integer studentId,String accessCode) {

		StringBuffer findInColumn = new StringBuffer();
		if (studentId != null){
			String studentIdCheck = " and tr.student_id = " + studentId;
			findInColumn.append(studentIdCheck);
		}
		if (accessCode != null &&  !accessCode.trim().equals("")){
			String studentIdCheck = " and ta.test_admin_id in (select distinct tais.test_admin_id "+
			" from test_admin_item_set tais where upper(tais.access_code) = '" + accessCode.toUpperCase() + "') ";
			findInColumn.append(studentIdCheck);
		}
		return findInColumn.toString();
	}

	private String generateSearchCriteriaForRoster (String accessCode) {

		StringBuffer findInColumn = new StringBuffer();

		if (accessCode != null &&  !accessCode.trim().equals("")){
			String studentIdCheck = " and upper(tais.access_code) = '" + accessCode.toUpperCase() + "' ";
			findInColumn.append(studentIdCheck);
		}
		return findInColumn.toString();
	}

}





