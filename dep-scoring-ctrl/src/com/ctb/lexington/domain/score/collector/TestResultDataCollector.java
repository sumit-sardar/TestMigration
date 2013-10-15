package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.ctb.lexington.data.ItemResponseVO;
import com.ctb.lexington.data.StudentItemSetStatusRecord;
import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.ScoreMoveData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StudentData;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.TestRosterData;
import com.ctb.lexington.db.data.UrlData;
import com.ctb.lexington.db.data.UserData;
import com.ctb.lexington.db.data.WsTvStudentItemResponseData;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.mapper.ItemResponseMapper;
import com.ctb.lexington.db.mapper.StudentItemSetStatusMapper;
import com.ctb.lexington.domain.score.ConnectionProvider;
import com.ctb.lexington.domain.teststructure.CompletionStatus;
import com.ctb.lexington.domain.teststructure.ScoringStatus;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SimpleCache;

public class TestResultDataCollector {
    private Long oasRosterId;
    private Connection oasConnection;

    // TODO: do we really need to throw all these exceptions??
    public ScoreMoveData collect(Long oasRosterId, ConnectionProvider connectionProvider)
            throws SecurityException, IllegalStateException,
            CTBSystemException, SQLException, DataException, NamingException {
        this.oasRosterId = oasRosterId;

        oasConnection = connectionProvider.getOASConnection();

        ScoreMoveData data = new ScoreMoveData();

        data.setTestData(getTestData());
        data.setDemographicData(getDemographicData());
        data.setAdminData(getAdminData());
        data.setOrgNodeData(getOrgNodeData());
        data.setStudentData(getStudentData());
                
        String key = String.valueOf(data.getAdminData().getAssessmentId());
        CurriculumData cachedCurriculumData = (CurriculumData) SimpleCache.checkCache("curriculumData", key, "scoringUser");
        if(cachedCurriculumData == null) {
        	cachedCurriculumData = getCurriculumData(data.getAdminData().getAssessmentType());
			SimpleCache.cacheResult("curriculumData", key, cachedCurriculumData, "scoringUser");
        }
        ContentArea[] allContentArea = cachedCurriculumData.getContentAreas();
        data.setCurriculumData(filterCurricula(cachedCurriculumData));
        data.getCurriculumData().setAllContentAreas(allContentArea);
              
        data.setUserData(getUserData());
        data.setTestRosterData(getTestRosterData());
        data.setStudentItemResponseData(getStudentItemResponseData());

        data.setTestRosterId(oasRosterId); // as in OAS
        data.setTestedFlag(CTBConstants.HARDCODEDTESTEDFLAG);
        data.setScoreDate(null);
        data.setRetestFlag(CTBConstants.HARDCODEDRETESTFLAG);
        String sessionNumber = data.getTestData().get(0).getSessionNumber();//REDTAG: .get(0) wrong
        data.setFileId(sessionNumber);
        data.setFileName(CTBConstants.OAS + " " + sessionNumber);
        data.setPassFail(CTBConstants.HARDCODEDPASSFAILFLAG);
        data.setValidScore(CTBConstants.HARDCODEDVALIDSCORE);

        data.setStudentItemScoreData(new StudentItemScoreData());
        data.setStudentScoreSummaryData(new StudentScoreSummaryData());
        data.setStudentSubtestScoresData(new StudentSubtestScoresData());
        data.setStsTestResultFactData(new StsTestResultFactData());
        data.setStsTotalStudentScoreData(new StsTotalStudentScoreData());
        data.setCaResponseWsTv(new WsTvStudentItemResponseData());
        if(data.getAdminData().getProductId() == 3700) {
        	data.setUrlData(getAcuityUrl(data.getAdminData().getSessionId()));
        }

        if(data.getAdminData().getProgramId() == null) {
        	throw new CTBSystemException("Program ID is null for roster: " + oasRosterId);
        }
        
        System.out.println("***** SCORING: TestResultDataCollector: collect: finished collecting context");
        
        return data;
    }

