package dto;

import java.util.ArrayList;
import java.util.List;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.testAdmin.ScorableItem;
import com.ctb.bean.dataExportManagement.ManageStudent;
public class DataExportVO {
	
	private String page;
	private String total;
	private String records;
	private List<ManageTestSession> testSessionList  = new ArrayList<ManageTestSession>();
	private List<ManageStudent> studentList = new ArrayList<ManageStudent>();
	private Integer unscoredStudentCount = null;
	private Integer studentBeingExportCount = null;
	private Integer scheduledStudentCount = 0;
	private Integer notTakenStudentCount = 0;
	private Integer notCompletedStudentCount = 0;
	private List<ScorableItem> scorableItems;
	private String processScoreBtn;
	
	public void setTestSessionList(List<ManageTestSession> testSessionList) {
		
		if(testSessionList != null)
			this.testSessionList = testSessionList;
	}
	
	public void setStudentList(List<ManageStudent> studentList) {
		
		if(studentList != null)
			this.studentList = studentList;
	}
	
	public void setUnscoredStudentCount(Integer unscoredStudentCount) {
		
		if(unscoredStudentCount!= null)
			this.unscoredStudentCount = unscoredStudentCount;
	}


	public void setStudentBeingExportCount(Integer studentBeingExportCount) {
		if(studentBeingExportCount != null)
		this.studentBeingExportCount = studentBeingExportCount;
	}

	public void setScheduledStudentCount(Integer scheduledStudentCount) {
		this.scheduledStudentCount = scheduledStudentCount;
	}

	public void setNotTakenStudentCount(Integer notTakenStudentCount) {
		this.notTakenStudentCount = notTakenStudentCount;
	}

	

	public void setNotCompletedStudentCount(Integer notCompletedStudentCount) {
		this.notCompletedStudentCount = notCompletedStudentCount;
	}
	
	public List<ScorableItem> getScorableItems() {
		return scorableItems;
	}
	public void setScorableItems(List<ScorableItem> scorableItems) {
		this.scorableItems = scorableItems;
	}
	
	/**
	 * @return the processScoreBtn
	 */
	public String getProcessScoreBtn() {
		return processScoreBtn;
	}
	/**
	 * @param processScoreBtn the processScoreBtn to set
	 */
	public void setProcessScoreBtn(String processScoreBtn) {
		this.processScoreBtn = processScoreBtn;
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
	
}
