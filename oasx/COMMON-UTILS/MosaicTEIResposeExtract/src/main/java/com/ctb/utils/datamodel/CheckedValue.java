package com.ctb.utils.datamodel;

import java.io.Serializable;

public class CheckedValue implements Serializable{


	private static final long serialVersionUID = 1L;
	private String id;
	private String screenId;
	
	public CheckedValue(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
	
	
}
