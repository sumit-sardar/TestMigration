package com.ctb.license.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.driver.OracleDriver;

public class DBAccess
{
  private static DBAccess dbConnection = null;
  private static String user = "oas";
  private static String password = "oasr5d";
  private static String host = "168.116.29.112";
  private static String sid = "OASR51D1";
  private static boolean useThin = true;
  private Connection connection = null;

  public static DBAccess createConnection(String env)
  {
    if (dbConnection != null)
    {
      return dbConnection;
    }

    Properties properties = loadProperties(env);
    dbConnection = new DBAccess();
    loadDBCredential(properties);
    dbConnection.setConnection(openConnection());
    return dbConnection;
  }

  public static DBAccess getDbConnection()
  {
    return dbConnection;
  }

  public static void setDbConnection(DBAccess dbConnection)
  {
    dbConnection = dbConnection;
  }

  public Connection getConnection()
  {
    return this.connection;
  }

  public void setConnection(Connection connection)
  {
    this.connection = connection;
  }

  private static void loadDBCredential(Properties properties)
  {
    user = properties.getProperty("db.user");
    password = properties.getProperty("db.password");
    sid = properties.getProperty("db.sid");
    host = properties.getProperty("db.host");
    useThin = Boolean.valueOf(properties.getProperty("db.useThin")).booleanValue();
  }

  private static Connection openConnection()
  {
    try
    {
      DriverManager.registerDriver(new OracleDriver());
      String url = connectionUrl();
      Connection conn = DriverManager.getConnection(url, user, password);

      return conn;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static boolean closeConnection(Connection conn)
  {
	  boolean connClosed = false;
	  try{
		  conn.close();
		  connClosed = true;
	  }
	  catch(Exception e){
		  connClosed = false;
	  }
	  return connClosed;
  }

  private static String connectionUrl()
  {
    String url;
    if (useThin)
      url = "jdbc:oracle:thin:@" + host + ":1521:" + sid;
    else {
      url = "jdbc:oracle:oci:@" + sid;
    }
    return url;
  }

  private static Properties loadProperties(String env)
  {
    InputStream in = null;
    Properties prop = new Properties();
    try {
      in = new FileInputStream(env);
      prop.load(in);
      return prop;
    }
    catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public ResultSet executeQuery(String sql)
    throws SQLException
  {
    Connection con = null;
    Statement stml = null;
    ResultSet rs = null;
    try
    {
      con = dbConnection.getConnection();
      stml = con.createStatement();
      if ((sql != null) && (sql.length() > 0)) {
        rs = stml.executeQuery(sql);
      }
    }
    catch (SQLException se)
    {
      se.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
    }

    return rs;
  }

  public boolean updateData(String sql)
    throws SQLException
  {
    Connection con = null;
    Statement stml = null;
    boolean isUpdate = false;
    try
    {
      con = dbConnection.getConnection();
      stml = con.createStatement();
      if (stml.executeUpdate(sql) > 0)
      {
        isUpdate = true;
      }

    }
    catch (SQLException se)
    {
      se.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
    }

    return isUpdate;
  }
  
  public void callStoredProcedure(String procedureName)
  {
	  Connection con = null;
	  CallableStatement cstmt;
	  try{
		  //System.out.println("Inside try before creating connection");
		  con = dbConnection.getConnection();
		  //System.out.println("After creating connection");
		  String call = "{call " + procedureName + "()}";
		  //System.out.println("Query is  :: " + call);
		  cstmt = con.prepareCall(call);
		  //System.out.println("After calling prepared call");
		  //System.out.println("Before executing procedure");
		  cstmt.execute();
		  //System.out.println("After executing procedure");
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
  }
}