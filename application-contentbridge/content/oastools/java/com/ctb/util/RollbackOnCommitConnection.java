package com.ctb.util;


import java.sql.*;


public class RollbackOnCommitConnection extends SafeConnection {

    public RollbackOnCommitConnection(Connection impl) throws SQLException {
        super(impl);
    }

    /**
     * Unit test connection rolls back on commits
     * @throws SQLException
     */
    public void commit() throws SQLException {
        super.rollback();
    }
    /**
     * rolls back on close
     * @throws SQLException
     */
    public void close() throws SQLException {
        rollback();
        close();
    }
}
