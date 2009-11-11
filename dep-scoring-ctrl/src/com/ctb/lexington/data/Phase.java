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

public class Phase implements Serializable, Cloneable {

  private int phaseID;
  private String phaseName;
  private boolean active;
  private int userID;

  public Phase() {
  }

	public Object clone() {
		Phase obj;
		try {
			obj = (Phase) super.clone();
			if (phaseName != null)
				obj.phaseName = new String(phaseName);
			return obj;
		}
		catch (CloneNotSupportedException cnse) {
			System.out.println("Cloning not allowed");
			return this;
		}
	}

  public int getPhaseID() {
    return phaseID;
  }

  public void setPhaseID(int phaseID) {
    this.phaseID = phaseID;
  }

  public void setPhaseName(String phaseName) {
    this.phaseName = phaseName;
  }

  public String getPhaseName() {
    return phaseName;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public int getUserID() {
    return userID;
  }
}


