package com.ctb.lexington.db;

import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class ConnectionFactory {
    private static ConnectionFactory instance;

    public static ConnectionFactory getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionFactory();
            DriverManager.registerDriver(new OracleDriver());
        }

        return instance;
    }

    protected Connection getConnection(String url, String username, String password)
            throws SQLException {
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);
        return conn;
    }

    public void shutdown() {
        instance = null;
    }

    // TODO: Should probably be moved into SQLUtil.close(*)
    public void release(PreparedStatement ps) throws SQLException {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException se) {
            //LoggerUtils.logError("ConnectionFactory", null, se);
            throw se;
        }
    }
}