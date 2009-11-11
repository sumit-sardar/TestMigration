package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class ContributingPointEvent extends ResponseEvent {
    private final ScoreLookupCode outputScoreType;
    private final Integer pointsObtained;

    public ContributingPointEvent(final Long testRosterId, final String itemId,
            final Integer itemSetId, final ScoreLookupCode outputScoreType, final Integer pointsObtained) {
        super(testRosterId, itemId, itemSetId);
        this.outputScoreType = outputScoreType;
        this.pointsObtained = pointsObtained;
    }

    public ScoreLookupCode getOutputScoreType() {
        return outputScoreType;
    }
    
    public Integer getPointsObtained() {
    	return pointsObtained;
    }
}