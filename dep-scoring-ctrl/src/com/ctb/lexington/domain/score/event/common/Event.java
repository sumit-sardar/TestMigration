package com.ctb.lexington.domain.score.event.common;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class Event {
    private final Long testRosterId;

    protected Event(final Long testRosterId) {
        this.testRosterId = testRosterId;
    }

    public final Long getTestRosterId() {
        return testRosterId;
    }

    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}