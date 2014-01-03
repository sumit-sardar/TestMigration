package com.ctb.csvread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.util.DBUtil;
import com.ctb.util.ExtractUtil;
import com.ctb.csvread.GRItemRuleData;



public class UploadDataToDB {
	
	private static String user ;
	private static String password ;
	private static String host ;
	private static String sid ;
	private static String productType ;
	private static String filelocation ;
	
	public UploadDataToDB() {
			
	}
	
	/**
	 * @param args
	 * Here the properties file name must be Config. We have to provide the file path location in argument.
	 */
	public static void main(String[] args) {
		ExtractUtil.loadPropertiesExternally("config", args[0]);
		user = ExtractUtil.getDetail("oas.db.user.name").trim();
		password = ExtractUtil.getDetail("oas.db.user.password").trim();
		host = ExtractUtil.getDetail("oas.db.host.address").trim();
		sid = ExtractUtil.getDetail("oas.db.sid.address").trim();
		productType = ExtractUtil.getDetail("current.product.type").trim();
		filelocation = ExtractUtil.getDetail("file.location").trim();
		
		if(user==null || user.trim().length()==0){
			System.err.println("Please provide valid file name.(eg set [excel_file_Path = /user/env.csv] in property file).");
			System.exit(0);
		}
		if(password==null || password.trim().length()==0){
			System.err.println("Please provide valid database password.(eg set [db_pass = tcs] in property file).");
			System.exit(0);
		}
		if(host==null || host.trim().length()==0){
			System.err.println("Please provide valid database ip address.(eg set [db_ip = 192.168.60.1] in property file).");
			System.exit(0);
		}
		if(sid==null || sid.trim().length()==0){
			System.err.println("Please provide valid database sid name.(eg set [db_sid = tcs] in property file).");
			System.exit(0);
		}
		if(productType==null || productType.trim().length()==0){
			System.err.println("Please provide valid product Type (eg set [current.product.type = TASC] in property file).");
			System.exit(0);
		}
		if(filelocation==null || filelocation.trim().length()==0){
			System.err.println("Please provide valid file name.(eg set [excel_file_Path = /user/env.csv] in property file).");
			System.exit(0);
		}
		
		UploadDataToDB uploadData = new UploadDataToDB();
		System.out.println("GR Item Rules Load Started:"+new java.util.Date());
		/*
		 * String s = "GR0,\"2,3,4,7,9\",4";
		   System.out.println(s+"--"+s.indexOf("\"",4)+"--"+s.substring(5, s.indexOf("\"",5)));
		*/
		uploadData.loadGRRules(filelocation);
		System.out.println("GR Item Rules Load Completed:"+new java.util.Date());
	   
	}
	
	private void loadGRRules(String fileName){
	
		 String line = "";
		 String csvSplit = ",";
		 int quotesCount = 0;
		 boolean logicalError = false ;
		 int lineNumber =0, startPosition = 0,endPosition = 0;
		 String copyLine , finalLine = "" ;
		 ArrayList<GRItemRuleData> grCollection = new ArrayList<GRItemRuleData>();
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(fileName));
			while((line = bf.readLine()) != null){
				GRItemRuleData grItems = new GRItemRuleData();
				quotesCount = 0;
				copyLine = line ;
				finalLine = "";
				startPosition = 0;
				endPosition = 0;
				lineNumber++;
				if (lineNumber == 1){					
					continue ;// Skipping Header Column
				}
				
				//Counting number of " present in each line. This must be even.
				 for (int i= 0 ; i < line.length() ; i++){
					 char ch = line.charAt(i);					
					 if (34 == (int)ch){ // 34 is ASCII value of "						 
						 quotesCount++;
					 }					 
				 }
				 if (quotesCount%2 != 0)
					 logicalError = true;				 
				//end of Counting number of " present in each line. This must be even.
				 
				 if (!logicalError){				 
					 String [] items = line.split("\"");
					 if (quotesCount == 2){						 
						 finalLine = items[0]+items[1].replace(",", "$") +items[2];
					 }else if (quotesCount == 4){
						 finalLine = items[0]+items[1].replace(",", "$") +items[2]+items[3].replace(",", "$");					 
					 }				 
					 
					 items = finalLine.split(csvSplit);
					 grItems.setMonarchItemId(items[0]);
					 //PEID is not needed for OAS. Hence skipping that
					 grItems.setItemRules(items[2].replace("$", ","));
					 grItems.setCorrectAnswer(items[3].replace("$", ","));						 
			    }
			  grCollection.add(grItems);
			}
			
			if (grCollection!=null){				
				DBUtil dbUtil = new DBUtil( host,  sid,  user,  password);
				dbUtil.setItemRuleList(grCollection);
				String[] grItemRules = dbUtil.getQueryString("GR_ITEM_RULES");
				try {
					dbUtil.insertBatchData(grItemRules);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
