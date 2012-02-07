package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.StudentProfileInformation;



public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<StudentProfileInformation> studentProfileInformation;
	private String studentIdArray;
	private List studentNode;
	Map<Integer,Integer> studentIdIndexer = new HashMap<Integer,Integer>();
	
	
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
	/**
	 * @return the studentIdArray
	 */
	public String getStudentIdArray() {
		return studentIdArray;
	}
	/**
	 * @param studentIdArray the studentIdArray to set
	 */
	public void setStudentIdArray(String studentIdArray) {
		this.studentIdArray = studentIdArray;
	}
	/**
	 * @return the studentNode
	 */
	public List getStudentNode() {
		return studentNode;
	}
	/**
	 * @param studentNode the studentNode to set
	 */
	public void setStudentNode(List studentNode) {
		this.studentNode = studentNode;
	}
	public Map<Integer, Integer> getStudentIdIndexer() {
		return studentIdIndexer;
	}
	public void setStudentIdIndexer(Map<Integer, Integer> studentIdIndexer) {
		this.studentIdIndexer = studentIdIndexer;
	}
	
	
	
	

}
