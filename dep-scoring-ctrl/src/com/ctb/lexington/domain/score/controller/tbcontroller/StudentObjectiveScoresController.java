package com.ctb.lexington.domain.score.controller.tbcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABESecObjFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABESecObjFactMapper;
import java.sql.Timestamp;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTABEPrimObjFactMapper poMapper;
    private IrsTABESecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsTABEPrimObjFactMapper(conn);
        soMapper = new IrsTABESecObjFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTABEPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
            IrsTABEPrimObjFactData newFact = pfacts[i];
            poMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                poMapper.insert(newFact);
            }
        }
        
        IrsTABESecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
            IrsTABESecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                soMapper.insert(newFact);
            }
        }
    }
    
    public IrsTABEPrimObjFactData [] getPrimObjFactBeans() {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        ArrayList primaries = new ArrayList();
        for(int i=0;i<prims.length;i++) {
            PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTABEPrimObjFactData primObjFact = new IrsTABEPrimObjFactData();
                primObjFact.setPrimObjid(new Long(Long.parseLong(String.valueOf(prims[i].getProductId()) + String.valueOf(prims[i].getPrimaryObjectiveId()))));
                primObjFact.setPointsObtained(details.getPointsObtained());
                primObjFact.setPointsPossible(details.getPointsPossible());
                primObjFact.setMasteryLevelid( new Long( 
                                                    "NM".equals(details.getMasteryLevel())?1:
                                                    "PM".equals(details.getMasteryLevel())?2:
                                                    "M".equals(details.getMasteryLevel())?3:
                                                    "NON-MASTERY".equals(details.getMasteryLevel())?1:
                                                    "PARTIAL MASTERY".equals(details.getMasteryLevel())?2:
                                                    "MASTERY".equals(details.getMasteryLevel())?3:
                                                    "Not Mastered".equals(details.getMasteryLevel())?1:
                                                    "Partially Mastered".equals(details.getMasteryLevel())?2:
                                                    "Mastered".equals(details.getMasteryLevel())?3:4 ));
                primObjFact.setPercentObtained(details.getPercentObtained());
                primObjFact.setPointsAttempted(details.getPointsAttempted());
                    
                // dim ids from context
                primObjFact.setAssessmentid(contextData.getAssessmentId());
                primObjFact.setCurrentResultid(contextData.getCurrentResultId());
                primObjFact.setFormid(new Long(
                                        "9".equals(prim.getSubtestForm())?1:
                                        "10".equals(prim.getSubtestForm())?2:3));
                primObjFact.setGradeid(contextData.getGradeId());
                primObjFact.setLevelid(new Long(
                                        "L".equals(prim.getSubtestLevel())?1:
                                        "E".equals(prim.getSubtestLevel())?2:
                                        "M".equals(prim.getSubtestLevel())?3:
                                        "D".equals(prim.getSubtestLevel())?4:
                                        "A".equals(prim.getSubtestLevel())?5:6));
                primObjFact.setOrgNodeid(contextData.getOrgNodeId());
                primObjFact.setProgramid(contextData.getProgramId());
                primObjFact.setSessionid(contextData.getSessionId());
                primObjFact.setStudentid(contextData.getStudentId());
                primObjFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = testData.getBySubtestId(prim.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                primObjFact.setTestCompletionTimestamp(subtestTime);  
                primObjFact.setAttr1id(contextData.getDemographicData().getAttr1Id());
                primObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
                primObjFact.setAttr3id(contextData.getDemographicData().getAttr3Id());
                primObjFact.setAttr4id(contextData.getDemographicData().getAttr4Id());
                primObjFact.setAttr5id(contextData.getDemographicData().getAttr5Id());
                primObjFact.setAttr6id(contextData.getDemographicData().getAttr6Id());
                primObjFact.setAttr7id(contextData.getDemographicData().getAttr7Id());
                primObjFact.setAttr8id(contextData.getDemographicData().getAttr8Id());
                primObjFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
                primObjFact.setAttr10id(contextData.getDemographicData().getAttr10Id());
                primObjFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
                primObjFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
                primObjFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
                primObjFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
                primObjFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
                primObjFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
                
                primaries.add(primObjFact);
            }
        }
        return (IrsTABEPrimObjFactData[]) primaries.toArray(new IrsTABEPrimObjFactData[0]);
    }
    
    public IrsTABESecObjFactData [] getSecObjFactBeans() {
        SecondaryObjective [] secs = currData.getSecondaryObjectives();
        ArrayList secondaries = new ArrayList();
        for(int i=0;i<secs.length;i++) {
            SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTABESecObjFactData secObjFact = new IrsTABESecObjFactData();
                secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
                secObjFact.setPointsObtained(details.getPointsObtained());
                secObjFact.setPointsPossible(details.getPointsPossible());
                secObjFact.setMasteryLevelid( new Long( 
                                                    "Not Mastered".equals(details.getMasteryLevel())?1:
                                                    "Partially Mastered".equals(details.getMasteryLevel())?2:
                                                    "Mastered".equals(details.getMasteryLevel())?3:4 ));
                secObjFact.setPercentObtained(details.getPercentObtained());
                secObjFact.setPointsAttempted(details.getPointsAttempted());

                // dim ids from context
                secObjFact.setAssessmentid(contextData.getAssessmentId());
                secObjFact.setCurrentResultid(contextData.getCurrentResultId());
                secObjFact.setFormid(new Long(
                                        "9".equals(sec.getSubtestForm())?1:
                                        "10".equals(sec.getSubtestForm())?2:3));
                secObjFact.setGradeid(contextData.getGradeId());
                secObjFact.setLevelid(new Long(
                                        "L".equals(sec.getSubtestLevel())?1:
                                        "E".equals(sec.getSubtestLevel())?2:
                                        "M".equals(sec.getSubtestLevel())?3:
                                        "D".equals(sec.getSubtestLevel())?4:
                                        "A".equals(sec.getSubtestLevel())?5:6));
                secObjFact.setOrgNodeid(contextData.getOrgNodeId());
                secObjFact.setProgramid(contextData.getProgramId());
                secObjFact.setSessionid(contextData.getSessionId());
                secObjFact.setStudentid(contextData.getStudentId());
                secObjFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                secObjFact.setTestCompletionTimestamp(subtestTime);  
                secObjFact.setAttr1id(contextData.getDemographicData().getAttr1Id());
                secObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
                secObjFact.setAttr3id(contextData.getDemographicData().getAttr3Id());
                secObjFact.setAttr4id(contextData.getDemographicData().getAttr4Id());
                secObjFact.setAttr5id(contextData.getDemographicData().getAttr5Id());
                secObjFact.setAttr6id(contextData.getDemographicData().getAttr6Id());
                secObjFact.setAttr7id(contextData.getDemographicData().getAttr7Id());
                secObjFact.setAttr8id(contextData.getDemographicData().getAttr8Id());
                secObjFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
                secObjFact.setAttr10id(contextData.getDemographicData().getAttr10Id());
                secObjFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
                secObjFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
                secObjFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
                secObjFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
                secObjFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
                secObjFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
                
                secObjFact.setSubtestName(sec.getSubtestName());
            
                secondaries.add(secObjFact);
            }
        }
        return (IrsTABESecObjFactData[]) secondaries.toArray(new IrsTABESecObjFactData[0]);

    }
}