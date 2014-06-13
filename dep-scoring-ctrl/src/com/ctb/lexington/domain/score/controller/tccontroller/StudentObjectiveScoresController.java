package com.ctb.lexington.domain.score.controller.tccontroller;

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
import com.ctb.lexington.db.data.CurriculumData.VirtualPrimObjsForTABECCSS;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABESecObjFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCPrimObjFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCSecObjFactData;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCPrimObjFactMapper;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCSecObjFactMapper;
import com.ibatis.sqlmap.client.SqlMapClient;


public class StudentObjectiveScoresController {
    private StudentScoreSummaryData studentScoreSummaryData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTCPrimObjFactMapper poMapper;
    private IrsTCSecObjFactMapper soMapper;

    public StudentObjectiveScoresController(Connection conn, StudentScoreSummaryData studentScoreSummaryData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentScoreSummaryData = studentScoreSummaryData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.contextData = contextData;
        poMapper = new IrsTCPrimObjFactMapper(conn);
        soMapper = new IrsTCSecObjFactMapper(conn);
    }

    public void run() throws SQLException {
        SqlMapClient insertSqlMap = null;
        SqlMapClient deleteSqlMap = null;
        ArrayList<Long> objectiveList = new ArrayList<Long>();
        
        IrsTCPrimObjFactData [] pfacts = getPrimObjFactBeans();
        for(int i=0;i<pfacts.length;i++) {
            IrsTCPrimObjFactData newFact = pfacts[i];
            deleteSqlMap = poMapper.deleteBatch(newFact,deleteSqlMap);
            if(new Long(1).equals(contextData.getCurrentResultId())  && !objectiveList.contains(newFact.getPrimObjid()))  {
            	objectiveList.add(newFact.getPrimObjid());
            	insertSqlMap = poMapper.insertBatch(newFact,insertSqlMap);
            }
        }
        poMapper.executeItemBatch(deleteSqlMap);
        poMapper.executeItemBatch(insertSqlMap);
        objectiveList.clear();
        deleteSqlMap = null;
        insertSqlMap = null;
                
        IrsTCSecObjFactData [] sfacts = getSecObjFactBeans();
        for(int i=0;i<sfacts.length;i++) {
            IrsTCSecObjFactData newFact = sfacts[i];
            deleteSqlMap = soMapper.deleteBatch(newFact,deleteSqlMap);
            if(new Long(1).equals(contextData.getCurrentResultId()) && !objectiveList.contains(newFact.getSecObjid()))  {
            	objectiveList.add(newFact.getSecObjid());
            	insertSqlMap = soMapper.insertBatch(newFact,insertSqlMap);
            }
        }
        soMapper.executeItemBatch(deleteSqlMap);
        soMapper.executeItemBatch(insertSqlMap);
    }
    
    public IrsTCPrimObjFactData [] getPrimObjFactBeans() {
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        String subtestName = null;
        ArrayList primaries = new ArrayList();
        for(int i=0;i<prims.length;i++) {
            PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTCPrimObjFactData primObjFact = new IrsTCPrimObjFactData();
                primObjFact.setPrimObjid(new Long(Long.parseLong(String.valueOf(prims[i].getProductId()) + String.valueOf(prims[i].getPrimaryObjectiveId()))));
                primObjFact.setPointsObtained(details.getPointsObtained());
                primObjFact.setPointsPossible(details.getPointsPossible());
                primObjFact.setPercentObtained(details.getPercentObtained());
                primObjFact.setPointsAttempted(details.getPointsAttempted());
                    
                primObjFact.setAssessmentid(contextData.getAssessmentId());
                primObjFact.setCurrentResultid(contextData.getCurrentResultId());
                primObjFact.setFormid(adminData.getFormId());
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
                
                if(subtestName == null){
                	subtestName = prim.getSubtestName();
                }
                primaries.add(primObjFact);
            }
        }
        
        if(null != subtestName && "READING".equalsIgnoreCase(subtestName)){
        	primaries = getVirtualPrimObjs(primaries);
        }
        return (IrsTCPrimObjFactData[]) primaries.toArray(new IrsTCPrimObjFactData[0]);
    }
    
    
    
    private ArrayList getVirtualPrimObjs(ArrayList primaries){

    	VirtualPrimObjsForTABECCSS [] virtualPrims = currData.getVirtualPrimObjs();
    	for(VirtualPrimObjsForTABECCSS vPrim : virtualPrims){
    		IrsTCPrimObjFactData primObjFact = new IrsTCPrimObjFactData();
            primObjFact.setPrimObjid(new Long(Long.parseLong(String.valueOf(vPrim.getProductId()) + String.valueOf(vPrim.getPrimaryObjectiveId()))));
                
            primObjFact.setAssessmentid(contextData.getAssessmentId());
            primObjFact.setCurrentResultid(contextData.getCurrentResultId());
            primObjFact.setFormid(adminData.getFormId());
            primObjFact.setGradeid(contextData.getGradeId());
            primObjFact.setLevelid(new Long(
                                    "L".equals(vPrim.getSubtestLevel())?1:
                                    "E".equals(vPrim.getSubtestLevel())?2:
                                    "M".equals(vPrim.getSubtestLevel())?3:
                                    "D".equals(vPrim.getSubtestLevel())?4:
                                    "A".equals(vPrim.getSubtestLevel())?5:6));
            primObjFact.setOrgNodeid(contextData.getOrgNodeId());
            primObjFact.setProgramid(contextData.getProgramId());
            primObjFact.setSessionid(contextData.getSessionId());
            primObjFact.setStudentid(contextData.getStudentId());
            primObjFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
            Timestamp subtestTime = testData.getBySubtestId(vPrim.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
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
    	return primaries;
    }
    
    public IrsTCSecObjFactData [] getSecObjFactBeans() {
        SecondaryObjective [] secs = currData.getSecondaryObjectives();
        ArrayList secondaries = new ArrayList();
        for(int i=0;i<secs.length;i++) {
            SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
            if(details != null && !"F".equals(details.getAtsArchive())) {
                IrsTCSecObjFactData secObjFact = new IrsTCSecObjFactData();
                secObjFact.setSecObjid(new Long(Long.parseLong(String.valueOf(secs[i].getProductId()) + String.valueOf(secs[i].getSecondaryObjectiveId()))));
                secObjFact.setPointsObtained(details.getPointsObtained());
                secObjFact.setPointsPossible(details.getPointsPossible());
                secObjFact.setPercentObtained(details.getPercentObtained());
                secObjFact.setPointsAttempted(details.getPointsAttempted());

                secObjFact.setAssessmentid(contextData.getAssessmentId());
                secObjFact.setCurrentResultid(contextData.getCurrentResultId());
                secObjFact.setFormid(adminData.getFormId());
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
        return (IrsTCSecObjFactData[]) secondaries.toArray(new IrsTCSecObjFactData[0]);

    }
}