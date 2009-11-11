package com.ctb.lexington.domain.score.event;

import java.sql.Timestamp;

import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.teststructure.CompletionStatus;

public class TestCompletionStatusEvent extends Event {
    private final CompletionStatus status;
    private final Timestamp time;

    public TestCompletionStatusEvent(final Long testRosterId, final CompletionStatus status,
            final Timestamp time) {
        super(testRosterId);
        this.status = status;
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }

    public CompletionStatus getStatus() {
        return status;
    }
}