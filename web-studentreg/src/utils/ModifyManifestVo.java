package utils;

import java.util.ArrayList;
import java.util.List;

import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.TestElement;



import dto.SubtestVO;
import dto.TestVO;



public class ModifyManifestVo {
	
	private TestVO testSession = null;
	private ArrayList<String> levelOptions = new ArrayList<String>();
	List<SubtestVO> studentDefaultManifest = new ArrayList<SubtestVO>();
	boolean hasDefaultAutoLocator = false;
	
	
	
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
	
	public void populateTestSession(ScheduledSession scheduledSession) {
		TestElement[] usTes = scheduledSession.getScheduledUnits();
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

	

	

}
