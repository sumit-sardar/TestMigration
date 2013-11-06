package com.ctb.lexington.domain.score.controller.tscontroller;

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
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCContentAreaFactData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCContentAreaFactMapper;
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
            if(new Long(1).equals(context.getCurrentResultId()))  {
                System.out.println("TASCCAFact record currency: " + mapper.isTASCCAFactCurrent(newFact));
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
                    ("TS CONTENT AREA".equals(contentAreas[i].getContentAreaType()) || "T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
                   StudentSubtestScoresDetails subtest = subtestData.get(contentAreas[i].getSubtestId());
                   IrsTASCContentAreaFactData newFact = new IrsTASCContentAreaFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   /*newFact.setFormid(new Long(
                                            "9".equals(contentAreas[i].getSubtestForm())?1:
                                            "10".equals(contentAreas[i].getSubtestForm())?2:1));*/
                   newFact.setFormid(new Long(18));
                   if(fact.getGradeEquivalent() != null) {
                        newFact.setGradeEquivalent( new Float(Float.parseFloat(fact.getGradeEquivalent().replace('+', '9'))));
                   }
                   newFact.setGradeid(context.getGradeId());
                   newFact.setLevelid(new Long(
                                            "L".equals(contentAreas[i].getSubtestLevel())?1:
                                            "E".equals(contentAreas[i].getSubtestLevel())?2:
                                            "M".equals(contentAreas[i].getSubtestLevel())?3:
                                            "D".equals(contentAreas[i].getSubtestLevel())?4:
                                            "A".equals(contentAreas[i].getSubtestLevel())?5:6));
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
                   
                   newFact.setAttr17id(context.getDemographicData().getAttr17Id());
                   newFact.setAttr18id(context.getDemographicData().getAttr18Id());
                   newFact.setAttr19id(context.getDemographicData().getAttr19Id());
                   if(context.getDemographicData().getAttr20Id() != null) {
                	   newFact.setAttr20id(Long.parseLong(context.getDemographicData().getAttr20Id()));
                   } else {
                	   newFact.setAttr20id(null);
                   }
                   if(context.getDemographicData().getAttr21Id() != null) {
                	   newFact.setAttr21id(Long.parseLong(context.getDemographicData().getAttr21Id()));
                   } else {
                	   newFact.setAttr21id(null);
                   }
                   //newFact.setAttr20id(Long.parseLong(context.getDemographicData().getAttr20Id() == null ? "8" : context.getDemographicData().getAttr20Id()));
                   //newFact.setAttr20id(Long.parseLong(context.getDemographicData().getAttr20Id() == null ? "0" : context.getDemographicData().getAttr20Id()));
                   newFact.setAttr22id(context.getDemographicData().getAttr22Id());
                   newFact.setAttr24id(context.getDemographicData().getAttr24Id());
                   newFact.setAttr25id(context.getDemographicData().getAttr25Id());
                   newFact.setAttr26id(context.getDemographicData().getAttr26Id());
                   newFact.setAttr27id(context.getDemographicData().getAttr27Id());
                   newFact.setAttr28id(context.getDemographicData().getAttr28Id());
                   newFact.setAttr29id(context.getDemographicData().getAttr29Id());
                   newFact.setAttr30id(context.getDemographicData().getAttr30Id());
                   newFact.setAttr36id(context.getDemographicData().getAttr36Id());
                   newFact.setAttr37id(context.getDemographicData().getAttr37Id());
                   
                   System.out.println(" Scoring Status " + fact.getSubtestScoringStatus());
                   newFact.setSubtestScoringStatus(fact.getSubtestScoringStatus());
                   facts.add(newFact);
               }
            }
        return (IrsTASCContentAreaFactData []) facts.toArray(new IrsTASCContentAreaFactData[0]);
    }
}