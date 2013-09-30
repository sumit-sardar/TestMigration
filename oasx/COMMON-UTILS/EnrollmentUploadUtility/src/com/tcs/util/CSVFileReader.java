package com.tcs.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			//System.out.println("siteSurverId = "+siteSurverId);
			
			preStatement.setString(2, rowData[8]);
			//System.out.println("8 = "+rowData[8]);
			
			preStatement.setString(3, rowData[9]);
			//System.out.println("9 = "+rowData[9]);
			
			preStatement.setString(4, rowData[10]);
			//System.out.println("10 = "+rowData[10]);
			
			preStatement.setString(5, rowData[11]);
			//System.out.println("11 = "+rowData[11]);
			
			preStatement.setString(6, rowData[12]);
			//System.out.println("12 = "+rowData[12]);
			
			preStatement.setString(7, rowData[13]);
			//System.out.println("13 = "+rowData[13]);
			
					
			
			if(isSubject){
				if(rowData[15].equalsIgnoreCase("YES"))
					rowData[15]="1".trim();
				else if(rowData[15].equalsIgnoreCase("NO"))
					rowData[15]="0".trim();
				else
					rowData[15]="0".trim();
				
				
				 if(rowData[16].equalsIgnoreCase("YES"))
					rowData[16]="1".trim();
				else if(rowData[16].equalsIgnoreCase("NO"))
					rowData[16]="0".trim();
				else
					rowData[16]="0".trim();
				
				 if(rowData[17].equalsIgnoreCase("YES"))
					rowData[17]="1".trim();
				else if(rowData[17].equalsIgnoreCase("NO"))
					rowData[17]="0".trim();
				else
					rowData[17]="0".trim();
				
				 if(rowData[18].equalsIgnoreCase("YES"))
					rowData[18]="1".trim();
				else if(rowData[18].equalsIgnoreCase("NO"))
					rowData[18]="0".trim();
				else
					rowData[18]="0".trim();
				
				 if(rowData[19].equalsIgnoreCase("YES"))
					rowData[19]="1".trim();
				else if(rowData[19].equalsIgnoreCase("NO"))
					rowData[19]="0".trim();
				else
					rowData[19]="0".trim();
				
				 if(rowData[20].equalsIgnoreCase("YES"))
					rowData[20]="1".trim();
				else if(rowData[20].equalsIgnoreCase("NO"))
					rowData[20]="0".trim();
				else
					rowData[20]="0".trim();
				
				 if(rowData[21].equalsIgnoreCase("YES"))
					rowData[21]="1".trim();
				else if(rowData[21].equalsIgnoreCase("NO"))
					rowData[21]="0".trim();
				else
					rowData[21]="0".trim();
				 
				 
				preStatement.setString(8, rowData[14]);
					//System.out.println("14 = "+rowData[14]);
				
				//System.out.println("15 = "+rowData[15]);
				preStatement.setString(9, rowData[15]);
				
				//System.out.println("16 = "+rowData[16]);
				preStatement.setString(10, rowData[16]);
				
				preStatement.setString(11, rowData[17]);
				//System.out.println("17 = "+rowData[17]);
				
				preStatement.setString(12, rowData[18]);
				//System.out.println("18 = "+rowData[18]);
				
				preStatement.setString(13, rowData[19]);
				//System.out.println("19 = "+rowData[19]);
				
				preStatement.setString(14, rowData[20]);
				//System.out.println("20 = "+rowData[20]);
				
				preStatement.setString(15, rowData[21]);
				//System.out.println("21 = "+rowData[21]);
				
				
				
				
				preStatement.setString(16, testSessionPerday);// for update
				//System.out.println("testSessionPerday ="+testSessionPerday);
				
				preStatement.setString(17, noTestingDays);// for update
				//System.out.println("noTestingDays ="+noTestingDays);
				
				preStatement.setString(18, testSessionPerday);//for insert
				//System.out.println("testSessionPerday ="+testSessionPerday);
				
				preStatement.setString(19, noTestingDays);//for insert
				//System.out.println("noTestingDays ="+noTestingDays);
				
				
				
				
			}
			else{
				if(rowData[14].equalsIgnoreCase("YES"))
					rowData[14]="1".trim();
				else if(rowData[14].equalsIgnoreCase("NO"))
					rowData[14]="0".trim();
				else
					rowData[14]="0".trim();
				
				if(rowData[15].equalsIgnoreCase("YES"))
					rowData[15]="1".trim();
				else if(rowData[15].equalsIgnoreCase("NO"))
					rowData[15]="0".trim();
				else
					rowData[15]="0".trim();
				
				
				 if(rowData[16].equalsIgnoreCase("YES"))
					rowData[16]="1".trim();
				else if(rowData[16].equalsIgnoreCase("NO"))
					rowData[16]="0".trim();
				else
					rowData[16]="0".trim();
				
				 if(rowData[17].equalsIgnoreCase("YES"))
					rowData[17]="1".trim();
				else if(rowData[17].equalsIgnoreCase("NO"))
					rowData[17]="0".trim();
				else
					rowData[17]="0".trim();
				
				 if(rowData[18].equalsIgnoreCase("YES"))
					rowData[18]="1".trim();
				else if(rowData[18].equalsIgnoreCase("NO"))
					rowData[18]="0".trim();
				else
					rowData[18]="0".trim();
				
				 if(rowData[19].equalsIgnoreCase("YES"))
					rowData[19]="1".trim();
				else if(rowData[19].equalsIgnoreCase("NO"))
					rowData[19]="0".trim();
				else
					rowData[19]="0".trim();
												 
				
				preStatement.setString(8, rowData[14]);
				preStatement.setString(9, rowData[15]);
				preStatement.setString(10, rowData[16]);
				preStatement.setString(11, rowData[17]);
				preStatement.setString(12, rowData[18]);
				preStatement.setString(13, rowData[19]);
				
				preStatement.setString(14, testSessionPerday);// for update
				preStatement.setString(15, noTestingDays);// for update
				preStatement.setString(16, testSessionPerday);//for insert
				preStatement.setString(17, noTestingDays);//for insert
				
				
				
				
			}
