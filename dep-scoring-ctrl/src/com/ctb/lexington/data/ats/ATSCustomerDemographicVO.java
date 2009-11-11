package com.ctb.lexington.data.ats;

/*
 * ATSCustomerDemographicVO.java
 *
 * Copyright CTB/McGraw-Hill, 2003
 * CONFIDENTIAL
 *
 */

// GRNDS imports
import java.util.Map;

/**
 * @author <a href="mailto:Nate_Cohen@ctb.com">Nate Cohen</a>
 * @version $Id$
 */
public class ATSCustomerDemographicVO extends Object implements java.io.Serializable, java.lang.Cloneable
{
  public static final String VO_LABEL       = "com.ctb.lexington.data.ats.ATSCustomerDemographicVO";
  public static final String VO_ARRAY_LABEL = "com.ctb.lexington.data.ats.ATSCustomerDemographicVO.array";

  private String customer;
  private String gender;
  private String ethnicity;
  private String specialEducation;
  private String englishSecondLanguage;
  private String lepLevel;
  private String socioeconomicStatus;
  private Integer demographicDimId;
  private Map researchData;

  public ATSCustomerDemographicVO() {
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
  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getGender() {
    return gender;
  }
  public void setEthnicity(String ethnicity) {
    this.ethnicity = ethnicity;
  }
  public String getEthnicity() {
    return ethnicity;
  }
  public void setSpecialEducation(String specialEducation) {
    this.specialEducation = specialEducation;
  }
  public String getSpecialEducation() {
    return specialEducation;
  }
  public void setEnglishSecondLanguage(String englishSecondLanguage) {
    this.englishSecondLanguage = englishSecondLanguage;
  }
  public String getEnglishSecondLanguage() {
    return englishSecondLanguage;
  }
  public void setLepLevel(String lepLevel) {
    this.lepLevel = lepLevel;
  }
  public String getLepLevel() {
    return lepLevel;
  }
  public void setSocioeconomicStatus(String socioeconomicStatus) {
    this.socioeconomicStatus = socioeconomicStatus;
  }
  public String getSocioeconomicStatus() {
    return socioeconomicStatus;
  }
    public void setDemographicDimId(Integer demographicDimId) {
        this.demographicDimId = demographicDimId;
    }
    public Integer getDemographicDimId() {
        return demographicDimId;
    }


/**
 * @return Returns the researchData.
 */
public Map getResearchData() {
	return researchData;
}
/**
 * @param researchData The researchData to set.
 */
public void setResearchData(Map researchData) {
	this.researchData = researchData;
}
}