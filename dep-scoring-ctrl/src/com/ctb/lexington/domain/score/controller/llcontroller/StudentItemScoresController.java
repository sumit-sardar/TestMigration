package com.ctb.lexington.domain.score.controller.llcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StudentItemResponseData;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentItemScoreDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.irsdata.irslldata.IrsLLItemFactData;
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import com.ctb.lexington.db.mapper.llmapper.IrsLLItemFactMapper;

public class StudentItemScoresController {
    private StudentItemScoreData studentItemScoreData;
    private StudentItemResponseData studentItemResponseData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsLLItemFactMapper ifMapper;

    public StudentItemScoresController(Connection conn, StudentItemScoreData studentItemScoreData, StudentItemResponseData studentItemResponseData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentItemScoreData = studentItemScoreData;
        this.studentItemResponseData = studentItemResponseData;
        this.currData = currData;
        this.contextData = contextData;
        this.testData = testData;
        this.adminData = adminData;
        ifMapper = new IrsLLItemFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsLLItemFactData [] facts = getItemFactBeans();
        for(int i=0;i<facts.length;i++) {
        	IrsLLItemFactData newFact = facts[i];
            ifMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                ifMapper.insert(newFact);
            }
            //System.out.println("***** SCORING: StudentItemScoresController: Persisted item fact: " + newFact.getItemid());
        }
    }
    
    public IrsLLItemFactData [] getItemFactBeans() {
        Item[] items = currData.getItems();
        ArrayList itemFacts = new ArrayList();
            for(int i=0;i<items.length;i++) {
                Item item = items[i];
                StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId());
                if(scoreDetails != null && !"F".equals(scoreDetails.getAtsArchive())) {
                	IrsLLItemFactData itemFact = new IrsLLItemFactData();
	                itemFact.setItemid(item.getItemId());
	                itemFact.setItemResponseTimestamp(scoreDetails.getCreatedDateTime());
	                itemFact.setPointsObtained(scoreDetails.getPoints() == null?null:new Long(scoreDetails.getPoints().intValue()));
	                itemFact.setPointsPossible(new Long(item.getItemPointsPossible().intValue()));
	                itemFact.setResponseid(new Long(
	                                            "A".equals(scoreDetails.getResponse())?1:
	                                            "B".equals(scoreDetails.getResponse())?2:
	                                            "C".equals(scoreDetails.getResponse())?3:
	                                            "D".equals(scoreDetails.getResponse())?4:
	                                            "E".equals(scoreDetails.getResponse())?5:6));
	                                            
	                itemFact.setAssessmentid(contextData.getAssessmentId());
	                itemFact.setCurrentResultid(contextData.getCurrentResultId());
	                itemFact.setFormid(new Long("A".equals(currData.getContentAreas()[0].getSubtestForm())?7:
	                       "B".equals(currData.getContentAreas()[0].getSubtestForm())?8:
	                           ("Espa?ol".equals(currData.getContentAreas()[0].getSubtestForm()) 
	                          		 || "Espanol".equals(currData.getContentAreas()[0].getSubtestForm()) 
	                          		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:10));
	                itemFact.setGradeid(contextData.getGradeId());
	                itemFact.setLevelid(new Long("K".equals(currData.getContentAreas()[0].getSubtestLevel())?16:
	                       "1".equals(currData.getContentAreas()[0].getSubtestLevel())?17:
	                       "2-3".equals(currData.getContentAreas()[0].getSubtestLevel())?18:
	                       "4-5".equals(currData.getContentAreas()[0].getSubtestLevel())?19:
	                       "6-8".equals(currData.getContentAreas()[0].getSubtestLevel())?20:
	                       "9-12".equals(currData.getContentAreas()[0].getSubtestLevel())?21:22));
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
	                itemFacts.add(itemFact);
                }
            }
        return (IrsLLItemFactData[]) itemFacts.toArray(new IrsLLItemFactData[0]);
    }
}