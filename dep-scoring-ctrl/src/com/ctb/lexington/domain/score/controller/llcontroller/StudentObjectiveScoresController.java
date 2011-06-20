package com.ctb.lexington.domain.score.controller.llcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLPrimObjFactData;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLSecObjFactData;
import com.ctb.lexington.db.mapper.llmapper.IrsLLPrimObjFactMapper;
import com.ctb.lexington.db.mapper.llmapper.IrsLLSecObjFactMapper;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsLLPrimObjFactMapper poMapper;
    private IrsLLSecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsLLPrimObjFactMapper(conn);
        soMapper = new IrsLLSecObjFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsLLPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
        	IrsLLPrimObjFactData newFact = pfacts[i];
            poMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                poMapper.insert(newFact);
            }
        }
        
        IrsLLSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
        	IrsLLSecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                soMapper.insert(newFact);
            }
        }
    }
    
    public IrsLLPrimObjFactData [] getPrimObjFactBeans() {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        ArrayList primaries = new ArrayList();
        for(int i=0;i<prims.length;i++) {
            PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
            if(details != null /*&& !"F".equals(details.getAtsArchive())*/) {
            	IrsLLPrimObjFactData primObjFact = new IrsLLPrimObjFactData();
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
                primObjFact.setFormid(new Long("A".equals(currData.getContentAreas()[0].getSubtestForm())?7:
                    "B".equals(currData.getContentAreas()[0].getSubtestForm())?8:
                        ("Espa?ol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                       		 || "Espanol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                       		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:10));
                primObjFact.setGradeid(contextData.getGradeId());
                primObjFact.setLevelid(new Long("K".equals(currData.getContentAreas()[0].getSubtestLevel())?16:
                    "1".equals(currData.getContentAreas()[0].getSubtestLevel())?17:
                    "2-3".equals(currData.getContentAreas()[0].getSubtestLevel())?18:
                    "4-5".equals(currData.getContentAreas()[0].getSubtestLevel())?19:
                    "6-8".equals(currData.getContentAreas()[0].getSubtestLevel())?20:
                    "9-12".equals(currData.getContentAreas()[0].getSubtestLevel())?21:22));
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
        return (IrsLLPrimObjFactData[]) primaries.toArray(new IrsLLPrimObjFactData[0]);
    }
    
    public IrsLLSecObjFactData [] getSecObjFactBeans() {
        SecondaryObjective [] secs = currData.getSecondaryObjectives();
        ArrayList secondaries = new ArrayList();
        for(int i=0;i<secs.length;i++) {
            SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
            if(details != null /*&& !"F".equals(details.getAtsArchive())*/) {
            	IrsLLSecObjFactData secObjFact = new IrsLLSecObjFactData();
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
                secObjFact.setFormid(new Long("A".equals(currData.getContentAreas()[0].getSubtestForm())?7:
                    "B".equals(currData.getContentAreas()[0].getSubtestForm())?8:
                        ("Espa?ol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                       		 || "Espanol".equals(currData.getContentAreas()[0].getSubtestForm()) 
                       		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:10));
                secObjFact.setGradeid(contextData.getGradeId());
                secObjFact.setLevelid(new Long("K".equals(currData.getContentAreas()[0].getSubtestLevel())?16:
                    "1".equals(currData.getContentAreas()[0].getSubtestLevel())?17:
                        "2-3".equals(currData.getContentAreas()[0].getSubtestLevel())?18:
                        "4-5".equals(currData.getContentAreas()[0].getSubtestLevel())?19:
                        "6-8".equals(currData.getContentAreas()[0].getSubtestLevel())?20:
                        "9-12".equals(currData.getContentAreas()[0].getSubtestLevel())?21:22));
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
        return (IrsLLSecObjFactData[]) secondaries.toArray(new IrsLLSecObjFactData[0]);

    }
}