package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ctb.lexington.db.record.ScoreLookupRecord;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentEndedEvent;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.ContentAreaDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaCompositeScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestInvalidEvent;
import com.ctb.lexington.domain.score.event.SubtestValidEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.CTBConstants;
import com.ctb.lexington.util.SafeHashMap;

public class TerraNovaCompositeScoreCalculator extends AbstractDerivedScoreCalculator {
    private final static String READING_CONTENT_AREA_NAME = "Reading";
    private final static String LANGUAGE_CONTENT_AREA_NAME = "Language";
    private final static String MATH_CONTENT_AREA_NAME = "Mathematics";
    private final static String COMPOSITE_TYPE = "Total Score";

    private final Map contentAreaDerivedScoreEvents = new SafeHashMap(String.class, ContentAreaDerivedScoreEvent.class);
    private final ArrayList contentAreaNamesRequiredForTotalComposite = new ArrayList();
    private boolean compositeScoresPublished;
    private List invalidContentAreas;
    private List validContentAreas;
    private String normGroup;
    private String normYear;
    private Long testRosterId;
    
    private long totalCompositePointsPossible;
    private long totalCompositePointsAttempted;
    private long totalCompositePointsObtained;
    
    private final Map contentAreaRawScoreNames = new SafeHashMap(String.class,
            String.class);
            
    private final ArrayList contentAreaRawScoreEvents = new ArrayList();

