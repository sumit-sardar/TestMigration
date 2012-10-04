package com.tcs.util;

public class ReadinessProgressSQL {

//	public static String isSiteSurveyIdExist (Integer siteSurveyID) {
//
//		String sql = "SELECT count(*) as isExist FROM site_survey_enrollment WHERE site_survey_id ="+siteSurveyID;		
//		return sql;
//	}

	public static String getSiteSurveyData (Integer siteSurveyID) {

		String sql = "SELECT * FROM SITE_SURVEY WHERE SITE_SURVEY_ID = " + siteSurveyID;
		return sql;

	}

	public static String getSiteEnrollmentData (Integer siteSurveyID) {

		String sql = "SELECT * FROM SITE_SURVEY_ENROLLMENT WHERE SITE_SURVEY_ID = " + siteSurveyID;
		return sql;

	}

	public static String getSiteEnrollmentDataBySubject (Integer siteSurveyID) {

		String sql = "SELECT * FROM SITE_SURVEY_ENROLLMENT_SUBJECT WHERE SITE_SURVEY_ID = " + siteSurveyID;
		return sql;

	}

	public static String updateCheckPoint1Part2SQL (Integer siteSurveyID) {

		String sql = "UPDATE SITE_SURVEY SET CHECK_POINT1_PART2 = ? WHERE SITE_SURVEY_ID = " + siteSurveyID;
		return sql;

	}

	public static String updateSchoolProgressSQL (Integer siteSurveyID) {

		String sql = "UPDATE SITE_SURVEY SET SCHOOL_PROGRESS_STATUS = ? WHERE SITE_SURVEY_ID = " + siteSurveyID;
		return sql;

	}

}
