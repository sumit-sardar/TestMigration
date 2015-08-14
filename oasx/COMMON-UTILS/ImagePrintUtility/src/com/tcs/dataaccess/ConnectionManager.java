package com.tcs.dataaccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager extends AbstractConnectionManager
{
	private String databasePropertiesFileName;

	public ConnectionManager(String databasePropertiesFileName)
			throws Exception
			{
		this.databasePropertiesFileName = databasePropertiesFileName;
		processProperties(databasePropertiesFileName);
			}

	public Connection getConnection() throws Exception {
		Class.forName(this.driver);
		Connection con = DriverManager.getConnection(this.url, this.userName, this.pwd);
		return con;
	}

	public Connection createConnection(String dataSource)
			throws SQLException, NamingException
			{
		Connection conn = null;
		try
		{
			Context ctx = new InitialContext();
			Context envCtx = (Context)ctx.lookup("java:comp/env");
			DataSource ds = (DataSource)envCtx.lookup(dataSource);
			conn = ds.getConnection();
		} catch (SQLException exceptionObject) {
			throw exceptionObject;
		} catch (NamingException exceptionObject) {
			throw exceptionObject;
		}

		return conn;
			}

	public void commitTransaction(Connection conn)
			throws SQLException
			{
		try
		{
			if (conn != null)
				conn.commit();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}

	public void rollBackTransaction(Connection conn)
			throws SQLException
			{
		try
		{
			if (conn != null)
				conn.rollback();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}

	public void close(Connection conn)
			throws SQLException
			{
		try
		{
			if (conn != null)
				conn.close();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}

	public void close(ResultSet result)
			throws SQLException
			{
		try
		{
			if (result != null)
				result.close();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}

	public void close(PreparedStatement stmt)
			throws SQLException
			{
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}

	public void close(CallableStatement callStatement)
			throws SQLException
			{
		try
		{
			if (callStatement != null)
				callStatement.close();
		}
		catch (SQLException exceptionObject) {
			throw exceptionObject;
		}
			}
}

