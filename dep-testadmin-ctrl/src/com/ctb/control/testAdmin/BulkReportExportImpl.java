package com.ctb.control.testAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Threading;

import com.ctb.bean.testAdmin.LiteracyProExportRequest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.BulkReportExportException;
import com.ctb.util.LiteracyDownloadProcess;
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
    
    
    
    private static final String oasDtataSourceJndiName = "oasDataSource";
    private Connection oasCon;
    private static String GET_FILE_CONTENT = " select FILE_CONTENT as fileContent , FILE_NAME as fileName from BULK_EXPORT_DATA_FILE where EXPORT_REQUEST_ID = ? ";
    
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
	Long exportReqId = (Long) paramMap.get("exportRequestId");
	try {
	    LiteracyProExportRequest literacyRequestObj = getFileContent(exportReqId);
	    return literacyRequestObj;
	} catch (BulkReportExportException se) {
	    BulkReportExportException bre = new BulkReportExportException("BulkReportExportImpl: getBulkReportCSVData : " + se.getMessage());
	    bre.setStackTrace(se.getStackTrace());
	    throw bre;
	} finally {
	    System.out.println("End: BulkReportExportImpl.getBulkReportCSVData(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
    }
    
    
    
    /**
     * JDBC Calls 
     * 
     **/
    
    private LiteracyProExportRequest getFileContent(Long exportRequestId) throws BulkReportExportException {
	PreparedStatement ps = null;
	ResultSet rset = null;
	LiteracyProExportRequest fileContent = null;
	try {	this.oasCon = openOASDBcon(false);
		ps = oasCon.prepareStatement(GET_FILE_CONTENT);
		ps.setLong(1, exportRequestId);
		rset = ps.executeQuery();
		while(rset.next()){
			fileContent = new LiteracyProExportRequest();
			fileContent.setFileContent(rset.getBlob(1));
			fileContent.setFileName(rset.getString("fileName"));
		}
	} catch (SQLException se) {
		BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getFileContent : " + se.getMessage());
		oe.setStackTrace(se.getStackTrace());
		throw oe;
	}catch (CTBBusinessException se) {
		BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getFileContent : " + se.getMessage());
		oe.setStackTrace(se.getStackTrace());
		throw oe;
	}
	finally {
		close(oasCon,ps,rset);
	}
	return fileContent;
}

    /**
     * Get OAS DB connection
     * @param isCommitable
     * @return
     * @throws CTBBusinessException
     */
    private static Connection openOASDBcon(boolean isCommitable)
    		throws CTBBusinessException {
    	Connection conn = null;
    	try {
    		DataSource ds = locateDataSource(oasDtataSourceJndiName);
    		conn = ds.getConnection();
    		if (isCommitable) {
    			conn.setAutoCommit(false);
    		}else{
    			conn.setAutoCommit(true);
    		}
    	} catch (NamingException e) {
    		System.err.println("NamingException:"
    				+ "JNDI name for oas datasource does not exists.");
    		throw new CTBBusinessException("NamingException:"
    				+ "JNDI name for oas datasource does not exists.");
    	} catch (SQLException e) {
    		System.err.println("SQLException:"
    				+ "while getting oas database connection.");
    		throw new CTBBusinessException("SQLException:"
    				+ "while getting oas database connection.");
    	} catch (Exception e) {
    		System.err.println("Exception:"
    				+ "while getting oas database connection.");
    		throw new CTBBusinessException("Exception:"
    				+ "while getting oas database connection.");
    	}
    
    	return conn;
    
    }
    
    /**
     * 
     * @param jndiName
     * @return
     * @throws NamingException
     */
    private static DataSource locateDataSource(String jndiName ) throws NamingException{
    	Context ctx = new InitialContext();
    	DataSource ds =  (DataSource) ctx.lookup(jndiName);
    	return ds;
    }
    
    
    
    /**
     * Close result set
     * @param rs
     */
    private static void close(ResultSet rs) {
    	if (rs != null) {
    		try {
    			rs.close();
    		} catch (SQLException e) {
    			// do nothing
    		}
    	}
    
    }
    
    
    /**
     * Close connection
     * @param con
     */
    private static void close(Connection con) {
    	if (con != null) {
    		try {
    			if(!con.getAutoCommit())
    				con.rollback();
    			con.close();
    		} catch (SQLException e) {
    			// do nothing
    		}
    	}
    
    }
    
    
    /**
     * Close statement
     * @param st
     */
    private static void close(Statement st) {
    	if (st != null) {
    		try {
    			st.close();
    		} catch (SQLException e) {
    			// do nothing
    		}
    	}
    
    }
    
    /**
     * Close connection, statement and result set 
     * @param con
     * @param st
     * @param rs
     */
    private static void close(Connection con, Statement st, ResultSet rs) {
    	close(rs);
    	close(st);
    	close(con);
    
    }
    
    
    /** End 
    **/
    
    
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
    
    /**
	 * Bulk Report download process
	 * @param paramMap
	 * @throws CTBBusinessException
	 */
    @Override
    public void downloadBulkReportProcess(Map<String, Object> paramMap) throws CTBBusinessException{
    	System.out.println("Start: BulkReportExportImpl.downloadBulkReportProcess()");
    	long startTime = System.currentTimeMillis();
    	System.out.println(paramMap);
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	Thread thread = new Thread(new LiteracyDownloadProcess(paramMap));
    	executor.execute(thread);
    	executor.shutdown();
    	try {
	    thread.join();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    	System.out.println("End: BulkReportExportImpl.downloadBulkReportProcess(): " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

}
