package com.tcs.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileReader {
	
	public static List getFileContent (String fileName) throws Exception {
		
		CSVReader reader = new CSVReader(new FileReader(fileName));
	   	List list = reader.readAll();//CSV file content.....
		return list;
	}
	
	
	public static boolean isExist (String siteId, String surveyType,  Connection con) throws Exception {
		
		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil.getSiteServey(siteId, surveyType));
		ResultSet rs = preStatement.executeQuery();
		boolean isExist = false;
		while (rs.next()) {
			if(rs.getInt("isExist") != 0)
				isExist = true;
		}
		
		preStatement.close();
		
		return isExist;
	}
	
	
	public static boolean ispathExist (String siteId, String path,  Connection con) throws Exception {
		
		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil.getSiteServeypath(siteId, path));
		ResultSet rs = preStatement.executeQuery();
		boolean isExist = false;
		while (rs.next()) {
			if(rs.getInt("isExist") != 0)
				isExist = true;
		}
		
		preStatement.close();
		
		return isExist;
	}
	
	public static boolean updateSiteSurvey (String rowData[],  Connection con) throws Exception {
		
		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil.UpdateSiteSurveyDataSQL(rowData));
		ResultSet rs = preStatement.executeQuery();
		
		preStatement.close();
		
		return true;
	}


	public static boolean insertSiteSurvey (String[] stmtArr,  Connection con) throws Exception {
		
		Statement stmt = con.createStatement();  
		con.setAutoCommit(false);
		for(int i = 0 ; i<stmtArr.length; i++){
			stmt.addBatch(stmtArr[i]);
		}
		if(stmtArr.length > 0)
			stmt.executeBatch();
		
        stmt.close();
		
		return true;
	}

	/* To get path, name  for the provided Site_id.
	 * (since multiple site can exist with same siteid but different path, so a check is performed for insertion of all the sites.)
	 */
	public static String[] getSiteDetail (String rowData[], String siteId,  Connection con) throws Exception {
		
		PreparedStatement preStatement = con.prepareStatement(FileUploadUtil.getSiteDetailSQL(siteId));
		ResultSet rs = preStatement.executeQuery();
		
		List<String> stmtArr = new ArrayList<String>();
		while (rs.next()) {
			SiteDataDetail siteDataDetail = new SiteDataDetail();
			if(rs.getString("ID") != null);
			siteDataDetail.setId( rs.getString("ID"));
			if(rs.getString("NAME") != null);
			siteDataDetail.setName( rs.getString("NAME"));
			if(rs.getString("SITEID") != null);
			siteDataDetail.setSiteId(rs.getString("SITEID"));
			if(rs.getString("PATH") != null);
			siteDataDetail.setPath( rs.getString("PATH"));
			if(!ispathExist(siteDataDetail.getSiteId(), siteDataDetail.getPath(), con))
				stmtArr.add(FileUploadUtil.insertSiteSurveyDataSQL(rowData, siteDataDetail));
			else {
				stmtArr.add(FileUploadUtil.UpdatepthBasedSiteSurveyDataSQL(rowData, siteDataDetail));
			}
		}
		preStatement.close();
		String[] strResult=new String[stmtArr.size()];   
		stmtArr.toArray(strResult);  
		return strResult;
	}
	
	public static int getRowCount(ResultSet set) throws Exception   
	{   
	   int rowCount;   
	   int currentRow = set.getRow();            
	   rowCount = set.last() ? set.getRow() : 0; 
	   if (currentRow == 0)                      
	      set.beforeFirst();                     
	   else                                      
	      set.absolute(currentRow);              
	   return rowCount;   
	}

}
