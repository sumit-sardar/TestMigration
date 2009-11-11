package com.ctb.lexington.data;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Accenture</p>
 * @author Vishal Saxena
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Date;

public class Subject implements Serializable{

  private int gradeID;
  private String gradeName;
  private Enrollment enrollment;
  private String updatedByLastName;
  private String updatedByFirstName;
  private Date updatedDate;
  private String updatedByUserName;
  
  public Subject() {
  }
  
  
  public void setUpdatedByUserName(String updatedByUserName) {
    this.updatedByUserName = updatedByUserName;
  }  
  public String getUpdatedByUserName() {
      return updatedByUserName;
  }

  public int getGradeID() {
    return gradeID;
  }
  public void setGradeID(int gradeID) {
    this.gradeID = gradeID;
  }
  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }
  public String getGradeName() {
    return gradeName;
  }
  public void setEnrollment(Enrollment enrollment) {
    this.enrollment = enrollment;
  }
  public Enrollment getEnrollment() {
    return enrollment;
  }
  public void setUpdatedByLastName(String updatedByLastName) {
    this.updatedByLastName = updatedByLastName;
  }
  public String getUpdatedByLastName() {
    return updatedByLastName;
  }
  
  public void setUpdatedByFirstName(String updatedByFirstName) {
    this.updatedByFirstName = updatedByFirstName;
  }
  public String getUpdatedByFirstName() {
    return updatedByFirstName;
  }
  
  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }
  public Date getUpdatedDate() {
    return updatedDate;
  }
}