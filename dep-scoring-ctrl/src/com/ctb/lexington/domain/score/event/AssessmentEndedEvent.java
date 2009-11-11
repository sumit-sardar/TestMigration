package com.ctb.lexington.domain.score.event;

public class AssessmentEndedEvent extends AssessmentEvent {
    public AssessmentEndedEvent(final Long testRosterId) {
        super(testRosterId);
    }
}