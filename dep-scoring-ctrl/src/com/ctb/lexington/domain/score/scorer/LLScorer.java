package com.ctb.lexington.domain.score.scorer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.data.SubtestContentAreaCompositeAndDerivedScore;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeAndDerivedEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.LasLinkContentAreaDerivedScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.LasLinkCompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.PrimaryObjectivePercentMasteryCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Stringx;


/**
*  @author TCS
*/
public class LLScorer extends STScorer {
    public LLScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new LasLinkContentAreaDerivedScoreCalculator(channel, this));
        addCalculator(new PrimaryObjectivePercentMasteryCalculator(channel, this));
        addCalculator(new LasLinkCompositeScoreCalculator(channel,this));

        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeAndDerivedEvent.class);
        channel.subscribe(this, PrimaryObjectivePercentMasteryEvent.class);
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
        if(event.getScaleScore() != null) {
        	factDetails.setScaleScore(event.getScaleScore()); }
        factDetails.setNormGroup(event.getNormGroup());
        factDetails.setNormYear(event.getNormYear());
        
        factDetails.setPerformanceLevelCode(event.getPerformanceLevel().getCode());
        factDetails.setPerformanceLevel(event.getPerformanceLevel().getDescription());
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
       
       
        
        if (event.getPointsAttempted() != null)
            detail.setPointsAttempted(event.getPointsAttempted());
        if (event.getPointsObtained() != null)
            detail.setPointsObtained(event.getPointsObtained());
        if (event.getPointsPossible() != null)
            detail.setPointsPossible(event.getPointsPossible());
        
        detail.setNormYear(event.getNormYear());
        detail.setValidScore(event.getValidScore());
        detail.setProficencyLevel(event.getProficencyLevel());
        

    
    }
    public void onEvent(SubtestContentAreaCompositeAndDerivedEvent event) {
    	System.out.println("In SubtestContentAreaCompositeAndDerivedEvent");
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
             factDetails.setPerformanceLevel(subtestContentAreaCompositeAndDerivedScore.getProficencyLevelDescription());
             factDetails.setPercentObtained(new Long(subtestContentAreaCompositeAndDerivedScore.getPercentObtained()));
    	 }
        

    
    }
    
    public void onEvent(SubtestValidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }

    public void onEvent(SubtestInvalidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.INVALID_SCORE);
    }
}