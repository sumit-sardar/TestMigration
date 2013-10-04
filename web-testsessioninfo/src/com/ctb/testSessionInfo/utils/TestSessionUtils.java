package com.ctb.testSessionInfo.utils;
import com.ctb.bean.testAdmin.RosterElement;
import com.ctb.bean.testAdmin.RosterElementData;
import com.ctb.bean.testAdmin.ScheduledSession;
import com.ctb.bean.testAdmin.SessionStudent;
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

import com.ctb.testSessionInfo.data.SubtestVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TestSessionUtils
{
    public static final String GENERIC_PRODUCT_TYPE = "genericProductType";
    public static final String TABE_BATTERY_SURVEY_PRODUCT_TYPE = "tabeBatterySurveyProductType";
    public static final String TABE_LOCATOR_PRODUCT_TYPE = "tabeLocatorProductType";
    public static final String TABE_ADAPTIVE_PRODUCT_TYPE = "tabeAdaptiveProductType";
    public static final String LASLINKS_PRODUCT_TYPE = "lasLinksProductType";
    public static final String TASC_PRODUCT_TYPE = "tascProductType";
	

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
    public static RosterElement addStudentToSession(ScheduleTest scheduleTest, String userName, SessionStudent ss, Integer testAdminId)
    {
        RosterElement roster = null;
        try {
            roster = scheduleTest.addStudentToSession(userName, ss, testAdminId);
        }
        catch (Exception be) {
            be.printStackTrace();
        }
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
											    		Integer testAdminId,
											    		StudentManifestData manifestData)  throws CTBBusinessException 
    {
        RosterElement roster = null;
        try {
            roster = scheduleTest.updateManifestForRoster(userName, studentId, null, testAdminId, manifestData);
        }
        //START- Added for deferred defect-64306
        catch (InsufficientLicenseQuantityException e)
        {
            e.printStackTrace();
            throw e;         
        }
        //END- Added for deferred defect-64306
        catch (Exception be) {
            be.printStackTrace();
        }
        return roster;
    }

    /**
     * getStudentSubtests
     */
    public static List<SubtestVO> getStudentSubtests(ScheduleTest scheduleTest, String userName, Integer studentId, Integer testAdminId)
    {
        List<SubtestVO> subtestList = new ArrayList<SubtestVO>();

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
    public static List<SubtestVO> getAllSubtestsForTest(ScheduleTest scheduleTest, String userName, Integer itemSetId)
    {
        List<SubtestVO> subtestList = new ArrayList<SubtestVO>();
        try {
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTest(userName, itemSetId, new Boolean(true), null, null, null);
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
    public static List<SubtestVO> getAllSubtestsForTestAdmin(ScheduleTest scheduleTest, String userName, Integer testAdminId)
    {
        List<SubtestVO> subtestList = new ArrayList<SubtestVO>();

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
    public static List<SubtestVO> retrieveSubtests(ScheduledSession scheduledSession)
    {
        List<SubtestVO> subtestList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> setupSessionSubtests(List<SubtestVO> sessionSubtests, List<SubtestVO> selectedSubtests)
    {
        List<SubtestVO> resultList = new ArrayList<SubtestVO>();

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
    public static List<SubtestVO> getAvailableSubtests(List<SubtestVO> subtests, List<SubtestVO> selectedSubtests)
    {
        List<SubtestVO> availableSubtests = new ArrayList<SubtestVO>();
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
                                                Integer itemSetId, Integer locatorItemSetId, List<SubtestVO> subtests)
    {
        String locatorSessionInfo = "";
        SubtestVO subtest = null;

        try {
            TABERecommendedLevel[] trls = scheduleTest.getTABERecommendedLevelForStudent(userName, studentId, itemSetId, locatorItemSetId);

            if (! checkRecommendedlevels(trls, subtests)) {
                return locatorSessionInfo;
            }

            HashMap<Integer,Integer> trlHash = new HashMap<Integer,Integer>();
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
     * this method implemented differently than Rapid Registration
     */
    public static boolean isRecommendedlevels(TABERecommendedLevel[] trls, List<SubtestVO> subtests)
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
                    String srcLevel = subtest   .getLevel() != null ? subtest.getLevel() : "E";
                    String desLevel = trl.getRecommendedLevel() != null ? trl.getRecommendedLevel() : "E";
                    if (! desLevel.equals(srcLevel)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * checkRecommendedlevels
     * this method implemented differently than Rapid Registration
     */
    public static boolean checkRecommendedlevels(TABERecommendedLevel[] trls, List<SubtestVO> subtests)
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
     public static void setDefaultLevels(List<SubtestVO> defaultSubtests, List<SubtestVO> subtests)
     {
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
                                                Integer itemSetId, Integer locatorItemSetId, List<SubtestVO> subtests)
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
                                                Integer itemSetId, Integer locatorItemSetId, List<SubtestVO> subtests)
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
    public static List<String> getLevelOptions(String[] forms)
    {
        List<String> options = new ArrayList<String>();
        for (int i=0 ; i<forms.length ; i++) {
            options.add(forms[i]);
        }
        return options;
    }

    /**
     * getLevelOptions
     */
    public static List<String> getLevelOptions()
    {
        List<String> options = new ArrayList<String>();
        options.add("E");
        options.add("M");
        options.add("D");
        options.add("A");
        return options;
    }

    /**
     * resetSubtestOrder
     */
    public static void resetSubtestOrder(List<SubtestVO> srcList)
    {
        for (int i=0 ; i<srcList.size() ; i++) {
            SubtestVO subtest = (SubtestVO)srcList.get(i);
            subtest.setSequence(String.valueOf(i+1));
        }
    }

    /**
     * getDefaultSubtests
     */
    public static List<SubtestVO> getDefaultSubtests(TestElement[] testElements)
    {
        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> getDefaultSubtests(List<SubtestVO> srcList)
    {
        if (srcList == null)
            return srcList;

        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> getDefaultSubtestsWithoutLocator(List<SubtestVO> srcList)
    {
        if (srcList == null)
            return srcList;

        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> getNonLocatorSubtests(List<SubtestVO> subtests)
    {
        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
            TestElementData suTed = scheduleTest.getSchedulableUnitsForTest(userName, itemSetId, new Boolean(true), null, null, null);
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
    public static SubtestVO getLocatorSubtest(List<SubtestVO> subtests)
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
    public static boolean locatorSubtestPresent(List<SubtestVO> subtests)
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
    public static boolean isLocatorTest(List<SubtestVO> subtests)
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
    
    public static boolean isLocatorSubtest(TestElement subtest)
    {
        return (subtest.getItemSetName().indexOf("Locator") > 0);
    }

    /**
     * sessionHasDefaultLocatorSubtest
     */
    public static boolean sessionHasDefaultLocatorSubtest(List<SubtestVO> subtests)
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
    public static boolean extractLocatorSubtest(List<SubtestVO> subtests)
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
    public static void restoreLocatorSubtest(List<SubtestVO> subtests, SubtestVO locatorSubtest)
    {
        if (locatorSubtest != null) {
            locatorSubtest.setSessionDefault("T");
            if (! isSubtestPresent(subtests, locatorSubtest)) {
                subtests.add(0, locatorSubtest);
            }
            else {
                for (int i=0 ; i<subtests.size() ; i++) {
                    SubtestVO subtest = (SubtestVO)subtests.get(i);
                    if (isLocatorSubtest(subtest)) {
                        subtest.setSessionDefault("T");
                        subtest.setTestAccessCode(locatorSubtest.getTestAccessCode());
                        break;
                    }
                }
            }
            for (int i=0 ; i<subtests.size() ; i++) {
                SubtestVO subtest = (SubtestVO)subtests.get(i);
                subtest.setSequence(String.valueOf(i+1));
                subtest.setLevel(null);     // no level when there is auto locator
            }
        }
    }

    /**
     * getSubtestById
     */
    public static SubtestVO getSubtestById(List<SubtestVO> subtests, Integer subtestId)
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
    public static boolean isSubtestPresent(List<SubtestVO> subtests, SubtestVO srcSubtest)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)subtests.get(i);
            if (subtest.getId().intValue() == srcSubtest.getId().intValue()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSubtestPresent(List<TestElement> subtests, TestElement srcSubtest)
    {
        for (int i=0 ; i<subtests.size() ; i++) {
        	TestElement subtest = (TestElement)subtests.get(i);
            if (subtest.getItemSetId().intValue() == srcSubtest.getItemSetId().intValue()) {
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
    public static List<SubtestVO> retrieveSelectedSubtestsFromRequest(HttpServletRequest request, List<SubtestVO> allSubtests)
    {
        List<SubtestVO> selectedSubtests = new ArrayList<SubtestVO>();
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
    public static void copySessionDefault(List<SubtestVO> allSubtests, List<SubtestVO> selectedSubtests)
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
    public static void copyTestAccessCode(List<SubtestVO> allSubtests, List<SubtestVO> selectedSubtests)
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
    public static void copySubtestLevel(List<SubtestVO> allSubtests, List<SubtestVO> selectedSubtests)
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
     * copySubtestLevelIfNull
     */
    public static void copySubtestLevelIfNull(List<SubtestVO> allSubtests, List<SubtestVO> selectedSubtests)
    {
        for (int i=0 ; i<allSubtests.size() ; i++) {
            SubtestVO subtest = (SubtestVO)allSubtests.get(i);
            for (int j=0 ; j<selectedSubtests.size() ; j++) {
                SubtestVO selSubtest = (SubtestVO)selectedSubtests.get(j);
                if (selSubtest.getId().intValue() == subtest.getId().intValue()) {
                    if (selSubtest.getLevel() == null) {
                        selSubtest.setLevel(subtest.getLevel());
                    }
                    break;
                }
            }
        }
    }

    /**
     * sortSubtestList
     */
    public static List<SubtestVO> sortSubtestList(List<SubtestVO> allSubtests, List<SubtestVO> selectedSubtests)
    {
        List<SubtestVO> desList = new ArrayList<SubtestVO>();
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
    public static void setDefaultLevels(List<SubtestVO> srcList, String level)
    {
        //List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> cloneSubtests(List<SubtestVO> srcList)
    {
        if (srcList == null)
            return srcList;

        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static List<SubtestVO> cloneSubtestsForRegistration(List<SubtestVO> srcList)
    {
        if (srcList == null)
            return srcList;

        List<SubtestVO> resultList = new ArrayList<SubtestVO>();
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
    public static String getStudentDisplayName(List<SessionStudent> students, Integer studentId)
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
        else if (productType.equals("TL"))
            return TABE_LOCATOR_PRODUCT_TYPE;
        else if (productType.equals("TA"))
        	return TABE_ADAPTIVE_PRODUCT_TYPE;
        else if (productType.equals("LL"))
        	return LASLINKS_PRODUCT_TYPE;
        else if (productType.equals("TS"))
        	return TASC_PRODUCT_TYPE;
        else
            return GENERIC_PRODUCT_TYPE;
    }

    /**
     * isTabeProduct
     */
    public static Boolean isTabeProduct(String productType)
    {
        return new Boolean(	(! productType.equals(GENERIC_PRODUCT_TYPE)) && 
        					(! productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE)) &&
							(! productType.equals(TASC_PRODUCT_TYPE)) &&
							(! productType.equals(LASLINKS_PRODUCT_TYPE)));
    }
    
    /**
     * isTabeAdaptiveProduct
     */
    public static Boolean isTabeAdaptiveProduct(String productType)
    {
        return new Boolean(productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE));
    }
    
    /**
     * isTabeOrTabeAdaptiveProduct
     */
    public static Boolean isTabeOrTabeAdaptiveProduct(String productType)
    {
        return new Boolean(	productType.equals(TABE_BATTERY_SURVEY_PRODUCT_TYPE) ||
        					productType.equals(TABE_LOCATOR_PRODUCT_TYPE) ||
        					productType.equals(TABE_ADAPTIVE_PRODUCT_TYPE));
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

    /**
     * isLasLinksProduct
     */
    public static Boolean isLasLinksProduct(String productType)
    {
        return new Boolean(productType.equals(LASLINKS_PRODUCT_TYPE));
    }

    /**
     * isTASCProduct
     */
    public static Boolean isTASCProduct(String productType)
    {
        return new Boolean(productType.equals(TASC_PRODUCT_TYPE));
    }
    
	public static TestElement[] setupSessionSubtests(TestElement[] sessionSubtests,	TestElement[] selectedSubtests) {

        List<TestElement> resultList = new ArrayList<TestElement>();

        // copy back non-default locator subtest from session subtest list into selected subtest list
        // the order of locator subtest will be set at the top of the list
        boolean isLocatorPresent = true;
        if (sessionSubtests.length > 0) {
        	TestElement locatorSubtest  = sessionSubtests[0];
            if (isLocatorSubtest(locatorSubtest) && !(selectedSubtests.length>0 && locatorSubtest.getItemSetId().intValue()==selectedSubtests[0].getItemSetId().intValue()) && locatorSubtest.getSessionDefault().equals("F")) {
                resultList.add(locatorSubtest);
               
            }
        }

        if(  !(sessionSubtests.length > 0 && selectedSubtests.length>0 && sessionSubtests[0].getItemSetId().intValue()==selectedSubtests[0].getItemSetId().intValue()) ){
        	isLocatorPresent = false;
        }
        // set selected subtest to default subtest and copy to new list
        for (int i=0 ; i<selectedSubtests.length ; i++) {
        	TestElement subtest  = selectedSubtests[i];
        	subtest.setSessionDefault("T");
            resultList.add(subtest);
        }

        // copy back non-default subtests from session subtest list into selected subtest list
        // the order of these subtests will be set at the end of the list
        for (int i=0 ; i<sessionSubtests.length ; i++) {
        	TestElement subtest  = sessionSubtests[i];
            if (subtest.getSessionDefault().equals("F")) {
                if (! isSubtestPresent(resultList, subtest)) {
                	if(isLocatorPresent)
                		subtest.setItemSetForm(null);
                    resultList.add(subtest);
                }
            }
        }
        if( !isLocatorPresent )
        	 TestSessionUtils.setDefaultLevel(resultList, "E");

        return resultList.toArray(new TestElement[resultList.size()]);
    
		
	}

	private static void setDefaultLevel(List<TestElement> srcList,	String level) {

        //List<SubtestVO> resultList = new ArrayList<SubtestVO>();
        for (int i=0 ; i<srcList.size() ; i++) {
        	TestElement src = srcList.get(i);
            if (level.equals("1"))
                src.setItemSetForm(level);
            else
            if (src.getItemSetForm() == null)
                src.setItemSetForm(level);
        }
    
		
	}

	public static TestElement getLocatorSubtest(TestElement[] usTes) {
		TestElement locatorSubtest = null;
		
		for (int i=0 ; i<usTes.length ; i++) {
            TestElement te = usTes[i];
            if (te != null) {
                if (te.getItemSetName().indexOf("Locator") > 0) {
                    locatorSubtest = te;
                    break;
                }
            }
        }
		return locatorSubtest;
	}
	 public static void setRecommendedLevelForSession( TestElement[] allSubTests, TABERecommendedLevel[] trls) {
		 TestElement subtest = null;
		for (int i = 0; i < allSubTests.length; i++) {
			subtest = allSubTests[i];
			for (int j = 0; j < trls.length; j++) {
				TABERecommendedLevel trl = trls[j];
				Integer id = trl.getItemSetId();
				if (id.intValue() == subtest.getItemSetId().intValue()) {
					if (trl.getTestAdminName() != null
								&& !"".equals(trl.getTestAdminName())) {
						if (trl.getRecommendedLevel() != null) {
							subtest.setItemSetForm(trl.getRecommendedLevel());
						}
					}
				}
			}
		}
	}

	public static String getLocatorSessionInfo(StudentManifest[] subtests,
			TABERecommendedLevel[] trls) {

		String locatorSessionInfo = "";
		StudentManifest subtest = null;

		if (!checkRecommendedlevels(trls, subtests)) {
			return locatorSessionInfo;
		}

		HashMap<Integer, Integer> trlHash = new HashMap<Integer, Integer>();
		for (int i = 0; i < subtests.length; i++) {
			subtest = subtests[i];
			for (int j = 0; j < trls.length; j++) {
				TABERecommendedLevel trl = trls[j];
				Integer id = trl.getItemSetId();
				if (id.intValue() == subtest.getItemSetId().intValue()) {
					if (trl.getTestAdminName() != null
							&& !"".equals(trl.getTestAdminName())) {
						if (!trlHash.containsKey(trl.getTestAdminId())) {
							String dateStr = DateUtils.formatDateToDateString(trl.getCompletedDate());
							locatorSessionInfo += (trl.getTestAdminName()+ ", " + dateStr + "\n");
							trlHash.put(trl.getTestAdminId(), trl.getTestAdminId());
						}
					}
				}
			}
		}

		return locatorSessionInfo;

	}
	
	public static boolean checkRecommendedlevels(TABERecommendedLevel[] trls, StudentManifest[] subtests)
    {
        if (trls.length == 0) {
            return false;
        }

        for (int i=0 ; i<subtests.length ; i++) {
        	StudentManifest subtest = subtests[i];
            for (int j=0 ; j<trls.length ; j++) {
                TABERecommendedLevel trl = trls[j];
                Integer id = trl.getItemSetId();
                if (id.intValue() == subtest.getItemSetId().intValue()) {
                    boolean same = false;
                    String srcLevel = subtest.getItemSetForm();
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

	public static void copySubtestLevelIfNull(
			Map<Integer, String> allSubtests, StudentManifest[] selectedSubtests) {
		
	for (Map.Entry<Integer, String> entry : allSubtests.entrySet()){
			Integer id = entry.getKey();
			String level = entry.getValue();
			 for (int j=0 ; j<selectedSubtests.length ; j++) {
				 if(selectedSubtests[j].getItemSetId().intValue() == id.intValue()){
					 if(selectedSubtests[j].getItemSetForm()==null){
						 selectedSubtests[j].setItemSetForm(level);
					 }
					 break;
				 }
			 }
			
		}
		
	}
	

}