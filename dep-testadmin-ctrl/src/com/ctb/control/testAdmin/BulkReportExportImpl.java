package com.ctb.control.testAdmin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Threading;

import com.ctb.bean.testAdmin.LiteracyProExportRequest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.BulkReportExportException;
import com.ctb.util.LiteracyExportProcessor;

/**
 * Platform control provides functions related to Bulk report export
 * 
 * @author TCS
 * 
 */
@Threading(org.apache.beehive.controls.api.bean.ThreadingPolicy.MULTI_THREADED)
@ControlImplementation(isTransient = true)
public class BulkReportExportImpl implements BulkReportExport {

    static final long serialVersionUID = 1L;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.TestAdmin admins;

    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.ImmediateReportingIrs reports;

    /**
     * Insert export request in database and return the generated export id
     * 
     * @param literacyRequestObj
     * @return
     * @throws CTBBusinessException
     */
    @Override
    public LiteracyProExportRequest insertBulkReportData(Map<String, Object> paramMap) throws CTBBusinessException {
	System.out.println("Start: BulkReportExportImpl.insertBulkReportData()");
	long startTime = System.currentTimeMillis();
	System.out.println(paramMap);
	Long exportRequestId = null;
	try {
	    Integer customerId = (Integer) paramMap.get("customerId");
	    Integer userId = (Integer) paramMap.get("userId");
	    String todayOfUserTimeZone = (String) paramMap.get("todayOfUserTimeZone");
	    Map<String, String> orgHierarchyMap = (HashMap<String, String>) paramMap.get("orgHierarchyMap");
	    String status = "In Progress";
	    String orgNodeId = getOrgNodeId(orgHierarchyMap);
	    String msg = "Requested process is in progress.";
	    msg = msg + "\n" + paramMap.get("msg");
	    String exportedDate = todayOfUserTimeZone;
	    //System.out.println("userId: " + userId);
	    //System.out.println("customerId: " + customerId);
	    //System.out.println("exportedDate: " + exportedDate);
	    //System.out.println("orgNodeId: " + orgNodeId);
	    //System.out.println("status: " + status);
	    //System.out.println("msg: " + msg);
	    admins.deleteBulkReportRequestData(customerId, userId);
	    //System.out.println("Table cleaned in " + (System.currentTimeMillis() - startTime) + " milliseconds");
	    long insertStart = System.currentTimeMillis();
	    LiteracyProExportRequest literacyRequestObj = new LiteracyProExportRequest(userId, customerId, exportedDate, Integer.parseInt(orgNodeId), status, msg);
	    exportRequestId = admins.getExportSequenceId();
	    literacyRequestObj.setExportRequestId(exportRequestId);
	    String dateFormat = "ddMMyyyyHHmmss";
	    String userTimeZone = (String) paramMap.get("userTimeZone");
	    String nowStr = com.ctb.util.DateUtils.getAdjustedTodayString(userTimeZone, dateFormat);
	    literacyRequestObj.setFileName("_" + nowStr + ".csv");
	    admins.insertBulkReportRequestData(literacyRequestObj);
	    //System.out.println("Record inserted in " + (System.currentTimeMillis() - insertStart) + " milliseconds");
	    System.out.println("End: BulkReportExportImpl.insertBulkReportData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	    return literacyRequestObj;
	} catch (SQLException se) {
	    BulkReportExportException bre = new BulkReportExportException("BulkReportExportImpl: insertBulkReportData() : " + se.getMessage());
	    bre.setStackTrace(se.getStackTrace());
	    throw bre;
	}
    }

    /**
     * Returns the bulk report csv file download data in a two dimentional
     * ArrayList
     * 
     * @param paramMap
     * @return
     * @throws CTBBusinessException
     */
    @Override
    public LiteracyProExportRequest getBulkReportCSVData(Map<String, Object> paramMap) throws CTBBusinessException {
	System.out.println("Start: BulkReportExportImpl.getBulkReportCSVData()");
	long startTime = System.currentTimeMillis();
	System.out.println(paramMap);
	Integer customerId = (Integer) paramMap.get("customerId");
	Integer userId = (Integer) paramMap.get("userId");
	Long exportReqId = (Long) paramMap.get("exportRequestId");
	try {
	    LiteracyProExportRequest literacyRequestObj = admins.getBulkExportReport(customerId, userId, exportReqId);
	    return literacyRequestObj;
	} catch (SQLException se) {
	    BulkReportExportException bre = new BulkReportExportException("BulkReportExportImpl: getBulkReportCSVData : " + se.getMessage());
	    bre.setStackTrace(se.getStackTrace());
	    throw bre;
	} finally {
	    System.out.println("End: BulkReportExportImpl.getBulkReportCSVData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
    }

    private String getOrgNodeId(Map<String, String> orgHierarchyMap) {
	String orgNodeId = "";
	String maxKey = "0";
	for (Map.Entry<String, String> entry : orgHierarchyMap.entrySet()) {
	    String key = entry.getKey();
	    if (Integer.parseInt(maxKey) < Integer.parseInt(key)) {
		maxKey = new String(key);
		String value = entry.getValue();
		if (!"-1".equals(value)) {
		    orgNodeId = value;
		}
	    }
	}
	return orgNodeId;
    }

    /**
     * bulk report export process to generate export csv file
     * 
     * @param paramMap
     * @throws CTBBusinessException
     */
    @Override
    public void bulkReportExportProcess(final Map<String, Object> paramMap) throws CTBBusinessException {
	System.out.println("Start: BulkReportExportImpl.bulkReportExportProcess()");
	long startTime = System.currentTimeMillis();
	System.out.println(paramMap);
	ExecutorService executor = Executors.newSingleThreadExecutor();
	Thread thread = new Thread(new LiteracyExportProcessor(paramMap));
	executor.execute(thread);
	executor.shutdown();
	System.out.println("End: BulkReportExportImpl.bulkReportExportProcess(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

    @Override
    public LiteracyProExportRequest[] getSumittedExportDetails(Map<String, Object> paramMap) throws CTBBusinessException {
	System.out.println("Start: BulkReportExportImpl.getSumittedExportDetails()");
	long startTime = System.currentTimeMillis();
	System.out.println(paramMap);
	Integer customerId = (Integer) paramMap.get("customerId");
	Integer userId = (Integer) paramMap.get("userId");
	try {
	    LiteracyProExportRequest[] literacyRequestArr = admins.getBulkExportReportDetailsForUser(customerId, userId);
	    for(LiteracyProExportRequest bean : literacyRequestArr) {
		System.out.println(bean);
	    }
	    return literacyRequestArr;
	} catch (SQLException se) {
	    BulkReportExportException bre = new BulkReportExportException("BulkReportExportImpl: getSumittedExportDetails : " + se.getMessage());
	    bre.setStackTrace(se.getStackTrace());
	    throw bre;
	} finally {
	    System.out.println("End: BulkReportExportImpl.getSumittedExportDetails(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
    }

}
