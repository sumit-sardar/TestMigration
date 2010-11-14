package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

public class OrgNodeLicenseInfo extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    
    private Integer orgNodeId;
    private Integer orgNodeLevel;
    private Integer licPurchased;
    private Integer licReserved;
    private Integer licUsed;
    
	public Integer getLicPurchased() {
		return licPurchased;
	}
	public void setLicPurchased(Integer licPurchased) {
		this.licPurchased = licPurchased;
	}
	public Integer getLicReserved() {
		return licReserved;
	}
	public void setLicReserved(Integer licReserved) {
		this.licReserved = licReserved;
	}
	public Integer getLicUsed() {
		return licUsed;
	}
	public void setLicUsed(Integer licUsed) {
		this.licUsed = licUsed;
	}
	public Integer getOrgNodeId() {
		return orgNodeId;
	}
	public void setOrgNodeId(Integer orgNodeId) {
		this.orgNodeId = orgNodeId;
	}
	public Integer getOrgNodeLevel() {
		return orgNodeLevel;
	}
	public void setOrgNodeLevel(Integer orgNodeLevel) {
		this.orgNodeLevel = orgNodeLevel;
	}
    
} 
