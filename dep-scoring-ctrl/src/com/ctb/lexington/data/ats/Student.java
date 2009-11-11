package com.ctb.lexington.data.ats;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c)  CTB McGraw-Hill 2002</p>
 * <p>Company: Accenture</p>
 * @author Vishal Saxena
 * @version 1.0
 */
import java.util.Date;

public class Student implements java.io.Serializable
{
  private int studentID;
  private int versionID;
  private Demographics demographics;
  private Biographics biographics;
  private OrgNode orgNode;
  private String updatedBy;
  private Date updatedDate;
  private String Customer;

  public Student() {
  }

  public String getCustomer()
  {
    return this.Customer;
  }

  public void setCustomer(String cust)
  {
      this.Customer = cust;
  }

  public void setUpdatedBy(String updtdBy)
  {
      this.updatedBy = updtdBy;
  }

  public String getUpdatedBy()
  {
      return this.updatedBy;
  }

  public void setUpdatedDate(Date dt)
  {
      this.updatedDate = dt;
  }

  public Date getUpdatedDate()
  {
    return this.updatedDate;
  }

  public void setStudentID(int studentID) {
    this.studentID = studentID;
  }
  public int getStudentID() {
    return studentID;
  }
  public void setVersionID(int versionID) {
    this.versionID = versionID;
  }
  public int getVersionID() {
    return versionID;
  }
  public void setDemographics(Demographics demographics) {
    this.demographics = demographics;
  }
  public Demographics getDemographics() {
    return demographics;
  }
  public void setBiographics(Biographics biographics) {
    this.biographics = biographics;
  }
  public Biographics getBiographics() {
    return biographics;
  }
  public void setOrgNode(OrgNode orgNode) {
    this.orgNode = orgNode;
  }
  public OrgNode getOrgNode() {
    return orgNode;
  }
}