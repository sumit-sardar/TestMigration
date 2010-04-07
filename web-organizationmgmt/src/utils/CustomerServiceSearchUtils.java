package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ctb.bean.request.FilterParams;
import com.ctb.bean.request.PageParams;
import com.ctb.bean.request.SortParams;
import com.ctb.bean.testAdmin.AuditFileReopenSubtest;
import com.ctb.bean.testAdmin.ScheduleElement;
import com.ctb.bean.testAdmin.ScheduleElementData;
import com.ctb.bean.testAdmin.Student;
import com.ctb.bean.testAdmin.StudentData;
import com.ctb.bean.testAdmin.StudentSessionStatus;
import com.ctb.bean.testAdmin.StudentSessionStatusData;
import com.ctb.bean.testAdmin.TestSession;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.bean.testAdmin.User;
import com.ctb.control.customerServiceManagement.CustomerServiceManagement;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.request.InvalidPageRequestedException;
import com.ctb.exception.request.InvalidSortFieldException;
import com.ctb.widgets.bean.PagerSummary;

import dto.ScheduleElementVO;
import dto.StudentProfileInformation;
import dto.StudentSessionStatusVO;
import dto.TestSessionVO;

public class CustomerServiceSearchUtils {

	private final static String TIME_FORMAT="hh:mm a";
	private final static String DATE_FORMAT="MM/dd/yy";


	/**
	 * searchAllUsersAtAndBelow
	 */    
	public static Student searchStudentData( 
			CustomerServiceManagement customerServiceManagement, 
			String loginUserName, 
			String studentLoginId) 
	throws CTBBusinessException {   


		Student sData = customerServiceManagement.getStudentDetail(loginUserName, studentLoginId);
		return sData;
	}

	public static TestSessionData getStudentTestSessionData(CustomerServiceManagement customerServiceManagement, 
			String loginUserName, 
			Integer studentId,
			Integer customerId,
			String  accessCode,
			FilterParams filter, 
			PageParams page, 
			SortParams sort
	) throws CTBBusinessException {   

		TestSessionData testSessionData = customerServiceManagement.getStudentTestSessionData(
				loginUserName, studentId,accessCode,filter,page,sort);
		return testSessionData;
	}

	public static StudentSessionStatusData getSubtestListForStudent(CustomerServiceManagement customerServiceManagement, 
			Integer rosterId,
			String testAccessCode,
			FilterParams filter,
			PageParams page, 
			SortParams sort 

	) throws CTBBusinessException {   

		StudentSessionStatusData statusData = customerServiceManagement.getSubtestListForStudent(
				rosterId, testAccessCode, filter, page, sort);
		return statusData;
	}

	public static StudentSessionStatusData getStudentListForSubTest(CustomerServiceManagement customerServiceManagement,
			Integer testAdminId,
			Integer itemSetId,
			FilterParams filter,
			PageParams page, 
			SortParams sort 
	) throws CTBBusinessException {
		StudentSessionStatusData statusData = customerServiceManagement.getStudentListForSubTest(testAdminId, itemSetId,filter,page,sort);
		return statusData;
	}

	/**
	 * buildOrgNodePagerSummary
	 */    
	public static PagerSummary buildTestDataPagerSummary(TestSessionData tsData, 
			Integer pageRequested) {
		PagerSummary pagerSummary = new PagerSummary();
		pagerSummary.setCurrentPage(pageRequested);        
		pagerSummary.setTotalObjects(tsData.getFilteredCount());
		pagerSummary.setTotalPages(tsData.getFilteredPages());
		pagerSummary.setTotalFilteredObjects(null);        
		return pagerSummary;
	}

	public static PagerSummary buildSubtestDataPagerSummary(StudentSessionStatusData sstData, 
			Integer pageRequested) {
		PagerSummary pagerSummary = new PagerSummary();
		pagerSummary.setCurrentPage(pageRequested);        
		pagerSummary.setTotalObjects(sstData.getFilteredCount());
		pagerSummary.setTotalPages(sstData.getTotalPages());
		pagerSummary.setTotalFilteredObjects(null);        
		return pagerSummary;
	}

	/*
	 * Put all the customer details in the list
	 */
	public static List buildTestSessionList (TestSessionData testSessionData) {

		List testSessionList = new ArrayList();
		if (testSessionData != null){

			TestSession[] testSessions = testSessionData.getTestSessions();

			if ( testSessions != null) {

				for ( TestSession testSession :  testSessions ) {

					if ( testSession != null && testSession.getTestAdminId() != null) {

						TestSessionVO testSessionDetails =
							new TestSessionVO(testSession);
						testSessionList.add(testSessionDetails);                                        
					}
				}
			}
		}
		return testSessionList;
	}

