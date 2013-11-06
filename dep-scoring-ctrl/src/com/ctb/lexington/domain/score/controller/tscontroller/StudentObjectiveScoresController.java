package com.ctb.lexington.domain.score.controller.tscontroller;

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
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCSecObjFactMapper;

import java.sql.Timestamp;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTASCPrimObjFactMapper poMapper;
    private IrsTASCSecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsTASCPrimObjFactMapper(conn);
        soMapper = new IrsTASCSecObjFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTASCPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
            IrsTASCPrimObjFactData newFact = pfacts[i];
            poMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                poMapper.insert(newFact);
            }
        }
        
        IrsTASCSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
            IrsTASCSecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                soMapper.insert(newFact);
            }
        }
    }
    
    public IrsTASCPrimObjFactData [] getPrimObjFactBeans() {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        ArrayList primaries = new ArrayList();
        System.out.println("Primary Objective Length  : " + prims.length);
        
        for(int i=0;i<prims.length;i++) {
            PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTASCPrimObjFactData primObjFact = new IrsTASCPrimObjFactData();
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
                /*primObjFact.setFormid(new Long(
                                        "9".equals(prim.getSubtestForm())?1:
                                        "10".equals(prim.getSubtestForm())?2:3));*/
                primObjFact.setFormid(new Long(18));
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
                Timestamp subtestTime = null;
                /*if(prim.getSubtestId().equals("296465")) {
                	subtestTime = null;
                }
                else {
                	subtestTime = testData.getBySubtestId(prim.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                }*/
                System.out.println("Sub test Id " + prim.getSubtestId() + " Subtest Level " + prim.getSubtestLevel());
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
                
                
                primObjFact.setAttr17id(contextData.getDemographicData().getAttr17Id());
                primObjFact.setAttr18id(contextData.getDemographicData().getAttr18Id());
                primObjFact.setAttr19id(contextData.getDemographicData().getAttr19Id());
                if(contextData.getDemographicData().getAttr20Id() != null) {
                	primObjFact.setAttr20id(Long.parseLong(contextData.getDemographicData().getAttr20Id()));
                } else {
                	primObjFact.setAttr20id(null);
                }
                if(contextData.getDemographicData().getAttr21Id() != null) {
                	primObjFact.setAttr21id(Long.parseLong(contextData.getDemographicData().getAttr21Id()));
                } else {
                	primObjFact.setAttr21id(null);
                }
                //primObjFact.setAttr20id(Long.parseLong(contextData.getDemographicData().getAttr20Id() == null ? "8" : contextData.getDemographicData().getAttr20Id()));
                //primObjFact.setAttr21id(Long.parseLong(contextData.getDemographicData().getAttr20Id() == null ? "0" : contextData.getDemographicData().getAttr20Id()));
                primObjFact.setAttr22id(contextData.getDemographicData().getAttr22Id());
                primObjFact.setAttr24id(contextData.getDemographicData().getAttr24Id());
                primObjFact.setAttr25id(contextData.getDemographicData().getAttr25Id());
                primObjFact.setAttr26id(contextData.getDemographicData().getAttr26Id());
                primObjFact.setAttr27id(contextData.getDemographicData().getAttr27Id());
                primObjFact.setAttr28id(contextData.getDemographicData().getAttr28Id());
                primObjFact.setAttr29id(contextData.getDemographicData().getAttr29Id());
                primObjFact.setAttr30id(contextData.getDemographicData().getAttr30Id());
                primObjFact.setAttr36id(contextData.getDemographicData().getAttr36Id());
                primObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());
                
                primaries.add(primObjFact);
            }
        }
        return (IrsTASCPrimObjFactData[]) primaries.toArray(new IrsTASCPrimObjFactData[0]);
    }
    
    public IrsTASCSecObjFactData [] getSecObjFactBeans() {
        SecondaryObjective [] secs = currData.getSecondaryObjectives();
        ArrayList secondaries = new ArrayList();
        for(int i=0;i<secs.length;i++) {
            SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTASCSecObjFactData secObjFact = new IrsTASCSecObjFactData();
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
                //Timestamp subtestTime = null; //testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                Timestamp subtestTime = testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                System.out.println("Sub test Id " + sec.getSubtestId() + " Subtest Level " + sec.getSubtestLevel());
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
                
                secObjFact.setAttr17id(contextData.getDemographicData().getAttr17Id());
                secObjFact.setAttr18id(contextData.getDemographicData().getAttr18Id());
                secObjFact.setAttr19id(contextData.getDemographicData().getAttr19Id());
                if(contextData.getDemographicData().getAttr20Id() != null) {
                	secObjFact.setAttr20id(Long.parseLong(contextData.getDemographicData().getAttr20Id()));
                } else {
                	secObjFact.setAttr20id(null);
                }
                if(contextData.getDemographicData().getAttr21Id() != null) {
                	secObjFact.setAttr21id(Long.parseLong(contextData.getDemographicData().getAttr21Id()));
                } else {
                	secObjFact.setAttr21id(null);
                }
                //secObjFact.setAttr20id(Long.parseLong(contextData.getDemographicData().getAttr20Id()));
                //secObjFact.setAttr21id(Long.parseLong(contextData.getDemographicData().getAttr21Id()));
                secObjFact.setAttr22id(contextData.getDemographicData().getAttr22Id());
                secObjFact.setAttr24id(contextData.getDemographicData().getAttr24Id());
                secObjFact.setAttr25id(contextData.getDemographicData().getAttr25Id());
                secObjFact.setAttr26id(contextData.getDemographicData().getAttr26Id());
                secObjFact.setAttr27id(contextData.getDemographicData().getAttr27Id());
                secObjFact.setAttr28id(contextData.getDemographicData().getAttr28Id());
                secObjFact.setAttr29id(contextData.getDemographicData().getAttr29Id());
                secObjFact.setAttr30id(contextData.getDemographicData().getAttr30Id());
                secObjFact.setAttr36id(contextData.getDemographicData().getAttr36Id());
                secObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());
                
                secObjFact.setSubtestName(sec.getSubtestName());
            
                secondaries.add(secObjFact);
            }
        }
        return (IrsTASCSecObjFactData[]) secondaries.toArray(new IrsTASCSecObjFactData[0]);

    }
}