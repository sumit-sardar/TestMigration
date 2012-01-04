package com.ctb.lexington.domain.score.controller.tvcontroller;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemResponseDetails;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentItemScoreDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.irsdata.irstvdata.IrsTVItemFactData;
import com.ctb.lexington.db.mapper.tvmapper.IrsTVItemFactMapper;
import com.ctb.lexington.db.mapper.StudentItemScoreMapper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class StudentItemScoresController {
    private StudentItemScoreData studentItemScoreData;
    private StudentItemResponseData studentItemResponseData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTVItemFactMapper ifMapper;

    public StudentItemScoresController(Connection conn, StudentItemScoreData studentItemScoreData, StudentItemResponseData studentItemResponseData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentItemScoreData = studentItemScoreData;
        this.studentItemResponseData = studentItemResponseData;
        this.currData = currData;
        this.contextData = contextData;
        this.testData = testData;
        this.adminData = adminData;
        ifMapper = new IrsTVItemFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTVItemFactData [] facts = getItemFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTVItemFactData newFact = facts[i];
            ifMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                ifMapper.insert(newFact);
            }
            //System.out.println("***** SCORING: StudentItemScoresController: Persisted item fact: " + newFact.getItemid());
        }
    }
    
    public IrsTVItemFactData [] getItemFactBeans() {
        Item[] items = currData.getItems();
        ArrayList itemFacts = new ArrayList();
            for(int i=0;i<items.length;i++) {
                Item item = items[i];
                IrsTVItemFactData itemFact = new IrsTVItemFactData();
                itemFact.setItemid(item.getItemId());
                itemFact.setPointsPossible(new Long(item.getItemPointsPossible().intValue()));
                itemFact.setAssessmentid(contextData.getAssessmentId());
                itemFact.setCurrentResultid(contextData.getCurrentResultId());
                itemFact.setFormid(new Long(
                                        "B".equals(items[i].getSubtestForm())?4:
                                        "G".equals(items[i].getSubtestForm())?5:
                                        "1".equals(items[i].getSubtestForm())?4:3));
                itemFact.setGradeid(contextData.getGradeId());
                itemFact.setLevelid(new Long(
                                        "13".equals(items[i].getSubtestLevel())?7:
                                        "14".equals(items[i].getSubtestLevel())?8:
                                        "15".equals(items[i].getSubtestLevel())?9:
                                        "16".equals(items[i].getSubtestLevel())?10:
                                        "17".equals(items[i].getSubtestLevel())?11:
                                        "18".equals(items[i].getSubtestLevel())?12:
                                        "19".equals(items[i].getSubtestLevel())?13:
                                        "19-20".equals(items[i].getSubtestLevel())?14:
                                        "19/20".equals(items[i].getSubtestLevel())?14:
                                        "20".equals(items[i].getSubtestLevel())?14:15));
                itemFact.setOrgNodeid(contextData.getOrgNodeId());
                itemFact.setProgramid(contextData.getProgramId());
                itemFact.setSessionid(contextData.getSessionId());
                itemFact.setStudentid(contextData.getStudentId());
                itemFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
                Timestamp subtestTime = testData.getBySubtestId(item.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                itemFact.setTestCompletionTimestamp(subtestTime);
                itemFact.setAttr1id(contextData.getDemographicData().getAttr1Id());
                itemFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
                itemFact.setAttr3id(contextData.getDemographicData().getAttr3Id());
                itemFact.setAttr4id(contextData.getDemographicData().getAttr4Id());
                itemFact.setAttr5id(contextData.getDemographicData().getAttr5Id());
                itemFact.setAttr6id(contextData.getDemographicData().getAttr6Id());
                itemFact.setAttr7id(contextData.getDemographicData().getAttr7Id());
                itemFact.setAttr8id(contextData.getDemographicData().getAttr8Id());
                itemFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
                itemFact.setAttr10id(contextData.getDemographicData().getAttr10Id());
                itemFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
                itemFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
                itemFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
                itemFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
                itemFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
                itemFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
                itemFact.setSubtestName(item.getSubtestName());
                
                String normGroup = "6".equals(adminData.getNormsGroup())?"FALL":
                        "18".equals(adminData.getNormsGroup())?"WINTER":
                        "19".equals(adminData.getNormsGroup())?"WINTER":
                        "30".equals(adminData.getNormsGroup())?"SPRING":
                        "31".equals(adminData.getNormsGroup())?"SPRING":
                        adminData.getNormsGroup();
                
                if( ("1" + contextData.getGradeId().toString()).equals(items[i].getSubtestLevel()) || ("19/20".equals(items[i].getSubtestLevel()) && ("9".equals(contextData.getGradeId().toString()) || "10".equals(contextData.getGradeId().toString())))) {
                    itemFact.setNationalAverage(item.getNationalAverage(normGroup + String.valueOf(contextData.getGradeId().longValue())));
                } else {
                    itemFact.setNationalAverage(new BigDecimal(-1.0));
                }
                PrimaryObjective [] prims = currData.getPrimaryObjectives();
                for(int j=0;j<prims.length;j++) {
    				String contentAreaName = prims[j].getPrimaryObjectiveName();
    				if(studentItemScoreData.contains(item.getOasItemId()+ contentAreaName)) {
        			StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId()+ contentAreaName);
	                if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && !"F".equals(scoreDetails.getAtsArchive())) {
	                	itemFact.setItemResponseTimestamp(scoreDetails.getCreatedDateTime());
		                itemFact.setPointsObtained(scoreDetails.getPoints() == null?null:new Long(scoreDetails.getPoints().intValue()));
		                itemFact.setResponseid(new Long(
		                                            "A".equals(scoreDetails.getResponse())?1:
		                                            "B".equals(scoreDetails.getResponse())?2:
		                                            "C".equals(scoreDetails.getResponse())?3:
		                                            "D".equals(scoreDetails.getResponse())?4:
		                                            "E".equals(scoreDetails.getResponse())?5:6));
	                } else { // change for defect # 66542
	                	itemFact.setItemResponseTimestamp(new Timestamp(System.currentTimeMillis()));
		                itemFact.setPointsObtained(null);
		                itemFact.setResponseid(new Long(6));
	                    itemFact.setCurrentResultid(new Long(2));
	                }
                }
                }
                // Change for defect #66936
                if(itemFact.getResponseid() == null) { // It means, the content area to which the item belongs is invalid and studentItemScoreData does not contain the required id.
                	itemFact.setResponseid(new Long(6));
                }
                itemFacts.add(itemFact);
            }
        return (IrsTVItemFactData[]) itemFacts.toArray(new IrsTVItemFactData[0]);
    }
}