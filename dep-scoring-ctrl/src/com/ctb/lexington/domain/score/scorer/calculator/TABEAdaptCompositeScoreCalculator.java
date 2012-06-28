package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.Timer;

public class TABEAdaptCompositeScoreCalculator extends AbstractDerivedScoreCalculator {
	
    public final static String READING_CONTENT_AREA_NAME = "Reading";
    public final static String LANGUAGE_CONTENT_AREA_NAME = "Language";
    public final static String MATH_COMP_CONTENT_AREA_NAME = "Mathematics Computation";
    public final static String MATH_APPLIED_CONTENT_AREA_NAME = "Applied Mathematics";

    public static final String TOTAL_MATH_CONTENT_AREA = "Total Mathematics";
    public static final String TOTAL_BATTERY_CONTENT_AREA = "Total Battery";

    public static final String TOTAL_MATH_COMPOSITE_TYPE = "Total Mathematics";
    public static final String TOTAL_BATTERY_COMPOSITE_TYPE = "Total Battery";
    
    private static final String TABE_FRAMEWORK_CODE = "TABE";
    private static final List ALLOWED_TEST_LEVELS_FOR_GED_CALCS = Arrays.asList(new String[] {"A",
    "D", "CAT", "Adaptive"});
    
    private final Map contentAreaDerivedScoreEvents = new SafeHashMap(String.class,
            ContentAreaDerivedScoreEvent.class);
    private final Map contentAreaRawScoreEvents = new SafeHashMap(String.class,
            ContentAreaRawScoreEvent.class);
    private final ArrayList contentAreaNamesRequiredForTotalComposite = new ArrayList();
    private final ArrayList contentAreaNamesRequiredForMathComposite = new ArrayList();

    private boolean totalCompositeDerivedScoresCalculated;
    private boolean mathCompositeDerivedScoresCalculated;

    private final CompositeScoreHolder mathCompositeScores;
    private final CompositeScoreHolder totalCompositeScores;
    private List invalidSubtestIds;
    
    public TABEAdaptCompositeScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        contentAreaNamesRequiredForTotalComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LANGUAGE_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(MATH_COMP_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(MATH_APPLIED_CONTENT_AREA_NAME);

        contentAreaNamesRequiredForMathComposite.add(MATH_COMP_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForMathComposite.add(MATH_APPLIED_CONTENT_AREA_NAME);

        totalCompositeDerivedScoresCalculated = false;
        totalCompositeScores = new CompositeScoreHolder();

        mathCompositeDerivedScoresCalculated = false;
        mathCompositeScores = new CompositeScoreHolder();

        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, ContentAreaDerivedScoreEvent.class);
    }
    
    
    public void onEvent(AssessmentStartedEvent event) {
        super.onEvent(event);
        pNormGroup = null;
        invalidSubtestIds = new ArrayList();
    }
    
    
    public void onEvent(ContentAreaDerivedScoreEvent event) {
        if(!"L".equals(event.getTestLevel())) {
            final String message = "TABECompositeScoreCalculator.onEvent(ContentAreaDerivedScoreEvent event)";
            Timer timer = Timer.startTimer();
            contentAreaDerivedScoreEvents.put(event.getContentAreaName(), event);
            //System.out.println("ContentAreaDerivedScoreEvent.getContentAreaName() -> " + event.getContentAreaName());
            Timer.logElapsed(message, timer);
        }
    }
    
