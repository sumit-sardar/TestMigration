package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.teststructure.MasteryLevel;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ObjectivePrimaryCumulativeNumberCorrectEvent extends ObjectiveNumberCorrectEvent {
    private final MasteryLevel mastery;

    public ObjectivePrimaryCumulativeNumberCorrectEvent(final Long testRosterId, final Long objectiveId,
            final int numberOfItems, final int numberCorrect, final int numberIncorrect,
            final int numberAttempted, final int numberUnattempted, final MasteryLevel mastery, final Long subtestId) {
        super(testRosterId, objectiveId, numberOfItems, numberCorrect, numberIncorrect,
                numberAttempted, numberUnattempted, subtestId);
        this.mastery = mastery;
    }

    public MasteryLevel getMastery() {
        return mastery;
    }
}