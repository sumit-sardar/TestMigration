package com.ctb.lexington.domain.score.controller.tvcontroller;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVSecObjFactData;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVSecObjFactMapper;
import com.ctb.lexington.domain.teststructure.MasteryLevel;
import java.math.BigDecimal;
import java.sql.Timestamp;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTVPrimObjFactMapper poMapper;
    private IrsTVSecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsTVPrimObjFactMapper(conn);
        soMapper = new IrsTVSecObjFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTVPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
            IrsTVPrimObjFactData newFact = pfacts[i];
            poMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                poMapper.insert(newFact);
            }
        }
        
        IrsTVSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
            IrsTVSecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                soMapper.insert(newFact);
            }
        }
    }
    
    public IrsTVPrimObjFactData [] getPrimObjFactBeans() {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        IrsTVPrimObjFactData [] primObjFacts = new IrsTVPrimObjFactData[prims.length];
            for(int i=0;i<prims.length;i++) {
                PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
                primObjFacts[i] = new IrsTVPrimObjFactData();
                primObjFacts[i].setPrimObjid(new Long(Long.parseLong(String.valueOf(prims[i].getProductId()) + String.valueOf(prims[i].getPrimaryObjectiveId()))));
                StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
                if(details != null) {
                    // scores
                    primObjFacts[i].setPointsObtained(details.getPointsObtained());
                    primObjFacts[i].setPointsPossible(details.getPointsPossible());
                    if(details != null && details.getPercentObtained() != null) {
                        MasteryLevel mastery = MasteryLevel.masteryLevelForPercentage(details.getPercentObtained().intValue());
                        details.setMasteryLevel(mastery.getCode());
                    }
                    primObjFacts[i].setMasteryLevelid( new Long( 
                                                        "NM".equals(details.getMasteryLevel())?1:
                                                        "PM".equals(details.getMasteryLevel())?2:
                                                        "M".equals(details.getMasteryLevel())?3:
                                                        "NON-MASTERY".equals(details.getMasteryLevel())?1:
                                                        "PARTIAL MASTERY".equals(details.getMasteryLevel())?2:
                                                        "MASTERY".equals(details.getMasteryLevel())?3:
                                                        "Not Mastered".equals(details.getMasteryLevel())?1:
                                                        "Partially Mastered".equals(details.getMasteryLevel())?2:
                                                        "Mastered".equals(details.getMasteryLevel())?3:4 ));
                    primObjFacts[i].setPercentObtained(details.getPercentObtained());
                    primObjFacts[i].setPointsAttempted(details.getPointsAttempted());
                    primObjFacts[i].setCurrentResultid(contextData.getCurrentResultId());
                } 
                if(details == null || "F".equals(details.getAtsArchive())) {
                    primObjFacts[i].setCurrentResultid(new Long(2));
                }
                // dim ids from context
                primObjFacts[i].setAssessmentid(contextData.getAssessmentId());
                primObjFacts[i].setFormid(new Long(
                                            "B".equals(prim.getSubtestForm())?4:
                                            "G".equals(prim.getSubtestForm())?5:
                                            "1".equals(prim.getSubtestForm())?4:3));
                primObjFacts[i].setGradeid(contextData.getGradeId());
                primObjFacts[i].setLevelid(new Long(
                                            "13".equals(prim.getSubtestLevel())?7:
                                            "14".equals(prim.getSubtestLevel())?8:
                                            "15".equals(prim.getSubtestLevel())?9:
                                            "16".equals(prim.getSubtestLevel())?10:
                                            "17".equals(prim.getSubtestLevel())?11:
                                            "18".equals(prim.getSubtestLevel())?12:
                                            "19".equals(prim.getSubtestLevel())?13:
                                            "19-20".equals(prim.getSubtestLevel())?14:
                                            "19/20".equals(prim.getSubtestLevel())?14:
                                            "20".equals(prim.getSubtestLevel())?14:
                                            "12".equals(currData.getContentAreas()[0].getSubtestLevel())?30:
                                            "21".equals(currData.getContentAreas()[0].getSubtestLevel())?31:
                                            "22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                            "21-22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                            "21/22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:15));
                primObjFacts[i].setOrgNodeid(contextData.getOrgNodeId());
                primObjFacts[i].setProgramid(contextData.getProgramId());
                primObjFacts[i].setSessionid(contextData.getSessionId());
                primObjFacts[i].setStudentid(contextData.getStudentId());
                primObjFacts[i].setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = testData.getBySubtestId(prim.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                primObjFacts[i].setTestCompletionTimestamp(subtestTime);  
                primObjFacts[i].setAttr1id(contextData.getDemographicData().getAttr1Id());
                primObjFacts[i].setAttr2id(contextData.getDemographicData().getAttr2Id());
                primObjFacts[i].setAttr3id(contextData.getDemographicData().getAttr3Id());
                primObjFacts[i].setAttr4id(contextData.getDemographicData().getAttr4Id());
                primObjFacts[i].setAttr5id(contextData.getDemographicData().getAttr5Id());
                primObjFacts[i].setAttr6id(contextData.getDemographicData().getAttr6Id());
                primObjFacts[i].setAttr7id(contextData.getDemographicData().getAttr7Id());
                primObjFacts[i].setAttr8id(contextData.getDemographicData().getAttr8Id());
                primObjFacts[i].setAttr9id(contextData.getDemographicData().getAttr9Id());
                primObjFacts[i].setAttr10id(contextData.getDemographicData().getAttr10Id());
                primObjFacts[i].setAttr11id(contextData.getDemographicData().getAttr11Id());
                primObjFacts[i].setAttr12id(contextData.getDemographicData().getAttr12Id());
                primObjFacts[i].setAttr13id(contextData.getDemographicData().getAttr13Id());
                primObjFacts[i].setAttr14id(contextData.getDemographicData().getAttr14Id());
                primObjFacts[i].setAttr15id(contextData.getDemographicData().getAttr15Id());
                primObjFacts[i].setAttr16id(contextData.getDemographicData().getAttr16Id());
                primObjFacts[i].setNationalAverage(prim.getNationalAverage() == null?new BigDecimal(0):prim.getNationalAverage());
            }
        return primObjFacts;
    }
    
    public IrsTVSecObjFactData [] getSecObjFactBeans() {
        SecondaryObjective [] secs = currData.getSecondaryObjectives();
        IrsTVSecObjFactData [] secObjFacts = new IrsTVSecObjFactData[secs.length];
            for(int i=0;i<secs.length;i++) {
                SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
                secObjFacts[i] = new IrsTVSecObjFactData();
                secObjFacts[i].setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
                StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
                if(details != null) {
                    // scores
                    secObjFacts[i].setPointsObtained(details.getPointsObtained());
                    secObjFacts[i].setPointsPossible(secs[i].getSecondaryObjectivePointsPossible());
                    secObjFacts[i].setMasteryLevelid( new Long( 
                                                        "Not Mastered".equals(details.getMasteryLevel())?1:
                                                        "Partially Mastered".equals(details.getMasteryLevel())?2:
                                                        "Mastered".equals(details.getMasteryLevel())?3:4 ));
                    secObjFacts[i].setPercentObtained(details.getPercentObtained());
                    secObjFacts[i].setPointsAttempted(details.getPointsAttempted());
                    secObjFacts[i].setCurrentResultid(contextData.getCurrentResultId());
                }
                if(details == null || "F".equals(details.getAtsArchive())) {
                    secObjFacts[i].setCurrentResultid(new Long(2));
                }
                // dim ids from context
                secObjFacts[i].setAssessmentid(contextData.getAssessmentId());
                secObjFacts[i].setFormid(new Long(
                                            "B".equals(sec.getSubtestForm())?4:
                                            "G".equals(sec.getSubtestForm())?5:
                                            "1".equals(sec.getSubtestForm())?4:3));
                secObjFacts[i].setGradeid(contextData.getGradeId());
                secObjFacts[i].setLevelid(new Long(
                                            "13".equals(sec.getSubtestLevel())?7:
                                            "14".equals(sec.getSubtestLevel())?8:
                                            "15".equals(sec.getSubtestLevel())?9:
                                            "16".equals(sec.getSubtestLevel())?10:
                                            "17".equals(sec.getSubtestLevel())?11:
                                            "18".equals(sec.getSubtestLevel())?12:
                                            "19".equals(sec.getSubtestLevel())?13:
                                            "19-20".equals(sec.getSubtestLevel())?14:
                                            "19/20".equals(sec.getSubtestLevel())?14:
                                            "20".equals(sec.getSubtestLevel())?14:
                                            "12".equals(currData.getContentAreas()[0].getSubtestLevel())?30:
                                            "21".equals(currData.getContentAreas()[0].getSubtestLevel())?31:
                                            "22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                            "21-22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:
                                            "21/22".equals(currData.getContentAreas()[0].getSubtestLevel())?32:15));
                secObjFacts[i].setOrgNodeid(contextData.getOrgNodeId());
                secObjFacts[i].setProgramid(contextData.getProgramId());
                secObjFacts[i].setSessionid(contextData.getSessionId());
                secObjFacts[i].setStudentid(contextData.getStudentId());
                secObjFacts[i].setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                secObjFacts[i].setTestCompletionTimestamp(subtestTime);  
                secObjFacts[i].setAttr1id(contextData.getDemographicData().getAttr1Id());
                secObjFacts[i].setAttr2id(contextData.getDemographicData().getAttr2Id());
                secObjFacts[i].setAttr3id(contextData.getDemographicData().getAttr3Id());
                secObjFacts[i].setAttr4id(contextData.getDemographicData().getAttr4Id());
                secObjFacts[i].setAttr5id(contextData.getDemographicData().getAttr5Id());
                secObjFacts[i].setAttr6id(contextData.getDemographicData().getAttr6Id());
                secObjFacts[i].setAttr7id(contextData.getDemographicData().getAttr7Id());
                secObjFacts[i].setAttr8id(contextData.getDemographicData().getAttr8Id());
                secObjFacts[i].setAttr9id(contextData.getDemographicData().getAttr9Id());
                secObjFacts[i].setAttr10id(contextData.getDemographicData().getAttr10Id());
                secObjFacts[i].setAttr11id(contextData.getDemographicData().getAttr11Id());
                secObjFacts[i].setAttr12id(contextData.getDemographicData().getAttr12Id());
                secObjFacts[i].setAttr13id(contextData.getDemographicData().getAttr13Id());
                secObjFacts[i].setAttr14id(contextData.getDemographicData().getAttr14Id());
                secObjFacts[i].setAttr15id(contextData.getDemographicData().getAttr15Id());
                secObjFacts[i].setAttr16id(contextData.getDemographicData().getAttr16Id());
            }
        return secObjFacts;
    }
}