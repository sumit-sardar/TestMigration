 package com.tcs.model;
 
 import java.io.Serializable;
 import java.util.List;
 import java.util.Map;
 
 public class SubTestObject
   implements Serializable
 {
   private Integer subTestId;
   private String subTestName;
   private String subject;
   private List<ItemObject> adsItemObjectList;
   private Map<Integer, String> ItemObjectOrderMap;
 
   public Integer getSubTestId()
   {
     return this.subTestId;
   }
 
   public void setSubTestId(Integer subTestId)
   {
     this.subTestId = subTestId;
   }
 
   public List<ItemObject> getAdsItemObjectList()
   {
     return this.adsItemObjectList;
   }
 
   public void setAdsItemObjectList(List<ItemObject> adsItemObjectList)
   {
     this.adsItemObjectList = adsItemObjectList;
   }
 
   public String getSubTestName()
   {
     return this.subTestName;
   }
 
   public void setSubTestName(String subTestName)
   {
     this.subTestName = subTestName;
   }
 
   public String getSubject()
   {
     return this.subject;
   }
 
   public void setSubject(String subject)
   {
     this.subject = subject;
   }
 
   public Map<Integer, String> getItemObjectOrderMap()
   {
     return this.ItemObjectOrderMap;
   }
 
   public void setItemObjectOrderMap(Map<Integer, String> itemObjectOrderMap)
   {
     this.ItemObjectOrderMap = itemObjectOrderMap;
   }
 }

