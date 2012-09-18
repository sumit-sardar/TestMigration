package com.tcs.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			Integer siteSurverId, String year, Connection con,String enrollmentTableName,
			boolean isSubject,String testSessionPerday,String noTestingDays) throws Exception {

		PreparedStatement preStatement = null;

		try {
			preStatement = con.prepareStatement(FileUploadUtil
					.getsaveOrUpdateSiteSurveyEnrollMentMergeSQL(rowData,enrollmentTableName,isSubject));
			preStatement.setInt(1, siteSurverId);
			preStatement.setString(2, rowData[8]);
			preStatement.setString(3, rowData[9]);
			preStatement.setString(4, rowData[10]);
			preStatement.setString(5, rowData[11]);
			preStatement.setString(6, rowData[12]);
			preStatement.setString(7, rowData[13]);
			if(isSubject){
				preStatement.setString(8, rowData[14]);
				preStatement.setString(9, testSessionPerday);// for update
				preStatement.setString(10, noTestingDays);// for update
				preStatement.setString(11, testSessionPerday);//for insert
				preStatement.setString(12, noTestingDays);//for insert
			}
			else{
				preStatement.setString(8, testSessionPerday);// for update
				preStatement.setString(9, noTestingDays);// for update
				preStatement.setString(10, testSessionPerday);//for insert
				preStatement.setString(11, noTestingDays);//for insert
			}
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

	public static String getCustomerId(String districtNo, String schoolNo, Connection con) throws Exception{
		PreparedStatement preStatement = null;
		String customerId = null;
		try {
			preStatement = con.prepareStatement(FileUploadUtil.getCustomerIDBySchoolAndDistrictNo());
			preStatement.setString(1, districtNo);
			preStatement.setString(2, schoolNo);
			ResultSet rs = preStatement.executeQuery();
			
			if (rs.next()) {
				customerId = rs.getString("CUSTOMER_ID");
			}				
		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}
		
		return customerId;
	}
	public static boolean isEnrollmentSubject(String districtNo, String schoolNo, Connection con)
	throws Exception {
		PreparedStatement preStatement = null;
		Integer customerId = 0;
		try {
			preStatement = con.prepareStatement(FileUploadUtil.getCustomerIDBySchoolAndDistrictNo());
			preStatement.setString(1, districtNo);
			preStatement.setString(2, schoolNo);
			ResultSet rs = preStatement.executeQuery();
			
			if (rs.next()) {
				customerId = rs.getInt("CUSTOMER_ID");
			}	
			if(customerId != 0) return true;
		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}
		
		return false;
		
	}
	public static Map<String,String> getCustomerPropValuesById(String custId,Connection conn) throws Exception{
		Connection con = null;
		PreparedStatement preStatement = null;
		ResultSet rs = null;
		Map<String,String> propType = null;		
		try {

			propType= new HashMap<String,String>();
			preStatement = conn.prepareStatement(FileUploadUtil.getCustomerProperties());
			preStatement.setString(1,custId);
			rs = preStatement.executeQuery();
			while (rs.next()){
				propType.put(rs.getString("name").toUpperCase(),rs.getString("value"));
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}
		return propType;
	}
}
