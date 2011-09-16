package utils;

import java.util.List;

import dto.StudentProfileInformation;



public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<StudentProfileInformation> studentProfileInformation;
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
	/**
	 * @return the studentProfileInformation
	 */
	public List<StudentProfileInformation> getStudentProfileInformation() {
		return studentProfileInformation;
	}
	/**
	 * @param studentProfileInformation the studentProfileInformation to set
	 */
	public void setStudentProfileInformation(
			List<StudentProfileInformation> studentProfileInformation) {
		this.studentProfileInformation = studentProfileInformation;
	}
	
	
	
	

}
