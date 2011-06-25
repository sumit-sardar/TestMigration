package com.ctb.lexington.domain.score.controller.tbcontroller;

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
import com.ctb.lexington.db.irsdata.irstbdata.IrsTABEItemFactData;
import com.ctb.lexington.db.mapper.tbmapper.IrsTABEItemFactMapper;
import com.ctb.lexington.db.mapper.StudentItemScoreMapper;
import java.sql.Timestamp;
import java.util.ArrayList;

public class StudentItemScoresController {
    private StudentItemScoreData studentItemScoreData;
    private StudentItemResponseData studentItemResponseData;
    private CurriculumData currData;
    private StudentTestData testData;
    private AdminData adminData;
    private ContextData contextData;
    private IrsTABEItemFactMapper ifMapper;

    public StudentItemScoresController(Connection conn, StudentItemScoreData studentItemScoreData, StudentItemResponseData studentItemResponseData, CurriculumData currData, StudentTestData testData, AdminData adminData, ContextData contextData) {
        this.studentItemScoreData = studentItemScoreData;
        this.studentItemResponseData = studentItemResponseData;
        this.currData = currData;
        this.contextData = contextData;
        this.testData = testData;
        this.adminData = adminData;
        ifMapper = new IrsTABEItemFactMapper(conn);
    }

    public void run() throws SQLException {
        IrsTABEItemFactData [] facts = getItemFactBeans();
        for(int i=0;i<facts.length;i++) {
            IrsTABEItemFactData newFact = facts[i];
            ifMapper.delete(newFact);
            if(new Long(1).equals(contextData.getCurrentResultId()))  {
                ifMapper.insert(newFact);
            }
            //System.out.println("***** SCORING: StudentItemScoresController: Persisted item fact: " + newFact.getItemid());
        }
    }
    
    public IrsTABEItemFactData [] getItemFactBeans() {
        Item[] items = currData.getItems();
        ArrayList itemFacts = new ArrayList();
            for(int i=0;i<items.length;i++) {
                Item item = items[i];
                PrimaryObjective [] prims = currData.getPrimaryObjectives();
                for(int j=0;j<prims.length;j++) {
    				String contentAreaName = prims[j].getPrimaryObjectiveName();
	                StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId()+ contentAreaName);
	                if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && !"F".equals(scoreDetails.getAtsArchive())) {
	                	IrsTABEItemFactData itemFact = new IrsTABEItemFactData();
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
		                itemFact.setFormid(new Long(
		                                        "9".equals(item.getSubtestForm())?1:
		                                        "10".equals(item.getSubtestForm())?2:1));
		                itemFact.setGradeid(contextData.getGradeId());
		                itemFact.setLevelid(new Long(
		                                        "L".equals(item.getSubtestLevel())?1:
		                                        "E".equals(item.getSubtestLevel())?2:
		                                        "M".equals(item.getSubtestLevel())?3:
		                                        "D".equals(item.getSubtestLevel())?4:
		                                        "A".equals(item.getSubtestLevel())?5:6));
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
            }
        return (IrsTABEItemFactData[]) itemFacts.toArray(new IrsTABEItemFactData[0]);
    }
}