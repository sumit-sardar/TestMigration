package com.ctb.common.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ctb.util.NoCommitConnection;
import com.ctb.util.SafeConnection;

public class DBConfig extends Config {
    private String sid;
    private String user;
    private String password;
    private String host;
    private boolean useThin;

    public DBConfig() {
        this(userPropertiesFile);
    }

    public DBConfig(
        String host,
        String sid,
        String user,
        String password,
        boolean useThin) {
        this.host = host;
        this.sid = sid;
        this.user = user;
        this.password = password;
        this.useThin = useThin;
    }

    public DBConfig(File file) {
        load(file);
    }

    protected void readProperties() {
        sid = getRequiredProperty(properties, "db.sid", file);
        user = getRequiredProperty(properties, "db.user", file);
        password = getRequiredProperty(properties, "db.password", file);

        String thinProperty = properties.getProperty("db.useThin");

        if (thinProperty == null) {
            useThin = false;
        } else {
            useThin = thinProperty.equals("true");
        }
        if (useThin) {
            host = getRequiredProperty(properties, "db.host", file);
        }
    }

    public String getSid() {
        return sid;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public boolean getUseThin() {
        return useThin;
    }

    public Connection openConnection() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String url = connectionUrl();
            Connection conn =
                DriverManager.getConnection(url, getUser(), getPassword());
            return new SafeConnection(conn);
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public Connection openNoCommitConnection() {
        try {
            return new NoCommitConnection(openConnection());
        } catch (SQLException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
    public String connectionUrl() {
        String url;

        if (getUseThin()) {
            url = "jdbc:oracle:thin:@" + getHost() + ":1521:" + getSid();
        } else {
            url = "jdbc:oracle:oci:@" + getSid();
        }
        return url;
    }
}
