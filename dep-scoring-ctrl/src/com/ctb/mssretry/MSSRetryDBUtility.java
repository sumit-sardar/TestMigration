package com.ctb.mssretry;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ctb.exception.CTBBusinessException;
import com.ctb.schedular.ScheduleRosterBO;

public class MSSRetryDBUtility {
	private static final String oasDtataSourceJndiName = "oasDataSource";
	private static final ResourceBundle rb = ResourceBundle.getBundle("config");
	private static final String SP_FETCH_MSS_ERRORS = "{call sp_fetch_mss_retry_errors(?,?)}";
	public static int retryReqRowCount = Integer.parseInt(rb.getString("ftte.retry.fetchsize"));
	private static final String SELECT_MSS_ERROR_LOG = "SELECT a.SCORING_INVOKE_LOG_KEY AS logkey, a.ROSTER_ID AS rosterId FROM SCORING_INVOKE_LOG a, TMP_MSS_ERROR_LOG t WHERE a.SCORING_INVOKE_LOG_KEY = t.INVOKE_LOG_KEY AND t.TMP_ID = ? ORDER BY a.UPDATED_DATE";
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
	
	public static void retryMSSTEScoringProgress() throws SQLException{
		PreparedStatement pst  = null;
		CallableStatement cst  = null;
		Connection con = null;
		ResultSet rs = null;
		long tmpLogId = 0L;
		long rosterId = 0L;
		long logkey = 0L;

		try {
			con = openOASDBcon(true);
			cst  = con.prepareCall(SP_FETCH_MSS_ERRORS);
			cst.setInt(1, retryReqRowCount);
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.execute();
			tmpLogId = cst.getLong(2);
			pst  = con.prepareStatement(SELECT_MSS_ERROR_LOG);
			pst.setLong(1, tmpLogId);
			rs = pst.executeQuery();
			while(rs.next()){
				logkey = rs.getLong("logkey");
				rosterId = rs.getLong("rosterId");
				new ScheduleRosterBO().scheduleRoster(rosterId+"#"+logkey);
			}
			
		} catch (Exception e) {
			System.err.println("Error in the MSSRetryDBUtility.retryMSSTEScoringProgress() method to execute query : \n ");
			e.printStackTrace();
		} finally {
			close(con);
		}
	}
	
	/**
	 * Close connection
	 * @param con
	 */
	private static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// do nothing
			}
		}
	}
}
