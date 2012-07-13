package com.ctb.util;

import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestSession;

import dto.Session;
import dto.Subtest;

public class SessionValidatorUtil {
	
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
		boolean isUpdated = false;
		if(!session.getStartDate().equals(testSession.getLoginStartDateString())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isEndDateUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getEndDate().equals(testSession.getLoginEndDateString())){
			isUpdated = true;
		}
		return isUpdated;
	}
	public static boolean isStartTimeUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getStartTime().equals(testSession.getDailyLoginStartTime())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isEndTimeUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getEndTime().equals(testSession.getDailyLoginEndTime())){
			isUpdated = true;
		}
		return isUpdated;
	}
	
	public static boolean isTimeZoneUpdated(Session session, TestSession testSession){
		boolean isUpdated = false;
		if(!session.getTimeZone().equals(testSession.getTimeZone())){
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
	public static String getInvalidField(boolean productUpdated, boolean subtestUpdated, boolean levelUpdated, boolean sessionUpdated, boolean  startDateUpdated, boolean endTimeUpdated, boolean timeZoneUpdated, boolean enforceBreakUpdated){
		String result = "";
		if(productUpdated){
			result += "product";
		} 
		
		if (subtestUpdated && result.length()>0){
			result += ", subtest";
		} else if (subtestUpdated && result.length()==0){
			result += "subtest";
		} 
		
		if (levelUpdated && result.length()>0){
			result += ", level";
		} else if (levelUpdated && result.length()==0){
			result += "level";
		} 
		
		if (sessionUpdated && result.length()>0){
			result += ", session name";
		} else if (sessionUpdated && result.length()==0){
			result += "session name";
		}
		
		if (startDateUpdated && result.length()>0){
			result += ",start date";
		} else if (startDateUpdated && result.length()==0){
			result += "start date";
		}
		
		if (endTimeUpdated && result.length()>0){
			result += ",end date";
		} else if (endTimeUpdated && result.length()==0){
			result += "end date";
		}
		
		if (timeZoneUpdated && result.length()>0){
			result += ",time zone";
		} else if (timeZoneUpdated && result.length()==0){
			result += "time zone";
		}
		
		if (enforceBreakUpdated && result.length()>0){
			result += ",has break";
		} else if (enforceBreakUpdated && result.length()==0){
			result += "has break";
		}
		
		return result;
	}

}
