package com.ctb.lexington.data.ats;

/*
 * ATSOrgNodeVO.java
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
public class ATSOrgNodeVO extends Object implements java.io.Serializable, java.lang.Cloneable
{
  public static final String VO_LABEL       = "com.ctb.lexington.data.ats.ATSOrgNodeVO";
  public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ats.ATSOrgNodeVO.array";

  private Integer orgNodeDimId;
  private Integer childOrgNodeId;
  private String active;
  private String customer;
  private Integer hierarchyLevel;
  private String type;
  private String code;
  private String abbreviation;
  private String name;
  private String description;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String county;
  private String state;
  private String zipCode;
  private Integer orgNodeDimVersionId;

  public ATSOrgNodeVO() {
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
  public void setCustomer(String customer) {
    this.customer = customer;
  }
  public String getCustomer() {
    return customer;
  }
  public String getAbbreviation() {
    return abbreviation;
  }
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }
  public String getActive() {
    return active;
  }
  public void setActive(String active) {
    this.active = active;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public Integer getHierarchyLevel() {
    return hierarchyLevel;
  }
  public void setHierarchyLevel(Integer hierarchyLevel) {
    this.hierarchyLevel = hierarchyLevel;
  }
  public Integer getOrgNodeDimId() {
    return orgNodeDimId;
  }
  public void setOrgNodeDimId(Integer orgNodeDimId) {
    this.orgNodeDimId = orgNodeDimId;
  }
  public Integer getChildOrgNodeId() {
    return childOrgNodeId;
  }
  public void setChildOrgNodeId(Integer childOrgNodeId) {
    this.childOrgNodeId = childOrgNodeId;
  }  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getDescription() {
    return description;
  }
  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }
  public String getAddressLine1() {
    return addressLine1;
  }
  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }
  public String getAddressLine2() {
    return addressLine2;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public String getCity() {
    return city;
  }
  public void setCounty(String county) {
    this.county = county;
  }
  public String getCounty() {
    return county;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getState() {
    return state;
  }
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }
  public String getZipCode() {
    return zipCode;
  }
  public void setOrgNodeDimVersionId(Integer orgNodeDimVersionId) {
    this.orgNodeDimVersionId = orgNodeDimVersionId;
  }
  public Integer getOrgNodeDimVersionId() {
    return orgNodeDimVersionId;
  }


}