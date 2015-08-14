 package com.tcs.model;
 
 import java.util.Map;
 
 public class ItemObject
 {
   private String itemId;
   private String itemObject;
   private Map<String, String> itemResponseMapPerRoster;
 
   public String getItemId()
   {
     return this.itemId;
   }
 
   public void setItemId(String itemId)
   {
     this.itemId = itemId;
   }
 
   public String getItemObject()
   {
     return this.itemObject;
   }
 
   public void setItemObject(String itemObject) {
     this.itemObject = itemObject;
   }
 
   public Map<String, String> getItemResponseMapPerRoster() {
     return this.itemResponseMapPerRoster;
   }
 
   public void setItemResponseMapPerRoster(Map<String, String> itemResponseMapPerRoster)
   {
     this.itemResponseMapPerRoster = itemResponseMapPerRoster;
   }
 }

