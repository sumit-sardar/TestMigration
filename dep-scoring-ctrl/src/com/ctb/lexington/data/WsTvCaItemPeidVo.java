package com.ctb.lexington.data;

import com.ctb.lexington.db.record.Persistent;

public class WsTvCaItemPeidVo extends Object implements Persistent {
	
	private String contentArea;
	private String itemId;
	private String peid;
	private Long itemSetId;
	
	public String getContentArea() {
		return contentArea;
	}
	public void setContentArea(String contentArea) {
		this.contentArea = contentArea;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getPeid() {
		return peid;
	}
	public void setPeid(String peid) {
		this.peid = peid;
	}
	public Long getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(Long itemSetId) {
		this.itemSetId = itemSetId;
	}

}
