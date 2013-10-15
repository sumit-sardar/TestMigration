package com.ctb.lexington.domain.score.controller.llcontroller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVContentAreaFactData;
import com.ctb.lexington.db.mapper.llmapper.IrsLLContentAreaFactMapper;

public class StudentContentAreaScoresController {
    private StudentSubtestScoresData subtestData;
    private StsTestResultFactData factData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData context;
    private IrsLLContentAreaFactMapper mapper;

    public StudentContentAreaScoresController(Connection conn, StudentSubtestScoresData subtestData, StsTestResultFactData factData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData context) {
        this.subtestData = subtestData;
        this.factData = factData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.context = context;
        mapper = new IrsLLContentAreaFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsLLContentAreaFactData [] facts = getContentAreaFactBeans();
        for(int i=0;i<facts.length;i++) {
        	IrsLLContentAreaFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsLLContentAreaFactData [] getContentAreaFactBeans() {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList facts = new ArrayList();
            for(int i=0;i<contentAreas.length;i++) {
               StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null &&
                    ("T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
                   StudentSubtestScoresDetails subtest = subtestData.get(contentAreas[i].getSubtestId());
                   IrsLLContentAreaFactData newFact = new IrsLLContentAreaFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   newFact.setFormid(new Long("A".equals(currData.getContentAreas()[0].getSubtestForm())?7:
                       "B".equals(currData.getContentAreas()[0].getSubtestForm())?8:
                           ("Espa?ol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                          		 || "Espanol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                          		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm())
                          		 || "Espa?ol A".equals(currData.getContentAreas()[0].getSubtestForm()) 
                          		 || "Espanol A".equals(currData.getContentAreas()[0].getSubtestForm()) 
                          		 || "Español A".equals(currData.getContentAreas()[0].getSubtestForm()))?9:
                          		"C".equals(currData.getContentAreas()[0].getSubtestForm())?15:
                          				"D".equals(currData.getContentAreas()[0].getSubtestForm())?16:
                          					("ESP B".equals(currData.getContentAreas()[0].getSubtestForm()) || "Espa?ol B".equals(currData.getContentAreas()[0].getSubtestForm()) || "Espanol B".equals(currData.getContentAreas()[0].getSubtestForm()) || "Español B".equals(currData.getContentAreas()[0].getSubtestForm()))?17:10));
                   
                   newFact.setGradeid(context.getGradeId());
                   newFact.setLevelid(new Long("K".equals(currData.getContentAreas()[0].getSubtestLevel())?16:
                       "1".equals(currData.getContentAreas()[0].getSubtestLevel())?17:
                       "2-3".equals(currData.getContentAreas()[0].getSubtestLevel())?18:
                       "4-5".equals(currData.getContentAreas()[0].getSubtestLevel())?19:
                       "6-8".equals(currData.getContentAreas()[0].getSubtestLevel())?20:
                       "9-12".equals(currData.getContentAreas()[0].getSubtestLevel())?21:22));
                   newFact.setNationalPercentile((fact.getNationalPercentile()==null)?null:new Long(fact.getNationalPercentile().longValue()));
                   newFact.setNormalCurveEquivalent((fact.getNormalCurveEquivalent()==null)?null:new Long(fact.getNormalCurveEquivalent().longValue()));
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setPercentageMastery((fact.getPercentObjectiveMastery()==null)?null:fact.getPercentObjectiveMastery());
                   newFact.setPercentObtained(fact.getDecimalPercentObtained());
                   newFact.setPointsAttempted(fact.getPointsAttempted());
                   newFact.setPointsObtained(fact.getPointsObtained());
                   newFact.setPointsPossible(fact.getPointsPossible());
                   newFact.setProgramid(context.getProgramId());
                   newFact.setScaleScore((fact.getScaleScore()==null)?null:new Long(fact.getScaleScore().longValue()));
                   newFact.setProficencyLevel((fact.getPerformanceLevelCode()==null)?null:new Long(fact.getPerformanceLevelCode()));
                   //newFact.setPercentileRank((fact.getPercentileRank()==null)?null:fact.getPercentileRank());
                   newFact.setLexileValue((fact.getLexileValue()==null)?null: fact.getLexileValue());
                   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setSubjectid(contentAreas[i].getSubjectId());
                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
                   Timestamp subtestTime = testData.getBySubtestId(contentAreas[i].getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                   if(subtestTime == null) subtestTime = context.getTestCompletionTimestamp();
                   newFact.setTestCompletionTimestamp(subtestTime);
                   newFact.setAttr2id(context.getDemographicData().getAttr2Id());
                   newFact.setAttr9id(context.getDemographicData().getAttr9Id());
                   newFact.setAttr11id(context.getDemographicData().getAttr11Id());
                   newFact.setAttr12id(context.getDemographicData().getAttr12Id());
                   newFact.setAttr13id(context.getDemographicData().getAttr13Id());
                   newFact.setAttr14id(context.getDemographicData().getAttr14Id());
                   newFact.setAttr15id(context.getDemographicData().getAttr15Id());
                   newFact.setAttr16id(context.getDemographicData().getAttr16Id());
                   newFact.setAttr17id(context.getDemographicData().getAttr17Id());
                   newFact.setAttr18id(context.getDemographicData().getAttr18Id());
                   newFact.setAttr19id(context.getDemographicData().getAttr19Id());
                   newFact.setAttr20id(context.getDemographicData().getAttr20Id());
                   newFact.setAttr21id(context.getDemographicData().getAttr21Id());
                   newFact.setAttr22id(context.getDemographicData().getAttr22Id());
                   newFact.setAttr23id(context.getDemographicData().getAttr23Id());
                   newFact.setAttr25id(context.getDemographicData().getAttr25Id());
                   newFact.setAttr26id(context.getDemographicData().getAttr26Id());
                   newFact.setAttr27id(context.getDemographicData().getAttr27Id());
                   newFact.setAttr28id(context.getDemographicData().getAttr28Id());
                   newFact.setAttr29id(context.getDemographicData().getAttr29Id());
                   newFact.setAttr30id(context.getDemographicData().getAttr30Id());
                   newFact.setAttr31id(context.getDemographicData().getAttr31Id());
                   newFact.setAttr32id(context.getDemographicData().getAttr32Id());
                   newFact.setAttr33id(context.getDemographicData().getAttr33Id());
                   newFact.setAttr34id(context.getDemographicData().getAttr34Id());
                   newFact.setAttr35id(context.getDemographicData().getAttr35Id());
                   newFact.setAttr36id(context.getDemographicData().getAttr36Id());
                   newFact.setAttr37id(context.getDemographicData().getAttr37Id());
                   facts.add(newFact);
               } else {
            	   IrsLLContentAreaFactData newFact = new IrsLLContentAreaFactData();
            	   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(new Long (2));
                   facts.add(newFact);
               }
            }
        return (IrsLLContentAreaFactData []) facts.toArray(new IrsLLContentAreaFactData[0]);
    }
}
