package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class ContributingCorrectResponseEvent extends ResponseEvent {
    private final ScoreLookupCode outputScoreType;

    public ContributingCorrectResponseEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId, final ScoreLookupCode outputScoreType) {
        super(testRosterId, itemId, itemSetId);
        this.outputScoreType = outputScoreType;
    }

    public ScoreLookupCode getOutputScoreType() {
        return outputScoreType;
    }
}