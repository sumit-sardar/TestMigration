package com.ctb.lexington.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ctb.lexington.domain.score.ConnectionProvider;

public abstract class AbstractConnectionProvider implements ConnectionProvider {
    private static final String KEY_OAS_DATABASE_URL = "OASDatabaseURL";
    private static final String KEY_OAS_USER_NAME = "OASUserName";
    private static final String KEY_OAS_PASSWORD = "OASPassword";
    private static final String KEY_IRS_DATABASE_URL = "IRSDatabaseURL";
    private static final String KEY_IRS_USER_NAME = "IRSUserName";
    private static final String KEY_IRS_PASSWORD = "IRSPassword"; 
    
    protected ResourceBundle bundle;
    private Connection oasConnection;
    private Connection irsConnection;

    protected abstract void initProperties();

    public Connection getOASConnection() throws SQLException {
        if (oasConnection == null) {
            initProperties();
            String url = bundle.getString(KEY_OAS_DATABASE_URL);
            String user = bundle.getString(KEY_OAS_USER_NAME);
            String password = bundle.getString(KEY_OAS_PASSWORD);
            oasConnection = getConnection(url, user, password);
        }
        return oasConnection;
    }

    public Connection getIRSConnection() throws SQLException {
        if (irsConnection == null) {
            initProperties();
            String url = bundle.getString(KEY_IRS_DATABASE_URL);
            String user = bundle.getString(KEY_IRS_USER_NAME);
            String password = bundle.getString(KEY_IRS_PASSWORD);
            irsConnection = getConnection(url, user, password);
        }
        return irsConnection;
    }
    public void close(boolean rollback, Connection connection) throws SQLException {
        try {
            if (rollback) {
                connection.rollback();
            } else {
                connection.commit();
            }
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    protected Connection getConnection(String url, String username, String password)
            throws SQLException {
        return ConnectionFactory.getInstance().getConnection(url, username, password);
    }

    public String getOASSchemaName() {
        initProperties();
        return bundle.getString(KEY_OAS_USER_NAME).toUpperCase();
    }

    public String getIRSSchemaName() {
      initProperties();
      return bundle.getString(KEY_IRS_USER_NAME).toUpperCase();
  }
}