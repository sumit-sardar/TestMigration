package com.ctb.bean.testAdmin;

import com.ctb.bean.CTBBean;

public class SubtestAccessCodeDetail extends CTBBean{
	static final long serialVersionUID = 1L;
	private String accessCode = null;
	private String subtestName = null;
		
	public SubtestAccessCodeDetail() {
		super();
	}
	public SubtestAccessCodeDetail(String accessCode, String subtestName) {
		super();
		this.accessCode = accessCode;
		this.subtestName = subtestName;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public String getSubtestName() {
		return subtestName;
	}
	public void setSubtestName(String subtestName) {
		this.subtestName = subtestName;
	}
	
}
