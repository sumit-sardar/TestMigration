 package com.tcs.executor;
 
 import com.tcs.dataaccess.StudentTestSessionDao;
 import com.tcs.dataaccess.StudentTestSessionDaoImpl;
 import com.tcs.model.TestRosterObject;
 
 public class SingleReportGeneratingThread extends Thread
 {
   public static Integer THREAD_COUNT = Integer.valueOf(0);
   TestRosterObject rosterObj;
   int studentCounter = 0;
 
   public SingleReportGeneratingThread(TestRosterObject rosterObj, int studentCounter) {
     this.rosterObj = rosterObj;
     this.studentCounter = studentCounter;
     setName("Thread-SingleReportGenerationThread-Count" + THREAD_COUNT);
   }
 
   public void run() {
     StudentTestSessionDao dao = new StudentTestSessionDaoImpl();
     try {
       dao.createReport(this.rosterObj, this.studentCounter);
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 }

