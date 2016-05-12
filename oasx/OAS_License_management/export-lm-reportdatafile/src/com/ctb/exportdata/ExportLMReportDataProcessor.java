package com.ctb.exportdata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import au.com.bytecode.opencsv.CSVWriter;

import com.ctb.bean.LicenseMergedReportData;
import com.ctb.utils.Configuration;
import com.ctb.utils.Constants;
import com.ctb.utils.DBUtil;
import com.ctb.utils.ExtractUtil;


//import com.opencsv.CSVWriter;
//import com.opencsv.bean.BeanToCsv;
//import com.opencsv.bean.ColumnPositionMappingStrategy;

/**
 * This Class is entry point to this Utility
 * 
 * @author TCS
 * 
 */

public class ExportLMReportDataProcessor {
	
	static Logger logger = Logger.getLogger(ExportLMReportDataProcessor.class.getName());
	
	/**
	 * This is starting point for LM Merged Data Report export process: method accepts single command-line argument denoting the Properties file Name
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<LicenseMergedReportData> reportDataList = null;
		String filePath = "";
		String productName = "";
		String absoluteFilePath = "";
		
		String envName = getPropFileFromCommandLine(args);
		ExtractUtil.loadExternalPropetiesFile(envName, args[1]);
		PropertyConfigurator.configure(Configuration.getLog4jFile());
		logger.info("Properties file successfully loaded for environment >> "+ envName);
		filePath = Configuration.getLocalFilePath();		
		productName = Configuration.getProductName().toUpperCase();
		
		logger.info("LM Merged Report Data Export Process:: Started for :: Product >> ["+productName+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		
		//TODO: Fetch LM Merged Report Data from database	
		logger.info("LM Merged Report Data Export Process:: Data Fetch : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		reportDataList = DBUtil.fetchLMReportDataByProduct(productName);
		logger.info("LM Merged Report Data Export Process:: Data Fetch : Completed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		//reportDataList = DBUtil.fetchLMReportData();
		//System.out.println(reportDataList.size());
		
		//TODO: Write LM Merged Report Data into CSV file at specified location
		if(reportDataList !=null && reportDataList.size() > 0){	
			absoluteFilePath = filePath.concat(getLMExportFileName(productName));
			//System.out.println(absoluteFilePath);
			logger.info("LM Merged Report Data Export Process:: Writing Data into CSV file : Started :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			writeDataIntoCSV(absoluteFilePath, reportDataList);	
			logger.info("LM Merged Report Data Export Process:: Writing Data into CSV file : Completed :: Timestamp >> "+ new Date(System.currentTimeMillis()));
			/*try {
				writeDataUsingBeanToCSV(absoluteFilePath, reportDataList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}else{
			//System.out.println("No records found in lm merged report data table for product = ["+productName+"]");
			logger.info("No records found in lm merged report data table for product >> ["+productName+"]");
		}
		logger.info("LM Merged Report Data Export Process:: Completed for :: Product >> ["+productName+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
	}
	

	/**
	 * This method is used to generate LM Merged Report Data export file name based on product name
	 * [File Name convention is - LT_EXP_{Product Name}_{Timestamp in format 'MM.dd.yyyy HH-mm-ss'}]
	 * 
	 * @param productName
	 * @return String
	 */
	public static String getLMExportFileName(String productName){
		String timestamp = getFileTimestamp();
		String fileName = Constants.FILE_NAME_PREFIX + Constants.FILE_NAME_DELIMITER + productName + Constants.FILE_NAME_DELIMITER + timestamp + Constants.FILE_EXTN;
		//System.out.println("File name >> "+fileName);
		logger.info("Generated File Name >> ["+fileName+"]");
		return fileName;
	}
	
	/**
	 * This method is used to generate timestamp in format 'MM.dd.yyyy HH-mm-ss'
	 * @return String
	 */
	public static String getFileTimestamp(){
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FILE_TIMESTAMP_FORMAT);
		String timeStamp = sdf.format(currentDate);
		
