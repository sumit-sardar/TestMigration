package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.Map;
import java.util.Set;

import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.event.common.Event;
import com.ctb.lexington.domain.score.event.common.EventRecipient;
import com.ctb.lexington.util.SafeHashMap;
import com.ctb.lexington.util.SafeHashSet;

/**
 * <code>EventStateTracker</code> is a implementation class for the <code>calculator</code>
 * package.  It tracks precedence requirements for events, <cite>e.g.</cite>,
 * <code>SubtestStartedEvent</code> must precede <code>ResponseReceivedEvent</code>.
 * Currently, the tracker only supports linear flows of events.
 *
 * This is just an implementation class, hence it is <code>package</code> protected.
 * But, thereby hangs a tale.  The unit tests are in the wrong package, which destroys
 * the whole idea of testable package implementation classes.
 */

public class EventStateTracker implements EventRecipient {

    private Set recordedEvents = new SafeHashSet(Class.class);
    private Map predecessorRequirements = new SafeHashMap(Class.class, Class.class);

    public EventStateTracker(Channel channel) {
        channel.subscribe(this, Event.class);
    }

    public void mustPrecede(Class predecessor, Class successor)
    {
        if (predecessor.equals(successor))
            throw new IllegalArgumentException("Cannot precede thyself: predecessor and successor both "
                    + predecessor);

        if (predecessorRequirements.containsKey(successor) &&
           !predecessorRequirements.get(successor).equals(predecessor))
            throw new IllegalArgumentException("Cannot specify two different predecessors for one event: predecessor "
                    + predecessor + "; new successor " + successor
                    + "; old successor " + predecessorRequirements.get(successor));

        predecessorRequirements.put(successor, predecessor);
    }

    public void onEvent(Event event)
    {
        recordedEvents.add(event.getClass());
    }

    public boolean isEventAllowed(Class event)
    {
        if (predecessorRequirements.containsKey(event)) {
            return (recordedEvents.contains(predecessorRequirements.get(event)));
        }
        return true;
    }

    public Class getRequiredPredecessor(Class event)
    {
        if (predecessorRequirements.containsKey(event))
            return (Class)predecessorRequirements.get(event);
        throw new IllegalArgumentException("No predecessor specified for event " + event);
    }
}
