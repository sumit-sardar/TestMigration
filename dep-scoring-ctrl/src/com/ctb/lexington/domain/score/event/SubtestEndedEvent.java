/*
 * Created on Jul 22, 2004 SubtestEndedEvent.java
 */
package com.ctb.lexington.domain.score.event;

public class SubtestEndedEvent extends SubtestEvent {
    public SubtestEndedEvent(final Long testRosterId, final Integer itemSetId) {
        super(testRosterId, itemSetId);
    }
}