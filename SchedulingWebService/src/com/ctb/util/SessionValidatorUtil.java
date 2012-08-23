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