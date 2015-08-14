 package com.tcs.model;
 
 import java.util.List;
 
 public class TestRosterObject
 {
   private Integer testRosterId;
   private Integer studentId;
   private String studentName;
   private String grade;
   private TestObject objTest;
   private List<ItemObject> responseItemObjectList;
   private String districtCode;
   private String districtName;
   private String studentPinNumber;
 
   public Integer getTestRosterId()
   {
     return this.testRosterId;
   }
 
   public void setTestRosterId(Integer testRosterId)
   {
     this.testRosterId = testRosterId;
   }
 
   public Integer getStudentId()
   {
     return this.studentId;
   }
 
   public void setStudentId(Integer studentId)
   {
     this.studentId = studentId;
   }
 
   public String getStudentName()
   {
/*  43 */     return this.studentName;
   }
 
   public void setStudentName(String studentName)
   {
     this.studentName = studentName;
   }
 
   public String getGrade()
   {
     return this.grade;
   }
 
   public void setGrade(String grade)
   {
     this.grade = grade;
   }
 
   public TestObject getObjTest()
   {
     return this.objTest;
   }
 
   public void setObjTest(TestObject objTest)
   {
     this.objTest = objTest;
   }
 
   public List<ItemObject> getResponseItemObjectList()
   {
     return this.responseItemObjectList;
   }
 
   public void setResponseItemObjectList(List<ItemObject> responseItemObjectList)
   {
     this.responseItemObjectList = responseItemObjectList;
   }
 
   public String getDistrictCode()
   {
/*  85 */     return this.districtCode;
   }
 
   public void setDistrictCode(String districtCode)
   {
     this.districtCode = districtCode;
   }
 
   public String getDistrictName()
   {
     return this.districtName;
   }
 
   public void setDistrictName(String districtName)
   {
     this.districtName = districtName;
   }
 
   public boolean equals(Object obj)
   {
     if (obj == null) return false;
     if (!(obj instanceof TestRosterObject)) return false;
     if (this.testRosterId == null) return false;
     return this.testRosterId.equals(((TestRosterObject)obj).testRosterId);
   }
 
   public int hashCode()
   {
     int hash = 1;
     hash = hash * 31 + this.testRosterId.intValue();
 
     return hash;
   }
 
   public String getStudentPinNumber()
   {
     return this.studentPinNumber;
   }
 
   public void setStudentPinNumber(String studentPinNumber)
   {
     if (studentPinNumber == null) {
       studentPinNumber = "";
     }
     this.studentPinNumber = studentPinNumber;
   }
 }

