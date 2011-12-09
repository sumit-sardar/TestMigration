package utils; 

import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
import com.ctb.bean.testAdmin.SessionStudentData;
import com.ctb.bean.testAdmin.StudentManifest;
import com.ctb.bean.testAdmin.StudentManifestData;
import com.ctb.bean.testAdmin.TABERecommendedLevel;
import com.ctb.bean.testAdmin.TestElement;
import com.ctb.bean.testAdmin.TestElementData;
import com.ctb.bean.testAdmin.TestSessionData;
import com.ctb.control.testAdmin.ScheduleTest;
import com.ctb.control.testAdmin.TestSessionStatus;
import com.ctb.exception.CTBBusinessException;
import com.ctb.exception.testAdmin.InsufficientLicenseQuantityException;

import data.SubtestVO;
import dto.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class TestSessionUtils 
{ 
    public static final String GENERIC_PRODUCT_TYPE = "genericProductType";
    public static final String TABE_BATTERY_SURVEY_PRODUCT_TYPE = "tabeBatterySurveyProductType";
    public static final String TABE_LOCATOR_PRODUCT_TYPE = "tabeLocatorProductType";
    
    /**
     * getTestSessionInformation
     */
    public static ScheduledSession getTestSessionInformation(ScheduleTest scheduleTest, String userName, Integer testAdminId)
    {    
        ScheduledSession scheduledSession = null;        
        try {
            scheduledSession = scheduleTest.getScheduledSession(userName, testAdminId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return scheduledSession;
    }
    
    /**
     * getTestSessionInformation
     */
    public static SessionStudent[] getSessionStudents(ScheduleTest scheduleTest, String userName, Integer testAdminId)
    {    
        SessionStudentData roster = null;        
        try {
            roster = scheduleTest.getSessionStudents(userName, testAdminId, null, null, null);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return roster.getSessionStudents();
    }

    /**
     * getTestSessionDataWithoutRoster
     */
    public static ScheduledSession getTestSessionDataWithoutRoster(ScheduleTest scheduleTest, String userName, Integer testAdminId)
    {    
        ScheduledSession scheduledSession = null;        
        try {
            scheduledSession = scheduleTest.getTestSessionDataWithoutRoster(userName, testAdminId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }
        return scheduledSession;
    }

    /**
     * addStudentToSession
     */
    public static RosterElement addStudentToSession(ScheduleTest scheduleTest, String userName, SessionStudent ss, Integer testAdminId) throws CTBBusinessException
    {
        RosterElement roster = null;        
        roster = scheduleTest.addStudentToSession(userName, ss, testAdminId);
        return roster;      
    }

    /**
     * getManifestForRoster
     */
    public static StudentManifestData getManifestForRoster(ScheduleTest scheduleTest, String userName, Integer studentId, Integer testAdminId) 
    {
        StudentManifestData manifestData = null;
        try {  
            manifestData = scheduleTest.getManifestForRoster(userName, studentId, testAdminId, null, null, null);
        }
        catch (Exception be) {
            be.printStackTrace();
        }  
        return manifestData;
    }

    /**
     * updateManifestForRoster
     */
    public static RosterElement updateManifestForRoster(ScheduleTest scheduleTest,
									    				String userName,
									    				Integer studentId,
									    				Integer orgNodeId,
									    				Integer testAdminId,
									    				StudentManifestData manifestData) throws CTBBusinessException 
    {
        RosterElement roster = null;        
        try {  
            roster = scheduleTest.updateManifestForRoster(userName, studentId, orgNodeId, testAdminId, manifestData);
        }
        //START- Added for Deferred defect #64308
        catch (InsufficientLicenseQuantityException e)
        {
            e.printStackTrace();
            throw e;         
        }
         //END- Added for Deferred defect #64308
        catch (Exception be) {
            be.printStackTrace();
        }  
        return roster;      
    }

    /**
     * getStudentSubtests
     */
    public static List getStudentSubtests(ScheduleTest scheduleTest, String userName, Integer studentId, Integer testAdminId)
    {
        List subtestList = new ArrayList();
        
        StudentManifestData smd = getManifestForRoster(scheduleTest, userName, studentId, testAdminId); 

        StudentManifest[] studentManifests = smd.getStudentManifests();
        
        for (int i=0; i<studentManifests.length; i++)
        {
            SubtestVO subtestVO = new SubtestVO(studentManifests[i].getItemSetId(),
                                                String.valueOf(i+1), 
                                                studentManifests[i].getItemSetName(), 
                                                "", 
                                                "",
                                                "",
                                                studentManifests[i].getItemSetForm(),
                                                false);
            subtestList.add(subtestVO);
        }
                
        return subtestList;
    }

    /**
     * getAllSubtestsForTest
     */
    public static List getAllSubtestsForTest(ScheduleTest scheduleTest, String userName, Integer itemSetId)
    {
        List subtestList = new ArrayList();
        try {      
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTest(userName, itemSetId, new Boolean(false), null, null, null);
            TestElement [] testElements = suTed.getTestElements();
            for (int i=0 ; i<testElements.length ; i++) {
                TestElement te = testElements[i];
                SubtestVO subtestVO = buildSubtestVO(te);
                subtestList.add(subtestVO);
            }   
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }                
        return subtestList;
    }


    /**
     * getAllSubtestsForTestAdmin
     */
    public static List getAllSubtestsForTestAdmin(ScheduleTest scheduleTest, String userName, Integer testAdminId)
    {
        List subtestList = new ArrayList();

        ScheduledSession scheduledSession = getTestSessionDataWithoutRoster(scheduleTest, userName, testAdminId);        
        TestElement[] testElements = scheduledSession.getScheduledUnits();
        
        for (int i=0; i<testElements.length; i++)
        {
            TestElement te = testElements[i];
            SubtestVO subtestVO = buildSubtestVO(te);
            subtestList.add(subtestVO);
        }
                
        return subtestList;
    }

    /**
     * retrieveSubtests
     */
    public static List retrieveSubtests(ScheduledSession scheduledSession)
    {
        List subtestList = new ArrayList();
        TestElement[] testElements = scheduledSession.getScheduledUnits();        
        for (int i=0; i<testElements.length; i++)
        {
            TestElement te = testElements[i];
            SubtestVO subtestVO = buildSubtestVO(te);
            subtestList.add(subtestVO);
        }
        return subtestList;
    }
    
    /**
     * setupSessionSubtests
     */
    public static List setupSessionSubtests(List sessionSubtests, List selectedSubtests)
    {
        List resultList = new ArrayList();

        // copy back non-default locator subtest from session subtest list into selected subtest list
        // the order of locator subtest will be set at the top of the list
        if (sessionSubtests.size() > 0) {
            SubtestVO locatorSubtest  = (SubtestVO)sessionSubtests.get(0);
            if (isLocatorSubtest(locatorSubtest) && locatorSubtest.getSessionDefault().equals("F")) {
                resultList.add(locatorSubtest);            
            }
        }
        
        // set selected subtest to default subtest and copy to new list
        for (int i=0 ; i<selectedSubtests.size() ; i++) {
            SubtestVO subtest  = (SubtestVO)selectedSubtests.get(i);
            SubtestVO copied = new SubtestVO(subtest);
            copied.setSessionDefault("T");
            resultList.add(copied);            
        }
        
        // copy back non-default subtests from session subtest list into selected subtest list
        // the order of these subtests will be set at the end of the list
        for (int i=0 ; i<sessionSubtests.size() ; i++) {
            SubtestVO subtest  = (SubtestVO)sessionSubtests.get(i);
            if (subtest.getSessionDefault().equals("F")) {
                if (! isSubtestPresent(resultList, subtest)) {
                    SubtestVO copied = new SubtestVO(subtest);
                    resultList.add(copied);            
                }
            }
        }
                
        return resultList;
    }
    
    /**
     * getAvailableSubtests
     */
    public static List getAvailableSubtests(List subtests, List selectedSubtests)
    {
        List availableSubtests = new ArrayList();
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO s1  = (SubtestVO)subtests.get(i);
            boolean found = false;
            for (int j=0 ; j<selectedSubtests.size() ; j++) {
                SubtestVO s2  = (SubtestVO)selectedSubtests.get(j);
                if (s1.getId().intValue() == s2.getId().intValue()) { 
                    found = true;
                    break;                          
                }
            }
            if (! found) {
                if (! isLocatorSubtest(s1)) {
                    SubtestVO subtest = new SubtestVO(s1);
                    availableSubtests.add(subtest);
                }
            }            
        }        
        return availableSubtests;
    }

    /**
     * getRosterForTestSession
     */
    public static RosterElementData getRosterForTestSession(TestSessionStatus testSessionStatus, String userName, Integer testAdminId) 
    {
        RosterElementData red = null;
        try {      
            red = testSessionStatus.getRosterForTestSession(userName, testAdminId, null, null, null);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return red;
    }
    

    /**
     * getTestSessionDetails
     */
     public static TestSessionData getTestSessionDetails(TestSessionStatus testSessionStatus, String userName, Integer testAdminId) 
    {
        TestSessionData tsd = null;
        try {      
            tsd = testSessionStatus.getTestSessionDetails(userName, testAdminId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return tsd;
    }
 

    /**
     * getScheduledSession
     */
     public static ScheduledSession getScheduledSession(ScheduleTest scheduleTest, String userName, Integer testAdminId) 
    {
        ScheduledSession session = null;
        try {      
            session = scheduleTest.getScheduledSession(userName, testAdminId);
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return session;
    }


    /**
     * getLocatorSessionInfo
     */
     public static String getLocatorSessionInfo(ScheduleTest scheduleTest, String userName, Integer studentId,
                                                Integer itemSetId, Integer locatorItemSetId, List subtests) 
    {
        String locatorSessionInfo = "";
        SubtestVO subtest = null;
        
        try {      
            TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, itemSetId, locatorItemSetId);
            
            if (! checkRecommendedlevels(trls, subtests)) {
                return locatorSessionInfo;
            }
            
            HashMap trlHash = new HashMap();
            for (int i=0 ; i<subtests.size() ; i++) {
                subtest = (SubtestVO)subtests.get(i);
                for (int j=0 ; j<trls.length ; j++) {
                    TABERecommendedLevel trl = trls[j];
                    Integer id = trl.getItemSetId();
                    if (id.intValue() == subtest.getId().intValue()) {
                        if (trl.getTestAdminName() != null && !"".equals(trl.getTestAdminName())) {                            
                            if (!trlHash.containsKey(trl.getTestAdminId())) {
                                String dateStr = DateUtils.formatDateToDateString(trl.getCompletedDate());
                                locatorSessionInfo += (trl.getTestAdminName() + ", " + dateStr + "\n");
                                trlHash.put(trl.getTestAdminId(),trl.getTestAdminId());
                            }
                        }
                    }
                }
            }
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }                
        return locatorSessionInfo;
    }
    
    /**
     * isRecommendedlevels
     * this method implemented differently than Schedule Test
     */
     public static boolean isRecommendedlevels(TABERecommendedLevel[] trls, List subtests) 
    {
        if (trls.length == 0) {
            return false;
        }
                        
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            for (int j=0 ; j<trls.length ; j++) {
                TABERecommendedLevel trl = trls[j];
                Integer id = trl.getItemSetId();
                if (id.intValue() == subtest.getId().intValue()) {
                    if ((trl.getRecommendedLevel() != null) && (subtest.getLevel() != null)) {                            
                        if (! trl.getRecommendedLevel().equals(subtest.getLevel())) {                            
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * checkRecommendedlevels
     * this method implemented differently than Schedule Test
     */
    public static boolean checkRecommendedlevels(TABERecommendedLevel[] trls, List subtests) 
    {
        if (trls.length == 0) {
            return false;
        }
                        
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            for (int j=0 ; j<trls.length ; j++) {
                TABERecommendedLevel trl = trls[j];
                Integer id = trl.getItemSetId();
                if (id.intValue() == subtest.getId().intValue()) {
                    boolean same = false;
                    String srcLevel = subtest.getLevel();                
                    String desLevel = trl.getRecommendedLevel();
                    if ((srcLevel == null) && (desLevel == null)) 
                        same = true;
                    else {
                        if (srcLevel == null) srcLevel = "E";
                        if (desLevel == null) desLevel = srcLevel;
                        same = desLevel.equals(srcLevel);                         
                    }
                    if (! same) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * setDefaultLevels
     */
     public static void setDefaultLevels(List defaultSubtests, List subtests, boolean isTabeAdaptive)
     {
     	if (isTabeAdaptive)
    		return; // do nothing for TABE Adaptive
    	
    	 
        for (int i=0 ; i<defaultSubtests.size() ; i++) {
            SubtestVO defSubtest = (SubtestVO)defaultSubtests.get(i);
            for (int j=0 ; j<subtests.size() ; j++) {
                SubtestVO subtest = (SubtestVO)subtests.get(j);
                if (defSubtest.getId().intValue() == subtest.getId().intValue()) {
                    subtest.setLevel(defSubtest.getLevel());
                    break;
                }
            }
        }        
     }

    /**
     * setDefaultLevelsIfNull
     */
     public static void setDefaultLevelsIfNull(List defaultSubtests, List subtests, boolean isTabeAdaptive)
     {
     	if (isTabeAdaptive)
    		return; // do nothing for TABE Adaptive
    	
    	 
        for (int i=0 ; i<defaultSubtests.size() ; i++) {
            SubtestVO defSubtest = (SubtestVO)defaultSubtests.get(i);
            for (int j=0 ; j<subtests.size() ; j++) {
                SubtestVO subtest = (SubtestVO)subtests.get(j);
                if (defSubtest.getId().intValue() == subtest.getId().intValue()) {
                    if (subtest.getLevel() == null) {
                        subtest.setLevel(defSubtest.getLevel());
                    }
                    break;
                }
            }
        }        
     }
     

    /**
     * setRecommendedLevelForStudent
     */
     public static void setRecommendedLevelForStudent(ScheduleTest scheduleTest, String userName, Integer studentId,
                                                Integer itemSetId, Integer locatorItemSetId, StudentManifest [] studentManifests) 
    {
        try {      
            TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, itemSetId, locatorItemSetId);
            for (int i=0 ; i<studentManifests.length ; i++) {
                StudentManifest sm = (StudentManifest)studentManifests[i];
                
                for (int j=0 ; j<trls.length ; j++) {
                    TABERecommendedLevel trl = trls[j];
                    Integer id = trl.getItemSetId();
                    if (id.intValue() == sm.getItemSetId().intValue()) {
                        if (trl.getTestAdminName() != null && !"".equals(trl.getTestAdminName())) {  
                            sm.setItemSetForm(trl.getRecommendedLevel());
                        }
                    }
                }
            }
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
    }

    /**
     * setRecommendedLevelForStudent
     */
     public static boolean setRecommendedLevelForStudent(ScheduleTest scheduleTest, String userName, Integer studentId,
                                                Integer itemSetId, Integer locatorItemSetId, List subtests) 
    {
        boolean hasRecommendedLevels = false;
        SubtestVO subtest = null;        
        if ((subtests == null) || (subtests.size() == 0)) {
            return false;
        }

        try {      
            TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, itemSetId, locatorItemSetId);
            
            if (! isRecommendedlevels(trls, subtests)) {
                return true;
            }
            
            for (int i=0 ; i<subtests.size() ; i++) {
                subtest = (SubtestVO)subtests.get(i);
                
                for (int j=0 ; j<trls.length ; j++) {
                    TABERecommendedLevel trl = trls[j];
                    Integer id = trl.getItemSetId();
                    if (id.intValue() == subtest.getId().intValue()) {
                        if (trl.getTestAdminName() != null && !"".equals(trl.getTestAdminName())) {  
                            hasRecommendedLevels = true;                          
                            subtest.setLevel(trl.getRecommendedLevel());
                        }
                    }
                }
            }
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
        return hasRecommendedLevels;
    }

    /**
     * setRecommendedLevelForSession
     */
     public static void setRecommendedLevelForSession(ScheduleTest scheduleTest, String userName, Integer studentId,
                                                Integer itemSetId, Integer locatorItemSetId, List subtests) 
    {
        SubtestVO subtest = null;        
        try {      
            TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, itemSetId, locatorItemSetId);
            for (int i=0 ; i<subtests.size() ; i++) {
                subtest = (SubtestVO)subtests.get(i);                
                for (int j=0 ; j<trls.length ; j++) {
                    TABERecommendedLevel trl = trls[j];
                    Integer id = trl.getItemSetId();
                    if (id.intValue() == subtest.getId().intValue()) {
                        if (trl.getTestAdminName() != null && !"".equals(trl.getTestAdminName())) {  
                            if (trl.getRecommendedLevel() != null) {
                                subtest.setLevel(trl.getRecommendedLevel());
                            }
                        }
                    }
                }
            }
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }        
    }

    /**
     * getLevelOptions
     */
    public static List getLevelOptions(String[] forms)
    {
        List options = new ArrayList();        
        for (int i=0 ; i<forms.length ; i++) {
            options.add(forms[i]);
        }
        return options;
    }

    /**
     * getLevelOptions
     */
    public static List getLevelOptions()
    {
        List options = new ArrayList();        
        options.add("E");
        options.add("M");
        options.add("D");
        options.add("A");
        return options;
    }

    /**
     * resetSubtestOrder
     */
    public static void resetSubtestOrder(List srcList)
    {
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO subtest = (SubtestVO)srcList.get(i); 
            subtest.setSequence(String.valueOf(i+1));
        }
    }

    /**
     * getDefaultSubtests
     */
    public static List getDefaultSubtests(TestElement[] testElements)
    {
        List resultList = new ArrayList();
        for (int i=0; i<testElements.length; i++)
        {
            TestElement te = testElements[i];
            if (te.getSessionDefault().equals("T")) {
                SubtestVO subtestVO = buildSubtestVO(te);
                resultList.add(subtestVO);
            }
        }

        resetSubtestOrder(resultList);
                
        return resultList;
    }

    
    /**
     * getDefaultSubtests
     */
    public static List getDefaultSubtests(List srcList)
    {
        if (srcList == null) 
            return srcList;
            
        List resultList = new ArrayList();
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO subtest = (SubtestVO)srcList.get(i); 
            if (subtest.getSessionDefault().equals("T")) {
                SubtestVO copied = new SubtestVO(subtest);
                resultList.add(copied);
            }
        }
        
        resetSubtestOrder(resultList);
        
        return resultList;
    }

    /**
     * getDefaultSubtestsWithoutLocator
     */
    public static List getDefaultSubtestsWithoutLocator(List srcList)
    {
        if (srcList == null) 
            return srcList;
            
        List resultList = new ArrayList();
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO subtest = (SubtestVO)srcList.get(i); 
            if (subtest.getSessionDefault().equals("T") && (! isLocatorSubtest(subtest))) {
                SubtestVO copied = new SubtestVO(subtest);
                resultList.add(copied);
            }
        }

        resetSubtestOrder(resultList);

        return resultList;
    }

    /**
     * getNonLocatorSubtests
     */
    public static List getNonLocatorSubtests(List subtests)
    {
        List resultList = new ArrayList();        
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            if (! isLocatorSubtest(subtest)) {
                SubtestVO obj = new SubtestVO(subtest); 
                resultList.add(obj);          
            }
        }

        resetSubtestOrder(resultList);

        return resultList;
    }

    /**
     * getLocatorSubtest
     */
    public static SubtestVO getLocatorSubtest(ScheduleTest scheduleTest, String userName, Integer itemSetId)
    {
        SubtestVO locatorSubtest = null;
        try {      
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTest(userName, itemSetId, new Boolean(false), null, null, null);
            TestElement [] usTes = suTed.getTestElements();
            for (int i=0 ; i<usTes.length ; i++) {
                TestElement te = usTes[i];
                if (te != null) {
                    SubtestVO subtest = buildSubtestVO(te);
                    if (isLocatorSubtest(subtest)) {
                        locatorSubtest = subtest;
                        break;
                    }
                }
            }
        }
        catch (CTBBusinessException be) {
            be.printStackTrace();
        }                
        return locatorSubtest;
    }
    

    /**
     * getLocatorSubtest
     */
    public static SubtestVO getLocatorSubtest(List subtests)
    {
        SubtestVO locatorSubtest = null;
        if ((subtests != null) && (subtests.size() > 0)) {
            for (int i=0 ; i<subtests.size() ; i++) {
                SubtestVO subtest = (SubtestVO)subtests.get(i);
                if (isLocatorSubtest(subtest)) {
                    locatorSubtest = new SubtestVO(subtest); 
                    break;                   
                }
            }
        }
        return locatorSubtest;
    }

    /**
     * locatorSubtestPresent
     */
    public static boolean locatorSubtestPresent(List subtests)
    {
        boolean present = false;
        if ((subtests != null) && (subtests.size() > 0)) {
            for (int i=0 ; i<subtests.size() ; i++) {
                SubtestVO subtest = (SubtestVO)subtests.get(i);
                if (isLocatorSubtest(subtest)) {
                    present = true;                    
                }
            }
        }
        return present;
    }

    /**
     * isLocatorTest
     */
    public static boolean isLocatorTest(List subtests)
    {
        boolean locatorTest = false;
        if ((subtests != null) && (subtests.size() == 1)) {
            SubtestVO subtest = (SubtestVO)subtests.get(0);
            if (isLocatorSubtest(subtest)) {
                locatorTest = true;                    
            }
        }
        return locatorTest;
    }

    /**
     * isLocatorSubtest
     */
    public static boolean isLocatorSubtest(SubtestVO subtest)
    {
        return (subtest.getSubtestName().indexOf("Locator") > 0);
    }

    /**
     * sessionHasDefaultLocatorSubtest
     */
    public static boolean sessionHasDefaultLocatorSubtest(List subtests)
    {
        if (subtests != null) {
            for (int i=0 ; i<subtests.size() ; i++) {
                SubtestVO subtest = (SubtestVO)subtests.get(i);
                if (isLocatorSubtest(subtest)) {
                    if (subtest.getSessionDefault().equals("T")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * extractLocatorSubtest
     */
    public static boolean extractLocatorSubtest(List subtests)
    {
        if (subtests != null) {
            for (int i=0 ; i<subtests.size() ; i++) {
                SubtestVO subtest = (SubtestVO)subtests.get(i);
                if (isLocatorSubtest(subtest)) {
                    subtests.remove(i);
                    for (int j=0 ; j<subtests.size() ; j++) {
                        subtest = (SubtestVO)subtests.get(j);                    
                        subtest.setSequence(String.valueOf(j+1));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * restoreLocatorSubtest
     */
    public static void restoreLocatorSubtest(List subtests, SubtestVO locatorSubtest)
    {
        if (locatorSubtest != null) {                
            locatorSubtest.setSessionDefault("T");                 
            if (! isSubtestPresent(subtests, locatorSubtest)) {   
                subtests.add(0, locatorSubtest);
                for (int i=0 ; i<subtests.size() ; i++) {
                    SubtestVO subtest = (SubtestVO)subtests.get(i);            
                    subtest.setSequence(String.valueOf(i+1));
                    subtest.setLevel(null);     // no level when there is auto locator                
                }
            }           
            else {
                for (int i=0 ; i<subtests.size() ; i++) {
                    SubtestVO subtest = (SubtestVO)subtests.get(i);
                    if (isLocatorSubtest(subtest)) {
                        subtest.setSessionDefault("T");  
                        break;               
                    }
                }
            }
        }
    }
    
    /**
     * getSubtestById
     */
    public static SubtestVO getSubtestById(List subtests, Integer subtestId)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);    
            if (subtest.getId().intValue() == subtestId.intValue()) {
                return subtest;
            }
        }        
        return null;
    }


    /**
     * isSubtestPresent
     */
    public static boolean isSubtestPresent(List subtests, SubtestVO srcSubtest)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            if (subtest.getId().intValue() == srcSubtest.getId().intValue()) {
                return true;    
            }
        }
        return false;
    }

    
    /**
     * buildSubtestVO
     */
    public static SubtestVO buildSubtestVO(TestElement te)
    {
        int durationMinutes = 0;
        if (te.getTimeLimit() != null)
            durationMinutes = te.getTimeLimit().intValue() / 60;
        String duration = (durationMinutes == 0) ? "Untimed" : durationMinutes + " minutes";
        
        SubtestVO subtest = new SubtestVO(te.getItemSetId(),
                                            String.valueOf(1), 
                                            te.getItemSetName(), 
                                            duration, 
                                            te.getAccessCode(),
                                            te.getSessionDefault(),
                                            te.getItemSetForm(),
                                            false);
        return subtest;                                    
    }
        
    
    /**
     * retrieveSelectedSubtestsFromRequest
     */
    public static List retrieveSelectedSubtestsFromRequest(HttpServletRequest request, List allSubtests)
    {
        List selectedSubtests = new ArrayList();
        int index = 1;

        for (int i=0 ; i<allSubtests.size() ; i++) {        
            for (int j=0 ; j<allSubtests.size() ; j++) {
                SubtestVO subtest = (SubtestVO)allSubtests.get(j);            
                String rowIndex = "index_" + String.valueOf(j + 1);
                rowIndex = (String)request.getParameter(rowIndex);            
                if (rowIndex != null) {
                    Integer value = new Integer(rowIndex);
                    if (value.intValue() == (i + 1)) {
                        SubtestVO obj = new SubtestVO(subtest);
                        obj.setSequence(String.valueOf(index));
                        index++;
                        String rowLevel = "level_" + String.valueOf(j + 1);
                        rowLevel = (String)request.getParameter(rowLevel);            
                        if (rowLevel != null) {
                            obj.setLevel(rowLevel);
                        }
                        selectedSubtests.add(obj);
                    }
                }   
            }
        }

        return selectedSubtests;
    }
        
    /**
     * copySessionDefault
     */
    public static void copySessionDefault(List allSubtests, List selectedSubtests) 
    {
        for (int i=0 ; i<allSubtests.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)allSubtests.get(i);    
            for (int j=0 ; j<selectedSubtests.size() ; j++) {        
                SubtestVO selSubtest = (SubtestVO)selectedSubtests.get(j);    
                if (selSubtest.getId().intValue() == subtest.getId().intValue()) {
                    selSubtest.setSessionDefault(subtest.getSessionDefault());
                    break;
                }
            }
        }
    }

    /**
     * copyTestAccessCode
     */
    public static void copyTestAccessCode(List allSubtests, List selectedSubtests) 
    {
        for (int i=0 ; i<allSubtests.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)allSubtests.get(i);    
            for (int j=0 ; j<selectedSubtests.size() ; j++) {        
                SubtestVO selSubtest = (SubtestVO)selectedSubtests.get(j);    
                if (selSubtest.getId().intValue() == subtest.getId().intValue()) {
                    selSubtest.setTestAccessCode(subtest.getTestAccessCode());
                    break;
                }
            }
        }
    }

    /**
     * copySubtestLevel
     */
    public static void copySubtestLevel(List allSubtests, List selectedSubtests) 
    {
        for (int i=0 ; i<allSubtests.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)allSubtests.get(i);    
            for (int j=0 ; j<selectedSubtests.size() ; j++) {        
                SubtestVO selSubtest = (SubtestVO)selectedSubtests.get(j);    
                if (selSubtest.getId().intValue() == subtest.getId().intValue()) {
                    selSubtest.setLevel(subtest.getLevel());
                    break;
                }
            }
        }
    }
        
    /**
     * sortSubtestList
     */
    public static List sortSubtestList(List allSubtests, List selectedSubtests) 
    {
        List desList = new ArrayList();
        for (int i=0 ; i<selectedSubtests.size() ; i++) {
            SubtestVO src = (SubtestVO)selectedSubtests.get(i);
            SubtestVO des = new SubtestVO(src);
            desList.add(des);
        }
           
        for (int i=0 ; i<allSubtests.size() ; i++) {        
            SubtestVO subtest = (SubtestVO)allSubtests.get(i);    
            boolean found = false;
            for (int j=0 ; j<selectedSubtests.size() ; j++) {        
                SubtestVO selSubtest = (SubtestVO)selectedSubtests.get(j);    
                if (selSubtest.getId().intValue() == subtest.getId().intValue()) {
                    found = true;
                }
            }
            if (! found) {
                SubtestVO obj = new SubtestVO(subtest);
                desList.add(obj);
            }                    
        }
        
        return desList;
    }
 
    /**
     * setDefaultLevels
     */
    public static void setDefaultLevels(List srcList, String level, boolean isTabeAdaptive)
    {
    	if (isTabeAdaptive)
    		return; // do nothing for TABE Adaptive
    	
        List resultList = new ArrayList();
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO src = (SubtestVO)srcList.get(i);   
            if (level.equals("1")) 
                src.setLevel(level); 
            else
            if (src.getLevel() == null)
                src.setLevel(level); 
        }
    }


    /**
     * cloneSubtests
     */
    public static List cloneSubtests(List srcList)
    {
        if (srcList == null) 
            return srcList;
            
        List resultList = new ArrayList();
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO src = (SubtestVO)srcList.get(i);    
            SubtestVO copied = new SubtestVO(src);
            resultList.add(copied);
        }
        return resultList;
    }

    /**
     * cloneSubtestsForRegistration
     */
    public static List cloneSubtestsForRegistration(List srcList)
    {
        if (srcList == null) 
            return srcList;
            
        List resultList = new ArrayList();
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO src = (SubtestVO)srcList.get(i);    
            SubtestVO copied = new SubtestVO(src);
            copied.setLevel(null);
            resultList.add(copied);
        }
        return resultList;
    }

    /**
     * getStudentDisplayName
     */
    public static String getStudentDisplayName(List students, Integer studentId)
    {
        String studentName = "";
        for (int i=0 ; i<students.size() ; i++) {
            SessionStudent ss = (SessionStudent)students.get(i);
            if (ss.getStudentId().intValue() == studentId.intValue()) {
                studentName = ss.getLastName() + ", " + ss.getFirstName();
                if (ss.getMiddleName() != null)
                    studentName += " " + ss.getMiddleName();
                break;
            }
        }   
        return studentName;     
    }
        
    /**
     * getProductType
     */
    public static String getProductType(String productType)
    {
        if (productType.equals("TB")) 
            return TABE_BATTERY_SURVEY_PRODUCT_TYPE;
        else
        if (productType.equals("TL")) 
            return TABE_LOCATOR_PRODUCT_TYPE;
        else            
            return GENERIC_PRODUCT_TYPE;
    }

    /**
     * isTabeProduct
     */
    public static Boolean isTabeProduct(String productType)
    {
        return new Boolean(! productType.equals(GENERIC_PRODUCT_TYPE));
    }
    
    /**
     * isTabeBatterySurveyProduct
     */
    public static Boolean isTabeBatterySurveyProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_BATTERY_SURVEY_PRODUCT_TYPE));
    }
    
    /**
     * isTabeLocatorProduct
     */
    public static Boolean isTabeLocatorProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_LOCATOR_PRODUCT_TYPE));
    }
    
    
    
} 