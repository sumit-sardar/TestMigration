package com.ctb.schedular;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;

import com.ctb.control.jms.QueueSend;

 /**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class ClosableHelper {

	public static void close(QueueSend qs) {
		if (qs != null) {
			try {
				qs.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public static void close(InitialContext ic) {
		if (ic != null) {
			try {
				ic.close();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
	
	public static void close(Connection con) {
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

	public static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
		}

	}

	public static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);

	}
	public static void close(Connection con, Statement st, ResultSet rs) {
		close(rs);
		close(st);
		close(con);

	}

	public static void close(Connection con, Statement st) {
		close(st);
		close(con);
		
	}
	
}
