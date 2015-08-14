 package com.tcs.dataaccess;
 
 import java.io.FileInputStream;
 import java.util.Properties;
 
 public abstract class AbstractConnectionManager
 {
   public String url;
   public String driver;
   public String userName;
   public String pwd;
 
   public void processProperties(String fileName)
     throws Exception
   {
     Properties prop = new Properties();
     prop.load(new FileInputStream(fileName));
     this.url = prop.getProperty("URL");
     if (this.url == null)
     {
       throw new Exception();
     }
 
     this.driver = prop.getProperty("DRIVER");
     if (this.driver == null)
     {
       throw new Exception();
     }
 
     this.userName = prop.getProperty("USERNAME");
     if (this.userName == null)
     {
       throw new Exception();
     }
 
     this.pwd = prop.getProperty("PWD");
     if (this.pwd == null)
     {
       throw new Exception();
     }
   }
 }

