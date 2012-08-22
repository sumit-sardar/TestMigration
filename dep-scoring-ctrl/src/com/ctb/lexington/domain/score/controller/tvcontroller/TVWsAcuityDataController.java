package com.ctb.lexington.domain.score.controller.tvcontroller;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreData;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentItemScoreDetails;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentTestData;
import com.ctb.lexington.db.data.StudentTestDetails;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.domain.teststructure.MasteryLevel;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.CompositeScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ContentAreaScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.ItemScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.PrimaryObjScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.SecondaryObjScore;
import com.mcgraw_hill.ctb.acuity.scoring.ScoringServiceStub.StudentScore;

/**
 * @author TCS
 * New controller to design score objects to provide to Acuity. 
 */

public class TVWsAcuityDataController {
	
	private StudentItemScoreData studentItemScoreData;
	private AdminData adminData;
	private ContextData contextData;
	private CurriculumData currData;
	private StsTotalStudentScoreData totalData;
	private StudentTestData testData;
	
	public TVWsAcuityDataController(CurriculumData currData, ContextData context, StsTestResultFactData factData, StudentScore wsTvData, 
			StudentScoreSummaryData studentScoreSummaryData, Map<String,String> lossHoss, AdminData adminData, 
			StudentItemScoreData studentItemScoreData, StsTotalStudentScoreData totalData, 
			Connection con, StudentTestData testData) {
		
		this.studentItemScoreData = studentItemScoreData;
		this.adminData = adminData;
		this.contextData = context;
		this.currData = currData;
		this.totalData = totalData;
		this.testData = testData;
		if(context.getCurrentResultId() == 1) {
			getContentAreaFactBeans(factData, wsTvData, lossHoss, studentScoreSummaryData);
			getCompositeFactBeans(wsTvData);
		}
    }
	
	//Set the composite fact data
	public void getCompositeFactBeans(StudentScore wsTvData) {
		
		if(totalData.size() > 0) {
			Composite [] composites = currData.getComposites();
			ArrayList<CompositeScore> facts = new ArrayList<CompositeScore>();
			for(int i=0;i<composites.length;i++) {
				StsTotalStudentScoreDetail total = totalData.get(composites[i].getCompositeName());
				if(total != null && ("T".equals(total.getValidScore()) || "Y".equals(total.getValidScore()))) {
					CompositeScore newFact = new CompositeScore();
					newFact.setCompositeId(composites[i].getCompositeId().toString());
					newFact.setCompositeName(composites[i].getCompositeName());
					if(total.getGradeEquivalent() != null) {
                        Float ge = new Float(Float.parseFloat(total.getGradeEquivalent().replaceAll("13","12.9").replace('+', '9')));
                        
                        DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
                        float gedec = new Float(df2.format(ge)).floatValue();
                        
                        newFact.setGradeEquivalent(new Float(gedec));
                   }
					newFact.setNationalPercentile((total.getNationalPercentile()==null)?0l:new Long(total.getNationalPercentile().longValue()));
	                newFact.setNationalStanine((total.getNationalStanine()==null)?0l:new Long(total.getNationalStanine().longValue()));
	                newFact.setNormCurveEquivalent((total.getNormalCurveEquivalent()==null)?0l:new Long(total.getNormalCurveEquivalent().longValue()));
	                newFact.setPointsAttempted(total.getPointsAttempted());
	                newFact.setPointsObtained(total.getPointsObtained());
	                newFact.setPointsPossible(composites[i].getCompositePointsPossible());
	                newFact.setScaleScore((total.getScaleScore()==null)?0l:new Long(total.getScaleScore().longValue()));
	                facts.add(newFact);
				}
			}
			wsTvData.setCompositeScores((CompositeScore []) facts.toArray(new CompositeScore[0]));
		}
		
	}
	
