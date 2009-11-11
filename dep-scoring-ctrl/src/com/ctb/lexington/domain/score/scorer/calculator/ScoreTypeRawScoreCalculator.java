package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.lexington.domain.score.event.ContributingPointEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeRawScoreEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

/**
 * ScoreTypeRawScoreCalculator.java
 * 
 * The ScoreTypeRawScoreCalculator listens for response
 * events for items which contribute to any derived
 * scores for a given subtest, and accumulates points
 * obtained by score type and subtest. When a subtest is
 * completed, a ScoreTypeRawScoreEvent is generated,
 * which contains the accumulated totals.
 * 
 * @version
 * @author ncohen
 */
public class ScoreTypeRawScoreCalculator extends Calculator {
    protected Map pointsForOutputType = new HashMap();
    protected Integer itemSetId;

    /**
     * Create a new ScoreTypeRawScoreCalculator, and
     * subscribe to subtest start, end, and response
     * events.
     * @param channel
     * @param scorer
     */
    public ScoreTypeRawScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, ContributingPointEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContributingPointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestStartedEvent.class, SubtestEndedEvent.class);
    }

    /**
     * Retain the current subtest's item set id
     */
    public void onEvent(SubtestStartedEvent event) {
        itemSetId = event.getItemSetId();
    }

    /**
     * Accumulate points obtained for each
     * contributing item against the appropriate
     * score type for the current subtest
     * 
     * @param event a ContributingPointEvent contains
     * the points obtained on an item which contributes
     * to a given score type for this subtest.
     */
    public void onEvent(ContributingPointEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(),
                "Response received for wrong itemSetId.");

        HashMap itemPoints = (HashMap) pointsForOutputType.get(event.getOutputScoreType());
        if(itemPoints == null) itemPoints = new HashMap();
        Integer points = new Integer(event.getPointsObtained().intValue());
        itemPoints.put(event.getItemId(), points);
        pointsForOutputType.put(event.getOutputScoreType(), itemPoints);
    }

    /**
     * A SubtestEndedEvent indicates a completed
     * subtest, accumulated raw scores by item type for that
     * subtest should be published.
     * 
     * @param event a SubtestEndedEvent.
     */
    public void onEvent(SubtestEndedEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(),
                "Subtest end received for wrong itemSetId.");

        final Long testRosterId = event.getTestRosterId();
        Iterator iter = pointsForOutputType.keySet().iterator();
        while(iter.hasNext()){
        	ScoreLookupCode code = (ScoreLookupCode)iter.next();
            HashMap items = (HashMap) pointsForOutputType.get(code);
            Iterator itemIter = items.values().iterator();
            int points = 0;
            while(itemIter.hasNext()) {
            	points += ((Integer) itemIter.next()).intValue();
            }
            channel.send(new ScoreTypeRawScoreEvent(testRosterId, code, itemSetId, points));
        }
    }

    /**
     * Don't accumulate points for items that are not part of the
     * current subtest, throw an error.
     * 
     * @param evtItemSetId
     * @param message
     */
    private void validateItemSetBeingProcessed(Integer evtItemSetId, String message) {
        if (!itemSetId.equals(evtItemSetId))
            throw new IllegalArgumentException(message + " expected " + itemSetId + " but got "
                    + evtItemSetId);
    }
}