package com.ctb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ffpojo.exception.FFPojoException;

import com.ctb.utils.Configuration;
import com.ctb.utils.EmetricUtil;
import com.ctb.utils.ExtractUtil;
/**
 * Entry point of this utility. Used for properties file data validation and start execution. 
 * @author TCS
 *
 */
public class ExportData {

	static Logger logger = Logger.getLogger(ExportData.class.getName());
	
	/**
	 * Entry point of this utility. Two arguments are needed. 
	 * 1. properties file name and 2. properties file absolute path
	 * 
	 * @param args (args[0]- Properties file name & args[1]- Properties file absolute path)
	 */
	public static void main(String[] args) {
		if(args.length == 0)
			throw new IllegalArgumentException();
		ExtractUtil.loadExternalProperties(args[0], args[1]);
		PropertyConfigurator.configure(Configuration.getLog4jFile());
		
		/**
		 * Initialization of objects
		 */
		String extractSpanStartDate = Configuration.getSessionStartDate();
		String extractSpanEndDate = Configuration.getSessionEndDate();
		String MFid = Configuration.getMfID();
		String customerIdStr = Configuration.getCustomerId();
		String frameworkProductId = Configuration.getFrameWorkProductId();
		String localPath = Configuration.getLocalFilePath();
		Integer customerId = new Integer(0);
		boolean isValidStartDate = false;
		boolean isValidEndDate = false;
		Long time = System.currentTimeMillis();
		
		try {
			logger.info("Execution started :: "+new Date());
			//Checking customer ID
			if ("".equals(customerIdStr) || customerIdStr.trim().length() == 0){
				logger.error("Customer Id field is madatory.\nExecution forcefully stopped.");
				System.exit(1);
			}else if (!EmetricUtil.checkIntegerValue(customerIdStr)) {
				logger.error("Customer Id field is madatory and should be numeric. \nExecution forcefully stopped.");
				System.exit(1);
			} else{
				customerId = Integer.parseInt(customerIdStr);
				logger.info("Processing started for customer Id :: " + customerId);
			}
			
			//Checking local file path
			if ("".equals(localPath) || localPath.trim().length() == 0){
				logger.error("\"oas.exportdata.filepath\" field is madatory. \nExecution forcefully stopped.");
				System.exit(1);
			} else if (!EmetricUtil.validLocalPath(localPath)){
				logger.error("Please provide valid oas.exportdata.filepath in property file \nExecution forcefully stopped.");
				System.exit(1);
			}

			// Checking MF id
			if ("".equals(MFid) || MFid.trim().length() == 0) {
				logger.error("Please provide valid MF id in property file. \nExecution forcefully stopped.");
				System.exit(1);
			} else if (MFid.trim().length() != 10) {
				logger.error("Please provide valid MF id in property file. Length must be 10 charater.\nExecution forcefully stopped.");
				System.exit(1);
			} else {
				logger.info("MF ID :: " + MFid.trim());
				// Continue...
			}

			// Checking start_date
			if (null == extractSpanStartDate || "".equals(extractSpanStartDate)) {
				logger.info("Optional Start_Date field is blank.");
				logger.info("Continuing...");
			} else if (!EmetricUtil.validateDateString(extractSpanStartDate)) {
				logger.error("Please provide valid span start date format (MM/DD/YYYY) or provide blank. \nExecution forcefully stopped.");
				System.exit(1);
			} else {
				isValidStartDate = true;
				logger.info("Start Date (MM/DD/YYYY) Of Extract Date Span : "
								+ extractSpanStartDate);
			}

			// Checking end_date
			if (null == extractSpanEndDate || "".equals(extractSpanEndDate)) {
				logger.info("Optional End_Date field is blank.");
				logger.info("Continuing...");
			} else if (!EmetricUtil.validateDateString(extractSpanEndDate)) {
				logger.error("Please provide valid span end date format (MM/DD/YYYY) or provide blank. \nExecution forcefully stopped.");
				System.exit(1);
			} else {
				isValidEndDate = true;
				logger.info("End Date (MM/DD/YYYY) Of Extract Date Span : "
								+ extractSpanEndDate);
			}

			// Checking product_id
			if (null == frameworkProductId || "".equals(frameworkProductId)) {
				logger.error("Framework Product Id field is madatory. \nExecution forcefully stopped.");
				System.exit(1);
			} else {
				if (Integer.parseInt(frameworkProductId) == 7500) {
					/*new CreateFiles2ndEdition(isValidStartDate, isValidEndDate,
							extractSpanStartDate, extractSpanEndDate,
							customerId, new Integer(frameworkProductId), MFid).writeToText();*/
					logger.error("Sorry !! LAS Links 2nd Edition will be coming soon. Change the framework product ID\n");
					System.exit(1);
				} else if (Integer.parseInt(frameworkProductId) == 7000) {
					new CreateFile(isValidStartDate, isValidEndDate,
							extractSpanStartDate, extractSpanEndDate,
							customerId, new Integer(frameworkProductId), MFid).writeToText();
				} else {
					logger.error("Framework Product Id is invalid. \nExecution forcefully stopped.");
					System.exit(1);
				}
			}

			logger.info("END !");
			logger.info("Total time taken :: "+((new Long(System.currentTimeMillis())).longValue()-time) + "ms \n");
		} catch (IOException e) {
			logger.error(e.getMessage() + "Processing Failed.\n");
			e.printStackTrace();
		} catch (FFPojoException e) {
			logger.error(e.getMessage() + "Processing Failed.\n");
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e.getMessage() + "Processing Failed.\n");
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage() + "Processing Failed.\n");
			e.printStackTrace();
		}
	}
	
}