	// Set the content area related data
	public void getContentAreaFactBeans(StsTestResultFactData factData, StudentScore wsTvData, Map<String,String> lossHoss, 
			StudentScoreSummaryData studentScoreSummaryData) {
        ContentArea [] contentAreas = currData.getContentAreas();
        ArrayList<ContentAreaScore> contentAreaFact = new ArrayList<ContentAreaScore>();
        Map<String,String> populated = new HashMap<String,String>();
        if(contentAreas != null && contentAreas.length > 0) {
            for(int i=0;i<contentAreas.length;i++) {
            	if(populated != null && !populated.containsKey(contentAreas[i].getContentAreaName())) {
            	StsTestResultFactDetails fact = factData.get(contentAreas[i].getContentAreaName());
               if(fact != null &&
                    ("T".equals(fact.getValidScore()) || "Y".equals(fact.getValidScore()))) {
            	   ContentAreaScore newFact = new ContentAreaScore();
                   newFact.setContentAreaId(contentAreas[i].getContentAreaId().toString().substring(4));
                   String caName = contentAreas[i].getContentAreaName();
                   newFact.setContentAreaName(caName);
                   updateIncompleteFlag(caName, newFact);
                   if(fact.getGradeEquivalent() != null) {
                        newFact.setGradeEquivalent( new Float(Float.parseFloat(fact.getGradeEquivalent().replaceAll("13","12.9").replace('+', '9'))));
                   }
                   newFact.setNationalPercentile((fact.getNationalPercentile()==null)?-99l: fact.getNationalPercentile().longValue());
                   newFact.setNationalStanine((fact.getNationalStanine()==null)?-99l: fact.getNationalStanine().longValue());
                   newFact.setNormCurveEquivalent((fact.getNormalCurveEquivalent()==null)?-99l:new Long(fact.getNormalCurveEquivalent().longValue()));
                   if(fact != null && fact.getPercentObjectiveMastery() != null)
                	   newFact.setPercentMastery((fact.getPercentObjectiveMastery()==null)?-99f:new Float(fact.getPercentObjectiveMastery().longValue()));
                   newFact.setPercentObtained((fact.getPercentObtained()==null)? -99f : fact.getPercentObtained().floatValue());
                   newFact.setScaleScore((fact.getScaleScore()==null)?-99l:new Long(fact.getScaleScore().longValue()));
                   newFact.setSem((fact.getStandardErrorOfMeasurement() == null) ? -99l : new Long(fact.getStandardErrorOfMeasurement().longValue()));
                   String[] lossHossValue = lossHoss.get(contentAreas[i].getContentAreaName()).split(",");
                   newFact.setMinScaleScore(Long.parseLong(lossHossValue[0]));
                   newFact.setMaxScaleScore(Long.parseLong(lossHossValue[1]));
                   newFact.setPointsAttempted((fact.getPointsAttempted()==null)? -99l:new Long(fact.getPointsAttempted().longValue()));
                   newFact.setPointsObtained((fact.getPointsObtained()==null)? -99l:new Long(fact.getPointsObtained().longValue()));
                   newFact.setPointsPossible((fact.getPointsPossible()==null)? -99l:new Long(fact.getPointsPossible().longValue()));
                   PrimaryObjScore[] priObjScores = getPrimaryObjectiveScores(contentAreas[i].getContentAreaId(), studentScoreSummaryData);
                   newFact.setPrimaryObjScores(priObjScores);
                   contentAreaFact.add(newFact);
               }
               populated.put(contentAreas[i].getContentAreaName(), contentAreas[i].getContentAreaName());
            }
            }
            wsTvData.setContentAreaScores((ContentAreaScore []) contentAreaFact.toArray(new ContentAreaScore[0]));
        }
    }
	
	private void updateIncompleteFlag(String caName, ContentAreaScore newFact) {
		boolean completeFlag = false;
		List<String> subtestNames = new ArrayList<String>();
		List<StudentTestDetails> studentTestDetails = new ArrayList<StudentTestDetails>();
		
		if(testData != null) {
			for (int i = 0; i < testData.size(); i++) {
				StudentTestDetails stuTestDetails = testData.get(i);
				if(stuTestDetails.getSubject().equalsIgnoreCase(caName) && stuTestDetails.getSample().equalsIgnoreCase("F")) {
					studentTestDetails.add(stuTestDetails);
				}
			}
			if(studentTestDetails != null && studentTestDetails.size() > 0) {
				int counter = 0;
				for (Iterator<StudentTestDetails> iterator = studentTestDetails.iterator(); iterator
						.hasNext();) {
					StudentTestDetails tempDetails = (StudentTestDetails) iterator
							.next();
					String coStatus = tempDetails.getCompletionStatus();
					if (!"SC".equals(coStatus) && !"NT".equals(coStatus)
							&& !"IP".equals(coStatus) && !"IN".equals(coStatus)) {
						counter ++;
						subtestNames.add(tempDetails.getSubTestName());
					}
				}
				if(counter == studentTestDetails.size()) {
					completeFlag = true;
				} else {
					completeFlag = false;
				}
				counter = 0;
			}
			newFact.setCompleteContArea(completeFlag);
			//newFact.setSubtestNames((String[]) subtestNames.toArray());
			System.out.println("newFact.getContentAreaName() -> " + newFact.getContentAreaName());
			System.out.println("IncompleteFlag -> " + completeFlag);
			System.out.println(subtestNames.size() + "-" + subtestNames.get(0).toString());
		}
	}
	
