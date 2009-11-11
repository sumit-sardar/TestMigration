package com.ctb.lexington.db.monitor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: kchandra
 * Date: Oct 20, 2004
 * Time: 5:26:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrackableDataSource implements Trackable , DataSource{

    DataSource dataSource;

    public TrackableDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return new TrackableConnection(dataSource.getConnection());
    }

    public Connection getConnection(String username, String password)
      throws SQLException {
        return new TrackableConnection(dataSource.getConnection(username, password));
    }

    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
