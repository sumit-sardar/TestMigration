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
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
                poMapper.insert(newFact);
            }
        }
        
        IrsLLSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
        	IrsLLSecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
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
            if(details != null && !"F".equals(details.getAtsArchive())) {
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
                primObjFact.setPercentObtained(details.getDecimalPercentObtained());
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
                /* primObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
                primObjFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
                primObjFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
                primObjFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
                primObjFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
                primObjFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
                primObjFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
                primObjFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
                primObjFact.setAttr17id(contextData.getDemographicData().getAttr17Id());
                primObjFact.setAttr18id(contextData.getDemographicData().getAttr18Id());
                primObjFact.setAttr19id(contextData.getDemographicData().getAttr19Id());
                primObjFact.setAttr20id(contextData.getDemographicData().getAttr20Id());
                primObjFact.setAttr21id(contextData.getDemographicData().getAttr21Id());
                primObjFact.setAttr22id(contextData.getDemographicData().getAttr22Id());
                primObjFact.setAttr23id(contextData.getDemographicData().getAttr23Id());
                primObjFact.setAttr25id(contextData.getDemographicData().getAttr25Id());
                primObjFact.setAttr26id(contextData.getDemographicData().getAttr26Id());
                primObjFact.setAttr27id(contextData.getDemographicData().getAttr27Id());
                primObjFact.setAttr28id(contextData.getDemographicData().getAttr28Id());
                primObjFact.setAttr29id(contextData.getDemographicData().getAttr29Id());
                primObjFact.setAttr30id(contextData.getDemographicData().getAttr30Id());
                primObjFact.setAttr31id(contextData.getDemographicData().getAttr31Id());
                primObjFact.setAttr32id(contextData.getDemographicData().getAttr32Id());
                primObjFact.setAttr33id(contextData.getDemographicData().getAttr33Id());
                primObjFact.setAttr34id(contextData.getDemographicData().getAttr34Id());
                primObjFact.setAttr35id(contextData.getDemographicData().getAttr35Id());
                primObjFact.setAttr36id(contextData.getDemographicData().getAttr36Id());
                primObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());*/
                
                primaries.add(primObjFact);
            } else {
            	IrsLLPrimObjFactData primObjFact = new IrsLLPrimObjFactData();
            	primObjFact.setSessionid(contextData.getSessionId());
                primObjFact.setStudentid(contextData.getStudentId());
                primObjFact.setCurrentResultid(new Long (2));
                primObjFact.setPrimObjid(new Long(Long.parseLong(String.valueOf(prims[i].getProductId()) + String.valueOf(prims[i].getPrimaryObjectiveId()))));
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
            if(details != null && !"F".equals(details.getAtsArchive())) {
            	IrsLLSecObjFactData secObjFact = new IrsLLSecObjFactData();
                secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
                secObjFact.setPointsObtained(details.getPointsObtained());
                secObjFact.setPointsPossible(details.getPointsPossible());
                secObjFact.setMasteryLevelid( new Long( 
                                                    "Not Mastered".equals(details.getMasteryLevel())?1:
                                                    "Partially Mastered".equals(details.getMasteryLevel())?2:
                                                    "Mastered".equals(details.getMasteryLevel())?3:4 ));
                secObjFact.setPercentObtained(details.getDecimalPercentObtained());
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
                /*secObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
                secObjFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
                secObjFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
                secObjFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
                secObjFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
                secObjFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
                secObjFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
                secObjFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
                secObjFact.setAttr17id(contextData.getDemographicData().getAttr17Id());
                secObjFact.setAttr18id(contextData.getDemographicData().getAttr18Id());
                secObjFact.setAttr19id(contextData.getDemographicData().getAttr19Id());
                secObjFact.setAttr20id(contextData.getDemographicData().getAttr20Id());
                secObjFact.setAttr21id(contextData.getDemographicData().getAttr21Id());
                secObjFact.setAttr22id(contextData.getDemographicData().getAttr22Id());
                secObjFact.setAttr23id(contextData.getDemographicData().getAttr23Id());
                secObjFact.setAttr25id(contextData.getDemographicData().getAttr25Id());
                secObjFact.setAttr26id(contextData.getDemographicData().getAttr26Id());
                secObjFact.setAttr27id(contextData.getDemographicData().getAttr27Id());
                secObjFact.setAttr28id(contextData.getDemographicData().getAttr28Id());
                secObjFact.setAttr29id(contextData.getDemographicData().getAttr29Id());
                secObjFact.setAttr30id(contextData.getDemographicData().getAttr30Id());
                secObjFact.setAttr31id(contextData.getDemographicData().getAttr31Id());
                secObjFact.setAttr32id(contextData.getDemographicData().getAttr32Id());
                secObjFact.setAttr33id(contextData.getDemographicData().getAttr33Id());
                secObjFact.setAttr34id(contextData.getDemographicData().getAttr34Id());
                secObjFact.setAttr35id(contextData.getDemographicData().getAttr35Id());
                secObjFact.setAttr36id(contextData.getDemographicData().getAttr36Id());
                secObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());*/
                
                
                
                secObjFact.setSubtestName(sec.getSubtestName());
            
                secondaries.add(secObjFact);
            } else {
            	IrsLLSecObjFactData secObjFact = new IrsLLSecObjFactData();
            	secObjFact.setSessionid(contextData.getSessionId());
            	secObjFact.setStudentid(contextData.getStudentId());
            	secObjFact.setCurrentResultid(new Long (2));
            	secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
            	secondaries.add(secObjFact);
            }
        }
        return (IrsLLSecObjFactData[]) secondaries.toArray(new IrsLLSecObjFactData[0]);

    }
}