package com.ctb.lexington.domain.score.scorer;

import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import java.io.IOException;

import com.ctb.lexington.db.data.StsTestResultFactData;
import com.ctb.lexington.db.data.StsTestResultFactDetails;
import com.ctb.lexington.db.data.StsTotalStudentScoreDetail;
import com.ctb.lexington.db.data.StudentScoreSummaryData;
import com.ctb.lexington.db.data.StudentScoreSummaryDetails;
import com.ctb.lexington.db.data.StudentSubtestScoresData;
import com.ctb.lexington.db.data.StudentSubtestScoresDetails;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.PrimaryObjectiveDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.scorer.calculator.PrimaryObjectiveDerivedScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TerraNovaCompositeScoreCalculator;
import com.ctb.lexington.domain.score.scorer.calculator.TerraNovaContentAreaDerivedScoreCalculator;
import com.ctb.lexington.domain.teststructure.MasteryLevel;
import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.Stringx;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TVScorer extends STScorer {
    private List invalidContentAreas;
    private List validContentAreas;
    
    public TVScorer() throws CTBSystemException, IOException {
        super();
        
        invalidContentAreas = new ArrayList();
        validContentAreas = new ArrayList();

        //addCalculator(new ContentAreaNumberCorrectCalculator(channel, this));
        addCalculator(new TerraNovaContentAreaDerivedScoreCalculator(channel, this));
        addCalculator(new TerraNovaCompositeScoreCalculator(channel, this));
        addCalculator(new PrimaryObjectiveDerivedScoreCalculator(channel, this));
        
        //channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SubtestContentAreaCompositeScoreEvent.class);
        channel.subscribe(this, PrimaryObjectiveDerivedScoreEvent.class);
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
        if(event.getHighNationalPercentile() != null)
        	factDetails.setHighNationalPercentile(event.getHighNationalPercentile());
        if(event.getLowNationalPercentile() != null)
        	factDetails.setLowNationalPercentile(event.getLowNationalPercentile());
        if(event.getNationalStanine() != null)
        	factDetails.setNationalStanine(event.getNationalStanine());
        if(Stringx.hasContent(event.getGradeEquivalent()))
        	factDetails.setGradeEquivalent(event.getGradeEquivalent());
        factDetails.setNormGroup(event.getNormGroup());
        factDetails.setNormYear(event.getNormYear());
        factDetails.setPerformanceLevelCode(event.getPerformanceLevel().getCode());
        factDetails.setPerformanceLevel(event.getPerformanceLevel().getDescription());
    }

    public void onEvent(SubtestContentAreaCompositeScoreEvent event) {
        // TODO: Where do we get the normGroup and other data values for this record?
        // are they obtained from the TerraNovaCompositeScoreCalculator?
        StsTotalStudentScoreDetail detail = getResultHolder().getStsTotalStudentScoreData().get(
                event.getType());
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
        detail.setPointsAttempted(event.getPointsAttempted());
        detail.setPointsObtained(event.getPointsObtained());
        detail.setPointsPossible(event.getPointsPossible());
        detail.setNormGroup(event.getNormGroup());
        detail.setNormYear(event.getNormYear());
        detail.setValidScore(event.getValidScore());
    }

    public void onEvent(PrimaryObjectiveDerivedScoreEvent event) {
        if(event.getPValue() != null) {
            float nationalAverage = ((float) Math.round((event.getPValue().floatValue() * (float) 10000000.0))) / (float) 100000.0;
            PrimaryObjective prim = getResultHolder().getCurriculumData().getPrimObjById(event.getObjectiveId());
            if(prim != null) {
                prim.setNationalAverage(new BigDecimal(nationalAverage));
            }
        }
        if(event.getHighMasteryRange() != null) {
            PrimaryObjective prim = getResultHolder().getCurriculumData().getPrimObjById(event.getObjectiveId());
            if(prim != null) {
                prim.setHighMasteryRange(event.getHighMasteryRange());
            }
        }
        if(event.getLowMasteryRange() != null) {
            PrimaryObjective prim = getResultHolder().getCurriculumData().getPrimObjById(event.getObjectiveId());
            if(prim != null) {
                prim.setLowMasteryRange(event.getLowMasteryRange());
            }
        }
        StudentScoreSummaryData data = getResultHolder().getStudentScoreSummaryData();
        StudentScoreSummaryDetails details = data.get(event.getObjectiveId());
        details.setNationalScore(event.getPValue());
        
    }
    
    public void onEvent(SubtestValidEvent event) {
        Iterator canIter = event.getContentAreaNames().iterator();
        while(canIter.hasNext()) {
            String ca = (String) canIter.next();
            // make sure we create a fact record for any valid content area, even if no items were answered
            StsTestResultFactData factData = getResultHolder().getStsTestResultFactData();
            StsTestResultFactDetails factDetails = factData.get(ca);
            factDetails.setValidScore(CTBConstants.VALID_SCORE);
            //
            if(invalidContentAreas.contains(ca)) {
                invalidContentAreas.remove(ca);
            }
            validContentAreas.add(ca);
            List cans = new ArrayList();
            cans.add(ca);
            setStatuses(cans, CTBConstants.VALID_SCORE);
        } 
    }

    public void onEvent(SubtestInvalidEvent event) {
        Iterator canIter = event.getContentAreaNames().iterator();
        while(canIter.hasNext()) {
            String ca = (String) canIter.next();
            if(!validContentAreas.contains(ca)) {
                invalidContentAreas.add(ca);
                List cans = new ArrayList();
                cans.add(ca);
                setStatuses(cans, CTBConstants.INVALID_SCORE);
            }
        }
    }

}