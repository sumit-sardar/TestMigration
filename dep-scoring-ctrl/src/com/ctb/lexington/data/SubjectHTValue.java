package com.ctb.lexington.data;

/**
 * <p>Title: </p>
 * <p>Description: Helper Class for web tier processing</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Accenture </p>
 * @author Vishal Saxena / Jake Swenson
 * @version 1.0
 */

import java.io.Serializable;

public class SubjectHTValue extends Subject implements Serializable{

  private String oldCount;
  private String newCount;
  private boolean isValid = true;
  private boolean isChanged = false;
  private boolean isLargeChange = false;
  private int seedCount;
  private int enrollmentCount; 
  private String errorCondition = null;


    
  public SubjectHTValue() {
  }
 
  public void setOldCount(String oldCount_) {
    this.oldCount = oldCount_;
  }
  public String getOldCount() {
    return this.oldCount;
  }
  public void setNewCount(String newCount_) {
    this.newCount = newCount_;
  }
  public String getNewCount() {
    return this.newCount;
  }
  public void setIsValid(boolean isValid_) {
    this.isValid = isValid_;
  }
  public boolean getIsValid() {
    return this.isValid;
  }
  
  public void setIsChanged(boolean isChanged_)
  {
      this.isChanged = isChanged_;
  }
  public boolean getIsChanged()
  {
      return this.isChanged;
  }
  public void setIsLargeChange(boolean isLargeChange_)
  {
      this.isLargeChange = isLargeChange_;
  }
  public boolean getIsLargeChange()
  {
      return this.isLargeChange;
  }
  
  public void setSeedCount( int seedCount_ )
  {
      this.seedCount = seedCount_;
  }
  public int getSeedCount()
  {
      return this.seedCount;
  }
  
  public void setEnrollmentCount( int enrollmentCount_ )
  {
      this.enrollmentCount = enrollmentCount_;
  }
  
  public int getEnrollmentCount()
  {
      return this.enrollmentCount;
  }
  
  public void setErrorCondition(String errorCondition_)
  {
      this.errorCondition = errorCondition_;
  }
  public String getErrorCondition()
  {
      return this.errorCondition;
  }
  
}

