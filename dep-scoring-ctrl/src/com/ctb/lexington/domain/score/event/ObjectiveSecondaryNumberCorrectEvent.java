package com.ctb.lexington.domain.score.event;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ObjectiveSecondaryNumberCorrectEvent extends ObjectiveNumberCorrectEvent {
    public ObjectiveSecondaryNumberCorrectEvent(final Long testRosterId, final Long objectiveId,
            final int numberOfItems, final int numberCorrect, final int numberIncorrect,
            final int numberAttempted, final int numberUnattempted, final Long subtestId) {
        super(testRosterId, objectiveId, numberOfItems, numberCorrect, numberIncorrect,
                numberAttempted, numberUnattempted, subtestId);
    }
}