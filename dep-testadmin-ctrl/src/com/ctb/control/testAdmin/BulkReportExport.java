package com.ctb.control.testAdmin;

import java.util.Map;

import org.apache.beehive.controls.api.bean.ControlInterface;

import com.ctb.bean.testAdmin.LiteracyProExportRequest;
import com.ctb.exception.CTBBusinessException;

/**
 * Platform control provides functions related to Bulk report export
 * @author TCS
 *
 */
@ControlInterface()
public interface BulkReportExport {

	
	/**
	 * Insert export request in database and return the generated export id
	 * @param literacyRequestObj
	 * @return
	 * @throws CTBBusinessException
	 */
	public LiteracyProExportRequest insertBulkReportData(Map<String, Object> paramMap) throws CTBBusinessException;
	
	/**
	 * bulk report export process to generate export csv file
	 * @param paramMap
	 * @throws CTBBusinessException
	 */
	public void bulkReportExportProcess(Map<String, Object> paramMap) throws CTBBusinessException;
	
	/**
	 * Returns the bulk report csv file download data in a two dimentional ArrayList
	 * @param paramMap
	 * @return
	 * @throws CTBBusinessException
	 */
	public LiteracyProExportRequest getBulkReportCSVData(Map<String, Object> paramMap) throws CTBBusinessException;

	
	/**
	 * 
	 * @param paramMap
	 * @return
	 * @throws CTBBusinessException
	 */
	public LiteracyProExportRequest[] getSumittedExportDetails(Map<String, Object> paramMap) throws CTBBusinessException;
	
	
	/**
	 * Bulk Report download process
	 * @param paramMap
	 * @throws CTBBusinessException
	 */
	public void downloadBulkReportProcess(Map<String, Object> paramMap) throws CTBBusinessException;
	
	
}
