package com.ctb.lexington.data.ats;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) CTB McGraw-Hill 2002</p>
 * <p>Company: Accenture</p>
 * @author Vishal Saxena
 * @version 1.0
 */

public class OrgNode implements java.io.Serializable
{
  private int orgNodeID;
  private int versionID;
  public OrgNode() {
  }
  public static void main(String[] args) {
    OrgNode OrgNode1 = new OrgNode();
  }
  public void setOrgNodeID(int orgNodeID) {
    this.orgNodeID = orgNodeID;
  }
  public int getOrgNodeID() {
    return orgNodeID;
  }
  public void setVersionID(int versionID) {
    this.versionID = versionID;
  }
  public int getVersionID() {
    return versionID;
  }
}