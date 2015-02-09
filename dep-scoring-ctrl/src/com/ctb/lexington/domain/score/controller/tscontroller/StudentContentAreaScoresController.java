package com.ctb.lexington.domain.score.controller.tscontroller;

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
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCContentAreaFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCContentAreaFactMapper;

public class StudentContentAreaScoresController {
    private StudentSubtestScoresData subtestData;
    private StsTestResultFactData factData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData context;
    private IrsTASCContentAreaFactMapper mapper;

    public StudentContentAreaScoresController(Connection conn, StudentSubtestScoresData subtestData, StsTestResultFactData factData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData context) {
        this.subtestData = subtestData;
        this.factData = factData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.context = context;
        mapper = new IrsTASCContentAreaFactMapper(conn);
    }

    public void run() throws SQLException {
    	IrsTASCContentAreaFactData [] facts = getContentAreaFactBeans();
    	for(int i=0;i<facts.length;i++) {
            IrsTASCContentAreaFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTASCContentAreaFactData [] getContentAreaFactBeans() {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList facts = new ArrayList();
            for(int i=0;i<contentAreas.length;i++) {
               StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null && 
                    ("T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
                   StudentSubtestScoresDetails subtest = subtestData.get(contentAreas[i].getSubtestId());
                   IrsTASCContentAreaFactData newFact = new IrsTASCContentAreaFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   newFact.setFormid(adminData.getFormId());
                   newFact.setGradeid(context.getGradeId());
                   newFact.setLevelid(new Long(
                                            "21-22".equals(contentAreas[i].getSubtestLevel())?34:35));
                   newFact.setNationalPercentile((fact.getNationalPercentile()==null)?null:new Long(fact.getNationalPercentile().longValue()));
                   newFact.setNormalCurveEquivalent((fact.getNormalCurveEquivalent()==null)?null:new Long(fact.getNormalCurveEquivalent().longValue()));
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setProficiencyLevel((fact.getPerformanceLevelCode()==null)?null:new Long(fact.getPerformanceLevelCode()));
                   newFact.setPercentObtained(fact.getPercentObtained());
                   newFact.setPointsAttempted(fact.getPointsAttempted());
                   newFact.setPointsObtained(fact.getPointsObtained());
                   newFact.setPointsPossible(contentAreas[i].getContentAreaPointsPossible());
                   newFact.setProgramid(context.getProgramId());
                   newFact.setScaleScore((fact.getScaleScore()==null)?null:new Long(fact.getScaleScore().longValue()));
                   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setSubjectid(contentAreas[i].getSubjectId());
                   newFact.setTestStartTimestamp(context.getTestStartTimestamp());
                   Timestamp subtestTime = testData.getBySubtestId(contentAreas[i].getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                   if(subtestTime == null) subtestTime = context.getTestCompletionTimestamp();
                   newFact.setTestCompletionTimestamp(subtestTime);
                   newFact.setSubtestScoringStatus(fact.getSubtestScoringStatus());
                   newFact.setProficiencyRange(fact.getProficiencyRange());
                   facts.add(newFact);
               } else {
            	   IrsTASCContentAreaFactData newFact = new IrsTASCContentAreaFactData();
            	   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(new Long (2));
                   facts.add(newFact);
               }
            }
        return (IrsTASCContentAreaFactData []) facts.toArray(new IrsTASCContentAreaFactData[0]);
    }
}