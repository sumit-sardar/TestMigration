package com.ctb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.ctb.bean.testAdmin.LiteracyProExportRequest;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.BulkReportExportException;
	
public class LiteracyDownloadProcess implements Runnable{
	private Map<String, Object> paramMap;
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private Connection oasCon;
	//private static final int BUFFER_SIZE = 4096;
	OutputStream stream = null;
	private static String GET_FILE_CONTENT = " select FILE_CONTENT as fileContent , FILE_NAME as fileName from BULK_EXPORT_DATA_FILE where EXPORT_REQUEST_ID = ? ";
	
	
	public LiteracyDownloadProcess(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public void run() {
		try{
			this.oasCon = openOASDBcon(false);
			String exportRequestId = (String) paramMap.get("exportRequestId");
			HttpServletResponse resp = (HttpServletResponse)paramMap.get("response");
			LiteracyProExportRequest litExportObj = getFileContent(Integer.valueOf(exportRequestId));
			if(litExportObj != null){
				int contentLength = (int) litExportObj.getFileContent().length();
				byte[] data = litExportObj.getFileContent().getBytes(1, contentLength);
				String fileName = litExportObj.getFileName();
				String bodypart = "attachment; filename=\"" + fileName + "\" ";
				byte [] unzipData = LayoutUtil.unZippedBytes(data);
				try {
				    	resp.setContentLength(unzipData.length);
					resp.setHeader("Content-Disposition", bodypart);
					resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					resp.setHeader("Cache-Control", "cache");
					resp.setHeader("Pragma", "public");
					System.out.println("Wrinting file content to response output stream...");
					stream = resp.getOutputStream();
					stream.write(unzipData);
					resp.flushBuffer();
				} catch (Exception e) {
				    System.err.println("Exception while wtite to stream: " + e.getMessage());
				    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				    try {
					resp.flushBuffer();
				    } catch (Exception e1) {
					// Nothing to do
				    }
				    e.printStackTrace();
				} finally {
				    if (stream != null) {
					try {
					    	stream.flush();
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    }
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	private LiteracyProExportRequest getFileContent(Integer exportRequestId) throws BulkReportExportException {
		PreparedStatement ps = null;
		ResultSet rset = null;
		LiteracyProExportRequest fileContent = null;
		try {
			ps = oasCon.prepareStatement(GET_FILE_CONTENT);
			ps.setInt(1, exportRequestId);
			rset = ps.executeQuery();
			while(rset.next()){
				fileContent = new LiteracyProExportRequest();
				fileContent.setFileContent(rset.getBlob(1));
				fileContent.setFileName(rset.getString("fileName"));
			}
		} catch (SQLException se) {
			BulkReportExportException oe = new BulkReportExportException("LiteracyExportProcessor: getBulkReportCSVData : " + se.getMessage());
			oe.setStackTrace(se.getStackTrace());
			throw oe;
		} finally {
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
}