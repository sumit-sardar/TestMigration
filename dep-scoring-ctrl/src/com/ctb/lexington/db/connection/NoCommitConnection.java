package com.ctb.lexington.db.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class NoCommitConnection extends SafeConnection {
    public NoCommitConnection(Connection conn) throws SQLException {
        super(conn);
    }

    public void commit() throws SQLException {
        // ignore commit
    }

//    public void close() throws SQLException {
//        //super.rollback();
//        super.close();
//    }

}