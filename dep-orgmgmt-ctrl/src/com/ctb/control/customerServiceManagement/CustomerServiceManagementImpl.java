package com.ctb.control.customerServiceManagement;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.beehive.controls.api.bean.ControlImplementation;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.ScheduleElement;
import com.ctb.bean.testAdmin.ScheduleElementData;
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
	 * @common:operation
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
		return student;
	}


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
		if( sort != null ) {
			testSessionData.applySorting(sort);
		}
		if( page != null ) {
			testSessionData.applyPaging(page);
		}

		return testSessionData;
	}
	
	
	public StudentSessionStatusData getSubtestListForStudent(Integer rosterId, 
			FilterParams filter, 
			PageParams page, 
			SortParams sort)
	throws CTBBusinessException { 
		com.ctb.bean.testAdmin.StudentSessionStatus[] studentSessionStatus = null;
		try {
			studentSessionStatus = studentItemSetStatus.getSubtestListForRoster(rosterId);
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
	
	public ScheduleElementData getSubTestListForTestSession(String loginUserName,
			String accessCode)
			throws CTBBusinessException { 
		ScheduleElement[] scheduleElements = null;
		ScheduleElementData scheduleElementData = null;
		
		try {
			scheduleElements = testAdminItemSet.getSubTestListForTestSession(accessCode);
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
	        		
	        }
	    }
					
		return scheduleElementData;
	}
	
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
	
	public boolean reopenSubtest(Integer userId, Integer testRosterId, 
			Integer studentId, Integer customerId, 
			String [] selectedItemSetIds, String [] completionStatus)
			throws CTBBusinessException { 
		
		boolean saveFlag = false;
		
		try {
			testRoster.updateTestRosterForReopen(testRosterId);
			students.updateStudentActiveSessionFlag(studentId);
			
			saveFlag = true;
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
	
		return saveFlag;
	}

	
	private String generateSearchCriteria (Integer studentId,String accessCode) {

		StringBuffer findInColumn = new StringBuffer();
		if (studentId != null){
			String studentIdCheck = " and tr.student_id = " + studentId;
			findInColumn.append(studentIdCheck);
		}
		if (accessCode != null &&  !accessCode.trim().equals("")){
			String studentIdCheck = " and ta.test_admin_id in (select distinct tais.test_admin_id "+
			" from test_admin_item_set tais where tais.access_code = '" + accessCode + "') ";
			findInColumn.append(studentIdCheck);
		}
		return findInColumn.toString();
	}
}





