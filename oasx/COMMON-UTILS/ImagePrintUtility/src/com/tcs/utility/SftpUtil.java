/*    */ package com.tcs.utility;
/*    */ 
/*    */ import com.jcraft.jsch.Channel;
/*    */ import com.jcraft.jsch.ChannelSftp;
/*    */ import com.jcraft.jsch.JSch;
/*    */ import com.jcraft.jsch.JSchException;
/*    */ import com.jcraft.jsch.Session;
/*    */ import com.jcraft.jsch.SftpException;
/*    */ import java.io.File;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class SftpUtil
/*    */ {
/*    */   public static Session getSFtpSession()
/*    */     throws Exception
/*    */   {
/* 17 */     JSch jsch = new JSch();
/* 18 */     Session session = null;
/*    */ 
/* 20 */     Properties properties = new Properties();
/* 21 */     properties.put("StrictHostKeyChecking", "no");
/* 22 */     properties.put("compression.s2c", "none");
/* 23 */     properties.put("compression.c2s", "none");
/* 24 */     String ftpHost = Configuration.ftphost;
/* 25 */     String ftpUser = Configuration.ftpuser;
/* 26 */     String ftpPass = Configuration.ftppassword;
/* 27 */     int ftpPort = Integer.parseInt(Configuration.ftpPort);
/*    */ 
/* 29 */     session = jsch.getSession(ftpUser, ftpHost, ftpPort);
/* 30 */     session.setConfig(properties);
/* 31 */     session.setPassword(ftpPass);
/*    */ 
/* 33 */     session.connect();
/* 34 */     return session;
/*    */   }
/*    */ 
/*    */   public static void doSftp(Session session, String destinationDir, String sourceDir, String filename) throws JSchException, SftpException
/*    */   {
/* 39 */     ChannelSftp sftpChannel = null;
/*    */     try {
/* 41 */       Channel channel = session.openChannel("sftp");
/* 42 */       channel.connect();
/* 43 */       sftpChannel = (ChannelSftp)channel;
/*    */       try
/*    */       {
/* 46 */         sftpChannel.cd(destinationDir);
/*    */       }
/*    */       catch (SftpException e)
/*    */       {
/* 50 */         sftpChannel.mkdir(destinationDir);
/* 51 */         sftpChannel.cd(destinationDir);
/*    */       }
/*    */ 
/* 54 */       sftpChannel.put(sourceDir + File.separator + filename, filename);
/*    */     }
/*    */     finally
/*    */     {
/* 59 */       if (sftpChannel != null)
/*    */         try {
/* 61 */           sftpChannel.exit();
/*    */         } catch (Exception e) {
/* 63 */           e.printStackTrace();
/*    */         }
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void closeSFtpClient(Session session)
/*    */   {
/*    */     try
/*    */     {
/* 74 */       session.disconnect();
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\522912.INDIA\Desktop\reports.jar
 * Qualified Name:     com.tcs.utility.SftpUtil
 * JD-Core Version:    0.6.0
 */