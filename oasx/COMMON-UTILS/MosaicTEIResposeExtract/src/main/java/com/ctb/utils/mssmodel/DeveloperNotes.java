package com.ctb.utils.mssmodel;

import java.io.Serializable;

public class DeveloperNotes implements Serializable{
	private static final long serialVersionUID = 1L;
	private String source;
	private String itemBankId;
	private String itemId;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getItemBankId() {
		return itemBankId;
	}
	public void setItemBankId(String itemBankId) {
		this.itemBankId = itemBankId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	

}
