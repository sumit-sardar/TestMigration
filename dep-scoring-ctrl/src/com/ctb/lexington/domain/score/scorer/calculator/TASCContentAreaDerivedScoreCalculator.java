package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.domain.teststructure.PerformanceLevel;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.SafeHashMap;

public class TASCContentAreaDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
	
	private static final String TASC_FRAMEWORK_CODE = "TASC";
	private final Map contentAreaRawScoreEvents = new SafeHashMap(String.class, ContentAreaRawScoreEvent.class);
    
    public TASCContentAreaDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContentAreaNumberCorrectEvent.class);
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
    	if(contentAreaRawScoreEvents.containsKey(event.getContentAreaName())){
    		ContentAreaRawScoreEvent eventObj = (ContentAreaRawScoreEvent)contentAreaRawScoreEvents.get(event.getContentAreaName());
    		int pointObtained = eventObj.getPointsObtained() + event.getPointsObtained();
    		int pointAttempted = eventObj.getPointsAttempted() + event.getPointsAttempted();
    		contentAreaRawScoreEvents
					.put(event.getContentAreaName(),
							new ContentAreaRawScoreEvent(event
									.getTestRosterId(), event
									.getContentAreaName(), pointObtained,
									pointAttempted, ScorerHelper
											.calculatePercentage(pointObtained,
													event.getPointsPossible()),
									event.getPointsPossible(), event
											.getContentAreaMap(), event
											.getItemSetId()));
    	}else{
            contentAreaRawScoreEvents.put(event.getContentAreaName(), event);
    	}
    }
    
    public void onEvent(ContentAreaNumberCorrectEvent event) {
        final Integer subtestId = DatabaseHelper.asInteger(event.getSubtestId());
        
        
        System.out.println("Content Area name in  ContentAreaNumberCorrectEvent event ::: "+ event.getContentAreaName());
        ContentAreaRawScoreEvent contentAreaRawScoreEvent = (ContentAreaRawScoreEvent)contentAreaRawScoreEvents.get(event.getContentAreaName());
        Integer obtainedNumber = contentAreaRawScoreEvent.getPointsObtained();
        BigDecimal pointsObtained = new BigDecimal(obtainedNumber.toString());
       
        
        final BigDecimal scaleScore = getScore(
    			subtestId,
    			event.getContentAreaName(),
				null,
				pDupTestForm,
				pTestLevel,
				(pGrade != null)?pGrade:null,
				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
				pointsObtained, // If it is wrong then we can use [new BigDecimal(event.getNumberCorrect())],
				ScoreLookupCode.SCALED_SCORE,
				pAgeCategory );
        
        final BigDecimal normalCurveEquivalent = (scaleScore==null) ? null : getTASCNCE(
        		TASC_FRAMEWORK_CODE,
    			event.getContentAreaName(),
 				pTestLevel,
 				scaleScore,
 				//(pGrade != null)?pGrade:null,
 				null,
 				pDupTestForm);
         
        final BigDecimal nationalPercentile = (scaleScore==null) ? null : getTASCPR(
        		TASC_FRAMEWORK_CODE,
       			event.getContentAreaName(),
       			pTestLevel,
    			scaleScore,
    			//(pGrade != null)?pGrade:null,
    			null,
    			pDupTestForm);
        
        final BigDecimal proficencyLevelValue = getTASCPerformanceLevel(
        		TASC_FRAMEWORK_CODE,
        		event.getContentAreaName(),
        		pTestLevel,
				scaleScore,
				//(pGrade != null)?pGrade:null,
				null,
				pDupTestForm);
        
        final String proficiencyRange = getTASCScaleScoreRangeForCutScore(
        		TASC_FRAMEWORK_CODE,
        		event.getContentAreaName(),
        		pTestLevel,
        		proficencyLevelValue,
				//(pGrade != null)?pGrade:null,
				null,
				pDupTestForm);
        
        final PerformanceLevel proficencyLevel = ("0".equals(proficencyLevelValue.toString()))? null :PerformanceLevel.getByCode(String.valueOf(proficencyLevelValue));
        
        channel.send(new ContentAreaDerivedScoreEvent(
                event.getTestRosterId(),
        		event.getSubtestId(), 
				event.getContentAreaId(), 
				event.getContentAreaName(), 
				scaleScore, 
				null,
				normalCurveEquivalent,
                null,
				null,
				null, 
				nationalPercentile, 
				null,
				null,
				proficencyLevel,
				proficiencyRange,
				pNormGroup,
                pNormYear,
				pAgeCategory,
                pTestLevel,
                pRecommendedLevel,
                subtestScoringStatus));
        
    }
}