package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public abstract class ScoreTypeEvent extends Event {
    private final ScoreLookupCode scoreType;

    public ScoreTypeEvent(final Long testRosterId, final ScoreLookupCode scoreType) {
        super(testRosterId);
        this.scoreType = scoreType;
    }

    public ScoreLookupCode getScoreType() {
        return scoreType;
    }
}