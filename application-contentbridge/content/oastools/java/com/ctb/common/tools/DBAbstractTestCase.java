package com.ctb.common.tools;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.sf.hibernate.Session;

import com.ctb.hibernate.HibernateSession;

public class DBAbstractTestCase extends TestCase {
    private DBConnection dbConnection = null;
    private Connection connection = null;
    private DBConfig dbconfig = null;

    protected void setUp() {
        if (dbconfig == null) {
            dbconfig = new DBConfig();
        }
        if (connection == null) {
            connection = dbconfig.openConnection();
            dbConnection = new DBConnection(connection);
        }
        HibernateSession.setUp(connection);
    }

    protected void tearDown() throws SQLException {
        HibernateSession.closeSession();
        if (connection != null) {
            connection.rollback();
            connection.close();
        }
    }

    protected DBConnection getDBConnection() {
        return dbConnection;
    }

    protected Connection getConnection() {
        return this.connection;
    }

    protected Session getSession() {
        return HibernateSession.currentSession();
    }

    public static long getUserID() {
        return OASConstants.CREATED_BY;
    }

}
