package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the subtest status counts
 * 
 * @author John_Wang
 */

public class SubtestStatusCount extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    
    private Integer itemSetId;
    private String status;
    private Integer rosterCount;
	public Integer getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getRosterCount() {
		return rosterCount;
	}
	public void setRosterCount(Integer rosterCount) {
		this.rosterCount = rosterCount;
	}
    
	
} 
