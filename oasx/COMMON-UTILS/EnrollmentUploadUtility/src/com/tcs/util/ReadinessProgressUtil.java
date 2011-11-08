package com.tcs.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ReadinessProgressUtil {


	public static void updateReadinessStatus (Integer siteSurveyId, Connection con) throws Exception{
		
		PreparedStatement preStatement = con.prepareStatement(ReadinessProgressSQL.getSiteSurveyData(siteSurveyId));
		ResultSet siteSurveyRS = preStatement.executeQuery();
		
		PreparedStatement preStatement1 = con.prepareStatement(ReadinessProgressSQL.getSiteEnrollmentData(siteSurveyId));
		ResultSet enrollmentRS = preStatement1.executeQuery();
		
		String check12 = checkPoint1Part2(siteSurveyRS, enrollmentRS);
		
		PreparedStatement preStatement2 = con.prepareStatement(ReadinessProgressSQL.updateCheckPoint1Part2SQL(siteSurveyId));
		preStatement2.setString(1, check12);
		preStatement2.executeUpdate();
		
		PreparedStatement preStatement3 = con.prepareStatement(ReadinessProgressSQL.updateSchoolProgressSQL(siteSurveyId));
		preStatement3.setInt(1, schoolProgressStatus(siteSurveyRS, check12));
		preStatement3.executeUpdate();
		
}

	private static int schoolProgressStatus (ResultSet siteSurveyRS, String checkPoint1Part2) throws Exception {
		
		String check1Part1 = siteSurveyRS.getString("CHECK_POINT1_PART1");
		String check1Part3 = siteSurveyRS.getString("CHECK_POINT1_PART3");
		String check2 = siteSurveyRS.getString("CHECK_POINT2");
		String check3 = siteSurveyRS.getString("CHECK_POINT3");
		int schoolProgress = 0;
		
		if(check1Part1 != null && !"".equals(check1Part1) && check1Part1.equalsIgnoreCase("T")) {
			if(checkPoint1Part2.equals("T")) {
				if(check1Part3 != null && !"".equals(check1Part3) && check1Part3.equalsIgnoreCase("T")) {
					if(check2 != null && !"".equals(check2) && check2.equalsIgnoreCase("T")) {
						if(check3 != null && !"".equals(check3) && check3.equalsIgnoreCase("T")) {
							schoolProgress = 5;
						}
						else {
							schoolProgress = 4;
						}
					}
					else {
						schoolProgress = 3;
					}
				}
				else {
					schoolProgress = 2;
				}
			}
			else {
				schoolProgress = 1;
			}
		} else {
			schoolProgress = 0;
		}
		
		return schoolProgress;
		
	}

	private static String checkPoint1Part2 (ResultSet siteSurveyRS, ResultSet enrollmentRS) throws Exception {
		
		int usableWorkstationCount = 0;
		int workstationCapacityAvailable = 0;
		int testSessionsPerDay = 0;
		int totalTestingDays = 0;
		int workstationCapacityRequired= 0;	
		
		usableWorkstationCount = siteSurveyRS.getInt("WORKSTATION_COUNT");
		testSessionsPerDay = enrollmentRS.getInt("TESTSESSION_PER_DAY");
		totalTestingDays = enrollmentRS.getInt("TOTAL_TESTING_DAYS");
		workstationCapacityRequired = getWorkstationRequired(enrollmentRS);
		workstationCapacityAvailable = usableWorkstationCount * testSessionsPerDay * totalTestingDays;
		
		if(workstationCapacityRequired <= workstationCapacityAvailable)
			return "T";
	    else
	    	return "F";
		
		
	}


	private static int getWorkstationRequired(ResultSet enrollmentRS) throws Exception{
	
		int totalStudentCount = 0;
		String thirdGradeCheck = enrollmentRS.getString("THIRD_GRADE_CHK");
		String fourthGradeCheck = enrollmentRS.getString("FOURTH_GRADE_CHK");
		String fifthGradeCheck = enrollmentRS.getString("FIFTH_GRADE_CHK");
		String sixthGradeCheck = enrollmentRS.getString("SIXTH_GRADE_CHK");
		String seventhGradeCheck = enrollmentRS.getString("SEVENTH_GRADE_CHK");
		String eighthGradeCheck = enrollmentRS.getString("EIGHTH_GRADE_CHK");
		String thirdGradeData = enrollmentRS.getString("THIRD_GRADE");
		String fourthGradeData = enrollmentRS.getString("FOURTH_GRADE");
		String fifthGradeData = enrollmentRS.getString("FIFTH_GRADE");
		String sixthGradeData = enrollmentRS.getString("SIXTH_GRADE");
		String seventhGradeData = enrollmentRS.getString("SEVENTH_GRADE");
		String eighthGradeData = enrollmentRS.getString("EIGHTH_GRADE");
	
			if (thirdGradeCheck != null && thirdGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(thirdGradeData != null && !("").equals(thirdGradeData) ? thirdGradeData : "0")
						* getContentArea(3);
			if (fourthGradeCheck != null && fourthGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(fourthGradeData != null && !("").equals(fourthGradeData) ? fourthGradeData : "0") 
						* getContentArea(4);
			if (fifthGradeCheck != null && fifthGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(fifthGradeData != null && !("").equals(fifthGradeData) ? fifthGradeData : "0")
						* getContentArea(5);
			if (sixthGradeCheck != null && sixthGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(sixthGradeData != null && !("").equals(sixthGradeData) ? sixthGradeData : "0")
						* getContentArea(6);
			if (seventhGradeCheck != null && seventhGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(seventhGradeData != null && !("").equals(seventhGradeData) ? seventhGradeData : "0")
						* getContentArea(7);
			if (eighthGradeCheck != null && eighthGradeCheck.equals("1"))
				totalStudentCount += Integer.parseInt(eighthGradeData != null && !("").equals(eighthGradeData) ? eighthGradeData : "0") 
						* getContentArea(8);
	
		return totalStudentCount;
	}

	private static int getContentArea(int grade) {
		int numberContent = 0;
	
		if (grade == 3)
			numberContent = 2;//considering TS level
		else if (grade == 4)
			numberContent = 3;
		else if (grade == 5)
			numberContent = 3;
		else if (grade == 6)
			numberContent = 3;
		else if (grade == 7)
			numberContent = 3;
		else if (grade == 8)
			numberContent = 2;
	
		return numberContent;
	}


}
