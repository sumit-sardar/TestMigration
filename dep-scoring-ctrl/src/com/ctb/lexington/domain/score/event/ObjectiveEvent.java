package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public abstract class ObjectiveEvent extends Event {
    private final Long objectiveId;
    private final Long subtestId;

    public ObjectiveEvent(final Long testRosterId, final Long objectiveId, Long subtestId) {
        super(testRosterId);
        this.objectiveId = objectiveId;
        this.subtestId = subtestId;
    }

    public Long getObjectiveId() {
        return objectiveId;
    }

	public Long getSubtestId() {
		return subtestId;
	}
}