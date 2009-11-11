package com.ctb.lexington.domain.score.event;

public class SubtestNumberCorrectEvent extends SubtestEvent {
    private final int numberCorrect;
    private final int numberIncorrect;
    private final int numberAttempted;
    private final int numberUnattempted;
    private final int numberOfItems;
    private final int percentCorrect;

    // TODO: constructor should take itemSetId
    public SubtestNumberCorrectEvent(final SubtestEvent event, final int numberOfItems,
            final int numberCorrect, final int numberIncorrect, final int numberAttempted,
            final int numberUnattempted, final int percentCorrect) {
        super(event.getTestRosterId(), event.getItemSetId());
        this.numberOfItems = numberOfItems;
        this.numberCorrect = numberCorrect;
        this.numberIncorrect = numberIncorrect;
        this.numberAttempted = numberAttempted;
        this.numberUnattempted = numberUnattempted;
        this.percentCorrect = percentCorrect;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public int getNumberAttempted() {
        return numberAttempted;
    }

    /**
     * @return Returns the numberIncorrect.
     */
    public int getNumberIncorrect() {
        return numberIncorrect;
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

    public int getPercentCorrect() {
        return percentCorrect;
    }
}