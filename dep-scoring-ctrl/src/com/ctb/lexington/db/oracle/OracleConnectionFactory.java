package com.ctb.lexington.db.oracle;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;
import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.ConnectionFactoryDelegate;


public abstract class OracleConnectionFactory implements ConnectionFactoryDelegate {
	protected Driver driver = null;
	protected String url;
	protected String username;
	protected String password;
	
	public OracleConnectionFactory(String url, String username, String password)
	throws SQLException {
		driver = new OracleDriver();
		DriverManager.registerDriver(driver);
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	public void shutdown() throws SQLException {
		DriverManager.deregisterDriver(driver);
	}
}
