package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class TABEContentAreaDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
    public TABEContentAreaDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, ContentAreaNumberCorrectEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContentAreaNumberCorrectEvent.class);
    }

    public void onEvent(ContentAreaNumberCorrectEvent event) {
        final Integer subtestId = DatabaseHelper.asInteger(event.getSubtestId());
        final BigDecimal scaleScore = getScore(
    			subtestId,
    			event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
				new BigDecimal(event.getNumberCorrect()),
				ScoreLookupCode.SCALED_SCORE,
				pAgeCategory );
        
        final BigDecimal standardErrorMeasurement = getScore(
    			subtestId,
    			event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SUBTEST_NUMBER_CORRECT,
				new BigDecimal(event.getNumberCorrect()),
				ScoreLookupCode.STANDARD_ERROR_MEASUREMENT,
				pAgeCategory);

        final BigDecimal normalCurveEquivalent = getScore(
    			subtestId,
    			event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SCALED_SCORE,
				scaleScore,
				ScoreLookupCode.NORMAL_CURVE_EQUIVALENT,
				pAgeCategory);
        
        final BigDecimal nationalPercentile = getScore(
        		subtestId,
        		event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SCALED_SCORE,
				scaleScore,
				ScoreLookupCode.NATIONAL_PERCENTILE,
				pAgeCategory);
        
        final BigDecimal nationalStanine = getScore(
        		subtestId,
        		event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SCALED_SCORE,
				scaleScore,
				ScoreLookupCode.NATIONAL_STANINE,
				pAgeCategory);
        
        final String gradeEquivalent = getGradeEquivalent(
        		subtestId,
        		event.getContentAreaName(),
				null,
				pTestForm,
				pTestLevel,
				null,
				ScoreLookupCode.SCALED_SCORE,
				scaleScore,
				ScoreLookupCode.GRADE_EQUIVALENT,
				pAgeCategory);

        channel.send(new ContentAreaDerivedScoreEvent(
                event.getTestRosterId(),
        		event.getSubtestId(), 
				event.getContentAreaId(), 
				event.getContentAreaName(), 
				scaleScore, 
				standardErrorMeasurement,
				normalCurveEquivalent,
                gradeEquivalent,
				null,
				nationalStanine, 
				nationalPercentile, 
				null,
				null,
				null,
				pNormGroup,
                pNormYear,
				pAgeCategory,
                pTestLevel,
                pRecommendedLevel));
    }
}