package fileUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {
	public static Connection openIRSDBcon()  
	{
		String dbip = ExtractUtil.getDetail("irs.db.host.address").trim();
		String sid = ExtractUtil.getDetail("irs.db.sid.address").trim();
		String user = ExtractUtil.getDetail("irs.db.user.name").trim();
		String password = ExtractUtil.getDetail("irs.db.user.password").trim();
		
		String connURL = "jdbc:oracle:thin:@" + dbip + ":1521:"	+ sid;
		Connection conn = null;
		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(connURL, user, password);
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

		return conn;
	}
	public static void close(Connection con,Statement ps) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();// do nothing
			}
		}

	}

}
