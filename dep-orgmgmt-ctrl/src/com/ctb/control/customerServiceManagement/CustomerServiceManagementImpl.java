package com.ctb.control.customerServiceManagement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

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
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.customerServiceManagement.StudentDataNotFoundException;
import com.ctb.exception.validation.ValidationException;


@ControlImplementation(isTransient=true)
public class CustomerServiceManagementImpl implements CustomerServiceManagement {

	/**
	 * @common:control
	 */
	@org.apache.beehive.controls.api.bean.Control()
	com.ctb.control.db.ItemSet itemSets;
	
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
		searchCriteria = generateSearchCriteria(studentId,accessCode);
		if(searchCriteria == null)
			searchCriteria = "";
		searchCriteria += " ORDER BY testAdminName";
		try {

			testSessions = testAdmin.getTestSessionData(searchCriteria);
			
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
			ArrayList<TestSession> testSessionList = new ArrayList<TestSession>();
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
	/*	UserTransaction userTrans = null;
    	boolean transanctionFlag = false;
    	TransactionPersistenceUtil transactionPersistenceUtil = new TransactionPersistenceUtil(); */
	
		try {
		/*	userTrans = transactionPersistenceUtil.getTransaction();
			userTrans.begin(); */
		
			for(int i=0;i<auditFileReopenSubtest.length;i++) {
				AuditFileReopenSubtest reopenSubtestInfo = auditFileReopenSubtest[i];
				RosterElement rosterElement = testRoster.getRosterElement(reopenSubtestInfo.getTestRosterId());
				reopenSubtestInfo.setOldSRosterCompStatus(rosterElement.getTestCompletionStatus());
				testRoster.updateTestRosterForReopen(reopenSubtestInfo.getTestRosterId(),new Date(),reopenSubtestInfo.getCreatedBy());
				students.updateStudentActiveSessionFlag(reopenSubtestInfo.getStudentId(),new Date(),reopenSubtestInfo.getCreatedBy());
				studentItemSetStatus.updateStudentItemSetStatus(reopenSubtestInfo.getTestRosterId(),
						reopenSubtestInfo.getItemSetTDId());
				TestElement testElement = itemSets.getParentItemset(reopenSubtestInfo.getItemSetTDId());
				reopenSubtestInfo.setItemSetTSId(testElement.getItemSetId());
				studentItemSetStatus.insertAuditRecordForReopenSubtestData(reopenSubtestInfo);
			}
		} catch(SQLException se){
		/*	transanctionFlag = true;
        	try {
        		userTrans.rollback();
        	}catch (Exception e1){
        		e1.printStackTrace();
        	}*/
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("UpdateStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("UpdateStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
		/* finally{

				try {
					transactionPersistenceUtil.closeTransaction(userTrans,transanctionFlag);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
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

	@Override
	public void wipeOutSubtest(AuditFileReopenSubtest[] auditFileReopenSubtest)
			throws CTBBusinessException { 
		
		Connection oascon = null;
		String msg = null;
		long eventId = 0L;
		String sql = "insert into AUDIT_FILE_WIPEOUT_SUBTEST ( AUDIT_ID , TEST_ROSTER_ID, ITEM_SET_TD_ID , CUSTOMER_ID, ORG_NODE_ID, TEST_ADMIN_ID, STUDENT_ID, ITEM_SET_TS_ID, OLD_ROSTER_COMPLETION_STATUS, NEW_ROSTER_COMPLETION_STATUS, OLD_SUBTEST_COMPLETION_STATUS, NEW_SUBTEST_COMPLETION_STATUS, TICKET_ID, REQUESTOR_NAME, REASON_FOR_REQUEST, CREATED_BY, CREATED_DATE_TIME, COMPLETION_DATE_TIME, START_DATE_TIME, ITEM_ANSWERED, TIME_SPENT ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		try {
			List<Roster> rosList = new ArrayList<Roster>();
			 oascon = studentItemSetStatus.getConnection();
			for(int i=0;i<auditFileReopenSubtest.length;i++) {
				AuditFileReopenSubtest reopenSubtestInfo = auditFileReopenSubtest[i];
				Integer[] subtestArr = new Integer [1];
				subtestArr[0] =	reopenSubtestInfo.getItemSetTDId();
				java.sql.Array subtestIds = new ARRAY( ArrayDescriptor.createDescriptor("NUM_ARRAY", oascon), oascon, subtestArr);
				Roster roster = new Roster(reopenSubtestInfo.getTestRosterId(), subtestIds);
				rosList.add(roster);
				RosterElement rosterElement = testRoster.getRosterElement(reopenSubtestInfo.getTestRosterId());
				reopenSubtestInfo.setOldSRosterCompStatus(rosterElement.getTestCompletionStatus());
			}
			if(rosList.size()>0) {
				java.sql.Array rosters = new ARRAY(ArrayDescriptor.createDescriptor("ROSTERARRAY", oascon), oascon, rosList.toArray());
				CallableStatement ctmt = oascon.prepareCall("{ call RESET_ROSTER (?, ?) }");
				ctmt.setArray(1, rosters);
				ctmt.registerOutParameter(2, Types.VARCHAR);
				ctmt.execute();
				
				msg = ctmt.getString(2);
				if(msg.indexOf("Success") > -1) {
					eventId = Long.valueOf(msg.split(":")[1]);
					PreparedStatement prest = oascon.prepareStatement(sql);
					for(int i=0;i<auditFileReopenSubtest.length;i++) {
						AuditFileReopenSubtest reopenSubtestInfo = auditFileReopenSubtest[i];
						prest.setLong(1, eventId);
						prest.setInt(2, reopenSubtestInfo.getTestRosterId());
						prest.setInt(3, reopenSubtestInfo.getItemSetTDId());
						prest.setInt(4, reopenSubtestInfo.getCustomerId());
						prest.setInt(5, reopenSubtestInfo.getOrgNodeId());
						prest.setInt(6, reopenSubtestInfo.getTestAdminId());
						prest.setInt(7, reopenSubtestInfo.getTestAdminId());
						prest.setInt(8, reopenSubtestInfo.getItemSetTSId());
						prest.setString(9, reopenSubtestInfo.getOldSRosterCompStatus());
						prest.setString(10, reopenSubtestInfo.getNewRosterCompStatus());
						prest.setString(11, reopenSubtestInfo.getNewSubtestCompStatus());
						prest.setString(12, reopenSubtestInfo.getOldSubtestCompStatus());
						prest.setString(13, reopenSubtestInfo.getTicketId());
						prest.setString(14, reopenSubtestInfo.getRequestorName());
						prest.setString(15, reopenSubtestInfo.getReasonForRequest());
						prest.setInt(16, reopenSubtestInfo.getCreatedBy());
						prest.setDate(17,   new java.sql.Date(reopenSubtestInfo.getCreatedDateTime().getTime()) );//
						if(reopenSubtestInfo.getCompletionDateTime()!=null){
							prest.setDate(18,   new java.sql.Date(reopenSubtestInfo.getCompletionDateTime().getTime()) );
						} else {
							prest.setDate(18,   null );
						}
						if(reopenSubtestInfo.getStartDateTime()!=null){
							prest.setDate(19,   new java.sql.Date(reopenSubtestInfo.getStartDateTime().getTime()) );
						} else {
							prest.setDate(19,  null);
						}
						
						prest.setString(20,  reopenSubtestInfo.getItemAnswered());
						prest.setString(21,  reopenSubtestInfo.getTimeSpent());
						prest.addBatch();	
					}
					prest.executeBatch();
					//studentItemSetStatus.insertAuditRecordForReopenSubtestData();
				} else {
					throw new SQLException(msg);
				}
			
			}
			
		} catch(SQLException se){
			se.printStackTrace();
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("UpdateStudentforTestSession.Failed");
			throw studentDataNotFoundException;                                                
		} catch (Exception e) {
			StudentDataNotFoundException studentDataNotFoundException = 
				new StudentDataNotFoundException
				("UpdateStudentforTestSession.Failed");
			studentDataNotFoundException.setStackTrace(e.getStackTrace());
			throw studentDataNotFoundException;
		} 
		
	}

}





