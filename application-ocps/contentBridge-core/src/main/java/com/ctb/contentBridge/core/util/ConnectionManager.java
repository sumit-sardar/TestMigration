/**
 * 
 */
package com.ctb.contentBridge.core.util;

/**
 * @author 543559
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {

   static Connection con;
   static String url;
         
   public static Connection getConnection()
   {
     
      try
      {
    	  /*
    	   *  Replace "DataSource" with Data Source name ,
    	   *  "username" with Oracle DB username and "password" with oracle DB password
    	   * 
    	   * 
    	   */
    	  
       //  String url = "jdbc:oracle:thin:@168.116.29.112:1521:OASR51D1"; 
         // assuming "DataSource" is your DataSource name

         //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
         Class.forName("oracle.jdbc.driver.OracleDriver");
         try
         {            	
            //con = DriverManager.getConnection("jdbc:oracle:thin:@168.116.29.112:1521:OASR51D1","ads","ads#123");
        	 con = DriverManager.getConnection("jdbc:oracle:thin:@168.116.29.112:1521:OASR51D1","oas","oasr5d");
             								
         // assuming your SQL Server's	username is "username"               
         // and password is "password"
              
         }
         
         catch (SQLException ex)
         {
            ex.printStackTrace();
         }
      }

      catch(ClassNotFoundException e)
      {
         System.out.println(e);
      }

   return con;
   }
}