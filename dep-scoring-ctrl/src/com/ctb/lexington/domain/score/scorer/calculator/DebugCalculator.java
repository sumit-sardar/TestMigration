/*
 * Created on Aug 10, 2004
 *
 * DebugCalculator.java
 */
package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.scorer.Scorer;

/**
 * <code>DebugCalculator</code> logs all events to the Grnds global logger as
 * debug messages.
 *
 * @author nate_cohen
 * @see GrndsLogger#global
 */
public class DebugCalculator extends Calculator {
    public DebugCalculator(final Channel channel, final Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, Event.class);
    }

    public void onEvent(final Event event) {
        System.out.println("event = " + event);
    }
}