	private PrimaryObjScore[] getPrimaryObjectiveScores (Long contentAreaId, StudentScoreSummaryData studentScoreSummaryData) {
		PrimaryObjective [] prims = currData.getPrimaryObjectives();
		ArrayList<PrimaryObjScore> primObjFacts = new ArrayList<PrimaryObjScore>();
		Set<Long> populated = new HashSet<Long>();
		
		for(int i=0;i<prims.length;i++) {
			if(prims[i].getContentAreaId().equals(contentAreaId) || prims[i].getContentAreaId().longValue() == contentAreaId.longValue()) {
				PrimaryObjective prim = currData.getPrimObjById(prims[i].getPrimaryObjectiveId());
				PrimaryObjScore priObjFacts = new PrimaryObjScore();
				priObjFacts.setPrimaryObjId(prims[i].getMonarchId());
				StudentScoreSummaryDetails details = studentScoreSummaryData.get(prims[i].getPrimaryObjectiveId());
				priObjFacts.setNationalAverage(prim.getNationalAverage() == null? -99f:prim.getNationalAverage().floatValue());
				priObjFacts.setHighModMasteryRange(new Long (prim.getHighMasteryRange()));
				priObjFacts.setLowModMasteryRange(new Long (prim.getLowMasteryRange()));
				priObjFacts.setModMasteryRange(prim.getLowMasteryRange() + "-" + prim.getHighMasteryRange());
				setMasteryRange(priObjFacts);
				if(details != null) {
					priObjFacts.setPercentObtained(details.getPercentObtained().floatValue());
					priObjFacts.setPointsAttempted(details.getPointsAttempted());
					priObjFacts.setPointsObtained(details.getPointsObtained());
					priObjFacts.setPointsPossible(details.getPointsPossible());
					if(details != null && details.getPercentObtained() != null) {
                        MasteryLevel mastery = MasteryLevel.masteryLevelForPercentage(details.getPercentObtained().intValue());
                        details.setMasteryLevel(mastery.getCode());
                        priObjFacts.setMasteryLevel( new Integer( 
                                	"NM".equals(details.getMasteryLevel())?1:
                                    "PM".equals(details.getMasteryLevel())?2:
                                    "M".equals(details.getMasteryLevel())?3:
                                    "NON-MASTERY".equals(details.getMasteryLevel())?1:
                                    "PARTIAL MASTERY".equals(details.getMasteryLevel())?2:
                                    "MASTERY".equals(details.getMasteryLevel())?3:
                                    "Not Mastered".equals(details.getMasteryLevel())?1:
                                    "Partially Mastered".equals(details.getMasteryLevel())?2:
                                    "Mastered".equals(details.getMasteryLevel())?3:4 ));
                    }
				}
				if(!populated.contains(prims[i].getPrimaryObjectiveId())) {
					SecondaryObjScore[] secondaryObjScore = getSecondaryObjScores(prims[i].getPrimaryObjectiveId(), studentScoreSummaryData);
					priObjFacts.setSecondaryObjScores(secondaryObjScore);
					primObjFacts.add(priObjFacts);
					populated.add(prims[i].getPrimaryObjectiveId());
				}
			}
			
        }
		return (PrimaryObjScore []) primObjFacts.toArray(new PrimaryObjScore[0]);
	}
	
