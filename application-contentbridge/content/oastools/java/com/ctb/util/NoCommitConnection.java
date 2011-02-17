package com.ctb.util;


import java.sql.*;


public class NoCommitConnection extends SafeConnection {

    public NoCommitConnection(Connection impl) throws SQLException {
        super(impl);
    }

	/**
	 * does nothing on commit
	 * @throws SQLException
	 */
	public void commit() throws SQLException {

	}
	
	/**
	 * does nothing on rollback
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {

	}
    /**
     * rolls back on close
     * @throws SQLException
     */
    public void close() throws SQLException {
        super.rollback();
        super.close();
    }

}
