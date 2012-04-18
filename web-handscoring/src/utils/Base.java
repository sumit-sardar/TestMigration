package utils;

import java.util.List;

import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.ScorableItem;

import dto.StudentProfileInformation;
import dto.TestSessionVO;



public class Base {
	
	private String page;
	private String total;
	private String records;
	private List<Row> rows;
	private List<StudentProfileInformation> studentProfileInformation;
	private List<TestSessionVO> testSessionCUPA;
	private List<ScorableItem> itemList;
	private List<ScorableItem> scorableItems;
	private List<RosterElement> scoreByStudentList;
	
	
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
	public List<TestSessionVO> getTestSessionCUPA() {
		return testSessionCUPA;
	}
	public void setTestSessionCUPA(List<TestSessionVO> testSessionCUPA) {
		this.testSessionCUPA = testSessionCUPA;
	}
	public List<ScorableItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<ScorableItem> itemList) {
		this.itemList = itemList;
	}
	
	public List<ScorableItem> getScorableItems() {
		return scorableItems;
	}
	public void setScorableItems(List<ScorableItem> scorableItems) {
		this.scorableItems = scorableItems;
	}
	public List<RosterElement> getScoreByStudentList() {
		return scoreByStudentList;
	}
	public void setScoreByStudentList(List<RosterElement> scoreByStudentList) {
		this.scoreByStudentList = scoreByStudentList;
	}
	
	
	
	

}
