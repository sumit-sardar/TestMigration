package com.ctb.lexington.domain.score.scorer.calculator;

import java.math.BigDecimal;

import com.ctb.lexington.domain.score.event.ScoreTypeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestDerivedScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

/**
 * SubtestDerivedScoreCalculator.java
 * @author ncohen
 *
 * Computes any appropriate derived scores (eg. Scale Score)
 * upon receipt of Subtest Number Correct or Raw Score
 * totals for a score type. (see score_lookup_item_set 
 * table in OAS schema)
 */
public class SubtestDerivedScoreCalculator extends AbstractDerivedScoreCalculator {
	
	/**
	 * Create a new SubtestDerivedScoreCalculator, and
	 * subscribe to number correct and raw score source
	 * events.
	 * 
	 * @param channel Channel on which events will appear
	 * @param scorer Scorer which maintains results for this
	 * roster
	 */
    public SubtestDerivedScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, ScoreTypeNumberCorrectEvent.class);
        channel.subscribe(this, ScoreTypeRawScoreEvent.class);
        mustPrecede(SubtestStartedEvent.class, ScoreTypeNumberCorrectEvent.class);
    }

    /**
     * Look up the scale score corresponding to an
     * accumulated subtest number correct, and send
     * output event.
     * 
     * @param event a ScoreTypeNumberCorrectEvent
     * contains accumulated number corect within
     * a subtest for a particular score type.
     */
    public void onEvent(ScoreTypeNumberCorrectEvent event) {
        if (!pItemSetId.equals(event.getItemSetId()))
            throw new IllegalArgumentException("Subtest end received for wrong itemSetId: expected"
                    + pItemSetId + " but got " + event.getItemSetId());

        if (event.getScoreType().equals(ScoreLookupCode.SCALED_SCORE)) {
            channel.send(new SubtestDerivedScoreEvent(event.getTestRosterId(),
                    event.getItemSetId(), event.getScoreType().getCode(), getScore(event
                            .getItemSetId(), null, null, null, null, null,
                            ScoreLookupCode.SUBTEST_NUMBER_CORRECT, new BigDecimal(event
                                    .getNumberCorrect()), event.getScoreType(), null)));
        }
    }
    
    /**
     * Look up the scale score corresponding to an
     * accumulated subtest raw score, and send
     * output event.
     * 
     * @param event a ScoreTypeRawScoreEvent
     * contains accumulated points obtained within
     * a subtest for a particular score type.
     */
    public void onEvent(ScoreTypeRawScoreEvent event) {
        if (!pItemSetId.equals(event.getItemSetId()))
            throw new IllegalArgumentException("Subtest end received for wrong itemSetId: expected"
                    + pItemSetId + " but got " + event.getItemSetId());

        if (event.getScoreType().equals(ScoreLookupCode.SCALED_SCORE)) {
            channel.send(new SubtestDerivedScoreEvent(event.getTestRosterId(),
                    event.getItemSetId(), event.getScoreType().getCode(), getScore(event
                            .getItemSetId(), null, null, null, null, null,
                            ScoreLookupCode.SUBTEST_RAW_SCORE, new BigDecimal(event
                                    .getPointsObtained()), event.getScoreType(), null)));
        }
    }
}