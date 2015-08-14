 package com.tcs.executor;
 
 import com.tcs.dataaccess.ConnectionManager;
 import com.tcs.dataaccess.DataAccessManager;
 
 public class ReportGeneratingMainThread extends Thread
 {
   public static Integer THREAD_COUNT = Integer.valueOf(0);
 
   private ConnectionManager oasConnectionManager = null;
   private ConnectionManager adsConnectionManager = null;
   private String testAdminId;
   private String[] testAdminIds;
 
   public ReportGeneratingMainThread(ConnectionManager oasConnectionManager, ConnectionManager adsConnectionManager, String testAdminId)
   {
     THREAD_COUNT = Integer.valueOf(THREAD_COUNT.intValue() + 1);
     setName("Thread-ReportGenerationMainThread-Count" + THREAD_COUNT);
     this.oasConnectionManager = oasConnectionManager;
     this.adsConnectionManager = adsConnectionManager;
     this.testAdminId = testAdminId;
     this.testAdminIds = new String[] { testAdminId };
   }
 
   public void run() {
     DataAccessManager mgr = new DataAccessManager();
     try {
       mgr.getSingleTestObject(this.oasConnectionManager, this.adsConnectionManager, this.testAdminIds);
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 }

