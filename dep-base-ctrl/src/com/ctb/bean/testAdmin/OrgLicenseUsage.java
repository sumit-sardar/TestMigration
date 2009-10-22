package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class OrgLicenseUsage extends CTBBean{
	
	static final long serialVersionUID = 1L;
	 private Integer totalLicenses;
	 private Integer reservedLicenseQuantity;
	 private Integer usedLicenseQuantity;
	 
	 
	
	public Integer getTotalLicenses() {
		return this.totalLicenses;
	}
	public void setTotalLicenses(Integer totalLicenses) {
		this.totalLicenses = totalLicenses;
	}
	public Integer getReservedLicenseQuantity() {
		return reservedLicenseQuantity;
	}
	public void setReservedLicenseQuantity(Integer reservedLicenseQuantity) {
		this.reservedLicenseQuantity = reservedLicenseQuantity;
	}
	public Integer getUsedLicenseQuantity() {
		return usedLicenseQuantity;
	}
	public void setUsedLicenseQuantity(Integer usedLicenseQuantity) {
		this.usedLicenseQuantity = usedLicenseQuantity;
	}
	
	 
}
