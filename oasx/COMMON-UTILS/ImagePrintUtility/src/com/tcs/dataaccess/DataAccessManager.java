 package com.tcs.dataaccess;
 
 import com.jcraft.jsch.Session;
 import com.tcs.Main;
 import com.tcs.conversion.MergerPDFApp;
 import com.tcs.executor.ReportGeneratingMainThread;
 import com.tcs.utility.Configuration;
 import com.tcs.utility.SftpUtil;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.FilenameFilter;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class DataAccessManager
 {
   private static Logger slf4jLogger = LoggerFactory.getLogger(DataAccessManager.class);
   private StudentTestSessionDao dao = null;
 
   public void generateReports(ConnectionManager oasConnectionManager, ConnectionManager adsConnectionManager, String productId, String customerId, String districtCode)
     throws Exception
   {
     try
     {
       this.dao = new StudentTestSessionDaoImpl();
       File tempDirectory = new File(Main.BASE_DIR + System.getProperty("file.separator") + "temp");
       if (!tempDirectory.exists()) {
         tempDirectory.mkdirs();
       }
 
       String[] testAdminIDArray = this.dao.getDistinctTestAdminIdPerProduct(oasConnectionManager, productId, customerId, districtCode);
       if ((testAdminIDArray != null) && (testAdminIDArray.length > 0)) {
         ExecutorService executorService = Executors.newFixedThreadPool(Main.THREAD_COUNT.intValue());
         ReportGeneratingMainThread thread;
         for (int c = 0; c < testAdminIDArray.length; c++) {
           thread = new ReportGeneratingMainThread(oasConnectionManager, 
             adsConnectionManager, testAdminIDArray[c]);
           executorService.execute(thread);
         }
 
         executorService.shutdown();
 
         while (!executorService.isTerminated());
         slf4jLogger.info("Uploading files to remote location......");
         uploadToFTLocation();
         slf4jLogger.info("Uploading files to remote location completed.... ");
         slf4jLogger.info("Cleaning up temporary directories......");
         directoryCleanUp(tempDirectory);
         for (String localDirectory : Main.corporationSet) {
           directoryCleanUp(new File(Main.BASE_DIR + File.separator + localDirectory));
         }
         slf4jLogger.info("Cleaning up temporary directories completed......");
       }
       else {
         slf4jLogger.info("There are no TestAdminIds...");
       }
 
       slf4jLogger.info("Report Generation Process completed !!!");
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   public void getSingleTestObject(ConnectionManager oasConnectionManager, ConnectionManager adsConnectionManager, String[] testAdminIds) throws Exception {
     Set testRosterObjectSet = new HashSet();
     int studentCounter = 1;
     long startOfTotalTime = System.currentTimeMillis();
     long startOfIndividualTime = 0L;
     try {
       this.dao = new StudentTestSessionDaoImpl();
       for (int indx = 0; indx < testAdminIds.length; indx++) {
         Integer testAdminId = Integer.valueOf(Integer.parseInt(testAdminIds[indx]));
         startOfIndividualTime = System.currentTimeMillis();
         testRosterObjectSet = this.dao.getItemIdsFromItemSet(oasConnectionManager, adsConnectionManager, testAdminId);
 
         startOfIndividualTime = System.currentTimeMillis();
 
         slf4jLogger.info("PDF Creation started for TestAdminID :: " + testAdminId);
 
         testRosterObjectSet = null;
         System.gc();
         slf4jLogger.info("Number of PDF generated for TestAdminId " + testAdminId + " # " + (testRosterObjectSet == null ? 0 : testRosterObjectSet.size()));
         slf4jLogger.info("PDF Creation completed for TestAdminID :: " + testAdminId);
       }
 
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw e;
     }
   }
 
   public void createPDFReportPerStudent()
     throws Exception
   {
     Map<Integer,List<String>> studentReportMap = new HashMap<Integer,List<String>>();
     String currentFileName = null;
     String baseDirName = Main.BASE_DIR + System.getProperty("file.separator") + "temp";
     File baseDirectory = new File(baseDirName);
     if (!baseDirectory.isDirectory()) {
       throw new Exception("Base directory is not a valid directory!!!!");
     }
     FilenameFilter textFilter = new FilenameFilter() {
       public boolean accept(File dir, String name) {
         String lowercaseName = name.toLowerCase();
 
         return lowercaseName.endsWith(".pdf");
       }
     };
     File[] files = baseDirectory.listFiles(textFilter);
     String[] splittedFileName = (String[])null;
     Integer studentId = null;
     for (File file : files) {
       currentFileName = file.getName();
       splittedFileName = currentFileName.split("_");
       studentId = new Integer(splittedFileName[1]);
 
       if (studentReportMap.get(studentId) == null)
         studentReportMap.put(studentId, new ArrayList());
       ((List)studentReportMap.get(studentId)).add(file.getAbsolutePath());
     }
     if ((studentReportMap != null) && (studentReportMap.size() > 0)) {
       for (Integer stdId : studentReportMap.keySet()) {
         Object list = null;
         List<String> studentTempFileNames = (List)studentReportMap.get(stdId);
         if ((studentTempFileNames != null) && (studentTempFileNames.size() > 0)) {
           list = new ArrayList();
           slf4jLogger.info("Student file list :: " + studentTempFileNames);
           for (String fileName : studentTempFileNames) {
             ((List)list).add(new FileInputStream(new File(fileName)));
           }
           slf4jLogger.info("Started Report Generator for Student ID :: " + stdId);
           MergerPDFApp.doMerge((List)list, 
             new FileOutputStream(new File(Main.BASE_DIR + 
             System.getProperty("file.separator") + stdId + ".pdf")));
           slf4jLogger.info("Ended Report Generator for Student ID :: " + stdId + " with file name :: " + Main.BASE_DIR + 
             System.getProperty("file.separator") + stdId + ".pdf");
         }
       }
     }
     slf4jLogger.info("");
   }
 
   private void uploadToFTLocation() throws Exception {
     String localDirectory = Main.BASE_DIR;
     String remoteDirectory = Configuration.ftpFilepath;
     Session session = null;
     try {
       session = SftpUtil.getSFtpSession();
       for (String corporationId : Main.corporationSet) {
         File corporationDirectory = new File(localDirectory + File.separator + corporationId);
         for (File file : corporationDirectory.listFiles())
           SftpUtil.doSftp(session, remoteDirectory + "/" + corporationId, 
             localDirectory + File.separator + corporationId, file.getName());
       }
     }
     catch (Exception e)
     {
       e.printStackTrace();
     } finally {
       SftpUtil.closeSFtpClient(session);
     }
   }
 
   private void directoryCleanUp(File tempDirectory)
   {
     if ((tempDirectory.exists()) && (tempDirectory.isDirectory()))
     {
       for (File file : tempDirectory.listFiles()) {
         file.delete();
       }
       tempDirectory.delete();
     }
   }
 }

