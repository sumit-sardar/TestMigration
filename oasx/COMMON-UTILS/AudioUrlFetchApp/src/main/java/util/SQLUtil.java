package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLUtil {
	public static Connection getConnection()  
	{
		String dbip = ConfigUtil.getDetail("oas.db.host.address").trim();
		String sid = ConfigUtil.getDetail("oas.db.sid.address").trim();
		String user = ConfigUtil.getDetail("oas.db.user.name").trim();
		String password = ConfigUtil.getDetail("oas.db.user.password").trim();
		String port = ":"+ConfigUtil.getDetail("oas.db.port.address")+":";
		
		String connURL = "jdbc:oracle:thin:@" + dbip + port	+ sid;

		Connection conn = null;
		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			System.err.println("Exception occurred while getting connection.");
			e.printStackTrace();
		}

		return conn;
	}
	public static void close(Connection conn,Statement st,ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the Connection.");
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the Statement.");
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				System.err.println("Exception occurred while closing the ResultSet.");
				e.printStackTrace();
			}
		}

	}

}
