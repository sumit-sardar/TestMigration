package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.util.SQLUtil;

public class UserCollector {
	private final Connection conn; 

	public UserCollector(Connection conn) {
		this.conn = conn;
	}

	public UserData collect(Long oasRosterId) throws SQLException{
		final String sql = "select * from users a, test_admin b, test_roster c " +
				"where c.test_admin_id=b.test_admin_id and b.created_by=a.user_id " +
				"and c.test_roster_id=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, oasRosterId.longValue());
			rs = ps.executeQuery();
			rs.next();

            final UserData data = new UserData();
			data.setFirstName(SQLUtil.getString(rs, "First_name"));
			data.setLastName(SQLUtil.getString(rs, "last_name"));
			return data;
		} finally {
            SQLUtil.close(rs);
			ConnectionFactory.getInstance().release(ps);
		}
	}
}