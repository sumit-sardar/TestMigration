package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.teststructure.ScoreLookupCode;

/**
 * Created by IntelliJ IDEA. User: kchandra Date: Sep 23, 2004 Time: 4:02:43 PM To change this
 * template use File | Settings | File Templates.
 */
public class NoContributingResponseEvent extends Event {
    private final String scoreTypeCode;

    public NoContributingResponseEvent(final Long testRosterId, final String scoreTypeCode) {
        super(testRosterId);
        this.scoreTypeCode = scoreTypeCode;
    }

    public ScoreLookupCode getScoreLookupCode() {
        return ScoreLookupCode.getByCode(scoreTypeCode);
    }
}