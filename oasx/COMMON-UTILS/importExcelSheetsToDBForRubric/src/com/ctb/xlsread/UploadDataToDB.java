package com.ctb.xlsread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ctb.util.DBUtil;
import com.ctb.util.FileUtil;

public class UploadDataToDB {

	private  String user ;
	private  String password ;
	private  String host ;
	private  String sid ;
	
	public UploadDataToDB(String db_ip, String db_user, String db_pass,
			String db_sid) {
		this.user= db_user;
		this.password = db_pass;
		this.sid = db_sid;
		this.host = db_ip;
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String fileName= "C:/Documents and Settings/370755/Desktop/Rubric_Data_Input_Template.xls";
		if (args.length==0 || args[0].trim().length()==0 ){
			System.err.println("Please provide envirion file path..");
			System.exit(0);
		}
	
		Properties pr = FileUtil.ReadProperty(args[0].trim());
		String fileName = pr.getProperty("excel_file_Path");
		String sheet_count = pr.getProperty("excel_sheet_count");
		String db_ip = pr.getProperty("db_ip");
		String db_user = pr.getProperty("db_user");
		String db_pass = pr.getProperty("db_pass");
		String db_sid = pr.getProperty("db_sid");
		int sheetCount =0;
		
		if(fileName==null || fileName.trim().length()==0){
			System.err.println("Please provide valid file name.(eg set [excel_file_Path = /user/env.xls] in property file).");
			System.exit(0);
		}
		
		if(sheet_count==null || sheet_count.trim().length()==0){
			System.err.println("Please provide valid sheet number.(eg set [excel_shet_count = 1] in property file).");
			System.exit(0);
		}
		try{
			sheetCount = Integer.parseInt(sheet_count.trim());
		} catch (Exception e) {
			System.err.println("Please provide valid sheet number.(eg set [excel_shet_count = 1] in property file).");
			e.printStackTrace();
			System.exit(0);
		}
		
		if(db_ip==null || db_ip.trim().length()==0){
			System.err.println("Please provide valid database ip address.(eg set [db_ip = 192.168.60.1] in property file).");
			System.exit(0);
		}
		if(db_user==null || db_user.trim().length()==0){
			System.err.println("Please provide valid database user name.(eg set [db_user = CTB] in property file).");
			System.exit(0);
		}
		if(db_pass==null || db_pass.trim().length()==0){
			System.err.println("Please provide valid database password.(eg set [db_pass = tcs] in property file).");
			System.exit(0);
		}
		if(db_sid==null || db_sid.trim().length()==0){
			System.err.println("Please provide valid database sid name.(eg set [db_sid = tcs] in property file).");
			System.exit(0);
		}
		
		UploadDataToDB uploadDataToDB = new UploadDataToDB(db_ip,db_user, db_pass, db_sid);
		uploadDataToDB.readFirstSheet(fileName,0);
		uploadDataToDB.readSecondSheet(fileName,1);
	}
    
	
	public void readFirstSheet(String fileName,int sheetno ) {

		File rubricFile = new File(fileName);
		FileInputStream fileInputStream = null;
		ArrayList<RubricViewData> rubricList = null;
		//read the file in to stream
		if(rubricFile.exists()) {
			try {
				fileInputStream = new FileInputStream(rubricFile);
				POIFSFileSystem pfs  = new POIFSFileSystem( fileInputStream );
				HSSFWorkbook wb = new HSSFWorkbook(pfs);
				HSSFSheet sheet = wb.getSheetAt(sheetno);

				HSSFCell cells = null; 
				 rubricList = new ArrayList<RubricViewData> ();
				if ( sheet != null) {
					int totalRows =  sheet.getPhysicalNumberOfRows();
					HSSFRow row = sheet.getRow(0);

					if(row == null){
						System.out.println("No file header content") ;
					} else { 
						int totalColumns = row.getPhysicalNumberOfCells();    

						for( int j = 1 ; j < totalRows ; j++) {
							RubricViewData rubViewD = new RubricViewData();
							HSSFRow dataRow = sheet.getRow(j);
							for ( int i = 0 ; i < totalColumns ; i++  ) {
								cells = row.getCell((short)i);
								if (cells.getStringCellValue().equals("ITEM ID")) {
									System.out.println(cells.getStringCellValue());
									rubViewD.setItemId(dataRow.getCell((short)i).getStringCellValue());
								}
								if (cells.getStringCellValue().equals("SCORE")) {
									System.out.println(cells.getStringCellValue());
									Double d = dataRow.getCell((short)i).getNumericCellValue();
									rubViewD.setScore(d.intValue());
								}
								if (cells.getStringCellValue().equals("DESCRIPTION")) {
									System.out.println(cells.getStringCellValue());
									rubViewD.setRubricDescription(dataRow.getCell((short)i).getStringCellValue());
								}
							}
							rubricList.add(rubViewD);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace(); // unexpected
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fileInputStream != null)
						fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  else {
			System.out.println("File Does not exists");
		}
		if(rubricList != null){
			DBUtil dbUtil = new DBUtil( host,  sid,  user,  password);
			dbUtil.setRubricViewList(rubricList);
			String[] rubricdata = dbUtil.getQueryString("item_rubric_data");
			try {
				dbUtil.insertBatchData(rubricdata);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Rubric data does not exists in excel.");
		}
		
	}


	public void readSecondSheet(String fileName,int sheetno) {
	
		File rubricFile = new File(fileName);
		FileInputStream fileInputStream = null;
		ArrayList<RubricViewData> rubricList = null;
		//read the file in to stream
		if(rubricFile.exists()) {
			try {
				fileInputStream = new FileInputStream(rubricFile);
	
				POIFSFileSystem pfs  = new POIFSFileSystem( fileInputStream );
				HSSFWorkbook wb = new HSSFWorkbook(pfs);
				HSSFSheet sheet = wb.getSheetAt(sheetno);
	
				HSSFCell cells = null; 
				 rubricList = new ArrayList<RubricViewData> ();
				if ( sheet != null) {
					int totalRows =  sheet.getPhysicalNumberOfRows();
					HSSFRow row = sheet.getRow(0);
	
					if(row == null){
						System.out.println("No file header content") ;
					} else { 	
						int totalColumns = row.getPhysicalNumberOfCells();    
	
						for( int j = 1 ; j < totalRows ; j++) {
							RubricViewData rubViewD = new RubricViewData();
							HSSFRow dataRow = sheet.getRow(j);
							for ( int i = 0 ; i < totalColumns ; i++  ) {
	
								cells = row.getCell((short)i);
	
								if (cells.getStringCellValue().equals("ITEM ID")) {
									rubViewD.setItemId(dataRow.getCell((short)i).getStringCellValue());
								}
								if (cells.getStringCellValue().equals("SCORE")) {
									Double d = dataRow.getCell((short)i).getNumericCellValue();
									rubViewD.setScore(d.intValue());
								}
								if (cells.getStringCellValue().equals("SAMPLE RESPONSE")) {
									rubViewD.setSampleResponse(dataRow.getCell((short)i).getStringCellValue());
								}
								if (cells.getStringCellValue().equals("EXPLANATION OF SCORE")) {
									rubViewD.setRubricExplanation(dataRow.getCell((short)i).getStringCellValue());
								}
							}
							rubricList.add(rubViewD);
						}

					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(rubricList != null){
			DBUtil dbUtil = new DBUtil( host,  sid,  user,  password);
			dbUtil.setRubricViewList(rubricList);
			String[] rubricdata = dbUtil.getQueryStringExplanation("item_rubric_exemplars");
			try {
				dbUtil.insertBatchData(rubricdata);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Rubric data does not exists in excel.");
		}
	}
}

