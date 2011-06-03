package com.ctb.tms.bean.login;

import java.sql.Clob;

public class ScratchpadData {
	private int itemSetId;
	private Clob scratchpadData;
	public int getItemSetId() {
		return itemSetId;
	}
	public void setItemSetId(int itemSetId) {
		this.itemSetId = itemSetId;
	}
	public Clob getScratchpadData() {
		return scratchpadData;
	}
	public void setScratchpadData(Clob scratchpadData) {
		this.scratchpadData = scratchpadData;
	}
	
}
