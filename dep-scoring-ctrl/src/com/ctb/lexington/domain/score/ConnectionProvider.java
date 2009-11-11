package com.ctb.lexington.domain.score;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

public interface ConnectionProvider {
    Connection getOASConnection() throws SQLException, NamingException;
    
    Connection getIRSConnection() throws SQLException, NamingException;

    void close(boolean rollback, Connection connection) throws SQLException;
}