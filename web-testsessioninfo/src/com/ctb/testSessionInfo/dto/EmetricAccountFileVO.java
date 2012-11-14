package com.ctb.testSessionInfo.dto;

import java.util.ArrayList;
import java.util.List;

public class EmetricAccountFileVO {

	private String page;
	private String total;
	private String records;
	private List<EmetricAccountFile> accountFileList = new ArrayList<EmetricAccountFile>();
	
	public EmetricAccountFileVO(){
		
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public List<EmetricAccountFile> getAccountFileList() {
		return accountFileList;
	}

	public void setAccountFileList(List<EmetricAccountFile> accountFileList) {
		this.accountFileList = accountFileList;
	}
	
}