    public void onEvent(AssessmentEndedEvent event) {
        final Long testRosterId = event.getTestRosterId();
        calculateMathCompositeDerivedScores();
        calculateTotalCompositeDerivedScores();
        publishScores(TOTAL_MATH_COMPOSITE_TYPE, testRosterId, mathCompositeScores);
        publishScores(TOTAL_BATTERY_COMPOSITE_TYPE, testRosterId, totalCompositeScores);
    }
    
    
    private void calculateMathCompositeDerivedScores() {
        if (!mathCompositeDerivedScoresCalculated) {
            mathCompositeScores.scaleScore = calculateMathCompositeScaleScore();
            mathCompositeScores.validScore = CTBConstants.VALID_SCORE;
            
            ContentAreaDerivedScoreEvent comp = null;
            ContentAreaDerivedScoreEvent applied = null;
            if(contentAreaDerivedScoreEvents.containsKey(MATH_COMP_CONTENT_AREA_NAME)) {
            	comp = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                    .get(MATH_COMP_CONTENT_AREA_NAME);
            }
            if(contentAreaDerivedScoreEvents.containsKey(MATH_APPLIED_CONTENT_AREA_NAME)) {
            	applied = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                    .get(MATH_APPLIED_CONTENT_AREA_NAME);
            }

            if (null != comp && null != applied && null != comp.getTestLevel() && applied.getTestLevel().equals(comp.getTestLevel())
                    && null != mathCompositeScores.scaleScore) {
                mathCompositeDerivedScoresCalculated = true;

                mathCompositeScores.gradeEquivalent = getGradeEquivalent(TOTAL_MATH_CONTENT_AREA,
                        mathCompositeScores.scaleScore, ScoreLookupCode.SCALED_SCORE,
                        ScoreLookupCode.GRADE_EQUIVALENT, null);
                
                if (areContentAreasInCorrectLevelForMath()) {
                    mathCompositeScores.expectedMathGed = getScore(TOTAL_MATH_CONTENT_AREA,
                            mathCompositeScores.scaleScore, ScoreLookupCode.SCALED_SCORE,
                            ScoreLookupCode.EXPECTED_GED_MATH, null);
                 //   System.out.println(" mathCompositeScores.expectedMathGed = " +  mathCompositeScores.expectedMathGed);
                }

            }
        }
    }
    
    
    private void calculateTotalCompositeDerivedScores() {
        totalCompositeScores.validScore = CTBConstants.VALID_SCORE;

        if (!totalCompositeDerivedScoresCalculated && mathCompositeDerivedScoresCalculated) {
            totalCompositeScores.scaleScore = calculateTotalCompositeScaleScore(mathCompositeScores.scaleScore);

            if (null != totalCompositeScores.scaleScore) {
                totalCompositeDerivedScoresCalculated = true;

                totalCompositeScores.gradeEquivalent = getGradeEquivalent(
                        TOTAL_BATTERY_CONTENT_AREA, totalCompositeScores.scaleScore,
                        ScoreLookupCode.SCALED_SCORE, ScoreLookupCode.GRADE_EQUIVALENT, null);
                
                if (areContentAreasInCorrectLevelForTotal()) {
                    totalCompositeScores.expectedAverageGed = getScore(TOTAL_BATTERY_CONTENT_AREA,
                            totalCompositeScores.scaleScore, ScoreLookupCode.SCALED_SCORE,
                            ScoreLookupCode.EXPECTED_GED_AVERAGE, null);
                }

            }
        }
        
        if (isContentAreaScored(READING_CONTENT_AREA_NAME)
                && isContentAreaEligibleForExpextedGed(READING_CONTENT_AREA_NAME)
                && null == totalCompositeScores.expectedReadingGed) {
            final BigDecimal readingScaleScore = getContentAreaScaleScore(READING_CONTENT_AREA_NAME);
            totalCompositeScores.expectedReadingGed = getScore(READING_CONTENT_AREA_NAME,
                    readingScaleScore, ScoreLookupCode.SCALED_SCORE,
                    ScoreLookupCode.EXPECTED_GED_READING, null);

            totalCompositeScores.expectedSocialStudiesGed = getScore(READING_CONTENT_AREA_NAME,
                    readingScaleScore, ScoreLookupCode.SCALED_SCORE,
                    ScoreLookupCode.EXPECTED_GED_SOCIAL, null);

            totalCompositeScores.expectedScienceGed = getScore(READING_CONTENT_AREA_NAME,
                    readingScaleScore, ScoreLookupCode.SCALED_SCORE,
                    ScoreLookupCode.EXPECTED_GED_SCIENCE, null);
        }

        if (isContentAreaScored(LANGUAGE_CONTENT_AREA_NAME)
                && isContentAreaEligibleForExpextedGed(LANGUAGE_CONTENT_AREA_NAME)
                && null == totalCompositeScores.expectedWritingGed) {
            totalCompositeScores.expectedWritingGed = getScore(LANGUAGE_CONTENT_AREA_NAME,
                    getContentAreaScaleScore(LANGUAGE_CONTENT_AREA_NAME),
                    ScoreLookupCode.SCALED_SCORE, ScoreLookupCode.EXPECTED_GED_WRITING, null);
        }

    }
    
    
    private boolean areContentAreasInCorrectLevelForTotal() {
        String reading = getContentAreaLevel(READING_CONTENT_AREA_NAME);
        //System.out.println("reading -> " + reading);
        String language = getContentAreaLevel(LANGUAGE_CONTENT_AREA_NAME);
        //System.out.println("language -> " + language);
        ContentAreaDerivedScoreEvent readDerivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(READING_CONTENT_AREA_NAME);
        Integer readSubtestId = DatabaseHelper.asInteger(readDerivedScoreEvent.getSubtestId());
        ContentAreaDerivedScoreEvent langDerivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(LANGUAGE_CONTENT_AREA_NAME);
        Integer langSubtestId = DatabaseHelper.asInteger(langDerivedScoreEvent.getSubtestId());
        if (!invalidSubtestIds.contains(readSubtestId) && !invalidSubtestIds.contains(langSubtestId)) {
        	return ALLOWED_TEST_LEVELS_FOR_GED_CALCS.contains(reading)
                    && ALLOWED_TEST_LEVELS_FOR_GED_CALCS.contains(language)
                    && areContentAreasInCorrectLevelForMath();
        }
        return false;
    }
    
    
    private boolean isContentAreaEligibleForExpextedGed(String contentAreaName) {
        if (contentAreaDerivedScoreEvents.keySet().contains(contentAreaName)) {
        	ContentAreaDerivedScoreEvent derivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(contentAreaName);
            Integer subtestId = DatabaseHelper.asInteger(derivedScoreEvent.getSubtestId());
            if (!invalidSubtestIds.contains(subtestId)) {
            	return ALLOWED_TEST_LEVELS_FOR_GED_CALCS.contains(getContentAreaLevel(contentAreaName));
            }
        }
        return false;
    }
    
