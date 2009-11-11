package com.ctb.lexington.domain.score.scorer;

import java.io.IOException;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentPredictedScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectivePercentMasteryEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.ContentAreaNumberCorrectCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.PrimaryObjectivePercentMasteryCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABECompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TABEContentAreaDerivedScoreCalculator;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Stringx;

public class TBScorer extends STScorer {
    public TBScorer() throws CTBSystemException, IOException {
        super();

        addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new TABEContentAreaDerivedScoreCalculator(channel, this));
        addCalculator(new PrimaryObjectivePercentMasteryCalculator(channel, this));
        addCalculator(new TABECompositeScoreCalculator(channel,this));

        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeScoreEvent.class);
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
        if(event.getScaleScore() != null) 
        	factDetails.setScaleScore(event.getScaleScore());
        if(event.getStandardErrorMeasurement() != null) 
        	factDetails.setStandardErrorOfMeasurement(event.getStandardErrorMeasurement());
        if(event.getNormalCurveEquivalent() != null)
        	factDetails.setNormalCurveEquivalent(event.getNormalCurveEquivalent());
        if(event.getNationalPercentile() != null)
        	factDetails.setNationalPercentile(event.getNationalPercentile());
        if(event.getNationalStanine() != null)
        	factDetails.setNationalStanine(event.getNationalStanine());
        if(Stringx.hasContent(event.getGradeEquivalent()))
        	factDetails.setGradeEquivalent(event.getGradeEquivalent());
        factDetails.setNormGroup(event.getNormGroup());
        factDetails.setNormYear(event.getNormYear());
//        factDetails.setPerformanceLevelCode(event.getPerformanceLevel().getCode());
//        factDetails.setPerformanceLevel(event.getPerformanceLevel().getDescription());
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

        if (event.hasExpectedGedScores()) {
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
        }
    }
    
    public void onEvent(SubtestValidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.VALID_SCORE);
    }

    public void onEvent(SubtestInvalidEvent event) {
        setStatuses(event.getContentAreaNames(), CTBConstants.INVALID_SCORE);
    }
}