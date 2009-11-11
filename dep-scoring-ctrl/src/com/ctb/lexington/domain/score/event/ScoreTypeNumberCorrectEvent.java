package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

public class ScoreTypeNumberCorrectEvent extends ScoreTypeEvent {
    private final int numberCorrect;
    private final Integer itemSetId;

    public ScoreTypeNumberCorrectEvent(final Long testRosterId, final ScoreLookupCode scoreType,
            final Integer itemSetId, final int numberCorrect) {
        super(testRosterId, scoreType);
        this.itemSetId = itemSetId;
        this.numberCorrect = numberCorrect;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }
}