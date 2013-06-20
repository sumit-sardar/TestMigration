package com.ctb.lexington.domain.score.controller.llcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.score.scorer.calculator.ObjectiveNumberCorrectCalculator;
import com.ctb.lexington.domain.teststructure.MasteryLevel;


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
    //protected Scorer scorer;
    public IrsLLPrimObjFactData [] getPrimObjFactBeans() {
    	HashMap<Long, StudentScoreSummaryDetails> tempMap = new HashMap();
        PrimaryObjective [] prims = currData.getPrimaryObjectives();
        ArrayList primaries = new ArrayList();
        if(adminData.getProductId() == 7500){
        	tempMap = populateVitualObjectiveDetails(prims,tempMap);
        }
        for(int i=0;i<prims.length;i++) {
            PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
            if(adminData.getProductId() == 7500){
        		if(tempMap.containsKey(prims[i].getPrimaryObjectiveId()))
        				details = tempMap.get(prims[i].getPrimaryObjectiveId());
        	}
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
                       		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:
                           		"C".equals(currData.getContentAreas()[0].getSubtestForm())?15:
                      				"D".equals(currData.getContentAreas()[0].getSubtestForm())?16:
                      					("ESP B".equals(currData.getContentAreas()[0].getSubtestForm()))?17:10));
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
                Timestamp subtestTime = ((prim.getSubtestId()!=null)?testData.getBySubtestId(prim.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone()): null);
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                primObjFact.setTestCompletionTimestamp(subtestTime);  
                 primObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
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
                primObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());
                
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
    
    private HashMap<Long, StudentScoreSummaryDetails> populateVitualObjectiveDetails(PrimaryObjective[] prim, HashMap<Long, StudentScoreSummaryDetails> tempMap) {
		// TODO ::
    	
    	Long comprehensionPointPosible = new Long(0), comprehensionPointAttempted = new Long(
				0), comprehensionPointObtained = new Long(0), oralPointPosible = new Long(
				0), oralPointAttempted = new Long(0), oralPointObtained = new Long(
				0), productivePointPosible = new Long(0), productivePointAttempted = new Long(
				0), productivePointObtained = new Long(0), literacyPointPosible = new Long(
				0), literacyPointAttempted = new Long(0), literacyPointObtained = new Long(
				0); 
    	for(int i=0; i<prim.length;i++){
			if(("Listening".equals(prim[i].getPrimaryObjectiveName()) || "Speaking".equals(prim[i].getPrimaryObjectiveName())) && !"F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
				oralPointPosible += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsPossible());
				oralPointAttempted += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsAttempted());
				oralPointObtained += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsObtained());
			}if(("Reading".equals(prim[i].getPrimaryObjectiveName()) || "Listening".equals(prim[i].getPrimaryObjectiveName())) && !"F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
				comprehensionPointPosible += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsPossible());
				comprehensionPointAttempted += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsAttempted());
				comprehensionPointObtained += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsObtained());
			}if(("Speaking".equals(prim[i].getPrimaryObjectiveName()) || "Writing".equals(prim[i].getPrimaryObjectiveName())) && !"F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
				productivePointPosible += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsPossible());
				productivePointAttempted += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsAttempted());
				productivePointObtained += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsObtained());
			}if(("Reading".equals(prim[i].getPrimaryObjectiveName()) || "Writing".equals(prim[i].getPrimaryObjectiveName())) && !"F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
				literacyPointPosible += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsPossible());
				literacyPointAttempted += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsAttempted());
				literacyPointObtained += new Long(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getPointsObtained());
			}if("Oral".equals(prim[i].getPrimaryObjectiveName())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(oralPointAttempted);
				details.setPointsObtained(oralPointObtained);
				details.setPointsPossible(oralPointPosible);
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(oralPointObtained.intValue(), oralPointPosible.intValue())));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(oralPointObtained.intValue(), oralPointPosible.intValue()))).getCode());
				tempMap.put(prim[i].getPrimaryObjectiveId(), details);
			}if("Comprehension".equals(prim[i].getPrimaryObjectiveName())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(comprehensionPointAttempted);
				details.setPointsObtained(comprehensionPointObtained);
				details.setPointsPossible(comprehensionPointPosible);
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(comprehensionPointObtained.intValue(), comprehensionPointPosible.intValue())));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(comprehensionPointObtained.intValue(), comprehensionPointPosible.intValue()))).getCode());
				tempMap.put(prim[i].getPrimaryObjectiveId(), details);
			}if("Productive".equals(prim[i].getPrimaryObjectiveName())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(productivePointAttempted);
				details.setPointsObtained(productivePointObtained);
				details.setPointsPossible(productivePointPosible);
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(productivePointObtained.intValue(), productivePointPosible.intValue())));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(productivePointObtained.intValue(), productivePointPosible.intValue()))).getCode());
				tempMap.put(prim[i].getPrimaryObjectiveId(), details);
			}if("Literacy".equals(prim[i].getPrimaryObjectiveName())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(literacyPointAttempted);
				details.setPointsObtained(literacyPointObtained);
				details.setPointsPossible(literacyPointPosible);
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(literacyPointObtained.intValue(), literacyPointPosible.intValue())));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(literacyPointObtained.intValue(), literacyPointPosible.intValue()))).getCode());
				tempMap.put(prim[i].getPrimaryObjectiveId(), details);
			}
		}
    	String compSet ="";
    	String oralSet ="";
    	String prodSet ="";
    	String liteSet ="";
    	for(int i=0; i<prim.length;i++){
    		if(("Listening".equals(prim[i].getPrimaryObjectiveName()) || "Speaking".equals(prim[i].getPrimaryObjectiveName())) && "F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
    			oralSet = "F";
    		}if(("Reading".equals(prim[i].getPrimaryObjectiveName()) || "Listening".equals(prim[i].getPrimaryObjectiveName())) && "F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
    			compSet = "F";
    		}if(("Speaking".equals(prim[i].getPrimaryObjectiveName()) || "Writing".equals(prim[i].getPrimaryObjectiveName())) && "F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
    			prodSet = "F";
    		}if(("Reading".equals(prim[i].getPrimaryObjectiveName()) || "Writing".equals(prim[i].getPrimaryObjectiveName())) && "F".equals(studentScoreSummaryData.get(prim[i].getPrimaryObjectiveId()).getAtsArchive())){
    			liteSet = "F";
    		}if("Oral".equals(prim[i].getPrimaryObjectiveName()) && !"".equals(oralSet)){
    			StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
    			details.setAtsArchive(oralSet);
    			tempMap.remove(prim[i].getPrimaryObjectiveId());
    			tempMap.put(prim[i].getPrimaryObjectiveId(), details);
    		}if("Comprehension".equals(prim[i].getPrimaryObjectiveName()) && !"".equals(compSet)){
    			StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
    			details.setAtsArchive(compSet);
    			tempMap.remove(prim[i].getPrimaryObjectiveId());
    			tempMap.put(prim[i].getPrimaryObjectiveId(), details);
    		}if("Productive".equals(prim[i].getPrimaryObjectiveName()) && !"".equals(prodSet)){
    			StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
    			details.setAtsArchive(prodSet);
    			tempMap.remove(prim[i].getPrimaryObjectiveId());
    			tempMap.put(prim[i].getPrimaryObjectiveId(), details);
    		}if("Literacy".equals(prim[i].getPrimaryObjectiveName()) && !"".equals(liteSet)){
    			StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
    			details.setAtsArchive(liteSet);
    			tempMap.remove(prim[i].getPrimaryObjectiveId());
    			tempMap.put(prim[i].getPrimaryObjectiveId(), details);
    		}
    	}
		
		return tempMap;
	}

	public IrsLLSecObjFactData [] getSecObjFactBeans() {
        HashMap<Long, StudentScoreSummaryDetails> secMap = new HashMap<Long, StudentScoreSummaryDetails>(); 
		SecondaryObjective [] secs = currData.getSecondaryObjectives();
        ArrayList secondaries = new ArrayList();
        if(adminData.getProductId() == 7500){
        	secMap = populateVitualSecObjectiveDetails(secs,secMap);
        }
        for(int i=0;i<secs.length;i++) {
            SecondaryObjective sec = currData.getSecObjById(secs[i].getSecondaryObjectiveId());
            StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
            if(adminData.getProductId() == 7500){
        		if(secMap.containsKey(secs[i].getSecondaryObjectiveId()))
        				details = secMap.get(secs[i].getSecondaryObjectiveId());
        	}
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
                       		 || "Español".equals(currData.getContentAreas()[0].getSubtestForm()))?9:
                           		"C".equals(currData.getContentAreas()[0].getSubtestForm())?15:
                      				"D".equals(currData.getContentAreas()[0].getSubtestForm())?16:
                      					("ESP B".equals(currData.getContentAreas()[0].getSubtestForm()))?17:10));
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
                Timestamp subtestTime = (sec.getSubtestId()!= null)?testData.getBySubtestId(sec.getSubtestId()).getSubtestCompletionTimestamp(adminData.getTimeZone()):null;
                if(subtestTime == null) subtestTime = contextData.getTestCompletionTimestamp();
                secObjFact.setTestCompletionTimestamp(subtestTime);  
                secObjFact.setAttr2id(contextData.getDemographicData().getAttr2Id());
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
                secObjFact.setAttr37id(contextData.getDemographicData().getAttr37Id());
                
                
                
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
	
	
	 private HashMap<Long, StudentScoreSummaryDetails> populateVitualSecObjectiveDetails(SecondaryObjective[] secs, HashMap<Long, StudentScoreSummaryDetails> tempMap) {
		 
		 Long readingPointPossible = new Long(0), listeningPointPossible = new Long(
				0), speakingPointPossible = new Long(0), writingPointPossible = new Long(
				0), readingPointAttempted = new Long(0), listeningPointAttempted = new Long(
				0), speakingPointAttempted = new Long(0), writingPointAttempted = new Long(
				0), readingPointObtained = new Long(0), listeningPointObtained = new Long(
				0), speakingPointObtained = new Long(0), writingPointObtained = new Long(
				0);
		 for(int ii=0; ii<secs.length;ii++){
	    		if(secs[ii].getSecondaryObjectiveName().contains("Language Arts / Social Studies / History")|| 
	    				secs[ii].getSecondaryObjectiveName().contains("Mathematics / Science / Technical Subjects") || 
	    					secs[ii].getSecondaryObjectiveName().contains("Foundational Skills")){
	    			
	    			if("READING".equals(secs[ii].getSubtestName().toUpperCase()) || "LECTURA".equals(secs[ii].getSubtestName().toUpperCase())){
	    				readingPointPossible += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsPossible());
	    				readingPointAttempted += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsAttempted());
	    				readingPointObtained += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsObtained());
	    			}else if("LISTENING".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCUCHANDO".equals(secs[ii].getSubtestName().toUpperCase())){
	    				listeningPointPossible += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsPossible());
	    				listeningPointAttempted += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsAttempted());
	    				listeningPointObtained += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsObtained());
	    			}else if("SPEAKING".equals(secs[ii].getSubtestName().toUpperCase()) || "HABLANDO".equals(secs[ii].getSubtestName().toUpperCase())){
	    				speakingPointPossible += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsPossible());
	    				speakingPointAttempted += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsAttempted());
	    				speakingPointObtained += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsObtained());
	    			}else if("WRITING".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCRITURA".equals(secs[ii].getSubtestName().toUpperCase())){
	    				writingPointPossible += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsPossible());
	    				writingPointAttempted += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsAttempted());
	    				writingPointObtained += new Long(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getPointsObtained());
	    			}
	    		}
		 }
		 for(int ii=0; ii<secs.length;ii++){
	    		if(secs[ii].getSecondaryObjectiveName().toUpperCase().contains("ACADEMIC")){
	    			if(secs[ii].getSubtestName()!= null){
		    			if("READING".equals(secs[ii].getSubtestName().toUpperCase()) || "LECTURA".equals(secs[ii].getSubtestName().toUpperCase())){
		    				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
		    				details.setPointsAttempted(readingPointAttempted);
		    				details.setPointsObtained(readingPointObtained);
		    				details.setPointsPossible(readingPointPossible);
		    				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(readingPointObtained.intValue(), readingPointPossible.intValue())));
		    				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(readingPointObtained.intValue(), readingPointPossible.intValue()))).getCode());
		    				details.setAtsArchive(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive());
		    				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
		    			}else if("LISTENING".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCUCHANDO".equals(secs[ii].getSubtestName().toUpperCase())){
		    				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
		    				details.setPointsAttempted(listeningPointAttempted);
		    				details.setPointsObtained(listeningPointObtained);
		    				details.setPointsPossible(listeningPointPossible);
		    				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(listeningPointObtained.intValue(), listeningPointPossible.intValue())));
		    				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(listeningPointObtained.intValue(), listeningPointPossible.intValue()))).getCode());
		    				details.setAtsArchive(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive());
		    				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
		    			}else if("SPEAKING".equals(secs[ii].getSubtestName().toUpperCase()) || "HABLANDO".equals(secs[ii].getSubtestName().toUpperCase())){
		    				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
		    				details.setPointsAttempted(speakingPointAttempted);
		    				details.setPointsObtained(speakingPointObtained);
		    				details.setPointsPossible(speakingPointPossible);
		    				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(speakingPointObtained.intValue(), speakingPointPossible.intValue())));
		    				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(speakingPointObtained.intValue(), speakingPointPossible.intValue()))).getCode());
		    				details.setAtsArchive(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive());
		    				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
		    			}else if("WRITING".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCRITURA".equals(secs[ii].getSubtestName().toUpperCase())){
		    				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
		    				details.setPointsAttempted(writingPointAttempted);
		    				details.setPointsObtained(writingPointObtained);
		    				details.setPointsPossible(writingPointPossible);
		    				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage(writingPointObtained.intValue(), writingPointPossible.intValue())));
		    				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage(writingPointObtained.intValue(), writingPointPossible.intValue()))).getCode());
		    				details.setAtsArchive(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive());
		    				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
		    			}
	    			}
	    		}
		}
		String compSet ="";
    	String oralSet ="";
    	String prodSet ="";
    	String liteSet ="";
    	for(int ii=0; ii<secs.length;ii++){
    		if(secs[ii].getSecondaryObjectiveName().toUpperCase().contains("ACADEMIC")){
	    		if(("READING".equals(secs[ii].getSubtestName().toUpperCase()) || "LISTENING".equals(secs[ii].getSubtestName().toUpperCase()) 
	    				|| "LECTURA".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCUCHANDO".equals(secs[ii].getSubtestName().toUpperCase())) 
	    						&& "F".equals(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive())){
	    			compSet = "F";
	    		}if(("LISTENING".equals(secs[ii].getSubtestName().toUpperCase()) || "SPEAKING".equals(secs[ii].getSubtestName().toUpperCase()) 
	    				||"ESCUCHANDO".equals(secs[ii].getSubtestName().toUpperCase()) || "HABLANDO".equals(secs[ii].getSubtestName().toUpperCase())) 
	    						&& "F".equals(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive())){
	    			oralSet = "F";
	    		}if(("SPEAKING".equals(secs[ii].getSubtestName()) || "WRITING".equals(secs[ii].getSubtestName())
	    				||"HABLANDO".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCRITURA".equals(secs[ii].getSubtestName().toUpperCase())) 
	    						&& "F".equals(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive())){
	    			prodSet = "F";
	    		}if(("READING".equals(secs[ii].getSubtestName()) || "WRITING".equals(secs[ii].getSubtestName())
	    				||"LECTURA".equals(secs[ii].getSubtestName().toUpperCase()) || "ESCRITURA".equals(secs[ii].getSubtestName().toUpperCase())) 
	    						&& "F".equals(studentScoreSummaryData.get(secs[ii].getSecondaryObjectiveId()).getAtsArchive())){
	    			liteSet = "F";
	    		}
    		}
    	}
    	for(int ii=0; ii<secs.length;ii++){
			if("COMPREHENSION ACADEMIC".equals(secs[ii].getSecondaryObjectiveName().toUpperCase())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(new Long(listeningPointAttempted.intValue()+readingPointAttempted.intValue()));
				details.setPointsObtained(new Long(listeningPointObtained.intValue()+readingPointObtained.intValue()));
				details.setPointsPossible(new Long(listeningPointPossible.intValue()+readingPointPossible.intValue()));
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage((listeningPointObtained.intValue()+readingPointObtained.intValue()), (listeningPointPossible.intValue()+readingPointPossible.intValue()))));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage((listeningPointObtained.intValue()+readingPointObtained.intValue()), (listeningPointPossible.intValue()+readingPointPossible.intValue())))).getCode());
				if(!"".equals(compSet)){
					details.setAtsArchive(compSet);
				}
				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
			}else if("ORAL ACADEMIC".equals(secs[ii].getSecondaryObjectiveName().toUpperCase())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(new Long(listeningPointAttempted.intValue()+speakingPointAttempted.intValue()));
				details.setPointsObtained(new Long(listeningPointObtained.intValue()+speakingPointObtained.intValue()));
				details.setPointsPossible(new Long(listeningPointPossible.intValue()+speakingPointPossible.intValue()));
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage((listeningPointObtained.intValue()+speakingPointObtained.intValue()), (listeningPointPossible.intValue()+speakingPointPossible.intValue()))));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage((listeningPointObtained.intValue()+speakingPointObtained.intValue()), (listeningPointPossible.intValue()+speakingPointPossible.intValue())))).getCode());
				if(!"".equals(oralSet)){
					details.setAtsArchive(oralSet);
				}
				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
			}else if("PRODUCTIVE ACADEMIC".equals(secs[ii].getSecondaryObjectiveName().toUpperCase())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(new Long(writingPointAttempted.intValue()+speakingPointAttempted.intValue()));
				details.setPointsObtained(new Long(writingPointObtained.intValue()+speakingPointObtained.intValue()));
				details.setPointsPossible(new Long(writingPointPossible.intValue()+speakingPointPossible.intValue()));
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage((writingPointObtained.intValue()+speakingPointObtained.intValue()), (writingPointPossible.intValue()+speakingPointPossible.intValue()))));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage((writingPointObtained.intValue()+speakingPointObtained.intValue()), (writingPointPossible.intValue()+speakingPointPossible.intValue())))).getCode());
				if(!"".equals(prodSet)){
					details.setAtsArchive(prodSet);
				}
				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
			}else if("LITERACY ACADEMIC".equals(secs[ii].getSecondaryObjectiveName().toUpperCase())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(new Long(writingPointAttempted.intValue()+readingPointAttempted.intValue()));
				details.setPointsObtained(new Long(writingPointObtained.intValue()+readingPointObtained.intValue()));
				details.setPointsPossible(new Long(writingPointPossible.intValue()+readingPointPossible.intValue()));
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage((writingPointObtained.intValue()+readingPointObtained.intValue()), (writingPointPossible.intValue()+readingPointPossible.intValue()))));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage((writingPointObtained.intValue()+readingPointObtained.intValue()), (writingPointPossible.intValue()+readingPointPossible.intValue())))).getCode());
				if(!"".equals(liteSet)){
					details.setAtsArchive(liteSet);
				}
				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
			}else if("OVERALL ACADEMIC".equals(secs[ii].getSecondaryObjectiveName().toUpperCase())){
				StudentScoreSummaryDetails details = new StudentScoreSummaryDetails();
				details.setPointsAttempted(new Long(readingPointAttempted.intValue()+writingPointAttempted.intValue()+listeningPointAttempted.intValue()+speakingPointAttempted.intValue()));
				details.setPointsObtained(new Long(readingPointObtained.intValue()+writingPointObtained.intValue()+listeningPointObtained.intValue()+speakingPointObtained.intValue()));
				details.setPointsPossible(new Long(readingPointPossible.intValue()+writingPointPossible.intValue()+listeningPointPossible.intValue()+speakingPointPossible.intValue()));
				details.setDecimalPercentObtained(new Float(ScorerHelper.calculatePercentage((readingPointObtained.intValue()+writingPointObtained.intValue()+listeningPointObtained.intValue()+speakingPointObtained.intValue()), (readingPointPossible.intValue()+writingPointPossible.intValue()+listeningPointPossible.intValue()+speakingPointPossible.intValue()))));
				details.setMasteryLevel((MasteryLevel.masteryLevelForPercentage(ScorerHelper.calculatePercentage((readingPointObtained.intValue()+writingPointObtained.intValue()+listeningPointObtained.intValue()+speakingPointObtained.intValue()), (readingPointPossible.intValue()+writingPointPossible.intValue()+listeningPointPossible.intValue()+speakingPointPossible.intValue())))).getCode());
				if(!"".equals(compSet) || !"".equals(oralSet) || !"".equals(prodSet) || !"".equals(liteSet)){
					details.setAtsArchive("F");
				}
				tempMap.put(secs[ii].getSecondaryObjectiveId(), details);
			}
		}
    	return tempMap; 
	 }
}