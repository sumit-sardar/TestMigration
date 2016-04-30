/**
 * 
 */
package com.ctb.importdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ctb.bean.SalesForceLicenseData;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.DBUtil;
import com.ctb.utils.ExtractUtil;

/**
 * This Class is entry point to this Utility
 * 
 * @author TCS
 * 
 */
public class ImportSFDataProcessor {
	

	static Logger logger = Logger.getLogger(ImportSFDataProcessor.class.getName());
	
	/**
	 * This is starting point for SF data import process: method accepts single command-line argument denoting the Properties file Name
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean isDBLoadSuccessful = false;
		String envName = getPropFileFromCommandLine(args);
		ExtractUtil.loadExternalPropetiesFile(envName, args[1]);
		PropertyConfigurator.configure(Configuration.getLog4jFile());
		logger.info("Properties file successfully loaded for environment >> "+ envName);
		ArrayList<SalesForceLicenseData> sfDataList = null;
		String fileName = Configuration.getLocalFilePath();		
		//System.out.println("File Name >> "+ fileName);
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		//System.out.println("File Type >> "+ fileType);
		logger.info("SF License Data Load Process:: Started for :: Filename >> ["+fileName+"] :: Filetype >> ["+fileType+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		if(fileType.equals(Constants.FILE_TYPE_XLS))
			sfDataList = readDataFromXLSFile(fileName);
		else if(fileType.equals(Constants.FILE_TYPE_XLSX))
			sfDataList = readDataFromXLSXFile(fileName);
		else{
			System.err.println("Input file type ["+fileType+"] is not supported. Please provide input file in [.xls] or [.xlsx] file format.");
			logger.error("SF License Data Load Process:: Stopped for :: Filename >> ["+fileName+"] :: Filetype >> ["+fileType+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			logger.info("Input file type ["+fileType+"] is not supported. Please provide input file in [.xls] or [.xlsx] file format.");
			System.exit(2);
		}	
		if(sfDataList != null){
			//TODO: Load SF data into database 
			//System.out.println("sfld.size() >> "+sfDataList.size());
			logger.info("Total no. of eligible rows to be populated >> ["+sfDataList.size()+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			//System.out.println("SF Data Population:: Batch Update :: Start Time >> "+ new Date(System.currentTimeMillis()));
			logger.info("SF Data Population:: Batch Update : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			isDBLoadSuccessful = DBUtil.saveSFLicenseDataInBatch(sfDataList);
			if(isDBLoadSuccessful)
				logger.info("SF Data Population:: Batch Update : Completed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			else
				logger.error("SF Data Population:: Batch Update : Failed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		}
		if(isDBLoadSuccessful)
			logger.info("SF License Data Load Process:: Completed for :: Filename >> ["+fileName+"] :: Filetype >> ["+fileType+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		else
			logger.error("SF License Data Load Process:: Failed for :: Filename >> ["+fileName+"] :: Filetype >> ["+fileType+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
	}
	
	private static String getPropFileFromCommandLine(String[] args) {
		String envName = "";
		String usage = "Usage:\n 	java -jar ImportStudentData.jar <properties file name>";
		if (args.length < 1) {
			System.err
					.println("Cannot parse command line. No command specified.\n"
							+ usage);
			logger.error("Cannot parse command line. No command specified.");
			logger.info(usage);
			System.exit(1);
		} else {
			envName = args[0].toUpperCase();
			//System.out.println("envName >> "+envName);
		}
		return envName;
	}
	
	public static ArrayList<SalesForceLicenseData> readDataFromXLSXFile(String fileName){
		File sfDataFile = new File(fileName);
		FileInputStream fileInputStream = null;		
		ArrayList<SalesForceLicenseData> sfLicenseDataList = null;
		
		//read the file in to stream
		if(sfDataFile.exists()) {
			//System.out.println("Reading data from .xlsx file started.");
			logger.info("Reading data from .xlsx file : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			try {
				fileInputStream = new FileInputStream(sfDataFile);
				
				//Create Workbook instance holding reference to .xlsx file
		        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		
		        //Get first/desired sheet from the workbook
		        XSSFSheet sheet = workbook.getSheetAt(0);			
		        
		        sfLicenseDataList = new ArrayList<SalesForceLicenseData>(); 
		        SalesForceLicenseData sfld = null;
		        
		        if(sheet != null){
		        	int totalRows = sheet.getPhysicalNumberOfRows();
		        	//System.out.println("Total no. of physical rows in file = "+ totalRows);
		        	logger.info("Total no. of physical rows in file = "+ totalRows);
		        	Row headerRow = sheet.getRow(0);
		        	Cell headerCell;
		        	Cell dataCell;
		        	if(headerRow == null){
		        		//System.out.println("No file header content found.") ;
		        		logger.info("No file header content found.");
		        	}
		        	else{
			        	int totalHeaderColumns = headerRow.getPhysicalNumberOfCells();
			        	//System.out.println("Total no. of header cells = "+ totalHeaderColumns);
			        	//logger.info("Total no. of header cells = "+ totalHeaderColumns);
			        	for( int rowCtr = 1 ; rowCtr < totalRows ; rowCtr++) {
							//System.out.println("Row No. >> "+rowCtr);
							Row dataRow = sheet.getRow(rowCtr);	
							if (dataRow != null){
								int totalRowColumns = dataRow.getPhysicalNumberOfCells();
								//System.out.println("Total no. of current data row cells = "+ totalRowColumns);
								//logger.info("Total no. of current data row cells = "+ totalRowColumns);
								logger.info("Row No. ["+rowCtr+"] :: Header Column Count = ["+totalHeaderColumns+"] :: Current Data Row Column Count = ["+totalRowColumns+"]");
								//Discard dummy rows in spreadsheet if the count of current data row columns not equal to header columns
						        if(totalHeaderColumns == totalRowColumns){
									boolean isCustomerIdBlank = dataRow.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK ? true:false;
									boolean isOrgNodeIdBlank = dataRow.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK ? true:false;
									//System.out.println("isCustomerIdBlank >> "+isCustomerIdBlank+" :: isOrgNodeIdBlank >> "+isOrgNodeIdBlank);
									logger.info("Row No. ["+rowCtr+"] :: isCustomerIdBlank >> "+isCustomerIdBlank+" :: isOrgNodeIdBlank >> "+isOrgNodeIdBlank);
									//Condition to skip row for SF data object population if customer id or orgnode id is blank
									if(!isCustomerIdBlank && !isOrgNodeIdBlank){
										sfld = new SalesForceLicenseData();
					        		
										// For each row, loop through each column
										for ( int colCtr = 0 ; colCtr < totalHeaderColumns ; colCtr++  ) {
						        			//System.out.println("Column No. >> "+colCtr);
						        			headerCell = headerRow.getCell(colCtr);
						        			dataCell = dataRow.getCell(colCtr);
						        			if(dataCell != null)
						        			{
							                	//System.out.println("dataCell.getCellType() >> "+dataCell.getCellType());
								                switch (dataCell.getCellType()) 
								                {	
									                case Cell.CELL_TYPE_BOOLEAN:
									                		//Do nothing
									                        System.out.println(dataCell.getBooleanCellValue());
									                        break;
					
									                case Cell.CELL_TYPE_NUMERIC:
									                        //System.out.println(dataCell.getNumericCellValue());
									                        populateSFDataNumericColValue(sfld, dataCell, headerCell);
									                        break;
					
									                case Cell.CELL_TYPE_STRING:
									                        //System.out.println(dataCell.getStringCellValue());
									                        populateSFDataStrColValue(sfld, dataCell, headerCell);
									                        break;
					
									                case Cell.CELL_TYPE_BLANK:							                		
									                        //System.out.println(" ");
									                		populateSFDataBlankColValue(sfld, dataCell, headerCell);
									                        break;
					
									                default:
									                        System.out.println(dataCell);
									                        break;
								                }
						        			}
						                }
										
										sfLicenseDataList.add(sfld);
					        		}
								}
							}
			        	}
			            
			        }
		        }      	
	       
			} catch (FileNotFoundException e) {
				logger.error("FileNotFoundException : occurred while procesing :: Filename >> ["+fileName+"]");
				e.printStackTrace(); // unexpected				
			}catch (IOException e) {
				logger.error("IOException : occurred while procesing :: Filename >> ["+fileName+"]");
				e.printStackTrace();
			} finally {
				try {
					if(fileInputStream != null)
						fileInputStream.close();
				} catch (IOException e) {
					logger.error("IOException : occurred while closing file input stream.");
					e.printStackTrace();
				}
			}
			//System.out.println("Reading data from .xlsx file completed.");
			logger.info("Reading data from .xlsx file : Completed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		}  else {
			//System.out.println("File does not exists");
			logger.error("File does not exists :: Filename >> ["+fileName+"]");
		}	
		return sfLicenseDataList;
	}

	
	public static ArrayList<SalesForceLicenseData> readDataFromXLSFile(String fileName){
		File sfDataFile = new File(fileName);
		FileInputStream fileInputStream = null;		
		ArrayList<SalesForceLicenseData> sfLicenseDataList = null;
		SalesForceLicenseData sfld = null;		
		
		if(sfDataFile.exists()) {
			//System.out.println("Reading data from .xls file started.");
			logger.info("Reading data from .xls file : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			try {
				//read the file in to stream
				fileInputStream = new FileInputStream(sfDataFile);
				
				//Create Workbook instance holding reference to .xls file
		        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
		
		        //Get first/desired sheet from the workbook
		        HSSFSheet sheet = workbook.getSheetAt(0);			
		        sfLicenseDataList = new ArrayList<SalesForceLicenseData>(); 
		        
		        if(sheet != null){
		        	int totalRows = sheet.getPhysicalNumberOfRows();
		        	//System.out.println("Total no. of physical rows in file = "+ totalRows);
		        	logger.info("Total no. of physical rows in file = "+ totalRows);
	
		        	Row headerRow = sheet.getRow(0);
		        	Cell headerCell;
		        	Cell dataCell;
		        	if(headerRow == null){
		        		//System.out.println("No file header content found.") ;
		        		logger.info("No file header content found.");
		        	}
		        	else{
			        	int totalHeaderColumns = headerRow.getPhysicalNumberOfCells();
			        	//System.out.println("Total no. of header cells = "+ totalHeaderColumns);
			        	logger.info("Total no. of header cells = "+ totalHeaderColumns);
			        	
			        	for( int rowCtr = 1 ; rowCtr < totalRows ; rowCtr++) {
							//System.out.println("Row No. >> "+rowCtr);
							Row dataRow = sheet.getRow(rowCtr);	
							if (dataRow != null){
								int totalRowColumns = dataRow.getPhysicalNumberOfCells();
								//System.out.println("Total no. of current data row cells = "+ totalRowColumns);
								//logger.info("Total no. of current data row cells = "+ totalRowColumns);
								logger.info("Row No. ["+rowCtr+"] :: Header Column Count = ["+totalHeaderColumns+"] :: Current Data Row Column Count = ["+totalRowColumns+"]");
							//Discard dummy rows in spreadsheet if the count of row columns not equal to header columns
					        	if(totalHeaderColumns == totalRowColumns){								
									boolean isCustomerIdBlank = dataRow.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK ? true:false;
									boolean isOrgNodeIdBlank = dataRow.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK ? true:false;
									//System.out.println("isCustomerIdBlank >> "+isCustomerIdBlank+" :: isOrgNodeIdBlank >> "+isOrgNodeIdBlank);
									logger.info("Row No. ["+rowCtr+"] :: isCustomerIdBlank >> "+isCustomerIdBlank+" :: isOrgNodeIdBlank >> "+isOrgNodeIdBlank);
									//Condition to skip row for SF data object population if customer id or orgnode id is blank
									if(!isCustomerIdBlank && !isOrgNodeIdBlank){
										sfld = new SalesForceLicenseData();
					        		
										// For each row, loop through each column
										for ( int colCtr = 0 ; colCtr < totalHeaderColumns ; colCtr++  ) {
						        			//System.out.println("Column No. >> "+colCtr);
						        			headerCell = headerRow.getCell(colCtr);
						        			dataCell = dataRow.getCell(colCtr);
						        			if(dataCell != null)
						        			{
							                	//System.out.println("dataCell.getCellType() >> "+dataCell.getCellType());
								                switch (dataCell.getCellType()) 
								                {	
									                case Cell.CELL_TYPE_BOOLEAN:
									                		//Do nothing
									                        //System.out.println(dataCell.getBooleanCellValue());
									                        break;
					
									                case Cell.CELL_TYPE_NUMERIC:
									                        //System.out.println(dataCell.getNumericCellValue());
									                        populateSFDataNumericColValue(sfld, dataCell, headerCell);
									                        break;
					
									                case Cell.CELL_TYPE_STRING:
									                        //System.out.println(dataCell.getStringCellValue());
									                        populateSFDataStrColValue(sfld, dataCell, headerCell);
									                        break;
					
									                case Cell.CELL_TYPE_BLANK:
									                		populateSFDataBlankColValue(sfld, dataCell, headerCell);
									                        break;
					
									                default:
									                        System.out.println(dataCell);
									                        break;
								                }
						        			}
						                }
										
										sfLicenseDataList.add(sfld);
									}	
				        		}
							}	
			        	}
			            
			        }
		        }      	
	       
			} catch (FileNotFoundException e) {
				logger.error("FileNotFoundException : occurred while procesing :: Filename >> ["+fileName+"]");
				e.printStackTrace(); // unexpected
			}catch (IOException e) {
				logger.error("IOException : occurred while procesing :: Filename >> ["+fileName+"]");
				e.printStackTrace();
			} finally {
				try {
					if(fileInputStream != null)
						fileInputStream.close();
				} catch (IOException e) {
					logger.error("IOException : occurred while closing file input stream.");
					e.printStackTrace();
				}
			}
			//System.out.println("Reading data from .xls file completed.");
			logger.info("Reading data from .xls file : Completed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		}  else {
			//System.out.println("File does not exists");
			logger.error("File does not exists :: Filename >> ["+fileName+"]");
		}	
		return sfLicenseDataList;
	}
	
	public static void populateSFDataStrColValue(SalesForceLicenseData sfld, Cell dataCell, Cell headerCell){
		//TODO:: Set sf data object from string column value
		String cellVal = dataCell.getStringCellValue().trim();
		
		if (headerCell.getStringCellValue().equals(Constants.CUSTOMER_ID))
			sfld.setCustomerId(Integer.parseInt(cellVal));
		else if((headerCell.getStringCellValue().equals(Constants.OAS_IMPLEMENTATION_ID)))
			sfld.setOasImplementationId(getStrValWithDesiredLen(cellVal, Constants.OAS_IMPLEMENTATION_ID_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.IMPL_RECORD_TYPE)))
			sfld.setImplRecordType(getStrValWithDesiredLen(cellVal, Constants.IMPL_RECORD_TYPE_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CUSTOMER_ACCOUNT_NAME)))
			sfld.setCustomerAccountName(getStrValWithDesiredLen(cellVal, Constants.CUSTOMER_ACCOUNT_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.ACCOUNT_STATE)))
			sfld.setAccountState(getStrValWithDesiredLen(cellVal, Constants.ACCOUNT_STATE_SIZE)); // Need to restrict value to 2 characters only
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_ID)))
			sfld.setOrgNodeId(Integer.parseInt(cellVal));
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_NAME)))
			sfld.setOrgNodeName(getStrValWithDesiredLen(cellVal, Constants.ORG_NODE_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_PHONE)))
			sfld.setContactPhone(getStrValWithDesiredLen(cellVal, Constants.CONTACT_PHONE_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT)))
			sfld.setContact(getStrValWithDesiredLen(cellVal, Constants.CONTACT_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_EMAIL)))
			sfld.setContactEmail(getStrValWithDesiredLen(cellVal, Constants.CONTACT_EMAIL_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_NAME)))
			sfld.setCategoryName(getStrValWithDesiredLen(cellVal, Constants.CATEGORY_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_LEVEL)))
			sfld.setCategoryLevel(Integer.parseInt(cellVal));
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_MODEL)))
			sfld.setLicenseModel(getStrValWithDesiredLen(cellVal, Constants.LICENSE_MODEL_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_COUNT)))
			sfld.setLicenseCount(Integer.parseInt(cellVal));
		else if((headerCell.getStringCellValue().equals(Constants.ORDER_QUANTITY)))
			sfld.setOrderQuantity(Integer.parseInt(cellVal));
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_DISTRIBUTED_TO)))
			sfld.setLicenseDistributedTo(getStrValWithDesiredLen(cellVal, Constants.LICENSE_DISTRIBUTED_TO_SIZE));		
		else if((headerCell.getStringCellValue().equals(Constants.CREATED_DATE)))
			sfld.setCreatedDate(dataCell.getDateCellValue());
		else if((headerCell.getStringCellValue().equals(Constants.INTERVAL_NAME)))
			sfld.setIntervalName(getStrValWithDesiredLen(cellVal, Constants.INTERVAL_NAME_SIZE));
			
	}
	
	public static void populateSFDataNumericColValue(SalesForceLicenseData sfld, Cell dataCell, Cell headerCell){
		//TODO:: Set sf data object from numeric column value
		Double cellVal = dataCell.getNumericCellValue();
		if (headerCell.getStringCellValue().equals(Constants.CUSTOMER_ID))
			sfld.setCustomerId(cellVal.intValue());
		else if((headerCell.getStringCellValue().equals(Constants.OAS_IMPLEMENTATION_ID)))
			sfld.setOasImplementationId(getStrValWithDesiredLen(cellVal.toString(), Constants.OAS_IMPLEMENTATION_ID_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.IMPL_RECORD_TYPE)))
			sfld.setImplRecordType(getStrValWithDesiredLen(cellVal.toString(), Constants.IMPL_RECORD_TYPE_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CUSTOMER_ACCOUNT_NAME)))
			sfld.setCustomerAccountName(getStrValWithDesiredLen(cellVal.toString(), Constants.CUSTOMER_ACCOUNT_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.ACCOUNT_STATE)))
			sfld.setAccountState(getStrValWithDesiredLen(cellVal.toString(), Constants.ACCOUNT_STATE_SIZE));// Need to restrict value to 2 characters only
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_ID)))
			sfld.setOrgNodeId(cellVal.intValue());
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_NAME)))
			sfld.setOrgNodeName(getStrValWithDesiredLen(cellVal.toString(), Constants.ORG_NODE_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_PHONE)))
			sfld.setContactPhone(getStrValWithDesiredLen(cellVal.toString(), Constants.CONTACT_PHONE_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT)))
			sfld.setContact(getStrValWithDesiredLen(cellVal.toString(), Constants.CONTACT_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_EMAIL)))
			sfld.setContactEmail(getStrValWithDesiredLen(cellVal.toString(), Constants.CONTACT_EMAIL_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_NAME)))
			sfld.setCategoryName(getStrValWithDesiredLen(cellVal.toString(), Constants.CATEGORY_NAME_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_LEVEL)))
			sfld.setCategoryLevel(cellVal.intValue());
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_MODEL)))
			sfld.setLicenseModel(getStrValWithDesiredLen(cellVal.toString(), Constants.LICENSE_MODEL_SIZE));
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_COUNT)))
			sfld.setLicenseCount(cellVal.intValue());
		else if((headerCell.getStringCellValue().equals(Constants.ORDER_QUANTITY)))
			sfld.setOrderQuantity(cellVal.intValue());
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_DISTRIBUTED_TO)))
			sfld.setLicenseDistributedTo(getStrValWithDesiredLen(cellVal.toString(), Constants.LICENSE_DISTRIBUTED_TO_SIZE));		
		else if((headerCell.getStringCellValue().equals(Constants.CREATED_DATE)))
			sfld.setCreatedDate(dataCell.getDateCellValue());
		else if((headerCell.getStringCellValue().equals(Constants.INTERVAL_NAME)))
			sfld.setIntervalName(getStrValWithDesiredLen(cellVal.toString(), Constants.INTERVAL_NAME_SIZE));
			
	}
	
	public static void populateSFDataBlankColValue(SalesForceLicenseData sfld, Cell dataCell, Cell headerCell){
		//TODO:: Set sf data object with blank column value
		String cellVal = " ";
		if (headerCell.getStringCellValue().equals(Constants.CUSTOMER_ID))
			sfld.setCustomerId(new Integer(0));
		else if((headerCell.getStringCellValue().equals(Constants.OAS_IMPLEMENTATION_ID)))
			sfld.setOasImplementationId(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.IMPL_RECORD_TYPE)))
			sfld.setImplRecordType(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CUSTOMER_ACCOUNT_NAME)))
			sfld.setCustomerAccountName(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.ACCOUNT_STATE)))
			sfld.setAccountState(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_ID)))
			sfld.setOrgNodeId(new Integer(0));
		else if((headerCell.getStringCellValue().equals(Constants.ORG_NODE_NAME)))
			sfld.setOrgNodeName(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_PHONE)))
			sfld.setContactPhone(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT)))
			sfld.setContact(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CONTACT_EMAIL)))
			sfld.setContactEmail(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_NAME)))
			sfld.setCategoryName(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.CATEGORY_LEVEL)))
			sfld.setCategoryLevel(null);
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_MODEL)))
			sfld.setLicenseModel(cellVal);
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_COUNT)))
			sfld.setLicenseCount(null);
		else if((headerCell.getStringCellValue().equals(Constants.ORDER_QUANTITY)))
			sfld.setOrderQuantity(null);
		else if((headerCell.getStringCellValue().equals(Constants.LICENSE_DISTRIBUTED_TO)))
			sfld.setLicenseDistributedTo(cellVal);		
		else if((headerCell.getStringCellValue().equals(Constants.CREATED_DATE)))
			sfld.setCreatedDate(dataCell.getDateCellValue());
		else if((headerCell.getStringCellValue().equals(Constants.INTERVAL_NAME)))
			sfld.setIntervalName(cellVal);
			
	}
	
	public static String getStrValWithDesiredLen(String str, int desiredlen){
		return (str.length() > desiredlen ? str.substring(0, desiredlen) : str);
	}
}
	

