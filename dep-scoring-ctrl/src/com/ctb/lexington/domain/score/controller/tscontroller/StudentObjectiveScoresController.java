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
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCSecObjFactData;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tsmapper.IrsTASCSecObjFactMapper;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StsTestResultFactData factData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTASCPrimObjFactMapper poMapper;
    private IrsTASCSecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StsTestResultFactData factData, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.factData = factData;
    	this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsTASCPrimObjFactMapper(conn);
        soMapper = new IrsTASCSecObjFactMapper(conn);
    }

    public void run() throws SQLException {
    	
    	// Do not insert in Prim Obj Fact for TASC
    	
        /*IrsTASCPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
            IrsTASCPrimObjFactData newFact = pfacts[i];
            poMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                poMapper.insert(newFact);
            }
        }*/
        
        IrsTASCSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
            IrsTASCSecObjFactData newFact = sfacts[i];
            soMapper.delete(newFact);
            if(new Long(1).equals(newFact.getCurrentResultid()))  {
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
                primObjFact.setFormid(new Long(
                                        "A1".equals(prim.getSubtestForm())?18:
                                        "B1".equals(prim.getSubtestForm())?19:
                                        "C1".equals(prim.getSubtestForm())?20:21));
                primObjFact.setGradeid(contextData.getGradeId());
                primObjFact.setLevelid(new Long(
                                        "21-22".equals(prim.getSubtestLevel())?34:35));
                primObjFact.setOrgNodeid(contextData.getOrgNodeId());
                primObjFact.setProgramid(contextData.getProgramId());
                primObjFact.setSessionid(contextData.getSessionId());
                primObjFact.setStudentid(contextData.getStudentId());
                primObjFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = null;
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                primObjFact.setTestCompletionTimestamp(subtestTime);  
                
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
            String contentAreaId = sec.getProductId().toString()+sec.getPrimaryObjectiveId().toString();
            StsTestResultFactDetails factDetails = factData.get(getContentAreaName(new Long(contentAreaId)));
            
            // Do not insert in SecObj Fact for TASC if SubtestScoringStatus is Suppressed & Omitted
            
            if(!"SUP".equals(factDetails.getSubtestScoringStatus())
            		&& !"OM".equals(factDetails.getSubtestScoringStatus())){
            	
	            if(details != null && !"F".equals(details.getAtsArchive())) {
	                IrsTASCSecObjFactData secObjFact = new IrsTASCSecObjFactData();
	                secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
	                secObjFact.setPointsObtained(details.getPointsObtained());
	                secObjFact.setPointsPossible(details.getPointsPossible());
	                secObjFact.setPercentObtained(details.getPercentObtained());
	                secObjFact.setPointsAttempted(details.getPointsAttempted());
	                secObjFact.setMasteryLevelid((details.getMasteryLevel() == null)?null: new Long(details.getMasteryLevel().toString()));
	
	                // dim ids from context
	                secObjFact.setAssessmentid(contextData.getAssessmentId());
	                secObjFact.setCurrentResultid(contextData.getCurrentResultId());
	                secObjFact.setFormid(adminData.getFormId());
	                secObjFact.setGradeid(contextData.getGradeId());
	                secObjFact.setLevelid(new Long(
	                                        "21-22".equals(sec.getSubtestLevel())?34:35));
	                secObjFact.setOrgNodeid(contextData.getOrgNodeId());
	                secObjFact.setProgramid(contextData.getProgramId());
	                secObjFact.setSessionid(contextData.getSessionId());
	                secObjFact.setStudentid(contextData.getStudentId());
	                secObjFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
	                Timestamp subtestTime = testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
	                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
	                secObjFact.setTestCompletionTimestamp(subtestTime);
	                secObjFact.setSubtestName(sec.getSubtestName());
	                long pointsAttempted = 0;
	                long pointsPossible = 0;
	                if(null != details.getPointsAttempted()) pointsAttempted = details.getPointsAttempted().longValue();
	                if(null != details.getPointsPossible()) pointsPossible = details.getPointsPossible().longValue();
	            	
	            	if(pointsAttempted == pointsPossible) {
	            		details.setObjectiveScoringStatus("A"); // All items attempted
	            	}
	            	else if(pointsAttempted < pointsPossible && pointsAttempted > 0) {
	            		details.setObjectiveScoringStatus("S"); // Some items attempted
	            	}
	            	else if(pointsAttempted == 0) {
	            		details.setObjectiveScoringStatus("N"); // No item attempted
	            	}
	                secObjFact.setObjectiveScoringStatus(details.getObjectiveScoringStatus());
	                secObjFact.setScaleScore(details.getScaleScore());
	                secObjFact.setConditionCode(details.getConditionCode());
	                secObjFact.setScaleScoreRangeForMasteryLevel(details.getScaleScoreRangeForMastery());
	                
	                secondaries.add(secObjFact);
	            } else{
	            	IrsTASCSecObjFactData secObjFact = new IrsTASCSecObjFactData();
	            	secObjFact.setSessionid(contextData.getSessionId());
	            	secObjFact.setStudentid(contextData.getStudentId());
	            	secObjFact.setCurrentResultid(new Long (2));
	            	secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
	            	secondaries.add(secObjFact);
	            }
            } else{
            	IrsTASCSecObjFactData secObjFact = new IrsTASCSecObjFactData();
            	secObjFact.setSessionid(contextData.getSessionId());
            	secObjFact.setStudentid(contextData.getStudentId());
            	secObjFact.setCurrentResultid(new Long (2));
            	secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
            	secondaries.add(secObjFact);
            }
        }
        return (IrsTASCSecObjFactData[]) secondaries.toArray(new IrsTASCSecObjFactData[0]);

    }
    
    
    private String getContentAreaName(Long contentAreaId) {
        for(int i=0;i<currData.getContentAreas().length;i++) {
            if(contentAreaId.equals(currData.getContentAreas()[i].getContentAreaId())) {
                return currData.getContentAreas()[i].getContentAreaName();
            }
        }
        return null;
    }
}