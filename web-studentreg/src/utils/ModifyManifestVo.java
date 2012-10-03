package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctb.bean.testAdmin.TestElement;



import dto.SubtestVO;
import dto.TestVO;



public class ModifyManifestVo {
	
	private TestVO testSession = null;
	private ArrayList<String> levelOptions = new ArrayList<String>();
	private List<SubtestVO> studentDefaultManifest = new ArrayList<SubtestVO>();
	boolean hasDefaultAutoLocator = false;
	private String locatorSessionInfo ;
	private Map<Integer, String> recomendedLevel =  new HashMap<Integer, String>();
	private List<Row> licenseData;
	
	
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
        subtestList = getDefaultSubtests(subtestList);
        if(locatorSubtest!=null && locatorSubtest.getSessionDefault().equalsIgnoreCase("T")){
        	hasDefaultAutoLocator = true;
        }

        TestVO testVO = new TestVO( subtestList, locatorSubtest);
        setTestSession(testVO);
        
	
	
 }
	
	public void populateDefaultTestSession(TestElement[] usTes) {
		//TestElement[] usTes = scheduledSession.getScheduledUnits();
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
            	//subtestList.add(subtestVO);
            	locatorSubtest = subtestVO;
            } else if (subtestVO.getSessionDefault()!=null && subtestVO.getSessionDefault().equalsIgnoreCase("T")){
            	subtestList.add(subtestVO);
            }
        }
        subtestList = getDefaultSubtests(subtestList);
        if(locatorSubtest!=null && locatorSubtest.getSessionDefault().equalsIgnoreCase("T")){
        	hasDefaultAutoLocator = true;
        }
        TestVO testVO = new TestVO( subtestList, locatorSubtest);
        setTestSession(testVO);
	}
	
	private List<SubtestVO> getDefaultSubtests(List<SubtestVO> srcList) {
		
		 if (srcList == null)
	            return srcList;

	        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
	        for (int i=0 ; i<srcList.size() ; i++) {
	            SubtestVO subtest = (SubtestVO)srcList.get(i);
	            if (subtest.getSessionDefault()!=null && subtest.getSessionDefault().equalsIgnoreCase("T") ) {
	                SubtestVO copied = new SubtestVO(subtest);
	                resultList.add(copied);
	            }
	        }

	        return resultList;
	}

	
	public void populateLevelOptions() {
		levelOptions = new ArrayList<String>();
		levelOptions.add("E");
		levelOptions.add("M");
		levelOptions.add("D");
		levelOptions.add("A");
	}
	
	/**
	 * @return the licenseData
	 */
	public List<Row> getLicenseData() {
		return licenseData;
	}

	/**
	 * @param licenseData the licenseData to set
	 */
	public void setLicenseData(List<Row> licenseData) {
		this.licenseData = licenseData;
	}

	public void populateDefaultManifest(TestElement[] usTes) {

		
		studentDefaultManifest = new ArrayList<SubtestVO>();
		
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
            	studentDefaultManifest.add(subtestVO);
        }
		studentDefaultManifest = getDefaultSubtests(studentDefaultManifest);
		//studentDefaultManifest = getDefaultSubtestsWithoutLocator(subtestList);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return the testSession
	 */
	public TestVO getTestSession() {
		return testSession;
	}

	/**
	 * @param testSession the testSession to set
	 */
	public void setTestSession(TestVO testSession) {
		this.testSession = testSession;
	}

	/**
	 * @return the studentDefaultManifest
	 */
	public List<SubtestVO> getStudentDefaultManifest() {
		return studentDefaultManifest;
	}

	/**
	 * @param studentDefaultManifest the studentDefaultManifest to set
	 */
	public void setStudentDefaultManifest(List<SubtestVO> studentDefaultManifest) {
		this.studentDefaultManifest = studentDefaultManifest;
	}

	/**
	 * @return the locatorSessionInfo
	 */
	public String getLocatorSessionInfo() {
		return locatorSessionInfo;
	}

	/**
	 * @param locatorSessionInfo the locatorSessionInfo to set
	 */
	public void setLocatorSessionInfo(String locatorSessionInfo) {
		this.locatorSessionInfo = locatorSessionInfo;
	}

	public void populateLocatorSubtest(TestElement locatorSubtest) {
		int durationMinutes = locatorSubtest.getTimeLimit().intValue()/60;
        String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " mins";
        SubtestVO lsubtest = new SubtestVO(locatorSubtest.getItemSetId(),
                                            String.valueOf(1), 
                                            locatorSubtest.getItemSetName(), 
                                            duration, 
                                            locatorSubtest.getAccessCode(),
                                            locatorSubtest.getSessionDefault(),
                                            locatorSubtest.getItemSetForm(),
                                            false);
        getTestSession().setLocatorSubtest(lsubtest);
		getTestSession().setAutoLocator(true);
		
	}

	/**
	 * @return the recomendedLevel
	 */
	public Map<Integer, String> getRecomendedLevel() {
		return recomendedLevel;
	}

	/**
	 * @param recomendedLevel the recomendedLevel to set
	 */
	public void setRecomendedLevel(Map<Integer, String> recomendedLevel) {
		this.recomendedLevel = recomendedLevel;
	}

	

	

}
