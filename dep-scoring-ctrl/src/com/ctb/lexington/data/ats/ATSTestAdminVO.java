package com.ctb.lexington.data.ats;

/*
 * ATSTestAdminVO.java
 *
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 *
 */

// GRNDS imports

/**
 * @author <a href="mailto:Nate_Cohen@ctb.com">Nate Cohen</a>
 * @version $Id$
 */
public class ATSTestAdminVO extends Object implements java.io.Serializable, java.lang.Comparable, java.lang.Cloneable
{
    public static final String VO_LABEL       = "com.ctb.lexington.data.ats.ATSTestAdminVO";
    public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ats.ATSTestAdminVO.array";

    private Integer adminDimId;
    private Integer instanceNumber;
    private Integer quarterMonth;
    private Integer calendarYear;
    private java.util.Date nominalAdminDate;
    private java.util.Date adminWindowBegin;
    private java.util.Date adminWindowEnd;
    private String customer;
    private String adminName;
    private String testName;
    private String adminDescription;
    private String schoolYear;
    private Integer studentDimId;
    private Integer parentOrgNodeId;
    private String testScoreType;
    private String studentGradeLevel;


  public ATSTestAdminVO() {
  }

  public Object clone(){
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
  }
  public void setAdminDimId(Integer adminDimId) {
    this.adminDimId = adminDimId;
  }
  public Integer getAdminDimId() {
    return adminDimId;
  }
  public void setInstanceNumber(Integer instanceNumber) {
    this.instanceNumber = instanceNumber;
  }
  public Integer getInstanceNumber() {
    return instanceNumber;
  }
  public void setQuarterMonth(Integer quarterMonth) {
    this.quarterMonth = quarterMonth;
  }
  public Integer getQuarterMonth() {
    return quarterMonth;
  }
  public void setCalendarYear(Integer calendarYear) {
    this.calendarYear = calendarYear;
  }
  public Integer getCalendarYear() {
    return calendarYear;
  }
  public void setNominalAdminDate(java.util.Date nominalAdminDate) {
    this.nominalAdminDate = nominalAdminDate;
  }
  public java.util.Date getNominalAdminDate() {
    return nominalAdminDate;
  }
  public void setAdminWindowBegin(java.util.Date adminWindowBegin) {
    this.adminWindowBegin = adminWindowBegin;
  }
  public java.util.Date getAdminWindowBegin() {
    return adminWindowBegin;
  }
  public void setAdminWindowEnd(java.util.Date adminWindowEnd) {
    this.adminWindowEnd = adminWindowEnd;
  }
  public java.util.Date getAdminWindowEnd() {
    return adminWindowEnd;
  }
  public void setCustomer(String customer) {
    this.customer = customer;
  }
  public String getCustomer() {
    return customer;
  }
  public void setAdminName(String adminName) {
    this.adminName = adminName;
  }
  public String getAdminName() {
    return adminName;
  }
  public void setTestName(String testName) {
    this.testName = testName;
  }
  public String getTestName() {
    return testName;
  }
  public void setAdminDescription(String adminDescription) {
    this.adminDescription = adminDescription;
  }
  public String getAdminDescription() {
    return adminDescription;
  }
  public void setSchoolYear(String schoolYear) {
    this.schoolYear = schoolYear;
  }
  public String getSchoolYear() {
    return schoolYear;
  }
    public void setStudentDimId(Integer studentDimId) {
        this.studentDimId = studentDimId;
    }
    public Integer getStudentDimId() {
        return studentDimId;
    }
    public void setParentOrgNodeId(Integer parentOrgNodeId) {
        this.parentOrgNodeId = parentOrgNodeId;
    }
    public Integer getParentOrgNodeId() {
        return parentOrgNodeId;
    }
    public void setTestScoreType(String testScoreType) {
        this.testScoreType = testScoreType;
    }
    public String getTestScoreType() {
        return testScoreType;
    }

    public int compareTo(Object other) {
        boolean isBefore = this.adminWindowBegin.before(((ATSTestAdminVO)other).getAdminWindowBegin());
        boolean isAfter = this.adminWindowBegin.after(((ATSTestAdminVO)other).getAdminWindowBegin());
        if(isBefore) {
            return -1;
        } else if(isAfter) {
            return 1;
        } else {
            return 0;
        }
    }
    public void setStudentGradeLevel(String studentGradeLevel) {
        this.studentGradeLevel = studentGradeLevel;
    }
    public String getStudentGradeLevel() {
        return studentGradeLevel;
    }
}