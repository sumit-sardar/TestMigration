package com.ctb.lexington.domain.score.event;

/**
 * @author mnkamiya
 * @version $Id$
 */
public class ContentAreaNumberCorrectEvent extends ObjectiveNumberCorrectEvent {
    private final String contentAreaName;
    private final Long subtestId;

    public ContentAreaNumberCorrectEvent(final Long testRosterId, final Long subtestId,
            final Long contentAreaId, final String contentAreaName, final int numberOfItems,
            final int numberCorrect, final int numberIncorrect, final int numberAttempted,
            final int numberUnattempted) {
        super(testRosterId, contentAreaId, numberOfItems, numberCorrect, numberIncorrect,
                numberAttempted, numberUnattempted, subtestId);
        this.contentAreaName = contentAreaName;
        this.subtestId = subtestId;
    }

    public Long getSubtestId() {
        return subtestId;
    }

    public String getContentAreaName() {
        return contentAreaName;
    }

    public Long getContentAreaId() {
        return getObjectiveId();
    }
}