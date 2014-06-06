package com.ctb.lexington.domain.score.controller.tccontroller;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstcdata.IrsTCContentAreaFactData;
import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCContentAreaFactData;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEContentAreaFactMapper;
import com.ctb.lexington.db.mapper.tcmapper.IrsTCContentAreaFactMapper;
import com.ctb.lexington.db.mapper.StudentSubtestScoresMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StudentContentAreaScoresController {
    private StudentSubtestScoresData subtestData;
    private StsTestResultFactData factData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData context;
    private IrsTCContentAreaFactMapper mapper;

    public StudentContentAreaScoresController(Connection conn, StudentSubtestScoresData subtestData, StsTestResultFactData factData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData context) {
        this.subtestData = subtestData;
        this.factData = factData;
        this.currData = currData;
        this.testData = testData;
        this.adminData = adminData;
        this.context = context;
        mapper = new IrsTCContentAreaFactMapper(conn);
    }

    public void run() throws SQLException {
    	IrsTCContentAreaFactData [] facts = getContentAreaFactBeans();

    	SqlMapClient insertSqlMap = null;
        SqlMapClient deleteSqlMap = null;
        ArrayList<Long> contentList = new ArrayList<Long>();
        for(int i=0;i<facts.length;i++) {
            IrsTCContentAreaFactData newFact = facts[i];
            deleteSqlMap = mapper.deleteBatch(newFact,deleteSqlMap);
            if(new Long(1).equals(context.getCurrentResultId()) && !contentList.contains(newFact.getContentAreaid()))  {
                contentList.add(newFact.getContentAreaid());
            	insertSqlMap = mapper.insertBatch(newFact, insertSqlMap);
            }
        }
        
        mapper.executeItemBatch(deleteSqlMap);
        mapper.executeItemBatch(insertSqlMap);
        contentList.clear();
    }
    
    public IrsTCContentAreaFactData [] getContentAreaFactBeans() {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList facts = new ArrayList();
            for(int i=0;i<contentAreas.length;i++) {
               StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null && 
                    ("T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
                   StudentSubtestScoresDetails subtest = subtestData.get(contentAreas[i].getSubtestId());
                   IrsTCContentAreaFactData newFact = new IrsTCContentAreaFactData();
                   newFact.setAssessmentid(context.getAssessmentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(context.getCurrentResultId());
                   newFact.setFormid(adminData.getFormId());
                   newFact.setGradeid(context.getGradeId());
                   newFact.setLevelid(new Long(
                                            "L".equals(contentAreas[i].getSubtestLevel())?1:
                                            "E".equals(contentAreas[i].getSubtestLevel())?2:
                                            "M".equals(contentAreas[i].getSubtestLevel())?3:
                                            "D".equals(contentAreas[i].getSubtestLevel())?4:
                                            "A".equals(contentAreas[i].getSubtestLevel())?5:6));
                   newFact.setGradeBand(contentAreas[i].getGradeBand());
                   newFact.setOrgNodeid(context.getOrgNodeId());
                   newFact.setPercentObtained(fact.getPercentObtained());
                   newFact.setPointsAttempted(fact.getPointsAttempted());
                   newFact.setPointsObtained(fact.getPointsObtained());
                   newFact.setPointsPossible(contentAreas[i].getContentAreaPointsPossible());
                   newFact.setProgramid(context.getProgramId());
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
               }
               else {
            	   IrsTCContentAreaFactData newFact = new IrsTCContentAreaFactData();
            	   newFact.setSessionid(context.getSessionId());
                   newFact.setStudentid(context.getStudentId());
                   newFact.setContentAreaid(contentAreas[i].getContentAreaId());
                   newFact.setCurrentResultid(new Long (2));
                   facts.add(newFact);
               }
            }
        return (IrsTCContentAreaFactData []) facts.toArray(new IrsTCContentAreaFactData[0]);
    }
}