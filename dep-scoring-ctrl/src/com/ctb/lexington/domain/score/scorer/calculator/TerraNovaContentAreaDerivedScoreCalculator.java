package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.ContentAreaCumulativeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.PerformanceLevel;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class TerraNovaContentAreaDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
    private static final String TERRANOVA_FRAMEWORK_CODE = "TERRAB";
    private static final String TERRANOVA_THIRD_EDITION_FRAMEWORK_CODE = "TERRAB3";

	public TerraNovaContentAreaDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, ContentAreaCumulativeNumberCorrectEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContentAreaCumulativeNumberCorrectEvent.class);
    }

    public void onEvent(ContentAreaCumulativeNumberCorrectEvent event) {
        final Integer subtestId = DatabaseHelper.asInteger(event.getSubtestId());
        String frameworkCode = TERRANOVA_FRAMEWORK_CODE;
        
        if("19/20".equals(pTestLevel) || "19-20".equals(pTestLevel)) {
            //QA indicates OAS 3.x uses level 20 norms for test level 19/20
            pTestLevel = "20";
        }
        
        if ("Algebra".equals(event.getContentAreaName())) {
        	frameworkCode = "AAS";
        }
        
      //Added for acuity students scheduled through web-service with IRT calculation logic.
        if(scorer.getResultHolder().getAdminData().getProductId() == 3700) {
        	if("21/22".equals(pTestLevel) || "21-22".equals(pTestLevel)) {
                //QA indicates OAS 3.x uses level 20 norms for test level 19/20
                pTestLevel = "22";
            }
        	try {
        	TVWSScaleScoreCalculator tvWSScaleScore = new TVWSScaleScoreCalculator();
        	//Retrieve the loss and hoss value
        	tvWSScaleScore.getLossHossValue(event.getSubtestId(), event.getContentAreaName(), pTestLevel, scorer.getOASConnection());
        	if(scorer.getResultHolder().getCaLossHoss() == null || scorer.getResultHolder().getCaLossHoss().size() == 0) {
        		Map<String,String> caLossHossVal = new HashMap<String,String>();
        		caLossHossVal.put(event.getContentAreaName(), tvWSScaleScore.getLoss()+","+tvWSScaleScore.getHoss());
        		scorer.getResultHolder().setCaLossHoss(caLossHossVal);
        	} else {
        		scorer.getResultHolder().getCaLossHoss().put(event.getContentAreaName(), tvWSScaleScore.getLoss()+","+tvWSScaleScore.getHoss());
        	}
            LinkedHashMap<String,LinkedHashMap<String,String>> contentAreaResponse = scorer.getResultHolder().getCaResponseWsTv().getContentAreaItems();
            final BigDecimal scaleScoreIRT = tvWSScaleScore.getTVWSScaleScoreCalculator(pTestLevel, 
            		event.getContentAreaName(), scheduledProductId, contentAreaResponse, 
            		scorer.getIRSConnection());
            
            final BigDecimal standardErrorMeasurement = getScore(
        			subtestId,
        			event.getContentAreaName(),
    				null,
    				pTestForm,
    				pTestLevel,
    				null,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScoreIRT,
    				ScoreLookupCode.STANDARD_ERROR_MEASUREMENT,
    				null);
            
            frameworkCode = TERRANOVA_THIRD_EDITION_FRAMEWORK_CODE;

            final BigDecimal normalCurveEquivalent = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScoreIRT,
    				ScoreLookupCode.NORMAL_CURVE_EQUIVALENT,
    				null);
            
            final BigDecimal nationalPercentile = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScoreIRT,
    				ScoreLookupCode.NATIONAL_PERCENTILE,
    				null);
            
            frameworkCode = TERRANOVA_FRAMEWORK_CODE;
            //For nationalStanine, the value of lookup is same for terrab3 as that for terrab
            final BigDecimal nationalStanine = getScore(
            		frameworkCode,
    				null,
    				null,
    				null,
    				null,
    				null,
    				ScoreLookupCode.NATIONAL_PERCENTILE,
    				nationalPercentile,
    				ScoreLookupCode.NATIONAL_STANINE,
    				null);
            
            final String gradeEquivalent = getGradeEquivalent(
        			subtestId,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				null,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScoreIRT,
    				ScoreLookupCode.GRADE_EQUIVALENT,
    				null);

            final BigDecimal performanceLevelValue = getTerraNovaPerformanceLevel(event.getContentAreaName(),
                    pTestLevel, scaleScoreIRT);
            final PerformanceLevel performanceLevel = PerformanceLevel.getByCode(String
                    .valueOf(performanceLevelValue.intValue()));

            channel.send(new ContentAreaDerivedScoreEvent(
                    event.getTestRosterId(),
            		event.getSubtestId(), 
    				event.getContentAreaId(), 
    				event.getContentAreaName(), 
    				scaleScoreIRT, 
    				standardErrorMeasurement,
    				normalCurveEquivalent,
                    gradeEquivalent,
    				null,
    				nationalStanine, 
    				nationalPercentile, 
    				null,
    				null,
    				performanceLevel, 
    				pNormGroup,
                    pNormYear,
    				pAgeCategory,
                    null,
                    pRecommendedLevel));
            
        	} catch(Exception e) {
        		throw new RuntimeException(e);
        	}
        } else { // For normal terra nova product except third edition
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
    				null);
            
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
    				null);

            final BigDecimal normalCurveEquivalent = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScore,
    				ScoreLookupCode.NORMAL_CURVE_EQUIVALENT,
    				null);
            
            final BigDecimal nationalPercentile = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScore,
    				ScoreLookupCode.NATIONAL_PERCENTILE,
    				null);
            
            /* final BigDecimal highNationalPercentile = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScore,
    				ScoreLookupCode.HIGH_NATIONAL_PERCENTILE,
    				null);
            
            final BigDecimal lowNationalPercentile = getScore(
            		frameworkCode,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				pGrade,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScore,
    				ScoreLookupCode.LOW_NATIONAL_PERCENTILE,
    				null); */
            
            final BigDecimal nationalStanine = getScore(
            		frameworkCode,
    				null,
    				null,
    				null,
    				null,
    				null,
    				ScoreLookupCode.NATIONAL_PERCENTILE,
    				nationalPercentile,
    				ScoreLookupCode.NATIONAL_STANINE,
    				null);
            
            final String gradeEquivalent = getGradeEquivalent(
        			subtestId,
        			event.getContentAreaName(),
    				pNormGroup,
    				null,
    				null,
    				null,
    				ScoreLookupCode.SCALED_SCORE,
    				scaleScore,
    				ScoreLookupCode.GRADE_EQUIVALENT,
    				null);

            final BigDecimal performanceLevelValue = getTerraNovaPerformanceLevel(event.getContentAreaName(),
                    pTestLevel, scaleScore);
            final PerformanceLevel performanceLevel = PerformanceLevel.getByCode(String
                    .valueOf(performanceLevelValue.intValue()));

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
    				performanceLevel, 
    				pNormGroup,
                    pNormYear,
    				pAgeCategory,
                    null,
                    pRecommendedLevel));
        }
        
        
    }
}