 package com.tcs.model;
 
 import java.io.Serializable;
 import java.util.List;
 import java.util.Map;
 
 public class TestObject
   implements Serializable
 {
   private Integer productId;
   private Integer testCatalogId;
   private String testName;
   private String testSessionName;
   private List<SubTestObject> subTestList;
   private String commodityCode;
   private Map<Integer, Integer> subtestOrderMap;
 
   public Integer getProductId()
   {
     return this.productId;
   }
 
   public void setProductId(Integer productId)
   {
     this.productId = productId;
   }
 
   public Integer getTestCatalogId()
   {
     return this.testCatalogId;
   }
 
   public void setTestCatalogId(Integer testCatalogId)
   {
     this.testCatalogId = testCatalogId;
   }
 
   public String getTestName()
   {
     return this.testName;
   }
 
   public void setTestName(String testName)
   {
     this.testName = testName;
   }
 
   public String getTestSessionName()
   {
     return this.testSessionName;
   }
 
   public void setTestSessionName(String testSessionName)
   {
     this.testSessionName = testSessionName;
   }
 
   public List<SubTestObject> getSubTestList()
   {
     return this.subTestList;
   }
 
   public void setSubTestList(List<SubTestObject> subTestList)
   {
     this.subTestList = subTestList;
   }
 
   public String getCommodityCode()
   {
     return this.commodityCode;
   }
 
   public void setCommodityCode(String commodityCode)
   {
     this.commodityCode = commodityCode;
   }
 
   public Map<Integer, Integer> getSubtestOrderMap()
   {
     return this.subtestOrderMap;
   }
 
   public void setSubtestOrderMap(Map<Integer, Integer> subtestOrderMap)
   {
     this.subtestOrderMap = subtestOrderMap;
   }
 }

