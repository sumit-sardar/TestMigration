package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ctb.lexington.domain.score.event.ContributingCorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ContributingIncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ScoreTypeNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;
import com.ctb.lexington.util.AutoHashMap;
import com.ctb.lexington.util.SafeHashSet;

/**
 * <code>ContributingNumberCorrectCalculator</code> counts correct contributing answers by output
 * score type.
 */
public class ScoreTypeNumberCorrectCalculator extends Calculator {
    public static final class ItemIdSet extends SafeHashSet {
        public ItemIdSet() {
            super(String.class);
        }
    }

    protected Map correctAnswersForOutputType = new AutoHashMap(ScoreLookupCode.class, Set.class,
            ItemIdSet.class);
    protected Integer itemSetId;

    public ScoreTypeNumberCorrectCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestStartedEvent.class);
        channel.subscribe(this, ContributingCorrectResponseEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContributingCorrectResponseEvent.class);
        channel.subscribe(this, ContributingIncorrectResponseEvent.class);
        mustPrecede(SubtestStartedEvent.class, ContributingIncorrectResponseEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestStartedEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(SubtestStartedEvent event) {
        itemSetId = event.getItemSetId();
    }

    public void onEvent(ContributingCorrectResponseEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(),
                "Response received for wrong itemSetId.");

        getItemsForOutputScoreType(event.getOutputScoreType()).add(event.getItemId());
    }

    public void onEvent(ContributingIncorrectResponseEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(),
                "Response received for wrong itemSetId.");

        final Set itemIds = getItemsForOutputScoreType(event.getOutputScoreType());
        final String itemId = event.getItemId();

        if (itemIds.contains(itemId))
            itemIds.remove(itemId);
    }

    private Set getItemsForOutputScoreType(ScoreLookupCode scoreLookupCode) {
        return (Set) correctAnswersForOutputType.get(scoreLookupCode);
    }

    public void onEvent(SubtestEndedEvent event) {
        validateItemSetBeingProcessed(event.getItemSetId(),
                "Subtest end received for wrong itemSetId.");

        final Long testRosterId = event.getTestRosterId();
        for (Iterator iter = correctAnswersForOutputType.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();

            channel.send(new ScoreTypeNumberCorrectEvent(testRosterId, (ScoreLookupCode) entry
                    .getKey(), itemSetId, ((Set) entry.getValue()).size()));
        }
    }

    private void validateItemSetBeingProcessed(Integer evtItemSetId, String message) {
        if (!itemSetId.equals(evtItemSetId))
            throw new IllegalArgumentException(message + " expected " + itemSetId + " but got "
                    + evtItemSetId);
    }
}