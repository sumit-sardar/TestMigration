/*    */ package com.tcs.utility;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class Configuration
/*    */ {
/*  9 */   public static String ftphost = "";
/* 10 */   public static String ftpuser = "";
/* 11 */   public static String ftppassword = "";
/* 12 */   public static String ftpFilepath = "";
/* 13 */   public static String ftpPort = "";
/*    */ 
/*    */   public static void doConfiguration(String configurationFileName) throws IOException {
/* 16 */     Properties prop = new Properties();
/* 17 */     prop.load(new FileInputStream(configurationFileName));
/*    */ 
/* 19 */     ftphost = prop.getProperty("oas.exportdata.ftphost");
/* 20 */     ftpuser = prop.getProperty("oas.exportdata.ftpuser");
/* 21 */     ftppassword = prop.getProperty("oas.exportdata.ftppassword");
/* 22 */     ftpFilepath = prop.getProperty("oas.exportdata.ftp.filepath");
/* 23 */     ftpPort = prop.getProperty("oas.exportdata.ftp.port");
/*    */   }
/*    */ }

