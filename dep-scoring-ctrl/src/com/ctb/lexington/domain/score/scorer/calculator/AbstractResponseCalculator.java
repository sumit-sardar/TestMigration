package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

/**
 * TODO: Redo class hierarchy
 */
public abstract class AbstractResponseCalculator extends Calculator {
    protected SubtestItemCollectionEvent sicEvent;

    /**
     * @param channel
     * @param scorer
     */
    public AbstractResponseCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, ResponseReceivedEvent.class);

        mustPrecede(SubtestItemCollectionEvent.class, ResponseReceivedEvent.class);
    }

    public void onEvent(SubtestItemCollectionEvent event) {
        if (event.getItemSetId() == null)
            throw new IllegalStateException("Null item set id.");

        sicEvent = event;
    }

    /**
     * Validates that an item set id is for this subtest.
     *
     * @param itemSetId the item set id
     */
    protected final void validateItemSetId(final Integer itemSetId) {
        if (itemSetId == null)
            throw new IllegalArgumentException("Null item set id.");

        if (!itemSetId.equals(sicEvent.getItemSetId()))
            throw new IllegalArgumentException("Wrong item set id for subtest: expected "
                    + sicEvent.getItemSetId() + " but got " + itemSetId);
    }

    public abstract void onEvent(final ResponseReceivedEvent event);
}