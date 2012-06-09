package com.ctb.bean.testAdmin; 

import com.ctb.bean.CTBBean;

/**
 * Data bean representing the test status in program status
 * 
 * @author John_Wang
 */

public class TestStatus extends CTBBean
{ 
    static final long serialVersionUID = 1L;
    
    private Integer itemSetId;
    private String itemSetType;
    private Integer itemSetCategoryId;
    private String itemSetName; 
    private Integer scheduledCount;
    private Integer startedCount;
    private Integer completedCount;
    
	public Integer getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(Integer itemSetId) {
		this.itemSetId = itemSetId;
	}
	public String getItemSetType() {
		return itemSetType;
	}
	public void setItemSetType(String itemSetType) {
		this.itemSetType = itemSetType;
	}
	public Integer getItemSetCategoryId() {
		return itemSetCategoryId;
	}
	public void setItemSetCategoryId(Integer itemSetCategoryId) {
		this.itemSetCategoryId = itemSetCategoryId;
	}
	public String getItemSetName() {
		return itemSetName;
	}
	public void setItemSetName(String itemSetName) {
		this.itemSetName = itemSetName;
	}
	public Integer getScheduledCount() {
		return scheduledCount;
	}
	public void setScheduledCount(Integer scheduledCount) {
		this.scheduledCount = scheduledCount;
	}
	public Integer getStartedCount() {
		return startedCount;
	}
	public void setStartedCount(Integer startedCount) {
		this.startedCount = startedCount;
	}
	public Integer getCompletedCount() {
		return completedCount;
	}
	public void setCompletedCount(Integer completedCount) {
		this.completedCount = completedCount;
	}
    
    
} 
