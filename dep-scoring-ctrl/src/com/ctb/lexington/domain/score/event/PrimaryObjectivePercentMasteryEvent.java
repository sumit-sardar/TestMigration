package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public class PrimaryObjectivePercentMasteryEvent extends Event {
    private final int percentMastery;
    private final Long contentAreaId;
    private final String contentAreaName;
    private final Integer itemSetId;

    public PrimaryObjectivePercentMasteryEvent(final Long testRosterId, final Integer itemSetId,
            final Long contentAreaId, final String contentAreaName, final int percentMastery) {
        super(testRosterId);
        this.itemSetId = itemSetId;
        this.contentAreaId = contentAreaId;
        this.contentAreaName = contentAreaName;
        this.percentMastery = percentMastery;
    }

    public int getPercentMastery() {
        return percentMastery;
    }

    public Long getContentAreaId() {
        return contentAreaId;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    public String getContentAreaName() {
        return contentAreaName;
    }
}