	private SecondaryObjScore[] getSecondaryObjScores(Long primaryObjId, StudentScoreSummaryData studentScoreSummaryData) {
		
		SecondaryObjective [] secs = currData.getSecondaryObjectives();
		ArrayList<SecondaryObjScore> secObjFacts = new ArrayList<SecondaryObjScore>();
		Set<Long> populated = new HashSet<Long>();
		
		for(int i=0;i<secs.length;i++) {
			if(secs[i].getPrimaryObjectiveId().equals(primaryObjId) || secs[i].getPrimaryObjectiveId().longValue() == primaryObjId.longValue()) {
				SecondaryObjScore newSecObjFacts = new SecondaryObjScore();
				newSecObjFacts.setSecondaryObjId(secs[i].getMonarchId());
				StudentScoreSummaryDetails details = studentScoreSummaryData.get(secs[i].getSecondaryObjectiveId());
				if(details != null) {
					newSecObjFacts.setPointsObtained(details.getPointsObtained());
					newSecObjFacts.setPointsPossible(secs[i].getSecondaryObjectivePointsPossible());
					newSecObjFacts.setPercentObtained(details.getPercentObtained().floatValue());
					newSecObjFacts.setPointsAttempted(details.getPointsAttempted());
					newSecObjFacts.setMasteryLevel( new Integer( 
                            "Not Mastered".equals(details.getMasteryLevel())?1:
                            "Partially Mastered".equals(details.getMasteryLevel())?2:
                            "Mastered".equals(details.getMasteryLevel())?3:4 ));
				}
				if(!populated.contains(secs[i].getSecondaryObjectiveId())) {
					ItemScore[] itemScores = getItemScoresFact(secs[i].getSecondaryObjectiveId());
					newSecObjFacts.setItemScores(itemScores);
					secObjFacts.add(newSecObjFacts);
					populated.add(secs[i].getSecondaryObjectiveId());
				}
			}
		}
		return (SecondaryObjScore []) secObjFacts.toArray(new SecondaryObjScore[0]);
	}
	
	private ItemScore[] getItemScoresFact (Long secondaryObjectiveId) {
		
		Item[] items = currData.getItems();
		ArrayList<ItemScore> itemFacts = new ArrayList<ItemScore>();
		Set<String> populated = new HashSet<String>();
		
		for(int i=0;i<items.length;i++) {
			if(items[i].getSecondaryObjectiveId().equals(secondaryObjectiveId) || items[i].getSecondaryObjectiveId().longValue() == secondaryObjectiveId.longValue()) {
				Item item = items[i];
				ItemScore itemFact = new ItemScore();
				itemFact.setItemId(item.getOasItemId());
				itemFact.setPointsPossible(new Long(item.getItemPointsPossible().intValue()));
				String normGroup = "6".equals(adminData.getNormsGroup())?"FALL":
					"07".equals(adminData.getNormsGroup())?"FALL":
                	"7".equals(adminData.getNormsGroup())?"FALL":
                    "18".equals(adminData.getNormsGroup())?"WINTER":
                    "19".equals(adminData.getNormsGroup())?"WINTER":
                    "30".equals(adminData.getNormsGroup())?"SPRING":
                    "31".equals(adminData.getNormsGroup())?"SPRING":
                    adminData.getNormsGroup();
				if( ("1" + contextData.getGradeId().toString()).equals(items[i].getSubtestLevel()) || 
                		("19/20".equals(items[i].getSubtestLevel()) && ("9".equals(contextData.getGradeId().toString()) || "10".equals(contextData.getGradeId().toString()))) ||
                		("21-22".equals(items[i].getSubtestLevel()) && ("11".equals(contextData.getGradeId().toString()) || "12".equals(contextData.getGradeId().toString()))) ||
                		("21/22".equals(items[i].getSubtestLevel()) && ("11".equals(contextData.getGradeId().toString()) || "12".equals(contextData.getGradeId().toString())))) {
                    itemFact.setNationalAverage(item.getNationalAverage(normGroup + String.valueOf(contextData.getGradeId().longValue())).doubleValue());
                } else {
                    itemFact.setNationalAverage(new Double(-1.0));
                }
				PrimaryObjective [] prims = currData.getPrimaryObjectives();
                for(int j=0;j<prims.length;j++) {
    				String contentAreaName = prims[j].getPrimaryObjectiveName();
    				if(studentItemScoreData.contains(item.getOasItemId()+ contentAreaName)) {
    					StudentItemScoreDetails scoreDetails = studentItemScoreData.get(item.getOasItemId()+ contentAreaName);
    					if(scoreDetails != null && scoreDetails.getAtsArchive()!= null && !"F".equals(scoreDetails.getAtsArchive())) {
    						itemFact.setPointsObtained(scoreDetails.getPoints() == null? -99l:new Long(scoreDetails.getPoints().intValue()));
    					} else {
    						itemFact.setPointsObtained(new Long(0));
    					}
    				}
                }
                if(!populated.contains(itemFact.getItemId())) {
                	itemFacts.add(itemFact);
                	populated.add(itemFact.getItemId());
                }
			}
		}
		return (ItemScore[]) itemFacts.toArray(new ItemScore[0]);
	}
	
	private void setMasteryRange(PrimaryObjScore priObjFacts) {
		
	}

}
