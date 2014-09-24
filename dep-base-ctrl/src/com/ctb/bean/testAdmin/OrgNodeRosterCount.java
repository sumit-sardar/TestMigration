package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class OrgNodeRosterCount extends CTBBean {
	private Integer orgNodeId;
	private int licenseCount;
	
	/**
	 * @return the orgNodeId
	 */
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	/**
	 * @param orgNodeId the orgNodeId to set
	 */
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	/**
	 * @return the rosterCount
	 */
	public int getLicenseCount() {
		return licenseCount;
	}
	/**
	 * @param rosterCount the rosterCount to set
	 */
	public void setLicenseCount(int licenseCount) {
		this.licenseCount = licenseCount;
	}
}
