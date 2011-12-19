package com.ctb.lexington.domain.score.controller.tacontroller;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEContentAreaFactData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEContentAreaFactMapper;
import com.ctb.lexington.db.mapper.StudentSubtestScoresMapper;
import java.sql.Timestamp;
import java.util.ArrayList;

public class StudentContentAreaScoresController {
    private StudentSubtestScoresData subtestData;
    private StsTestResultFactData factData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData context;
    private IrsTABEContentAreaFactMapper mapper;

    public StudentContentAreaScoresController(Connection conn, StudentSubtestScoresData subtestData, StsTestResultFactData factData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData context) {
        this.subtestData = subtestData;
        this.factData = factData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.context = context;
        mapper = new IrsTABEContentAreaFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTABEContentAreaFactData [] facts = getContentAreaFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTABEContentAreaFactData newFact = facts[i];
            mapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                System.out.println("TABECAFact record currency: " + mapper.isTABECAFactCurrent(newFact));
                mapper.insert(newFact);
            }
        }
    }
    
    public IrsTABEContentAreaFactData [] getContentAreaFactBeans() {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList facts = new ArrayList();
            for(int i=0;i<contentAreas.length;i++) {
               StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null && fact.getScaleScore() != null) {
                   StudentSubtestScoresDetails subtest = subtestData.get(contentAreas[i].getSubtestId());
                   IrsTABEContentAreaFactData newFact = new IrsTABEContentAreaFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   newFact.setFormid(new Long(
                                            "CAT".equals(contentAreas[i].getSubtestForm())?13:14));
                   if(fact.getGradeEquivalent() != null) {
                        newFact.setGradeEquivalent( new Float(Float.parseFloat(fact.getGradeEquivalent().replace('+', '9'))));
                   }
                   newFact.setGradeid(context.getGradeId());
                   newFact.setLevelid(new Long(
                                            "CAT".equals(contentAreas[i].getSubtestLevel())?23:24));
                   newFact.setNationalPercentile((fact.getNationalPercentile()==null)?null:new Long(fact.getNationalPercentile().longValue()));
                   newFact.setNationalStanine((fact.getNationalStanine()==null)?null:new Long(fact.getNationalStanine().longValue()));
                   newFact.setNormalCurveEquivalent((fact.getNormalCurveEquivalent()==null)?null:new Long(fact.getNormalCurveEquivalent().longValue()));
                   if(fact.getScaleScore() != null && "Reading".equals(contentAreas[i].getContentAreaName())) {
                            if (fact.getScaleScore().longValue() <= 367)
                                newFact.setNrsLevelid(new Long(1));
                            else if (fact.getScaleScore().longValue() <= 460)
                                newFact.setNrsLevelid(new Long(2));
                            else if (fact.getScaleScore().longValue() <= 517)
                                newFact.setNrsLevelid(new Long(3));
                            else if (fact.getScaleScore().longValue() <= 566)
                                newFact.setNrsLevelid(new Long(4));
                            else if (fact.getScaleScore().longValue() <= 595)
                                newFact.setNrsLevelid(new Long(5));
                            else newFact.setNrsLevelid(new Long(6));
                   } else if(fact.getScaleScore() != null && "Language".equals(contentAreas[i].getContentAreaName())) {
                            if (fact.getScaleScore().longValue() <= 389)
                                newFact.setNrsLevelid(new Long(1));
                            else if (fact.getScaleScore().longValue() <= 490)
                                newFact.setNrsLevelid(new Long(2));
                            else if (fact.getScaleScore().longValue() <= 523)
                                newFact.setNrsLevelid(new Long(3));
                            else if (fact.getScaleScore().longValue() <= 559)
                                newFact.setNrsLevelid(new Long(4));
                            else if (fact.getScaleScore().longValue() <= 585)
                                newFact.setNrsLevelid(new Long(5));
                            else newFact.setNrsLevelid(new Long(6));
                   } else {
                            newFact.setNrsLevelid(new Long(0));
                   }
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setPercentageMastery((fact.getPercentObjectiveMastery()==null)?null:fact.getPercentObjectiveMastery());
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
                   newFact.setAttr1id(context.getDemographicData().getAttr1Id());
                   newFact.setAttr2id(context.getDemographicData().getAttr2Id());
                   newFact.setAttr3id(context.getDemographicData().getAttr3Id());
                   newFact.setAttr4id(context.getDemographicData().getAttr4Id());
                   newFact.setAttr5id(context.getDemographicData().getAttr5Id());
                   newFact.setAttr6id(context.getDemographicData().getAttr6Id());
                   newFact.setAttr7id(context.getDemographicData().getAttr7Id());
                   newFact.setAttr8id(context.getDemographicData().getAttr8Id());
                   newFact.setAttr9id(context.getDemographicData().getAttr9Id());
                   newFact.setAttr10id(context.getDemographicData().getAttr10Id());
                   newFact.setAttr11id(context.getDemographicData().getAttr11Id());
                   newFact.setAttr12id(context.getDemographicData().getAttr12Id());
                   newFact.setAttr13id(context.getDemographicData().getAttr13Id());
                   newFact.setAttr14id(context.getDemographicData().getAttr14Id());
                   newFact.setAttr15id(context.getDemographicData().getAttr15Id());
                   newFact.setAttr16id(context.getDemographicData().getAttr16Id());
                   facts.add(newFact);
               } else {
            	   IrsTABEContentAreaFactData newFact = new IrsTABEContentAreaFactData();
            	   newFact.setSessionid(context.getSessionId());
            	   newFact.setStudentid(context.getStudentId());
            	   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
            	   newFact.setCurrentResultid(new Long (2));
            	   facts.add(newFact);
               }
            }
        return (IrsTABEContentAreaFactData []) facts.toArray(new IrsTABEContentAreaFactData[0]);
    }
}