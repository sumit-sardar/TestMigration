package com.ctb.testSessionInfo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.testSessionInfo.data.SubtestVO;
import com.ctb.testSessionInfo.data.TestVO;
import com.ctb.util.OperationStatus;

@SuppressWarnings("unused")
public class ScheduledSavedStudentDetailsVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<SessionStudent> savedStudentsDetails = null;
	private TestVO testSession = null;
	private OperationStatus operationStatus;
	private List<SubtestVO> studentManifests;
	private ArrayList<String> levelOptions = null;
	private int page = 1;
	private Map<Integer,Map> accomodationMap;
	private Map<Integer,String> recomendedLevelMap;
	private String locatorSessionInfo;
	
	
	public void setSavedStudentsDetails(List<SessionStudent> savedStudentsDetails) {
		this.savedStudentsDetails = savedStudentsDetails;
	}
	public void setTestSession(TestVO testSession) {
		this.testSession = testSession;
	}
	public void setOperationStatus(OperationStatus operationStatus) {
		this.operationStatus = operationStatus;
	}
	public void populateTestSession(TestElement[] usTes) {
		
			SubtestVO locatorSubtest = null;
			List<SubtestVO> subtestList = new ArrayList<SubtestVO>();
            for (int j=0; j<usTes.length; j++)
            {
                int durationMinutes = usTes[j].getTimeLimit().intValue()/60;
                String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " mins";
                SubtestVO subtestVO = new SubtestVO(usTes[j].getItemSetId(),
                                                    String.valueOf(j+1), 
                                                    usTes[j].getItemSetName(), 
                                                    duration, 
                                                    usTes[j].getAccessCode(),
                                                    usTes[j].getSessionDefault(),
                                                    usTes[j].getItemSetForm(),
                                                    false);
                if(locatorSubtest == null && usTes[j].getItemSetName().indexOf("Locator") > 0){
                	locatorSubtest = subtestVO;
                } else {
                	subtestList.add(subtestVO);
                }
            }
            subtestList = getDefaultSubtestsWithoutLocator(subtestList);

            TestVO testVO = new TestVO( subtestList, locatorSubtest);
            setTestSession(testVO);
		
		
	}
	
	private List<SubtestVO> getDefaultSubtestsWithoutLocator(List<SubtestVO> srcList) {
		
		 if (srcList == null)
	            return srcList;

	        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
	        for (int i=0 ; i<srcList.size() ; i++) {
	            SubtestVO subtest = (SubtestVO)srcList.get(i);
	            if (subtest.getSessionDefault().equals("T") ) {
	                SubtestVO copied = new SubtestVO(subtest);
	                resultList.add(copied);
	            }
	        }

	        return resultList;
	}
	public void setStudentManifests(List<SubtestVO> studentManifests) {
		this.studentManifests = studentManifests;
	}
	public void populateManifests(StudentManifest[] studentManifests) {
		List<SubtestVO> subtestList = new ArrayList<SubtestVO>();
		int count = 0;
		for( StudentManifest manifest : studentManifests){
			SubtestVO subtestVO = new SubtestVO(manifest.getItemSetId(), String.valueOf(++count) , manifest.getItemSetName(),null, null, "T" , manifest.getItemSetForm(),false);
			subtestList.add(subtestVO);
		}
		setStudentManifests(subtestList); 
	}
	
	public void populateLevelOptions() {
		levelOptions = new ArrayList<String>();
		levelOptions.add("E");
		levelOptions.add("M");
		levelOptions.add("D");
		levelOptions.add("A");
	}
	public Map<Integer, Map> getAccomodationMap() {
		return accomodationMap;
	}
	public void setAccomodationMap(Map<Integer, Map> accomodationMap) {
		this.accomodationMap = accomodationMap;
	}
	public Map<Integer, String> getRecomendedLevelMap() {
		return recomendedLevelMap;
	}
	public void setRecomendedLevelMap(Map<Integer, String> recomendedLevelMap) {
		this.recomendedLevelMap = recomendedLevelMap;
	}
	public String getLocatorSessionInfo() {
		return locatorSessionInfo;
	}
	public void setLocatorSessionInfo(String locatorSessionInfo) {
		this.locatorSessionInfo = locatorSessionInfo;
	}
	
	
	

}
