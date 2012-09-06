package com.ctb.util;

import java.util.Date;

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;

import dto.Session;
import dto.Subtest;

public class SessionValidatorUtil {
	
	public static final String MESSAGE_PRODUCT_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Product\" for this assignment.";
	public static final String MESSAGE_SUBTEST_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Subtest\" for this assignment.";
	public static final String MESSAGE_LEVEL_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Level\" for this assignment.";
	public static final String MESSAGE_ALLOW_BREAK_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Allow breaks\" for this assignment.";
	public static final String MESSAGE_ASSESSMENT_NAME_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit the \"Assignment Name\" for this assignment.";
	public static final String MESSAGE_START_DATE_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Start Date\" for this assignment.";
	public static final String MESSAGE_END_DATE_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"End Date\" for this assignment.";
	public static final String MESSAGE_TIME_ZONE_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Time Zone\" for this assignment.";
	public static final String MESSAGE_UPDATE_ACCOMODATION_AT_SESSION = "OK:One or more students have started this assessment. The changes you have made to the accommodations will be visible to students who begin this test <i>after</i> this change is saved.";
	public static final String MESSAGE_UPDATE_ACCOMODATION_AT_STUDENT = "Error:You can no longer edit accomodation for this student.";
	public static final String MESSAGE_START_TIME_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"Start Time\" for this assignment.";
	public static final String MESSAGE_END_TIME_UNEDITABLE = "Error:One or more students have started this assessment. You can no longer edit \"End Time\" for this assignment.";
	//User Story : TN on Acuity – OAS – 039 – OAS Error Messages : Start
	public static final String MESSAGE_CREATE_SESSION_FAILED = "Error:This assignment was not created successfully. Please try again later.";
	public static final String MESSAGE_SESSION_EXPIRED = "Error:This assignment has expired. Please adjust the end date before making any other changes to this assignment.";
	public static final String MESSAGE_STUDENT_ASSIGNMENT_FAILED = "Error:Some of your students were not successfully assigned/unassigned. Click on this link to see those students. Please edit the assignment at a later time to add/remove these students.";
	public static final String MESSAGE_STUDENT_ALREADY_LOGGED_IN = "Error:One or more students have started this assessment. You can no longer edit the following: Allow breaks, Assignment Name and Start Date. If you want to edit any value(s) apart from the ones noted, please only edit those value(s) and try again.";
	public static final String MESSAGE_DELETE_SESSION_FAILED = "Error:One or more students have started this assessment. You can no longer delete this assignment.";
	public static final String MESSAGE_INVALID_USER = "Error:Invalid user";
	public static final String MESSAGE_INVALID_DATA = "Error:Invalid data";
	public static final String MESSAGE_STATUS_OK = "OK";
	public static final String MESSAGE_POPULATE_SESSION_FAILED = "Error:Failed to populate test session";
	public static final String MESSAGE_POPULATE_SCHEDULED_UNITS_FAILED = "Error:Failed to populate scheduled units";
	public static final String MESSAGE_POPULATE_SESSION_STUDENTS_FAILED = "Error:Failed to populate session students";
	public static final String MESSAGE_POPULATE_PROCTOR_FAILED = "Error:Failed to populate proctor"; 
	public static final String MESSAGE_INVALID_PRODUCT_ID = "Error:Invalid Product ID";
	public static final String MESSAGE_INVALID_LEVEL = "Error:Invalid Level";
	public static final String MESSAGE_INVALID_SESSION_NAME = "Error:Invalid Session Name";
	public static final String MESSAGE_INVALID_START_TIME = "Error:Invalid Start Time";
	public static final String MESSAGE_INVALID_END_TIME = "Error:Invalid End Time";
	public static final String MESSAGE_INVALID_START_DATE = "Error:Invalid Start Date";
	public static final String MESSAGE_INVALID_END_DATE = "Error:Invalid End Date";
	public static final String MESSAGE_INVALID_TEST_BREAK = "Error:Invalid Test Break";
	public static final String MESSAGE_INVALID_TIME_ZONE = "Error:Invalid Time Zone";
	public static final String MESSAGE_INVALID_SUBTESTS = "Error:Invalid Subtests";
	public static final String MESSAGE_INVALID_FIRST_NAME = "Error:Invalid First Name";
	public static final String MESSAGE_INVALID_LAST_NAME = "Error:Invalid Last Name";
	public static final String MESSAGE_INVALID_GENDER = "Error:Invalid Gender";
	public static final String MESSAGE_INVALID_GRADE = "Error:Invalid Grade";
	public static final String MESSAGE_INVALID_SINGLE_SUBTEST = "Error:Invalid Subtest";
	public static final String MESSAGE_OAS_SYSTEM_ERROR = "Error:OAS System Error.";
	//User Story : TN on Acuity – OAS – 039 – OAS Error Messages : End
	

