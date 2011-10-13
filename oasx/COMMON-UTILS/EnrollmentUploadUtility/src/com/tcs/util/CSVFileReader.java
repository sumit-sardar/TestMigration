package com.tcs.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileReader {

	public static List getFileContent(String fileName) throws Exception {

		CSVReader reader = new CSVReader(new FileReader(fileName));
		List list = reader.readAll();// CSV file content.....
		return list;
	}

	public static boolean isExist(String siteId, String surveyType,
			Connection con) throws Exception {

		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil
				.getSiteServey(siteId, surveyType));
		ResultSet rs = preStatement.executeQuery();
		boolean isExist = false;
		while (rs.next()) {
			if (rs.getInt("isExist") != 0)
				;
			isExist = true;
		}

		preStatement.close();

		return isExist;
	}

	public static boolean updateSiteSurvey(String rowData[], Connection con)
			throws Exception {

		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil
				.UpdateSiteSurveyDataSQL(rowData));
		ResultSet rs = preStatement.executeQuery();

		preStatement.close();

		return true;
	}

	public static Integer getSiteSurveyID(String customerId, String siteId,
			String sitePath, Connection con) throws Exception {

		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil
				.getSiteServeyIdSQL(customerId, siteId, sitePath));
		ResultSet rs = preStatement.executeQuery();
		Integer siteSurveyId = 0;
		while (rs.next()) {

			siteSurveyId = rs.getInt("site_survey_id");
			break;
		}
		preStatement.close();
		return siteSurveyId;
	}

	public static void saveOrUpdateSiteSurveyEnrollMent(String[] rowData,
			Integer siteSurverId, String year, Connection con) throws Exception {

		PreparedStatement preStatement = null;

		try {
			preStatement = con.prepareStatement(FileUploadUtil
					.getsaveOrUpdateSiteSurveyEnrollMentMergeSQL(rowData));
			preStatement.setInt(1, siteSurverId);
			preStatement.setString(2, rowData[8]);
			preStatement.setString(3, rowData[9]);
			preStatement.setString(4, rowData[10]);
			preStatement.setString(5, rowData[11]);
			preStatement.setString(6, rowData[12]);
			preStatement.setString(7, rowData[13]);
/*			preStatement.setInt(9, Integer.valueOf(rowData[9]));
			preStatement.setInt(10, Integer.valueOf(rowData[10]));
			preStatement.setInt(11, Integer.valueOf(rowData[11]));
			preStatement.setInt(12, Integer.valueOf(rowData[12]));
			preStatement.setInt(13, Integer.valueOf(rowData[13]));*/
			preStatement.execute();

		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}

	}

	public static Integer getSiteSurveyIDBySchoolAndDistrictNo(
			String districtNo, String schoolNo, Connection con)
			throws Exception {
		PreparedStatement preStatement = null;
		Integer siteSurveyId = 0;
		try {
			preStatement = con.prepareStatement(FileUploadUtil
					.getSiteSurveyIDBySchoolAndDistrictNo());
			preStatement.setString(1, districtNo);
			preStatement.setString(2, schoolNo);
			ResultSet rs = preStatement.executeQuery();
			
			if (rs.next()) {
				siteSurveyId = rs.getInt("SITE_SURVEY_ID");
			}	
		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}
		

		return siteSurveyId;
	}

}
