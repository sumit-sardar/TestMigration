package dto;

import java.util.ArrayList;
import java.util.List;
import com.ctb.bean.testAdmin.ManageTestSession;
import com.ctb.bean.dataExportManagement.ManageStudent;
public class DataExportVO {
	
	List<ManageTestSession> testSessionList  = new ArrayList<ManageTestSession>();
	List<ManageStudent> studentList = new ArrayList<ManageStudent>();
	Integer unscoredStudentCount = null;
	Integer studentBeingExportCount = null;
	
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
	
	
}