	public static boolean isProductUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(session.getProductId().intValue() != testSession.getProductId().intValue()){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	
	public static boolean isSubtestUpdated(Subtest[] subtests,
			TestElement[] testUnits) {
		boolean isUpdated = false;
		if (subtests == null || testUnits == null) {
			isUpdated = true;
		} else if (subtests.length != testUnits.length) {
			isUpdated = true;
		} else {
			for (TestElement testElement : testUnits) {
				boolean found = false;
				for (Subtest subtest : subtests) {
					if (testElement.getItemSetName().equalsIgnoreCase(subtest.getSubtestName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					isUpdated = true;
				}
			}
		}
		return isUpdated;
	}
	
	
	public static boolean isLevelUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getLevel().equalsIgnoreCase(testSession.getTestLevel())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isSessionNameUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getSessionName().equals(testSession.getTestAdminName())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isStartDateUpdated(Session session, TestSession testSession){
		System.out.println("StartDate from db:"+testSession.getLoginStartDateString());
		System.out.println("StartDate from acuity:"+session.getStartDate());
		boolean isUpdated = false;
		if(!session.getStartDate().equals(testSession.getLoginStartDateString())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isEndDateUpdated(Session session, TestSession testSession){
		 System.out.println("EndDate from db:["+testSession.getLoginEndDateString()+"]");
		 System.out.println("EndDate from acuity:["+session.getEndDate()+"]");
		boolean isUpdated = false;
		if(!session.getEndDate().equals(testSession.getLoginEndDateString())){
			isUpdated = true;
		}
		return isUpdated;
	}
	public static boolean isStartTimeUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		String dbStartTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginStartTime());
		String inPutStartTime	= DateUtils.formatDateToTimeString(DateUtil.getDateFromTimeString(session.getStartTime()));
		System.out.println("StartTime from db:"+dbStartTime+"]");
		System.out.println("StartTime from acuity:"+inPutStartTime+"]");
		if(!inPutStartTime.equals(dbStartTime)){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isEndTimeUpdated(Session session, TestSession testSession){
		String dbEndTime = DateUtils.formatDateToTimeString(testSession.getDailyLoginEndTime());
		String inPutEndTime	= DateUtils.formatDateToTimeString(DateUtil.getDateFromTimeString(session.getEndTime()));
		System.out.println("EndTime from db:"+dbEndTime+"]");
		System.out.println("EndTime from acuity:"+inPutEndTime+"]");
		boolean isUpdated = false;
		if(!inPutEndTime.equals(dbEndTime)){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isTimeZoneUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		String timeZone = DateUtil.getUITimeZone(testSession.getTimeZone());
		System.out.println("timeZone from db:"+timeZone+"]");
		System.out.println("timeZone from acuity:"+session.getTimeZone()+"]");
		if(!session.getTimeZone().equals(timeZone)){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isEnforceBreakUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		 String hasBreak  = session.getHasBreak().booleanValue() ? "T" : "F";
		 String savedHasBreak  = testSession.getEnforceBreak();
		if(!hasBreak.equalsIgnoreCase(savedHasBreak)){
			isUpdated = true;
		}
		return isUpdated;
	}
	public static String getInvalidField(boolean productUpdated, boolean subtestUpdated, boolean levelUpdated, boolean sessionUpdated, boolean  startDateUpdated,  boolean  endDateUpdated, boolean startTimeUpdated, boolean endTimeUpdated, boolean timeZoneUpdated, boolean enforceBreakUpdated){
		String result = "";
		if(productUpdated){
			result = SessionValidatorUtil.MESSAGE_PRODUCT_UNEDITABLE;
		} 
		
		if (subtestUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_SUBTEST_UNEDITABLE;
		} else if (subtestUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_SUBTEST_UNEDITABLE;
		} 
		
		if (levelUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_LEVEL_UNEDITABLE;
		} else if (levelUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_LEVEL_UNEDITABLE;
		} 
		
		if (enforceBreakUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_ALLOW_BREAK_UNEDITABLE;
		} else if (enforceBreakUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_ALLOW_BREAK_UNEDITABLE;
		}
		
		if (sessionUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_ASSESSMENT_NAME_UNEDITABLE;
		} else if (sessionUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_ASSESSMENT_NAME_UNEDITABLE;
		}
		
		if (startDateUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_START_DATE_UNEDITABLE;
		} else if (startDateUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_START_DATE_UNEDITABLE;
		}
		
		if (endDateUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_END_DATE_UNEDITABLE;
		} else if (endDateUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_END_DATE_UNEDITABLE;;
		}

		if (startTimeUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_START_TIME_UNEDITABLE;
		} else if (startTimeUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_START_TIME_UNEDITABLE;;
		}
		
		if (endTimeUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_END_TIME_UNEDITABLE;
		} else if (endTimeUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_END_TIME_UNEDITABLE;;
		}
		
		if (timeZoneUpdated && result.length()>0){
			result += " | " + SessionValidatorUtil.MESSAGE_TIME_ZONE_UNEDITABLE;
		} else if (timeZoneUpdated && result.length()==0){
			result = SessionValidatorUtil.MESSAGE_TIME_ZONE_UNEDITABLE;
		}
		
		
		return result;
	}

}
