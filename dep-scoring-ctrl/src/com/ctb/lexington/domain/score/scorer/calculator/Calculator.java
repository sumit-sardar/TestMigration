package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.event.common.EventProcessor;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.exception.EventChannelIllegalStateException;

public abstract class Calculator extends EventProcessor {
    protected final Scorer scorer;
    private final EventStateTracker tracker;
    
    public Calculator(final Channel channel, final Scorer scorer) {
        super(channel);

        if (scorer == null) {
            throw new IllegalArgumentException("scorer cannot be null");
        }
        this.scorer = scorer;
        this.tracker = new EventStateTracker(channel);
    }

    /**
     * Requires that at least one event of <var>predecessor </var> class must come before events of
     * <var>successor </var> class come.
     * 
     * @param predecessor the predecessor class
     * @param successor the successor class
     * @throws IllegalArgumentException if <var>predecessor </var> and <var>successor </var> are the
     *             same or if there is already defined a different <var>predecessor </var> for
     *             <var>successor </var>
     */
    protected final void mustPrecede(final Class predecessor, final Class successor) {
        tracker.mustPrecede(predecessor, successor);
    }

    public final void validateState(final Event event) {
        final Class eventClass = event.getClass();

        if (!tracker.isEventAllowed(eventClass)) {
            throw new EventChannelIllegalStateException(eventClass, tracker
                    .getRequiredPredecessor(eventClass));
        }
    }
}