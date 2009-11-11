package com.ctb.lexington.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactoryDelegate {
    Connection getConnection() throws SQLException;
    void shutdown() throws SQLException;
}