	/*
	 * Put all the customer details in the list
	 */
	public static List buildTestDeliveritemList (ScheduleElementData scheduleElementData) {

		List testDeliveryitemList = new ArrayList();
		if (scheduleElementData != null){

			ScheduleElement[] scheduleElements = scheduleElementData.getElements();

			if ( scheduleElements != null) {

				for ( ScheduleElement scheduleElement :  scheduleElements ) {

					if ( scheduleElement != null && scheduleElement.getTestAdminId() != null) {

						ScheduleElementVO testDeliveryItemDetails =
							new ScheduleElementVO(scheduleElement);
						testDeliveryitemList.add(testDeliveryItemDetails);                                        
					}
				}
			}
		}
		return testDeliveryitemList;
	}

	/*
	 * Put all the student details in the list
	 */
	public static List buildStudentList (StudentData studentData) {

		List studentList = new ArrayList();
		if (studentData != null){

			Student[] students = studentData.getStudents();

			if ( students != null) {

				for ( int i = 0; i < students.length; i++ ) {
					Student student = students[i];

					if ( student != null && student.getStudentId() != null) {

						StudentProfileInformation studentDetails =
							new StudentProfileInformation(student);
						studentList.add(studentDetails);                                      
					}
				}
			}
		}
		return studentList;
	}

	public static ScheduleElementData getTestDeliveryDataInTestSession( CustomerServiceManagement customerServiceManagement, 
			String loginUserName,String testAccessCode
	) throws CTBBusinessException {   

		ScheduleElementData scheduleElementData = 
			customerServiceManagement.getSubTestListForTestSession(loginUserName,testAccessCode);
		return scheduleElementData;
	}


	/*
	 * Put all the subtest details in the list
	 */
	public static List buildSubtestList (StudentSessionStatusData statusData,String timeZone) {
		List subtestList = new ArrayList();
		if (statusData != null){

			StudentSessionStatus[] subtests = statusData.getStudentSessionStatuses();

			if ( subtests != null) {

				for ( StudentSessionStatus subtest :  subtests ) {

					if ( subtest != null && subtest.getItemSetId() != null) {

						StudentSessionStatusVO subtestDetails =
							new StudentSessionStatusVO(subtest);

						if (subtest.getStudentId() != null && subtest.getItemSetId() != null) {

							subtestDetails.setStudentItemId(
									subtest.getStudentId().toString().concat("_").concat(subtest.getItemSetId().toString()));
						}

						if(subtest.getCompletionDateTime() != null) {
							subtestDetails.setCompletionDateTime(
									getAdjustedDateTime(subtest.getCompletionDateTime(),timeZone));
						}
						if(subtest.getCompletionDateTime() != null) {
							subtestDetails.setStartDateTime(
									getAdjustedDateTime(subtest.getStartDateTime(),timeZone));
						}

						subtestList.add(subtestDetails);                                        
					}
				}
			}
		}
		return subtestList;
	}

	public   static   void  reOpenSubtest( CustomerServiceManagement customerServiceManagement,
			User user, String requestDescription, String serviceRequestor, String ticketId,
			Integer testAdminId, Integer customerId, List studentTestStatusDetailsList, 
			Integer itemTsSetId, Integer creatorOrgId, Integer studentId) 
	throws  CTBBusinessException {

		if  (studentTestStatusDetailsList != null  && studentTestStatusDetailsList.size() > 0) {
			AuditFileReopenSubtest[] auditFileReopenSubtestArray = new  
			AuditFileReopenSubtest[studentTestStatusDetailsList.size()];
			for  ( int  i = 0 ; i < studentTestStatusDetailsList.size() ; i++) {

				StudentSessionStatusVO studentSessionStatusVO = 
					(StudentSessionStatusVO)studentTestStatusDetailsList.get(i);
				AuditFileReopenSubtest auditFileReopenSubtest = new  AuditFileReopenSubtest();

				auditFileReopenSubtest.setTestRosterId(studentSessionStatusVO.getTestRosterId());
				auditFileReopenSubtest.setNewRosterCompStatus( "IN" );
				auditFileReopenSubtest.setNewRosterCompStatus( "IN" );
				auditFileReopenSubtest.setNewSubtestCompStatus( "IN" );
				//issue
				/*auditFileReopenSubtest.
				setOldSRosterCompStatus(FilterSortPageUtils. testStatus_StringToCode (studentSessionStatusVO.getCompletionStatus()));
				*/
				auditFileReopenSubtest.setOldSubtestCompStatus(
						FilterSortPageUtils. testStatus_StringToCode (studentSessionStatusVO.getCompletionStatus()));

				auditFileReopenSubtest.setItemSetTDId(studentSessionStatusVO.getItemSetId());
				auditFileReopenSubtest.setItemSetTSId(Integer.valueOf(itemTsSetId));
				auditFileReopenSubtest.setCreatedBy(user.getUserId());

				auditFileReopenSubtest.setCreatedDateTime( new  Date());
				auditFileReopenSubtest.setCustomerId(customerId);
				auditFileReopenSubtest.setReasonForRequest(requestDescription);
				auditFileReopenSubtest.setRequestorName(serviceRequestor);
				if (studentSessionStatusVO.getStudentId() != null) {//condition true for test session

					auditFileReopenSubtest.setStudentId(studentSessionStatusVO.getStudentId());
				} else {
					auditFileReopenSubtest.setStudentId(studentId);
				}

				auditFileReopenSubtest.setTestAdminId(testAdminId);
				auditFileReopenSubtest.setTicketId(ticketId);
				auditFileReopenSubtest.setOrgNodeId(creatorOrgId);

				auditFileReopenSubtestArray[i] = auditFileReopenSubtest;
			}
			customerServiceManagement.reopenSubtest(auditFileReopenSubtestArray);

		}
	}


