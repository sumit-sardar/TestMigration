package com.tcs.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.*;


import au.com.bytecode.opencsv.CSVReader;

import com.tcs.dataaccess.ConnectionManager;

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
			if(rs.getInt("isExist") != 0);
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

}