		return timeStamp;
	}
	

	/**
	 * This method lm report data into a CSV file at specified location
	 * [Jar dependencies : opencsv-2.4.jar]
	 * @param filePath - absolute path of the file
	 * @param lmrdList - array list of report data bean
	 * @return void
	 */
	private static void writeDataIntoCSV(String filePath, ArrayList<LicenseMergedReportData> lmrdList){
		BufferedWriter bWriter;
		CSVWriter csvWriter;
		logger.info("LM Merged Report Data :: Exported for merged_seq_num = ["+lmrdList.get(0).getMergedSeqNum()+"]");
		logger.info("Total number of data rows (excluding header row) = ["+lmrdList.size()+"]");
		try{
			bWriter = new BufferedWriter(new FileWriter(filePath));
			csvWriter = new CSVWriter(bWriter);
			//Write header row
			String [] rowHeader = new String[]{
										Constants.SF_CUSTOMER_ID, 
										Constants.OAS_CUSTOMER_ID, 
										Constants.OAS_CONTACT_NAME, 
										Constants.OAS_CONTACT_PHONE, 
										Constants.OAS_CONTACT_EMAIL, 
										Constants.OAS_STATE_PR_DESC, 
										Constants.OAS_STATE_PR, 
										Constants.OAS_EXT_CUSTOMER_ID,
										Constants.OAS_PRODUCT_NAME,
										Constants.SF_OAS_IMPL_CONTACT_NAME,
										Constants.SF_OAS_IMPL_CONTACT_PHONE,
										Constants.SF_OAS_IMPL_EMAIL,
										Constants.SF_CUSTOMER_ACCOUNT_NAME,
										Constants.SF_ACCOUNT_STATE,
										Constants.SF_ORG_NODE_ID,
										Constants.SF_ORG_NODE_NAME,
										Constants.SF_CATEGORY_NAME,
										Constants.SF_CATEGORY_LEVEL,
										Constants.OAS_ORG_NODE_ID,
										Constants.OAS_ORG_NODE_NAME,
										Constants.OAS_ORG_NODE_CATEGORY_NAME,
										Constants.OAS_ORG_NODE_CATEGORY_LEVEL,
										Constants.OAS_PARENT_ORG_NODE_ID,
										Constants.OAS_PARENT_ORG_NODE_NAME,
										Constants.SF_LICENSE_MODEL,
										Constants.OAS_SUBTEST_MODEL,
										Constants.SF_LICENSE_COUNT,
										Constants.SF_ORDER_QUANTITY,
										Constants.SF_LICENSE_DISTRIBUTED_TO,
										Constants.SF_CREATED_DATE,
										Constants.SF_INTERVAL_NAME,
										Constants.LT_LOAD_DATE_TIME,
										Constants.LT_LOAD_SEQ_NUM,
										Constants.OAS_LIC_AVL_IN_DB,
										Constants.OAS_CUM_LIC_AVL_IN_DB,
										Constants.OAS_LIC_TO_BE_RELEASED,
										Constants.OAS_CUM_LIC_TO_BE_RELEASED,
										Constants.OAS_LIC_RESERVED_AT_NODE,
										Constants.OAS_CUM_LIC_RESERVED,
										Constants.OAS_LIC_CONSUMED_AT_NODE,
										Constants.OAS_CUM_LIC_CONSUMED,
										Constants.OAS_NODE_LVL_AVAILABLE,
										Constants.OAS_CUM_AVAILABLE,
										Constants.OAS_NODE_NET_AVAILABLE,
										Constants.OAS_CUM_NET_AVAILABLE,
										Constants.OAS_LIC_MOD_CONSUMED_AT_NODE, 
										Constants.OAS_CUM_LIC_MOD_CONSUMED,
										Constants.OAS_LIC_MOD_RESERVED_AT_NODE,
										Constants.OAS_CUM_LIC_MOD_RESERVED,
										Constants.OAS_ACTIVATION_STATUS,
										Constants.OAS_EXTRACT_DATE_TIME,
										Constants.LT_MERGED_DATE_TIME,
										Constants.LT_MERGED_SEQ_NUM,
										Constants.LT_EXPORT_DATE_TIME,
										Constants.LT_EXTRACT_SEQ_NUM
										};	
			csvWriter.writeNext(rowHeader);
			logger.info("Total no. of header fields = ["+rowHeader.length+"]");
			//Write data rows
			String [] rowData;
			int rowDataCtr = 1;
			for(LicenseMergedReportData lmrd: lmrdList){
				rowData = new String[]{
								lmrd.getSfCustomerId(), 
								lmrd.getOasCustomerId(), 
								lmrd.getOasContactName(), 
								lmrd.getOasContactPhone(), 
								lmrd.getOasContactEmail(),
								lmrd.getOasStatePRDesc(),
								lmrd.getOasStatePR(),
								lmrd.getOasExtCustomerId(),
								lmrd.getOasProductName(),
								lmrd.getSfContactName(),
								lmrd.getSfContactPhone(),
								lmrd.getSfContactEmail(),
								lmrd.getSfCustomerActName(),
								lmrd.getSfActState(),
								lmrd.getSfOrgNodeId(),
								lmrd.getSfOrgNodeName(),
								lmrd.getSfCategoryName(),
								lmrd.getSfCategoryLevel(),
								lmrd.getOasOrgNodeId(),
								lmrd.getOasOrgNodeName(),
								lmrd.getOasOrgNodeCategoryName(),
								lmrd.getOasOrgNodeCategoryLevel(),
								lmrd.getOasParentOrgNodeId(),
								lmrd.getOasParentOrgNodeName(),
								lmrd.getSfLicenseModel(),
								lmrd.getOasLicenseModel(),
								lmrd.getSfLicenseCount(),
								lmrd.getSfOrderQuantity(),
								lmrd.getSfLicenseDistributedTo(),
								lmrd.getSfCreatedDate(),
								lmrd.getSfIntervalName(),
								lmrd.getSfLoadDateTime(),
								lmrd.getSfLoadSeqNum(),
								lmrd.getOasLicAvlInDb(),
								lmrd.getOasCumLicAvlInDb(),
								lmrd.getOasLicToBeReleased(),
								lmrd.getOasCumLicToBeReleased(),
								lmrd.getOasLicReservedAtNode(),
								lmrd.getOasCumLicReserved(),
								lmrd.getOasLicConsumedAtNode(),
								lmrd.getOasCumLicConsumed(),
								lmrd.getOasNodeLvlAvailable(),
								lmrd.getOasCumAvailable(),
								lmrd.getOasNodeNetAvailable(),
								lmrd.getOasCumNetAvailable(),
								lmrd.getOasLicModConsumedAtNode(),
								lmrd.getOasCumLicModConsumed(),
								lmrd.getOasLicModReservedAtNode(),
								lmrd.getOasCumLicModReserved(),
								lmrd.getOasActivationStatus(),
								lmrd.getOasExtractDateTime(),
								lmrd.getMergedDateTime(),
								lmrd.getMergedSeqNum(),
								lmrd.getLtExportDateTime(),
								lmrd.getOasExtractSeqNum()
								};
				csvWriter.writeNext(rowData);
				logger.info("Row Data No.["+rowDataCtr+"] : Fields count = ["+rowData.length+"]");
				rowDataCtr++;
			}
			bWriter.close();
			csvWriter.close();
			logger.info("LM Merged Report Data exported into CSV file >> ["+filePath+"] :: Timestamp >> "+ new Date(System.currentTimeMillis()));
		}catch(IOException ioe){
			// TODO Auto-generated catch block
			logger.error("Error occurred while writing data into .csv file.");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * This method will generate CSV file from a List of Java Bean object using Bean2Csv API: Need to keep bean member's name same as that of header names else BeanToCsv will not work
	 * [Jar dependencies : opencsv-3.7.jar, commons-lang3-3.4.jar]
	 * Java dependency: Java 7 and above (can't use this API as Dagobah server where the application will be deployed have Java 6 installed : Need to upgrade)
	 * @param : filePath - absolute path of the file, 
	 * 			lmrdList - array list of report data bean 
	 * @return void
	 */
	
	/*private static void writeDataUsingBeanToCSV(String filePath, ArrayList<LicenseMergedReportData> lmrdList) throws IOException{
		CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath+"BeanToCSVTest.csv"));
		ColumnPositionMappingStrategy<LicenseMergedReportData> mappingStrategy = new ColumnPositionMappingStrategy<LicenseMergedReportData>();
		mappingStrategy.setType(LicenseMergedReportData.class);
		
		String[] columns = new String[]{"sfCustomerId", "oasCustomerId", "oasContactName", "oasContactPhone", "sfOrgNodeName"};
		mappingStrategy.setColumnMapping(columns);
		
		BeanToCsv<LicenseMergedReportData> bean2CSV = new BeanToCsv<LicenseMergedReportData>();
		
		bean2CSV.write(mappingStrategy, csvWriter, lmrdList);
		csvWriter.close();
		System.out.println("CSV File written successfully!!!");
	}*/
	
	private static String getPropFileFromCommandLine(String[] args) {
		String envName = "";
		String usage = "Usage:\n 	java -jar export-lm-reportdatafile.jar <properties file name>";
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

}
