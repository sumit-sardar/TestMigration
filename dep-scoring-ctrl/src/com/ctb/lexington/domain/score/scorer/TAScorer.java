package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.PrimaryObjectivePercentMasteryCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABEAdaptCompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABECompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABEAdaptContentAreaDerivedScoreCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.Stringx;

public class TAScorer extends STScorer {
	
	public TAScorer() throws CTBSystemException, IOException {
        super();
        System.out.println("-------Inside TAScorer.java-------");
        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new TABEAdaptContentAreaDerivedScoreCalculator(channel, this));
        addCalculator(new PrimaryObjectivePercentMasteryCalculator(channel, this));
        addCalculator(new TABEAdaptCompositeScoreCalculator(channel,this));

        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeScoreEvent.class);
        channel.subscribe(this, PrimaryObjectivePercentMasteryEvent.class);
    }
	
	
	public void onEvent(ContentAreaDerivedScoreEvent event) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        StsTestResultFactDetails factDetails = factData.get(event.getContentAreaName());
        if(event.getScaleScore() != null) 
        	factDetails.setScaleScore(event.getScaleScore());
        if(event.getStandardErrorMeasurement() != null) 
        	factDetails.setStandardErrorOfMeasurement(event.getStandardErrorMeasurement());
        if(event.getNormalCurveEquivalent() != null)
        	factDetails.setNormalCurveEquivalent(event.getNormalCurveEquivalent());
        if(event.getNationalPercentile() != null)
        	factDetails.setNationalPercentile(event.getNationalPercentile());
        if(Stringx.hasContent(event.getGradeEquivalent()))
        	factDetails.setGradeEquivalent(event.getGradeEquivalent());
        factDetails.setNormGroup(event.getNormGroup());
        factDetails.setNormYear(event.getNormYear());
       // System.out.println("TAScorer -> ContentAreaDerivedScoreEvent -> event.getScaleScore()==" + event.getScaleScore());
    }
	
	public void onEvent(PrimaryObjectivePercentMasteryEvent popmEvent) {
        StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
        //System.out.println("popmEvent.getContentAreaName() -> " + popmEvent.getContentAreaName() + "    ----    " + "popmEvent.getPercentMastery() -> " + popmEvent.getPercentMastery());
        StsTestResultFactDetails details = factData.get(popmEvent.getContentAreaName());
        details.setPercentObjectiveMastery(new Long(popmEvent.getPercentMastery()));
    }
	
	 public void onEvent(SubtestContentAreaCompositeScoreEvent event) {
		 //System.out.println("TAScorer -> ContentAreaDerivedScoreEvent -> event.getScaleScore()**<==>**" + event.getScaleScore());
	        StsTotalStudentScoreDetail detail = getResultHolder().getStsTotalStudentScoreData().get(event.getType());
	        if(event.getScaleScore() != null)
	        	detail.setScaleScore(event.getScaleScore());
	        if(event.getNormalCurveEquivalent() != null)
	        	detail.setNormalCurveEquivalent(event.getNormalCurveEquivalent());
	        if(event.getNationalPercentile() != null)
	        	detail.setNationalPercentile(event.getNationalPercentile());
	        if(event.getNationalStanine() != null)
	        	detail.setNationalStanine(event.getNationalStanine());
	        if(event.getGradeEquivalent() != null)
	        	detail.setGradeEquivalent(event.getGradeEquivalent());
	        if (event.getPointsAttempted() != null)
	            detail.setPointsAttempted(event.getPointsAttempted());
	        if (event.getPointsObtained() != null)
	            detail.setPointsObtained(event.getPointsObtained());
	        if (event.getPointsPossible() != null)
	            detail.setPointsPossible(event.getPointsPossible());
	        
	        detail.setNormYear(event.getNormYear());
	        detail.setValidScore(event.getValidScore());

	       // if (event.hasExpectedGedScores()) {
	            StudentPredictedScoresData predictedGedScores = getResultHolder().getOrCreateStudentPredictedScoresData();
	            if (event.getExpectedAverageGed()!=null)
	                predictedGedScores.setExpectedGedAverage(event.getExpectedAverageGed());
	            if (event.getExpectedMathGed()!=null)
	                predictedGedScores.setExpectedGedMath(event.getExpectedMathGed());
	            if (event.getExpectedReadingGed()!=null)
	                predictedGedScores.setExpectedGedReading(event.getExpectedReadingGed());
	            if (event.getExpectedScienceGed()!=null)
	                predictedGedScores.setExpectedGedScience(event.getExpectedScienceGed());
	            if (event.getExpectedSocialStudiesGed()!=null)
	                predictedGedScores.setExpectedGedSocialStudies(event.getExpectedSocialStudiesGed());
	            if (event.getExpectedWritingGed()!=null)
	                predictedGedScores.setExpectedGedWriting(event.getExpectedWritingGed());
	       // }
	    }

}
