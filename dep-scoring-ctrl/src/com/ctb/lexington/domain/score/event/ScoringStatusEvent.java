package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.teststructure.ScoringStatus;

public class ScoringStatusEvent extends Event {
    private final ScoringStatus status;

    public ScoringStatusEvent(final Long testRosterId, final ScoringStatus status) {
        super(testRosterId);
        this.status = status;
    }

    public ScoringStatus getStatus() {
        return status;
    }
}