/*    */ package com.tcs.test;
/*    */ 
/*    */ import com.tcs.dataaccess.ConnectionManager;
/*    */ import com.tcs.dataaccess.StudentTestSessionDao;
/*    */ import com.tcs.dataaccess.StudentTestSessionDaoImpl;
/*    */ import com.tcs.model.ItemObject;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
/*    */ 
/*    */ public class TestBlob
/*    */ {
/* 14 */   private ConnectionManager adsConnectionManager = null;
/* 15 */   private StudentTestSessionDao dao = null;
/*    */ 
/*    */   public static void main(String[] a) throws Exception
/*    */   {
/* 19 */     TestBlob mainObject = new TestBlob();
/* 20 */     mainObject.setupEnv(a);
/*    */ 
/* 22 */     mainObject.testBlob();
/*    */   }
/*    */ 
/*    */   public void testBlob()
/*    */     throws Exception
/*    */   {
/* 29 */     String[] arrAdsItemIds = { "CR000C5EF5" };
/* 30 */     this.dao = new StudentTestSessionDaoImpl();
/* 31 */     List<ItemObject> list = this.dao.getItemListFromADS(this.adsConnectionManager, arrAdsItemIds);
/* 32 */     for (ItemObject obj : list) {
/* 33 */       System.out.println(obj.getItemObject());
/* 34 */       System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setupEnv(String[] args) throws Exception
/*    */   {
/* 40 */     if (args.length != 1)
/*    */     {
/* 42 */       System.err.println("Incorrect argument....");
/* 43 */       System.out.println("Please provide the argument with order: <OAS database properties file> <ADS database properties file>");
/* 44 */       throw new Exception("Incorrect argument....");
/*    */     }
/*    */ 
/* 48 */     this.adsConnectionManager = new ConnectionManager(args[0]);
/*    */   }
/*    */ }

/* Location:           C:\Users\522912.INDIA\Desktop\reports.jar
 * Qualified Name:     com.tcs.test.TestBlob
 * JD-Core Version:    0.6.0
 */