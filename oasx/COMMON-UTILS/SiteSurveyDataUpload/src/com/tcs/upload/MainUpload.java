package com.tcs.upload;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.tcs.dataaccess.AbstractConnectionManager;
import com.tcs.dataaccess.ConnectionManager;
import com.tcs.util.CSVFileReader;
import com.tcs.util.FileUploadUtil;

public class MainUpload {
	

	/**
	 * @param args
	 */
	public static void main(String... args) throws Exception{
		// TODO Auto-generated method stub
		
		Connection con = null;
		String dbprop = null;
		String csv = null;
		
		if (args.length != 2) {
			
			System.out.println("Incorrect argument....");
			throw new Exception(); 
		} else {
			
			dbprop = args[0];
			csv = args[1];
			
			if (!dbprop.substring(dbprop.indexOf(".")+1).equals("properties")) {
				
				System.out.println("First Argument is not a properties file...");
				throw new Exception();
				
			} else if (!csv.substring(csv.indexOf(".")+1).equals("csv")) {
				
				System.out.println("Secound Argument is not a CSV file...");
				throw new Exception();
				
			} else {
				
				AbstractConnectionManager.processProperties(dbprop);
			}
			
		}
		
		try {
			
			con = ConnectionManager.getConnection();
			
			List list = CSVFileReader.getFileContent(csv);
			
			for (int i = 0; i < list.size(); i++) {
				
				String rowData[] = (String[])list.get(i);
				
				if (CSVFileReader.isExist(rowData[1], rowData[2], con)) {
					
					//update data 
					 CSVFileReader.updateSiteSurvey(rowData, con);
					 
				}
				
				
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				con.commit();
				ConnectionManager.close(con);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			 System.out.println("Finished....");
		}
	
		
	}
	
	

}
