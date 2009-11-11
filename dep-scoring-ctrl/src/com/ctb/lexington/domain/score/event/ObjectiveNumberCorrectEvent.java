package com.ctb.lexington.domain.score.event;

public class ObjectiveNumberCorrectEvent extends ObjectiveEvent {
    private final int numberOfItems;
    private final int numberCorrect;
    private final int numberIncorrect;
    private final int numberAttempted;
    private final int numberUnattempted;

    public ObjectiveNumberCorrectEvent(final Long testRosterId, final Long objectiveId,
            final int numberOfItems, final int numberCorrect, final int numberIncorrect,
            final int numberAttempted, final int numberUnattempted, Long subtestId) {
        super(testRosterId, objectiveId, subtestId);
        this.numberOfItems = numberOfItems;
        this.numberCorrect = numberCorrect;
        this.numberIncorrect = numberIncorrect;
        this.numberAttempted = numberAttempted;
        this.numberUnattempted = numberUnattempted;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public int getNumberIncorrect() {
        return numberIncorrect;
    }

    /**
     * @return Returns the numberAttempted.
     */
    public int getNumberAttempted() {
        return numberAttempted;
    }

    /**
     * @return Returns the numberOfItems.
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * @return Returns the numberUnattempted.
     */
    public int getNumberUnattempted() {
        return numberUnattempted;
    }
}