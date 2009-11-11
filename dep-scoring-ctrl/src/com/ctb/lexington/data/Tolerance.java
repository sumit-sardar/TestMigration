package com.ctb.lexington.data;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Accenture</p>
 * @author Jessica Glissmann
 * @version 1.0
 */
import java.io.Serializable;

public class Tolerance implements Serializable {

  private int unitTolerance;
  private int percentageTolerance;
  
  public Tolerance() {
  }
  

  public int getUnitTolerance()
  {
    return this.unitTolerance;
  }

  public void setUnitTolerance(int unitTolerance_)
  {
    this.unitTolerance = unitTolerance_;
  }

  public int getPercentageTolerance()
  {
    return this.percentageTolerance;
  }

  public void setPercentageTolerance(int percentageTolerance_)
  {
    this.percentageTolerance = percentageTolerance_;
  }

}