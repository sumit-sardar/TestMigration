package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public abstract class ResponseEvent extends Event {
    private final String itemId;
    private final Integer itemSetId;

    protected ResponseEvent(final Long testRosterId, final String itemId, final Integer itemSetId) {
        super(testRosterId);
        this.itemId = itemId;
        this.itemSetId = itemSetId;
    }

    public String getItemId() {
        return itemId;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }
}