/*			preStatement.setInt(9, Integer.valueOf(rowData[9]));
			preStatement.setInt(10, Integer.valueOf(rowData[10]));
			preStatement.setInt(11, Integer.valueOf(rowData[11]));
			preStatement.setInt(12, Integer.valueOf(rowData[12]));
			preStatement.setInt(13, Integer.valueOf(rowData[13]));*/
			//System.out.println("query = "+preStatement.toString());
			preStatement.execute();
			

		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}

	}

	/*public static void saveOrUpdateSiteSurveyEnrollMent(String[] rowData,
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
			}*/
/*			preStatement.setInt(9, Integer.valueOf(rowData[9]));
			preStatement.setInt(10, Integer.valueOf(rowData[10]));
			preStatement.setInt(11, Integer.valueOf(rowData[11]));
			preStatement.setInt(12, Integer.valueOf(rowData[12]));
			preStatement.setInt(13, Integer.valueOf(rowData[13]));*/
			//preStatement.execute();

		/*} finally {
			if (preStatement != null) {
				preStatement.close();
			}
		}

	}*/

	public static Integer getSiteSurveyIDBySchoolAndDistrictNo(int CUSTOMER_ID_RTS,
			String districtNo, String schoolNo, Connection con)
			throws Exception {
		PreparedStatement preStatement = null;
		Integer siteSurveyId = 0;
		try {
			preStatement = con.prepareStatement(FileUploadUtil
					.getSiteSurveyIDBySchoolAndDistrictNo());
			
			preStatement.setInt(1, CUSTOMER_ID_RTS);
			preStatement.setString(2, districtNo);
			preStatement.setString(3, schoolNo);
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
		String customerType="CUSTOMERTYPE";
		try {

			propType= new HashMap<String,String>();
			preStatement = conn.prepareStatement(FileUploadUtil.getCustomerProperties());
			preStatement.setString(1,custId);
			rs = preStatement.executeQuery();
			while (rs.next()){
				propType.put(customerType,rs.getString("ctype"));
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

	public static void saveOrUpdateSiteSurveyEnrollMent_WV(String[] rowData,
			Integer siteSurverId, String year, Connection con,
			String enrollmentTableName, String testSessionPerday,
			String noTestingDays) {
		PreparedStatement preStatement = null;


		try {
			preStatement = con.prepareStatement(FileUploadUtil
					.getSaveOrUpdateEnrollmentByGrade_wv(enrollmentTableName));
			preStatement.setInt(1, siteSurverId);
			System.out.println("siteSurverId = "+siteSurverId);
			
			preStatement.setString(2, rowData[8]);
			System.out.println("8 = "+rowData[8]);
			
			preStatement.setString(3, rowData[9]);
			System.out.println("9 = "+rowData[9]);
			
			preStatement.setString(4, rowData[10]);
			System.out.println("10 = "+rowData[10]);
			
			preStatement.setString(5, rowData[11]);
			System.out.println("11 = "+rowData[11]);
			
			preStatement.setString(6, rowData[12]);
			System.out.println("12 = "+rowData[12]);
			
			preStatement.setString(7, rowData[13]);
			System.out.println("13 = "+rowData[13]);
			
			//////////////////////start////////////////////////////
			
			preStatement.setString(8, rowData[14]);
			System.out.println("14 = "+rowData[14]);
			
			preStatement.setString(9, rowData[15]);
			System.out.println("15 = "+rowData[15]);
			
			preStatement.setString(10, rowData[16]);
			System.out.println("16 = "+rowData[16]);
			
			///////////////////////end////////////////////////////
	
		
			 if(rowData[17].equalsIgnoreCase("YES"))
				rowData[17]="1".trim();
			else if(rowData[17].equalsIgnoreCase("NO"))
				rowData[17]="0".trim();
			else
				rowData[17]="1".trim();
			
			 if(rowData[18].equalsIgnoreCase("YES"))
				rowData[18]="1".trim();
			else if(rowData[18].equalsIgnoreCase("NO"))
				rowData[18]="0".trim();
			else
				rowData[18]="1".trim();
			
			 if(rowData[19].equalsIgnoreCase("YES"))
				rowData[19]="1".trim();
			else if(rowData[19].equalsIgnoreCase("NO"))
				rowData[19]="0".trim();
			else
				rowData[19]="1".trim();
			 
			 if(rowData[20].equalsIgnoreCase("YES"))
					rowData[20]="1".trim();
				else if(rowData[20].equalsIgnoreCase("NO"))
					rowData[20]="0".trim();
				else
					rowData[20]="1".trim();
				
				if(rowData[21].equalsIgnoreCase("YES"))
					rowData[21]="1".trim();
				else if(rowData[21].equalsIgnoreCase("NO"))
					rowData[21]="0".trim();
				else
					rowData[21]="1".trim();
				
				
				 if(rowData[22].equalsIgnoreCase("YES"))
					rowData[22]="1".trim();
				else if(rowData[22].equalsIgnoreCase("NO"))
					rowData[22]="0".trim();
				else
					rowData[22]="1".trim();
											 
				 if(rowData[23].equalsIgnoreCase("YES"))
						rowData[23]="1".trim();
					else if(rowData[23].equalsIgnoreCase("NO"))
						rowData[23]="0".trim();
					else
						rowData[23]="1".trim();
					
					if(rowData[24].equalsIgnoreCase("YES"))
						rowData[24]="1".trim();
					else if(rowData[24].equalsIgnoreCase("NO"))
						rowData[24]="0".trim();
					else
						rowData[24]="1".trim();
					
					
					 if(rowData[25].equalsIgnoreCase("YES"))
						rowData[25]="1".trim();
					else if(rowData[25].equalsIgnoreCase("NO"))
						rowData[25]="0".trim();
					else
						rowData[25]="1".trim();
		////////////////////////end //////////////////////////////			 
					 
			
					 	preStatement.setString(11, rowData[17]);
						preStatement.setString(12, rowData[18]);
						preStatement.setString(13, rowData[19]);
						preStatement.setString(14, rowData[20]);
						preStatement.setString(15, rowData[21]);
						preStatement.setString(16, rowData[22]);		 
						preStatement.setString(17, rowData[23]);
						preStatement.setString(18, rowData[24]);
						preStatement.setString(19, rowData[25]);		 
			
		
						preStatement.setString(20, testSessionPerday);// for update
						preStatement.setString(21, noTestingDays);// for update
						preStatement.setString(22, testSessionPerday);//for insert
						preStatement.setString(23, noTestingDays);//for insert						
						int flag =	preStatement.executeUpdate(); 				
						if(flag==1 ){
							if(rowData[17].equals("1")||rowData[18].equals("1")||rowData[19].equals("1")||rowData[20].equals("1")||rowData[21].equals("1")||rowData[22].equals("1")||rowData[23].equals("1")||rowData[24].equals("1")||rowData[25].equals("1"))
							{
								String sqlSiteSurvey = "update site_survey set enrollment_status='T', check_point1_part2='T' where  site_survey_id=?";
								preStatement =con.prepareStatement(sqlSiteSurvey);
								preStatement.setString(1, siteSurverId.toString());
								preStatement.execute();
							}							
						}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		finally {
			if (preStatement != null) {
				try {
					preStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}//end of class
