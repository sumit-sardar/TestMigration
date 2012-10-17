package utils;

import java.util.List;


import com.ctb.bean.CTBBean;
import com.ctb.bean.CTBBeanData;
import com.ctb.bean.dataExportManagement.ManageJobData;
import com.sun.rowset.internal.Row;


public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	//private ManageJobData manageJobData;
	private List manageJobData;
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
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	/*public ManageJobData getManageJobData() {
		return manageJobData;
	}
	public void setManageJobData(ManageJobData manageJobData) {
		this.manageJobData = manageJobData;
	}*/
	public List getManageJobData() {
		return manageJobData;
	}
	public void setManageJobData(List manageJobData) {
		this.manageJobData = manageJobData;
	}

}
