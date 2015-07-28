package com.ctb.lexington.domain.score.controller.trcontroller;

/**
 * @author Somenath_Chakroborti
 *
 */


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
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.irsdata.irstrdata.IrsTRItemFactData;
import com.ctb.lexington.db.mapper.trmapper.IrsTRItemFactMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

public class StudentItemScoresController {
	private StudentItemScoreData studentItemScoreData;
	private StudentItemScoreData studentfieldTestTEItemScoreData;
	private StudentItemResponseData studentItemResponseData;
	private CurriculumData currData;
	private StudentTestData testData;
	private AdminData adminData;
	private ContextData contextData;
	private IrsTRItemFactMapper ifMapper;
	boolean isRetryFieldTestTE;

	public StudentItemScoresController(Connection conn,
			StudentItemScoreData studentItemScoreData,
			StudentItemScoreData studentfieldTestTEItemScoreData,
			StudentItemResponseData studentItemResponseData,
			CurriculumData currData, StudentTestData testData,
			AdminData adminData, ContextData contextData, boolean isRetryFieldTestTE) {
		this.studentItemScoreData = studentItemScoreData;
		this.studentItemResponseData = studentItemResponseData;
		this.studentfieldTestTEItemScoreData = studentfieldTestTEItemScoreData;
		this.isRetryFieldTestTE = isRetryFieldTestTE;
		this.currData = currData;
		this.contextData = contextData;
		this.testData = testData;
		this.adminData = adminData;
		ifMapper = new IrsTRItemFactMapper(conn);
	}

	public void run() throws SQLException {
		SqlMapClient insertSqlMap = null;
		SqlMapClient deleteSqlMap = null;
		ArrayList<Long> itemList = new ArrayList<Long>();

		if(!isRetryFieldTestTE){
			IrsTRItemFactData[] facts = getItemFactBeans();
			for (int i = 0; i < facts.length; i++) {
				IrsTRItemFactData newFact = facts[i];
				deleteSqlMap = ifMapper.deleteBatch(newFact, deleteSqlMap);
				if (new Long(1).equals(contextData.getCurrentResultId())
						&& !itemList.contains(newFact.getItemid())
						&& new Long(1).equals(newFact.getCurrentResultid())) {
					insertSqlMap = ifMapper.insertBatch(newFact, insertSqlMap);
					itemList.add(newFact.getItemid());
				}
			}
			if(deleteSqlMap != null)ifMapper.executeItemBatch(deleteSqlMap);
			if(insertSqlMap != null)ifMapper.executeItemBatch(insertSqlMap);
			itemList.clear();
		}
		
		//Added for FT TE Items scoring
		insertSqlMap = null;
		deleteSqlMap = null;
		ArrayList<Long> TEItemList = new ArrayList<Long>();
		IrsTRItemFactData[] TEFacts = getTEItemFactBeans();
		for (int i = 0; i < TEFacts.length; i++) {
			IrsTRItemFactData newFact = TEFacts[i];
			deleteSqlMap = ifMapper.deleteBatch(newFact, deleteSqlMap);
			if (new Long(1).equals(contextData.getCurrentResultId())
					&& !TEItemList.contains(newFact.getItemid())
					&& new Long(1).equals(newFact.getCurrentResultid())) {
				insertSqlMap = ifMapper.upadateFTBatch(newFact, insertSqlMap);
				TEItemList.add(newFact.getItemid());
			}
		}
		if(deleteSqlMap != null)ifMapper.executeItemBatch(deleteSqlMap);
		if(insertSqlMap != null)ifMapper.executeItemBatch(insertSqlMap);
		TEItemList.clear();
	}

	private IrsTRItemFactData[] getItemFactBeans() {
        Item[] items = currData.getItems();
        ArrayList itemFacts = new ArrayList();
            for(int i=0;i<items.length;i++) {
                Item item = items[i];
                PrimaryObjective [] prims = currData.getPrimaryObjectives();
                for(int j=0;j<prims.length;j++) {
    				String contentAreaName = prims[j].getPrimaryObjectiveName();
    				if(studentItemScoreData.contains(item.getOasItemId()+ contentAreaName)) {
    				StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId()+ contentAreaName);
	                if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && !"F".equals(scoreDetails.getAtsArchive())) {
	                	IrsTRItemFactData itemFact = new IrsTRItemFactData();
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
		                itemFact.setFormid(adminData.getFormId());
		                itemFact.setGradeid(contextData.getGradeId());
		                itemFact.setLevelid(new Long(
		                                        "21-22".equals(item.getSubtestLevel())?34:35));
		                itemFact.setOrgNodeid(contextData.getOrgNodeId());
		                itemFact.setProgramid(contextData.getProgramId());
		                itemFact.setSessionid(contextData.getSessionId());
		                itemFact.setStudentid(contextData.getStudentId());
		                itemFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
		                Timestamp subtestTime = testData.getBySubtestId(item.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
	                    if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
	                    itemFact.setTestCompletionTimestamp(subtestTime);
		                itemFact.setSubtestName(item.getSubtestName());
		                itemFacts.add(itemFact);
	                }  else if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && "F".equals(scoreDetails.getAtsArchive())) {
	                	IrsTRItemFactData itemFact = new IrsTRItemFactData();
						itemFact.setItemid(item.getItemId());
						itemFact.setSessionid(contextData.getSessionId());
						itemFact.setStudentid(contextData.getStudentId());
						itemFact.setCurrentResultid(new Long (2));
						itemFacts.add(itemFact);
					}
                }
                }
            }
        return (IrsTRItemFactData[]) itemFacts.toArray(new IrsTRItemFactData[0]);
    }
	
	//Added for TASC and TASC Readiness FT TE Items scoring
	private IrsTRItemFactData[] getTEItemFactBeans() {
        Item[] items = currData.getTEItems();
        ArrayList itemFacts = new ArrayList();
            for(int i=0;i<items.length;i++) {
                Item item = items[i];
    				if(studentfieldTestTEItemScoreData.contains(item.getOasItemId())) {
    				StudentItemScoreDetails scoreDetails = studentfieldTestTEItemScoreData.get(item.getOasItemId());
	                if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && !"F".equals(scoreDetails.getAtsArchive())) {
	                	IrsTRItemFactData itemFact = new IrsTRItemFactData();
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
		                itemFact.setFormid(adminData.getFormId());
		                itemFact.setGradeid(contextData.getGradeId());
		                itemFact.setLevelid(new Long(
		                                        "21-22".equals(item.getSubtestLevel())?34:35));
		                itemFact.setOrgNodeid(contextData.getOrgNodeId());
		                itemFact.setProgramid(contextData.getProgramId());
		                itemFact.setSessionid(contextData.getSessionId());
		                itemFact.setStudentid(contextData.getStudentId());
		                itemFact.setTestStartTimestamp(contextData.getTestStartTimestamp());
		                Timestamp subtestTime = testData.getBySubtestId(item.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone());
	                    if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
	                    itemFact.setTestCompletionTimestamp(subtestTime);
		                itemFact.setSubtestName(item.getSubtestName());
		                itemFacts.add(itemFact);
	                }  else if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && "F".equals(scoreDetails.getAtsArchive())) {
	                	IrsTRItemFactData itemFact = new IrsTRItemFactData();
						itemFact.setItemid(item.getItemId());
						itemFact.setSessionid(contextData.getSessionId());
						itemFact.setStudentid(contextData.getStudentId());
						itemFact.setCurrentResultid(new Long (2));
						itemFacts.add(itemFact);
					}
                }
            }
        return (IrsTRItemFactData[]) itemFacts.toArray(new IrsTRItemFactData[0]);
    }
}