package com.ctb.lexington.domain.score.event;

import java.util.Collections;
import java.util.List;

public class SubtestInvalidEvent extends SubtestEvent {
    private final List contentAreaNames;

    public SubtestInvalidEvent(final Long testRosterId, final Integer itemSetId,
            final List contentAreaNames) {
        super(testRosterId, itemSetId);
        this.contentAreaNames = contentAreaNames;
    }

    public List getContentAreaNames() {
        return Collections.unmodifiableList(contentAreaNames);
    }
}