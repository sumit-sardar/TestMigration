package com.tcs.upload;


import java.sql.Connection;
import java.util.List;

import com.tcs.dataaccess.AbstractConnectionManager;
import com.tcs.dataaccess.ConnectionManager;
import com.tcs.util.CSVFileReader;
import com.tcs.util.ReadinessProgressUtil;

public class MainUpload {
	

	/**
	 * @param args
	 */
	public static void main(String... args) throws Exception{
		// TODO Auto-generated method stub
		
		Connection con = null;
		String dbprop = null;
		String csv = null;
		String year = null;
		
		if (args.length != 3) {
			
			System.err.println("Incorrect argument....");
			System.out.println("Please provide the argument with order: <properties file> <csv file> <year>");
			throw new Exception("Incorrect argument...."); 
		} else {
			
			dbprop = args[0];
			csv = args[1];
			year = args[2];
			
			if (!dbprop.substring(dbprop.indexOf(".")+1).equals("properties")) {
				
				System.out.println("First Argument is not a properties file...");
				System.out.println("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();
				
			} else if (!csv.substring(csv.indexOf(".")+1).equals("csv")) {
				
				System.out.println("Secound Argument is not a CSV file...");
				System.out.println("Please provide the three argument with order: <properties file> <csv file> <year>");
				throw new Exception();
				
			}  else {
				try{
					Integer.valueOf(year);
				 } catch (Exception e ){
					 System.out.println("Third Argument is not a valid year ...");
					 System.out.println("Please provide the three argument with order: <properties file> <csv file> <year>");
					 throw new Exception();
				 }
				 
				AbstractConnectionManager.processProperties(dbprop);
			}
			
		}
		
		try {
			
			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			
			List list = CSVFileReader.getFileContent(csv);
			Integer siteSurverId = 0;
			for (int i = 0; i < list.size(); i++) {
				System.out.println("Processing started for row==>"+(i+1));
				String rowData[] = (String[])list.get(i);
				
				siteSurverId = CSVFileReader.getSiteSurveyIDBySchoolAndDistrictNo(rowData[4], rowData[5], con);
				System.out.println("siteSurverId===>"+siteSurverId);
				
				if ( siteSurverId == 0 ) {
					System.out.println("Given information combination does not matches in DB :district id [" +rowData[4]+"], school id ["+rowData[5]+"]. Skipping processing of this row.");
				
				} else {
					
					CSVFileReader.saveOrUpdateSiteSurveyEnrollMent(rowData, siteSurverId , year, con);
					
					//Updating the readiness progress and check point status
					ReadinessProgressUtil.updateReadinessStatus(siteSurverId, con);
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