	public static String getAdjustedDateTime(Date getStartDateTime,String userTimeZone) {

		Date adjStartDate = com.ctb.util.DateUtils.getAdjustedDate(getStartDateTime, "GMT", userTimeZone, getStartDateTime);
		String startDate = DateUtils.formatDateToDateString(adjStartDate,DATE_FORMAT);
		String startTime = DateUtils.formatDateToTimeString(adjStartDate);                                
		return startDate + " " + startTime	;	
	}

	public static StudentSessionStatusData getStudentSessionStatus(HashMap studentStatusData,PageParams page,SortParams sort) {

		StudentSessionStatus[] result = new StudentSessionStatus[studentStatusData.size()];
		StudentSessionStatusData studentSessionStatusData = null;
		int i=0;
		for (Iterator it = studentStatusData.keySet().iterator(); it.hasNext(); )
		{
			StudentSessionStatusVO studentSessionStatusVO = (StudentSessionStatusVO)studentStatusData.get(it.next());
			StudentSessionStatus studentSessionStatus = new StudentSessionStatus();
			studentSessionStatus.setStudentLoginName(studentSessionStatusVO.getStudentLoginName());
			studentSessionStatus.setCompletionStatus(studentSessionStatusVO.getCompletionStatus());
			studentSessionStatus.setStartDateTime(studentSessionStatusVO.getTestStartDate());
			studentSessionStatus.setCompletionDateTime(studentSessionStatusVO.getTestEndDate());
			studentSessionStatus.setItemAnswered(studentSessionStatusVO.getItemAnswered());
			studentSessionStatus.setItemSetName(studentSessionStatusVO.getItemSetName());
			studentSessionStatus.setTimeSpent(studentSessionStatusVO.getTimeSpent());
			studentSessionStatus.setItemSetId(studentSessionStatusVO.getItemSetId());
			studentSessionStatus.setExternalStudentId(studentSessionStatusVO.getItemAnswered());
			studentSessionStatus.setItemAnswered(studentSessionStatusVO.getItemAnsweredByStudent());
			studentSessionStatus.setItemSetOrder(studentSessionStatusVO.getItemSetOrder());
			studentSessionStatus.setMaxScore(studentSessionStatusVO.getMaxScore());
			studentSessionStatus.setOrg_name(studentSessionStatusVO.getOrg_name());
			studentSessionStatus.setTestRosterId(studentSessionStatusVO.getTestRosterId());
			studentSessionStatus.setRawScore(studentSessionStatusVO.getRawScore());
			studentSessionStatus.setStudentId(studentSessionStatusVO.getStudentId());
			studentSessionStatus.setStudentLoginName(studentSessionStatusVO.getStudentLoginName());
			studentSessionStatus.setStudentName(studentSessionStatusVO.getStudentName());
			studentSessionStatus.setTestAccessCode(studentSessionStatusVO.getTestAccessCode());
			studentSessionStatus.setTotalItem(studentSessionStatusVO.getTotalItem());
			
			result[i] = studentSessionStatus;
			i++;
		}
		
			studentSessionStatusData = new StudentSessionStatusData();
			studentSessionStatusData.setStudentSessionStatuses(result,page.getPageSize() );
			
			if (sort != null && studentSessionStatusData != null) {
				try {
					studentSessionStatusData.applySorting(sort);
				} catch (InvalidSortFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (page != null && studentSessionStatusData != null) {
				try {
					studentSessionStatusData.applyPaging(page);
					
				} catch (InvalidPageRequestedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		
		
		return studentSessionStatusData;
	}

}
