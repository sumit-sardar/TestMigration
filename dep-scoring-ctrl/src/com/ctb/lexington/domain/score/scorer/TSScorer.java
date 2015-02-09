package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentItemScoreData;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SecondaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TASCCompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TASCContentAreaDerivedScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TASCSecondaryObjectiveDerivedScoreCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;

public class TSScorer extends STScorer {
	private List invalidSubtestIds;
	private List subtestIds;
    
    public TSScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new TASCContentAreaDerivedScoreCalculator(channel, this));
        addCalculator(new TASCSecondaryObjectiveDerivedScoreCalculator(channel, this));
      	addCalculator(new TASCCompositeScoreCalculator(channel,this));
      	
        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SecondaryObjectiveDerivedScoreEvent.class);
      	channel.subscribe(this, SubtestContentAreaCompositeScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeAndDerivedEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
   	 super.onEvent(event);
       invalidSubtestIds = new ArrayList();
    }
    
    // NOTE: This is the same code in TBScorer and TVScorer
    public void onEvent(final SubtestItemCollectionEvent event) {
        super.onEvent(event);
        final StudentSubtestScoresData subtestScoresData = getResultHolder()
                .getStudentSubtestScoresData();
        final StudentSubtestScoresDetails subtestScoresDetail = subtestScoresData.get(DatabaseHelper.asLong(
                event.getItemSetId()));
        subtestScoresDetail.setItemSetName(event.getItemSetName());
    }

    // NOTE: This is the same code in TBScorer and TVScorer
    public void onEvent(ContentAreaDerivedScoreEvent event) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        StsTestResultFactDetails factDetails = factData.get(event.getContentAreaName());
        if(event.getScaleScore() != null) 
        	factDetails.setScaleScore(event.getScaleScore());
        if(event.getNormalCurveEquivalent() != null)
        	factDetails.setNormalCurveEquivalent(event.getNormalCurveEquivalent());
        if(event.getNationalPercentile() != null)
        	factDetails.setNationalPercentile(event.getNationalPercentile());
        factDetails.setNormGroup(event.getNormGroup());
        factDetails.setNormYear(event.getNormYear());
        factDetails.setSubtestScoringStatus(event.getSubtestScoringStatus());
        factDetails.setPerformanceLevelCode(event.getPerformanceLevel().getCode());
        factDetails.setPerformanceLevel(event.getPerformanceLevel().getDescription());
        factDetails.setProficiencyRange(event.getProficiencyRange());
    }

    public void onEvent(PrimaryObjectivePercentMasteryEvent popmEvent) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        StsTestResultFactDetails details = factData.get(popmEvent.getContentAreaName());
        details.setPercentObjectiveMastery(new Long(popmEvent.getPercentMastery()));
    }

    public void onEvent(SubtestContentAreaCompositeScoreEvent event) {
        StsTotalStudentScoreDetail detail = getResultHolder().getStsTotalStudentScoreData().get(event.getType());
        if(event.getScaleScore() != null)
        	detail.setScaleScore(event.getScaleScore());
        if(event.getNormalCurveEquivalent() != null)
        	detail.setNormalCurveEquivalent(event.getNormalCurveEquivalent());
        if(event.getNationalPercentile() != null)
        	detail.setNationalPercentile(event.getNationalPercentile());
        if(event.getProficencyLevel() != null)
        	detail.setProficencyLevel(event.getProficencyLevel());
        if (event.getPointsAttempted() != null)
            detail.setPointsAttempted(event.getPointsAttempted());
        if (event.getPointsObtained() != null)
            detail.setPointsObtained(event.getPointsObtained());
        if (event.getPointsPossible() != null)
            detail.setPointsPossible(event.getPointsPossible());
        
        detail.setNormYear(event.getNormYear());
        detail.setValidScore(event.getValidScore());
        detail.setScaleScoreRangeForProficiency(event.getScaleScoreRange());
    }
    
    public void onEvent(SubtestValidEvent event) {
        //setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }

    public void onEvent(SubtestInvalidEvent event) {
    	invalidSubtestIds.add(event.getItemSetId());
    }
    
    public void onEvent(SubtestContentAreaCompositeAndDerivedEvent event) {
    	StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
    	Map subtestContentAreaCompositeAndDerivedList = event.getSubtestContentAreaCompositeAndDerivedScore();
    	 for (final Iterator it = subtestContentAreaCompositeAndDerivedList.entrySet().iterator(); it.hasNext();) {
             final Map.Entry entry = (Map.Entry) it.next();
             final String contentArea = (String) entry.getKey();
             final SubtestContentAreaCompositeAndDerivedScore subtestContentAreaCompositeAndDerivedScore = (SubtestContentAreaCompositeAndDerivedScore) entry.getValue();
             StsTestResultFactDetails factDetails = factData.get(contentArea);
             factDetails.setPointsObtained( new Long(subtestContentAreaCompositeAndDerivedScore.getPointsObtained()));
             factDetails.setPointsAttempted( new Long(subtestContentAreaCompositeAndDerivedScore.getPointsAttempted()));
             factDetails.setPointsPossible( new Long(subtestContentAreaCompositeAndDerivedScore.getPointsPossible()));
             factDetails.setPerformanceLevelCode(String.valueOf(subtestContentAreaCompositeAndDerivedScore.getProficencyLevelCode()));
             factDetails.setDecimalPercentObtained(new Float(subtestContentAreaCompositeAndDerivedScore.getPercentObtained()));
             factDetails.setScaleScore(new BigDecimal(subtestContentAreaCompositeAndDerivedScore.getScaleScore()));
             
             if(new BigDecimal(subtestContentAreaCompositeAndDerivedScore.getNationalPercentile())!=null){
            	 factDetails.setNationalPercentile(new BigDecimal(subtestContentAreaCompositeAndDerivedScore.getNationalPercentile()));
             }
             if(new BigDecimal(subtestContentAreaCompositeAndDerivedScore.getNormCurveEquivalent())!=null){
            	 factDetails.setNormalCurveEquivalent(new BigDecimal(subtestContentAreaCompositeAndDerivedScore.getNormCurveEquivalent()));
             }
             if(subtestContentAreaCompositeAndDerivedScore.getProficencyLevelDescription() != null)
            	 factDetails.setPerformanceLevel(subtestContentAreaCompositeAndDerivedScore.getProficencyLevelDescription());
             if(subtestContentAreaCompositeAndDerivedScore.getValidScore() != null) {
            	 factDetails.setValidScore(subtestContentAreaCompositeAndDerivedScore.getValidScore());
             	 markObjectiveAndItemsForSubtest(contentArea, subtestContentAreaCompositeAndDerivedScore.getValidScore());
             }
    	 }
    }
    
    public void onEvent(SecondaryObjectiveDerivedScoreEvent event){
    	StudentScoreSummaryData summaryData = getResultHolder().getStudentScoreSummaryData();
    	StudentScoreSummaryDetails details = summaryData.get(event.getObjectiveId());
    	
    	if(event.getScaleScore()!= null){
	    	if(details.getScaleScore() == null) {
	    		details.setScaleScore(Long.valueOf(event.getScaleScore().toBigInteger().longValue()));
	    	}else if (!details.getSubtestId().equals(event.getSubtestId())){
	    		details.setScaleScore(Long.valueOf(event.getScaleScore().toBigInteger().longValue()));
	    	}
    	}
    	if(event.getMasteryLevel() != null ){
	    	if(details.getMasteryLevel() == null){
	    		details.setMasteryLevel(event.getMasteryLevel().toString());
	    	}else if (!details.getSubtestId().equals(event.getSubtestId())){
	    		details.setMasteryLevel(event.getMasteryLevel().toString());
	    	}
    	}
    	if(event.getScaleScoreRangeForMastery() != null){
    		details.setScaleScoreRangeForMastery(event.getScaleScoreRangeForMastery().toString());
    	}
    }
    
    public void onEvent(ResponseReceivedEvent event){
    	super.onEvent(event);
    	StudentScoreSummaryData summaryData = getResultHolder().getStudentScoreSummaryData();
    	StudentItemScoreData itemData = getResultHolder().getStudentItemScoreData();
    	CurriculumData currData = getResultHolder().getCurriculumData();
    	Map crItemMap = (Map)currData.getCrItemMap();
    	Item item = null;
    	if(crItemMap.containsKey(event.getItemId())){
    		item = (Item)crItemMap.get(event.getItemId());
    	}
    	// Considering one secondary objective has single Item
    	if (item != null){
	    	StudentScoreSummaryDetails details = summaryData.get(item.getSecondaryObjectiveId());
	    	if(event.getConditionCode()!= null) {
	    		details.setConditionCode(event.getConditionCode().toString());
	    	}
    	}
    }
    
    private void markObjectiveAndItemsForSubtest(String contentArea, String validScore ){
        StudentScoreSummaryData summaryData = getResultHolder().getStudentScoreSummaryData();
        StudentItemScoreData itemData = getResultHolder().getStudentItemScoreData();
    	CurriculumData currData = getResultHolder().getCurriculumData();
        SecondaryObjective[] sec = currData.getSecondaryObjectives();
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
    	List contentAreas = currData.getContentAreasByName(contentArea);
    	Iterator caIter = contentAreas.iterator();
    	 while(caIter.hasNext()) {
             ContentArea ca = (ContentArea) caIter.next();
            	 Long caid = ca.getContentAreaId();
                 for(int i=0;i<sec.length;i++) {
		                if(CTBConstants.VALID_SCORE.equals(validScore) && computeValidSubtestScoreFlag(ca.getSubtestId().intValue())) { 
		                     if(caid.longValue() == (new Long(sec[i].getProductId().toString()+sec[i].getPrimaryObjectiveId().toString())).longValue()) {
		                         summaryData.get(sec[i].getSecondaryObjectiveId()).setAtsArchive("T");
		                     }
		                     
		                     if("Writing".equalsIgnoreCase(ca.getContentAreaName())&& "Essay Writing".equals(sec[i].getSecondaryObjectiveName())
		                    		 && factData.get(ca.getContentAreaName()).getPerformanceLevelCode() != null 
		                    		 && summaryData.get(sec[i].getSecondaryObjectiveId()).getMasteryLevel() != null &&
		                    		 (new Integer(summaryData.get(sec[i].getSecondaryObjectiveId()).getMasteryLevel()).intValue() 
		                    		 < new Integer(factData.get(ca.getContentAreaName()).getPerformanceLevelCode()).intValue())){
		                    	 factData.get(ca.getContentAreaName()).setPerformanceLevelCode(summaryData.get(sec[i].getSecondaryObjectiveId()).getMasteryLevel());
		                     }
		                     
		                }
		              else {
		            	  if(caid.longValue() == (new Long(sec[i].getProductId().toString()+sec[i].getPrimaryObjectiveId().toString())).longValue()) {
		                      summaryData.get(sec[i].getSecondaryObjectiveId()).setAtsArchive("F");
		                  }
		             }
                 }
                 if(CTBConstants.VALID_SCORE.equals(validScore) && computeValidSubtestScoreFlag(ca.getSubtestId().intValue())){
                	 itemData.markItemsValidForSubtest(ca.getSubtestId());
                 }else {
                	 itemData.markItemsNonValidForSubtest(ca.getSubtestId());
                 }
    	 }
    }
    
    private boolean computeValidSubtestScoreFlag(final Integer subtestId) {
        if (invalidSubtestIds.contains(subtestId)) {
            return false;
        }
        return true;
    }
}