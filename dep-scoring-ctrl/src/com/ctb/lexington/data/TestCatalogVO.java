package com.ctb.lexington.data;


/*
 * TestCatalogVO.java
 *
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 *
 */


/**
 *
 * @author  Wen-Jin Chang
 * @version 
 */

public class TestCatalogVO  implements java.io.Serializable
{

    public static final String VO_LABEL = "com.ctb.lexington.bean.TestCatalogVO";
    public static final String VO_ARRAY_LABEL = VO_LABEL + ".array";
    
    private String testID;
    
    private String testName;
    
    private String grade;
    
    private String testDescription;
    
    private String testForm;
    
    private String userName;
    
    /** Creates new TestCatalogVO */
    public TestCatalogVO() {
    }
    
    /** get the test catalog ID
     */    
    public String getTestID() {
        return this.testID;
    }
    
    /** set the test ID
     */    
    public void setTestID(String testID_) {
        this.testID=testID_;
    }
    
    /** get the name of the test
     */    
    public String getTestName() {
        return this.testName;
    }
    
    /** set the name of the test
     */    
    public void setTestName(String testName_) {
        this.testName=testName_;
    }
    
    /** get grade level
     */    
    public String getGrade() {
        return this.grade;
    }
    
    /** set grade level
     */    
    public void setGrade(String grade_) {
        this.grade=grade_;
    }
    
     /** set the test description
     */   
    public void setTestDescription(String descrip){
        this.testDescription=descrip;
    }
    
     /** get the test description
     */ 
    public String getTestDescription(){
        return this.testDescription;
    }
    
         /** set the form
     */   
    public void setTestForm(String form_){
        this.testForm=form_;
    }
    
     /** get the form
     */ 
    public String getTestForm(){
        return this.testForm;
    }
    
    /** set the user name
     */   
    public void setUserName(String userName_){
        this.userName=userName_;
    }
    
     /** get the user name
     */ 
    public String getUserName(){
        return this.userName;
    }
}
