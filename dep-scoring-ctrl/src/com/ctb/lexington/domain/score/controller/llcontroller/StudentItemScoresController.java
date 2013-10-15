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
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
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
			if(new Long(1).equals(newFact.getCurrentResultid()))  {
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
			PrimaryObjective [] prims = currData.getPrimaryObjectives();
			for(int j=0;j<prims.length;j++) {
				String contentAreaName = prims[j].getPrimaryObjectiveName();
				if(!contentAreaName.equals("Oral") && !contentAreaName.equals("Comprehension")
						&& !contentAreaName.equals("Productive") && !contentAreaName.equals("Literacy")){
					if(studentItemScoreData.contains(item.getOasItemId()+ contentAreaName)) {
					StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId()+ contentAreaName);
					if(scoreDetails != null && scoreDetails.getAtsArchive()!= null &&!"F".equals(scoreDetails.getAtsArchive())) {
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
										|| "Español".equals(currData.getContentAreas()[0].getSubtestForm())
										|| "Espa?ol A".equals(currData.getContentAreas()[0].getSubtestForm()) 
										|| "Espanol A".equals(currData.getContentAreas()[0].getSubtestForm()) 
										|| "Español A".equals(currData.getContentAreas()[0].getSubtestForm()))?9:
										"C".equals(currData.getContentAreas()[0].getSubtestForm())?15:
											"D".equals(currData.getContentAreas()[0].getSubtestForm())?16:
												("Espa?ol2".equals(currData.getContentAreas()[0].getSubtestForm()) 
														|| "Espanol2".equals(currData.getContentAreas()[0].getSubtestForm()) 
														|| "Español2".equals(currData.getContentAreas()[0].getSubtestForm())
														|| "Espa?ol B".equals(currData.getContentAreas()[0].getSubtestForm()) 
														|| "Espanol B".equals(currData.getContentAreas()[0].getSubtestForm()) 
														|| "Español B".equals(currData.getContentAreas()[0].getSubtestForm()))?17:10));
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
						itemFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
						itemFact.setAttr9id(contextData.getDemographicData().getAttr9Id());
						itemFact.setAttr11id(contextData.getDemographicData().getAttr11Id());
						itemFact.setAttr12id(contextData.getDemographicData().getAttr12Id());
						itemFact.setAttr13id(contextData.getDemographicData().getAttr13Id());
						itemFact.setAttr14id(contextData.getDemographicData().getAttr14Id());
						itemFact.setAttr15id(contextData.getDemographicData().getAttr15Id());
						itemFact.setAttr16id(contextData.getDemographicData().getAttr16Id());
						itemFact.setAttr17id(contextData.getDemographicData().getAttr17Id());
						itemFact.setAttr18id(contextData.getDemographicData().getAttr18Id());
						itemFact.setAttr19id(contextData.getDemographicData().getAttr19Id());
						itemFact.setAttr20id(contextData.getDemographicData().getAttr20Id());
						itemFact.setAttr21id(contextData.getDemographicData().getAttr21Id());
						itemFact.setAttr22id(contextData.getDemographicData().getAttr22Id());
						itemFact.setAttr23id(contextData.getDemographicData().getAttr23Id());
						itemFact.setAttr25id(contextData.getDemographicData().getAttr25Id());
						itemFact.setAttr26id(contextData.getDemographicData().getAttr26Id());
						itemFact.setAttr27id(contextData.getDemographicData().getAttr27Id());
						itemFact.setAttr28id(contextData.getDemographicData().getAttr28Id());
						itemFact.setAttr29id(contextData.getDemographicData().getAttr29Id());
						itemFact.setAttr30id(contextData.getDemographicData().getAttr30Id());
						itemFact.setAttr31id(contextData.getDemographicData().getAttr31Id());
						itemFact.setAttr32id(contextData.getDemographicData().getAttr32Id());
						itemFact.setAttr33id(contextData.getDemographicData().getAttr33Id());
						itemFact.setAttr34id(contextData.getDemographicData().getAttr34Id());
						itemFact.setAttr35id(contextData.getDemographicData().getAttr35Id());
						itemFact.setAttr36id(contextData.getDemographicData().getAttr36Id());
						itemFact.setAttr37id(contextData.getDemographicData().getAttr37Id());

						itemFact.setSubtestName(item.getSubtestName());
						itemFacts.add(itemFact);
					} else if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && "F".equals(scoreDetails.getAtsArchive())) {
						IrsLLItemFactData itemFact = new IrsLLItemFactData();
						itemFact.setItemid(item.getItemId());
						itemFact.setSessionid(contextData.getSessionId());
						itemFact.setStudentid(contextData.getStudentId());
						itemFact.setCurrentResultid(new Long (2));
						itemFacts.add(itemFact);
					}
				}
				}

			}
		}
		return (IrsLLItemFactData[]) itemFacts.toArray(new IrsLLItemFactData[0]);
	}
}