    private CurriculumData filterCurricula(CurriculumData currData) {
        StudentItemSetStatusMapper mapper = new StudentItemSetStatusMapper(oasConnection);
        Map subtestMap = mapper.getMapOfSubtestStatusForTestRoster(oasRosterId);
        
        CurriculumData filteredCurrData = new CurriculumData();
        
        filteredCurrData.setComposites(currData.getComposites());
        
        ContentArea [] contentAreas = currData.getContentAreas();
        
        ArrayList contentAreaList = new ArrayList();
        ArrayList primObjList = new ArrayList();
        ArrayList secObjList = new ArrayList();
        ArrayList itemList = new ArrayList();
        
        for(int i=0;i<contentAreas.length;i++) {
            StudentItemSetStatusRecord subtest = (StudentItemSetStatusRecord) subtestMap.get(contentAreas[i].getSubtestId());
            String contentAreaName = contentAreas[i].getContentAreaName();
            if(subtest != null && !"SC".equals(subtest.getCompletionStatus()) 
                               && !"NT".equals(subtest.getCompletionStatus())
                               && !"IP".equals(subtest.getCompletionStatus())
                               && !"IN".equals(subtest.getCompletionStatus()) || ("Comprehension".equals(contentAreaName) 
                               || "Oral".equals(contentAreaName) || "Productive".equals(contentAreaName) ||"Literacy".equals(contentAreaName) 
                               && ("C".equals(contentAreas[i].getSubtestForm()) || "ESP B".equals(contentAreas[i].getSubtestForm()) || "Espa?ol B".equals(contentAreas[i].getSubtestForm()) || "Espanol B".equals(contentAreas[i].getSubtestForm()) || "Español B".equals(contentAreas[i].getSubtestForm())))) { // added for laslink second edition
                contentAreaList.add(contentAreas[i]);
                PrimaryObjective [] primObjs = currData.getPrimaryObjectives();
                for(int j=0;j<primObjs.length;j++) {
                    if(primObjs[j].getContentAreaId().equals(contentAreas[i].getContentAreaId()) &&
                        ((contentAreas[i].getSubtestLevel().equals("CAT") || contentAreas[i].getSubtestLevel().equalsIgnoreCase("Adaptive")) 
                        		|| primObjs[j].getSubtestLevel().equals(contentAreas[i].getSubtestLevel()))) { // Changes for TABE Cat
                        primObjList.add(primObjs[j]);
                        SecondaryObjective [] secObjs = currData.getSecondaryObjectives();
                        for(int k=0;k<secObjs.length;k++) {
                            if(((secObjs[k].getPrimaryObjectiveId() != null && secObjs[k].getPrimaryObjectiveId().equals(primObjs[j].getPrimaryObjectiveId()))||
                            	(("C".equals(contentAreas[i].getSubtestForm()) || "ESP B".equals(contentAreas[i].getSubtestForm()) || "Espa?ol B".equals(contentAreas[i].getSubtestForm()) || "Espanol B".equals(contentAreas[i].getSubtestForm()) || "Español B".equals(contentAreas[i].getSubtestForm())) && secObjs[k].getSecondaryObjectiveName().contains("Overall")))	&&
                                secObjs[k].getSubtestLevel().equals(contentAreas[i].getSubtestLevel())) {
                                secObjList.add(secObjs[k]);
                                Item [] items = currData.getItems();
                                for(int l=0;l<items.length;l++) {
                                    if(items[l].getSecondaryObjectiveId().equals(secObjs[k].getSecondaryObjectiveId()) &&
                                        items[l].getSubtestLevel().equals(contentAreas[i].getSubtestLevel())) {
                                        itemList.add(items[l]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        filteredCurrData.setContentAreas((ContentArea[]) contentAreaList.toArray(new ContentArea[0]));
        filteredCurrData.setPrimaryObjectives((PrimaryObjective[]) primObjList.toArray(new PrimaryObjective[0]));
        filteredCurrData.setSecondaryObjectives((SecondaryObjective[]) secObjList.toArray(new SecondaryObjective[0]));
        filteredCurrData.setItems((Item[]) itemList.toArray(new Item[0]));
        
        return filteredCurrData;
    }

    private TestRosterData getTestRosterData() {
        TestRosterData rosterData = new TestRosterData();
        // TODO: set these appropriately from db.
        rosterData.setTestRosterId(oasRosterId);
        rosterData.setRosterScoringStatus(ScoringStatus.IN_PROGRESS.getCode());
        rosterData.setRosterTestCompletionStatus(CompletionStatus.IN_PROGRESS.getCode());
        return rosterData;
    }

    private UserData getUserData() throws SQLException {
        UserCollector collector = new UserCollector(oasConnection);
        return collector.collect(oasRosterId);
    }

    private AdminData getAdminData() throws SQLException {
        AdminCollector collector = new AdminCollector(oasConnection);
        return collector.collectAdminData(oasRosterId);
    }

    private CurriculumData getCurriculumData(String productType) throws SQLException {
        CurriculumCollector collector = new CurriculumCollector(oasConnection);
        return collector.collectCurriculumData(oasRosterId, productType);
    }

    private StudentDemographicData getDemographicData() throws SQLException {
        StudentDemographicsCollector collector = new StudentDemographicsCollector(oasConnection);
        return collector.collectDemographicData(oasRosterId);
    }

    private OrgNodeData getOrgNodeData() throws SQLException, DataException {
        OrgNodeCollector collector = new OrgNodeCollector(oasConnection);
        return collector.collectOrgNodeData(oasRosterId);
    }

    private StudentData getStudentData() throws SQLException {
        StudentCollector collector = new StudentCollector(oasConnection);
        return collector.collectStudentData(oasRosterId);
    }

    private StudentTestData getTestData() throws SQLException, DataException {
        StudentTestCollector collector = new StudentTestCollector(oasConnection);
        return collector.collectStudentTestData(oasRosterId);
    }
    
    private StudentItemResponseData getStudentItemResponseData() throws CTBSystemException {
        StudentItemResponseData data = new StudentItemResponseData();
    	ItemResponseMapper mapper = new ItemResponseMapper(oasConnection);
        List responseVOList = mapper.findItemResponsesByRoster(oasRosterId);
        Iterator iterator = responseVOList.iterator();
        while (iterator.hasNext()) {
        	data.addStudentItemResponseVO((ItemResponseVO) iterator.next());
        }
        return data;
    }
    
    private UrlData getAcuityUrl(Long testAdminId) throws SQLException, DataException {
    	UrlDataCollector collector = new UrlDataCollector(oasConnection);
    	return collector.collectAquityUrlData(testAdminId);
    }
}