    /**
     * Constructor for TerraNovaCompositeScoreCalculator.
     *
     * @param channel
     * @param scorer
     */
    public TerraNovaCompositeScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        contentAreaNamesRequiredForTotalComposite.add(READING_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(LANGUAGE_CONTENT_AREA_NAME);
        contentAreaNamesRequiredForTotalComposite.add(MATH_CONTENT_AREA_NAME);

        compositeScoresPublished = false;

        channel.subscribe(this, ContentAreaDerivedScoreEvent.class);
        channel.subscribe(this, SubtestInvalidEvent.class);
        channel.subscribe(this, SubtestValidEvent.class);
        channel.subscribe(this, AssessmentEndedEvent.class);
        channel.subscribe(this, ContentAreaRawScoreEvent.class);
        mustPrecede(AssessmentStartedEvent.class, ContentAreaDerivedScoreEvent.class);
        mustPrecede(AssessmentStartedEvent.class, AssessmentEndedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, ContentAreaRawScoreEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        super.onEvent(event);
        invalidContentAreas = new ArrayList();
        validContentAreas = new ArrayList();
        this.testRosterId = event.getTestRosterId();
    }

    public void onEvent(ContentAreaDerivedScoreEvent event) {
        contentAreaDerivedScoreEvents.put(event.getContentAreaName(), event);
        this.normYear = event.getNormYear();
        this.normGroup = event.getNormGroup();
    }
    
    public void onEvent(AssessmentEndedEvent event) {
        calculateTotalCompositeRawScores();
    	publishCompositeScores();
    }

    public void onEvent(SubtestValidEvent event) {
        Iterator canIter = event.getContentAreaNames().iterator();
        while(canIter.hasNext()) {
            String ca = (String) canIter.next();
            if(invalidContentAreas.contains(ca)) {
                invalidContentAreas.remove(ca);
            }
            validContentAreas.add(ca);
        }
    }

    public void onEvent(SubtestInvalidEvent event) {
        Iterator canIter = event.getContentAreaNames().iterator();
        while(canIter.hasNext()) {
            String ca = (String) canIter.next();
            if(!validContentAreas.contains(ca)) {
                invalidContentAreas.add(ca);
            }
        }
    }

    public void onEvent(ContentAreaRawScoreEvent event) {
        contentAreaRawScoreNames.put(event.getContentAreaName(), event.getContentAreaName());
        contentAreaRawScoreEvents.add(event);
    }
    
    private void calculateTotalCompositeRawScores() {
        if (areContentAreasRequiredForTotalRawsScored()) {
            for (Iterator i = contentAreaRawScoreEvents.iterator(); i.hasNext();) {
                ContentAreaRawScoreEvent event = (ContentAreaRawScoreEvent) i.next();
                if (contentAreaNamesRequiredForTotalComposite.contains(event.getContentAreaName())) {
                	totalCompositePointsAttempted += event.getPointsAttempted();
                	totalCompositePointsObtained += event.getPointsObtained();
                	totalCompositePointsPossible += event.getPointsPossible();
                }
            }
        }
    }
    
    private boolean areContentAreasRequiredForTotalRawsScored() {
        if (contentAreaNamesRequiredForTotalComposite.size() <= contentAreaRawScoreEvents.size()) {
            return contentAreaRawScoreNames.keySet().containsAll(
                    contentAreaNamesRequiredForTotalComposite);
        }
        return false;
    }

    private void publishCompositeScores() {
        if (compositeScoresPublished)
            return;

        if (areContentAreasRequiredForTotalCompositesScored()) {
            final BigDecimal scaleScore = calculateTotalCompositeScaleScore();
            if (scaleScore != null) {
	            final BigDecimal normalCurveEquivalent = getScore(scaleScore,ScoreLookupCode.SCALED_SCORE,
	                    ScoreLookupCode.NORMAL_CURVE_EQUIVALENT);

	            final BigDecimal nationalPercentile = getScore(scaleScore,ScoreLookupCode.SCALED_SCORE,
	                    ScoreLookupCode.NATIONAL_PERCENTILE);

	            final BigDecimal nationalStanine = calculateNationalStanineFromNationalPercentile(nationalPercentile);

	            final String gradeEquivalent = getGradeEquivalent(scaleScore,
	                    ScoreLookupCode.SCALED_SCORE, ScoreLookupCode.GRADE_EQUIVALENT);

                String validScore = computeValidScoreFlag(contentAreaNamesRequiredForTotalComposite);

	            channel.send(new SubtestContentAreaCompositeScoreEvent(this.testRosterId,
	                    COMPOSITE_TYPE, scaleScore, normalCurveEquivalent, gradeEquivalent,
	                    nationalStanine, nationalPercentile, this.normGroup, this.normYear,
                        null, null, null, null, null, null, new Long(totalCompositePointsObtained), 
                        new Long(totalCompositePointsAttempted), new Long(totalCompositePointsPossible), 
                        new Long(Math.round(((float) totalCompositePointsObtained / (float) totalCompositePointsPossible) * 100)), null, validScore, null));
	            compositeScoresPublished = true;
            }
        }
    }

    private String computeValidScoreFlag(final List contentAreaNames) {
        for (Iterator iter = contentAreaNames.iterator(); iter.hasNext();) {
            String contentAreaName = (String) iter.next();
            if (!validContentAreas.contains(contentAreaName)) {
                    return CTBConstants.INVALID_SCORE;
            }
        }
        return CTBConstants.VALID_SCORE;
    }

    protected BigDecimal getScore(BigDecimal sourceScoreValue, ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType) {
        ScoreLookupRecord score = getScoreRecord(sourceScoreValue, sourceScoreType, destScoreType);
        return score == null ? null : score.getDestScoreValue();
    }

    protected String getGradeEquivalent(BigDecimal sourceScoreValue, ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType) {
        ScoreLookupRecord score = getScoreRecord(sourceScoreValue, sourceScoreType, destScoreType);
        return ScoreLookupHelper.buildGradeEquivalent(score);
    }

    protected ScoreLookupRecord getScoreRecord(BigDecimal sourceScoreValue, ScoreLookupCode sourceScoreType, ScoreLookupCode destScoreType) {
        final Connection conn = getOASConnection();
        try {
            ScoreLookupRecord score = getScoreLookupHelper().getTerraNovaCompositeScore(sourceScoreValue, sourceScoreType,destScoreType,pNormGroup,pNormYear,pGrade,conn);
            return score;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * calculates a national stanine from a national percentile score
     * @param nationalPercentile
     * @return
     */
    private static BigDecimal calculateNationalStanineFromNationalPercentile(BigDecimal nationalPercentile) {
        //TODO: put this into a table and look it up
    	if(nationalPercentile != null) {
	        final int np = nationalPercentile.intValue();

	        if (np < 0 || np > 100)
	            throw new IllegalArgumentException("Bad percentage: " + np);

	        if (np<=4)
	            return new BigDecimal("1");
	        else if (np <=10)
	            return new BigDecimal("2");
	        else if (np <=22)
	            return new BigDecimal("3");
	        else if (np <=40)
	            return new BigDecimal("4");
	        else if (np <=59)
	            return new BigDecimal("5");
	        else if (np <=77)
	            return new BigDecimal("6");
	        else if (np <=89)
	            return new BigDecimal("7");
	        else if (np <=95)
	            return new BigDecimal("8");
	        else
	            return new BigDecimal("9");
    	} else {
    		return null;
    	}
    }

    /**
     * calculates a composite scale score: reading + language + math / 3
     *
     * @return
     */
    private BigDecimal calculateTotalCompositeScaleScore() {
    	BigDecimal reading = getReadingCompositeScaleScore();
    	BigDecimal language = getLanguageCompositeScaleScore();
    	BigDecimal math = getMathCompositeScaleScore();

    	if(reading != null && language != null && math != null) {
    		return reading.add(language.add(math)).divide(
                new BigDecimal("3"), BigDecimal.ROUND_HALF_UP);
    	} else {
    		return null;
    	}
    }

    /**
     * For OAS 3.5 this simply returns the reading content area's scale score because we do not have
     * the plus sections of TerraNova.
     *
     * @return scale score
     */
    private BigDecimal getReadingCompositeScaleScore() {
        ContentAreaDerivedScoreEvent readingScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(READING_CONTENT_AREA_NAME);
        return readingScoreEvent.getScaleScore();
    }

    /**
     * For OAS 3.5 this simply returns the language content area's scale score because we do not
     * have the plus sections of TerraNova.
     *
     * @return scale score
     */
    private BigDecimal getLanguageCompositeScaleScore() {
        ContentAreaDerivedScoreEvent readingScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(LANGUAGE_CONTENT_AREA_NAME);
        return readingScoreEvent.getScaleScore();
    }

    /**
     * For OAS 3.5 this simply returns the math content area's scale score because we do not have
     * the plus sections of TerraNova.
     *
     * @return scale score
     */
    private BigDecimal getMathCompositeScaleScore() {
        ContentAreaDerivedScoreEvent readingScoreEvent = (ContentAreaDerivedScoreEvent) contentAreaDerivedScoreEvents
                .get(MATH_CONTENT_AREA_NAME);
        return readingScoreEvent.getScaleScore();
    }

    private boolean areContentAreasRequiredForTotalCompositesScored() {
        if (contentAreaNamesRequiredForTotalComposite.size() != contentAreaDerivedScoreEvents
                .size()) {
            return false;
        }
        return contentAreaDerivedScoreEvents.keySet().containsAll(
                contentAreaNamesRequiredForTotalComposite);
    }
}