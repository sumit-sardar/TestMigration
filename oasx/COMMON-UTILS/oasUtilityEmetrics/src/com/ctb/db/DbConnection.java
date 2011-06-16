package com.ctb.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.ctb.utils.ExtractUtil;

public class DbConnection {
	Connection connection;
	Connection oasConnection;



	public DbConnection()
	{
		connection = null;
		oasConnection = null;

	}


	public Connection getConnection()
	throws Exception
	{
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connection = DriverManager.getConnection(connectionUrl(), ExtractUtil.getDetail("ads.server.userName"),ExtractUtil.getDetail("ads.server.password"));
			connection.setAutoCommit(false);
			//System.out.println("connection established" + connection);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		return connection;
	}

	public Connection getOasConnection() 
	throws Exception
	{
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			oasConnection= DriverManager.getConnection(connectionUrl(), ExtractUtil.getDetail("oas.server.userName"),ExtractUtil.getDetail("oas.server.password"));
			oasConnection.setAutoCommit(false);
			//System.out.println("connection established" + connection);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		return oasConnection;


	}



	/**
	 * This method will be used for retriveing url depends 
	 * upon on thin driver or thick driver
	 * @return String
	 */
	private static String connectionUrl() {
		String url;


		url = "jdbc:oracle:thin:@" + ExtractUtil.getDetail("database.server.host") + ":"+ ExtractUtil.getDetail("database.server.port")+ ":" + ExtractUtil.getDetail("database.server.name");
		// System.out.println("url==>"+url);
		return url;
	}

}
