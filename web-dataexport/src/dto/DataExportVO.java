package dto;

import java.util.ArrayList;
import java.util.List;
import com.ctb.bean.testAdmin.ManageTestSession;

public class DataExportVO {
	
	List<ManageTestSession> testSessionList  = new ArrayList<ManageTestSession>();
	
	public void setTestSessionList(List<ManageTestSession> testSessionList) {
		
		if(testSessionList != null)
			this.testSessionList = testSessionList;
	}
}