    private boolean areContentAreasInCorrectLevelForMath() {
        String mathApplied = getContentAreaLevel(MATH_APPLIED_CONTENT_AREA_NAME);
    //    System.out.println("mathApplied = " + mathApplied);
        String mathComp = getContentAreaLevel(MATH_COMP_CONTENT_AREA_NAME);
   //     System.out.println("mathComp = " + mathComp);
        ContentAreaDerivedScoreEvent appDerivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(MATH_APPLIED_CONTENT_AREA_NAME);
        Integer appSubtestId = DatabaseHelper.asInteger(appDerivedScoreEvent.getSubtestId());
        ContentAreaDerivedScoreEvent compDerivedScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents.get(MATH_COMP_CONTENT_AREA_NAME);
        Integer compSubtestId = DatabaseHelper.asInteger(compDerivedScoreEvent.getSubtestId());
        if (!invalidSubtestIds.contains(appSubtestId) && !invalidSubtestIds.contains(compSubtestId)) {
	        return ALLOWED_TEST_LEVELS_FOR_GED_CALCS.contains(mathApplied)
	                && ALLOWED_TEST_LEVELS_FOR_GED_CALCS.contains(mathComp);
        }
   //     System.out.println("areContentAreasInCorrectLevelForMath returning false");
        return false;
    }
    
    
    private String getContentAreaLevel(String contentAreaName) {
        ContentAreaDerivedScoreEvent scoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(contentAreaName);
        return scoreEvent.getTestLevel();
    }
    
    
    protected BigDecimal getScore(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getScore(TABE_FRAMEWORK_CODE, contentArea, null,
                    null, level, null, sourceScoreType, sourceScoreValue, destScoreType, conn,
                    pAgeCategory);
        } finally {
            closeConnection(conn);
        }
    }
    
    
    
    private boolean areContentAreasRequiredForMathDerivedCompositesScored() {
        if (contentAreaNamesRequiredForMathComposite.size() > contentAreaDerivedScoreEvents.size()) {
            return false;
        }
        return contentAreaDerivedScoreEvents.keySet().containsAll(
                contentAreaNamesRequiredForMathComposite);
    }
    
    private boolean areContentAreasRequiredForTotalDerivedCompositesScored() {
        if (contentAreaNamesRequiredForTotalComposite.size() <= contentAreaDerivedScoreEvents.size()) {
            return contentAreaDerivedScoreEvents.keySet().containsAll(
                    contentAreaNamesRequiredForTotalComposite);
        }
        return false;
    }
    
    private BigDecimal calculateMathCompositeScaleScore() {
        BigDecimal appliedMath = getContentAreaScaleScore(MATH_APPLIED_CONTENT_AREA_NAME);
        BigDecimal mathComputation = getContentAreaScaleScore(MATH_COMP_CONTENT_AREA_NAME);

        if (appliedMath != null && mathComputation != null)
            return appliedMath.add(mathComputation).divide(new BigDecimal("2"),
                    BigDecimal.ROUND_HALF_DOWN);//total math scores are rounded down.
        else
            return null;
    }
    
    private BigDecimal getContentAreaScaleScore(String contentAreaName) {
    	if(contentAreaDerivedScoreEvents.containsKey(contentAreaName)) {
	        ContentAreaDerivedScoreEvent scoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
	                .get(contentAreaName);
	        return scoreEvent.getScaleScore();
    	}
    	return null;
    }
    
    
    protected String getGradeEquivalent(String contentArea, BigDecimal sourceScoreValue,
            ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType, String level) {
        final Connection conn = getOASConnection();
        try {
            return getScoreLookupHelper().getGradeEquivalent(TABE_FRAMEWORK_CODE, contentArea,
                    null, null, level, null, sourceScoreType, sourceScoreValue,
                    destScoreType, conn, null);
        } finally {
            closeConnection(conn);
        }
    }
    
    
    /**
     * calculates a composite scale score: reading + language + math / 3
     * 
     * @return
     */
    private BigDecimal calculateTotalCompositeScaleScore(BigDecimal mathCompositeScaleScore) {
        BigDecimal reading = getContentAreaScaleScore(READING_CONTENT_AREA_NAME);
        BigDecimal language = getContentAreaScaleScore(LANGUAGE_CONTENT_AREA_NAME);

        if (reading != null && language != null && mathCompositeScaleScore != null) {
            return reading.add(language.add(mathCompositeScaleScore)).divide(new BigDecimal("3"),
                    BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }
    
    
    private boolean isContentAreaScored(String contentAreaName) {
        return contentAreaDerivedScoreEvents.keySet().contains(contentAreaName);
    }
    
    
    private void publishScores(final String scoreType, final Long testRosterId,
            final CompositeScoreHolder scores) {
        //if (null != scores.scaleScore || 0 != scores.pointsPossible || scores.hasPredictedGed()) {
            channel.send(new SubtestContentAreaCompositeScoreEvent(testRosterId, scoreType,
                    scores.scaleScore, scores.normalCurveEquivalent, scores.gradeEquivalent,
                    scores.nationalStanine, scores.nationalPercentile, pNormGroup, pNormYear,
                    scores.expectedMathGed, scores.expectedReadingGed, scores.expectedWritingGed,
                    scores.expectedSocialStudiesGed, scores.expectedScienceGed,
                    scores.expectedAverageGed, scores.getPointsObtained(), scores
                            .getPointsAttempted(), scores.getPointsPossible(), scores
                            .getPercentObtained(), null, scores.validScore, null));
        //}
    }
    
    
    private class CompositeScoreHolder {
        BigDecimal scaleScore;
        BigDecimal normalCurveEquivalent;
        String gradeEquivalent;
        BigDecimal nationalStanine;
        BigDecimal nationalPercentile;
        BigDecimal expectedMathGed;
        BigDecimal expectedReadingGed;
        BigDecimal expectedWritingGed;
        BigDecimal expectedSocialStudiesGed;
        BigDecimal expectedScienceGed;
        BigDecimal expectedAverageGed;
        int pointsObtained;
        int pointsAttempted;
        int pointsPossible;
        String validScore;

        CompositeScoreHolder() {
            scaleScore = null;
            normalCurveEquivalent = null;
            gradeEquivalent = null;
            nationalStanine = null;
            nationalPercentile = null;
            expectedAverageGed = null;
            expectedMathGed = null;
            expectedReadingGed = null;
            expectedScienceGed = null;
            expectedSocialStudiesGed = null;
            expectedWritingGed = null;
            pointsAttempted = 0;
            pointsObtained = 0;
            pointsPossible = 0;
            validScore = null;
        }

        Long toLong(int score) {
            if (pointsPossible == 0)
                return null;
            return new Long(score);
        }

        Long getPointsAttempted() {
            return toLong(pointsAttempted);
        }

        Long getPointsObtained() {
            return toLong(pointsObtained);
        }

        Long getPointsPossible() {
            return toLong(pointsPossible);
        }

        Long getPercentObtained() {
            if (pointsPossible == 0)
                return null;

            float percent = ((float) pointsObtained / (float) pointsPossible) * 100;
            return new Long(Math.round(percent));
        }

        public boolean hasPredictedGed() {
            return (null != expectedAverageGed || null != expectedMathGed
                    || null != expectedReadingGed || null != expectedScienceGed
                    || null != expectedSocialStudiesGed || null != expectedWritingGed);
        }
    }

}
