package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.Map;

import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestRawScoreEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.util.SafeHashMap;

public class SubtestRawScoreCalculator extends Calculator {
    protected final Map itemPointsAttempted = new SafeHashMap(String.class, Integer.class);
    protected final Map itemPointsObtained = new SafeHashMap(String.class, Integer.class);

    protected Integer itemSetId;
    protected int pointsPossible;

    public SubtestRawScoreCalculator(final Channel channel,
            final Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestItemCollectionEvent.class);
        channel.subscribe(this, PointEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, PointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        itemSetId = event.getItemSetId();
        pointsPossible = event.getPointsPossible().intValue();

        if (pointsPossible < 1)
            throw new IllegalArgumentException("Subtest has no items for item set id "
                    + itemSetId);
    }

    public void onEvent(final PointEvent event) {
        validateItemSetId(event.getItemSetId());

        itemPointsAttempted.put(event.getItemId(), event.getPointsAttempted());

        if (null != event.getPointsObtained())//Don't put null points (CR items) into the map...
            itemPointsObtained.put(event.getItemId(),
                    event.getPointsObtained());
    }

    public void onEvent(final SubtestEndedEvent event) {
        validateItemSetId(event.getItemSetId());

        final int pointsAttempted = ScorerHelper.sumAsInt(itemPointsAttempted.values());
        final int pointsObtained = ScorerHelper.sumAsInt(itemPointsObtained.values());
        final int percentObtained = ScorerHelper.calculatePercentage(pointsObtained, pointsPossible);

        channel.send(new SubtestRawScoreEvent(event, pointsPossible, pointsAttempted, pointsObtained, percentObtained));
    }

    private void validateItemSetId(final Integer itemSetId) {
        if (!this.itemSetId.equals(itemSetId))
            throw new IllegalArgumentException("Wrong item set id; expected "
                    + this.itemSetId + " but got " + itemSetId);
    }
}
