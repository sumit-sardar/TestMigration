package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public class AssessmentEvent extends Event {
    public AssessmentEvent(final Long testRosterId) {
        super(testRosterId);
    }
}