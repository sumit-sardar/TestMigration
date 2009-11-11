package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.HashSet;
import java.util.Set;

import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestNumberCorrectEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;

/**
 * This class counts the number of correct answers for a subtest for specific roster instance.
 */
public class SubtestNumberCorrectCalculator extends AbstractResponseCalculator {
    protected Integer itemSetID;
    private int numberOfItems;
    protected Set correctAnswers;
    protected Set incorrectAnswers;
    protected Set attemptedAnswers;
    protected Set unattemptedAnswers;

    public SubtestNumberCorrectCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, CorrectResponseEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, CorrectResponseEvent.class);
        channel.subscribe(this, IncorrectResponseEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, IncorrectResponseEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestItemCollectionEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(SubtestItemCollectionEvent event) {
        super.onEvent(event);
        correctAnswers = new HashSet();
        attemptedAnswers = new HashSet();
        incorrectAnswers = new HashSet();
        unattemptedAnswers = new HashSet();
        itemSetID = event.getItemSetId();
        numberOfItems = event.getItems().size();
    }

    public void onEvent(ResponseReceivedEvent event) {
        validateItemSetId(event.getItemSetId());

        if (sicEvent.isAttempted(event)) {
        	attemptedAnswers.add(event.getItemId());
        } else {
        	unattemptedAnswers.add(event.getItemId());
        }
    }

    public void onEvent(CorrectResponseEvent event) {
        validateItemSetId(event.getItemSetId());

        correctAnswers.add(event.getItemId());
        incorrectAnswers.remove(event.getItemId());
    }

    public void onEvent(IncorrectResponseEvent event) {
        validateItemSetId(event.getItemSetId());

        incorrectAnswers.add(event.getItemId());
        correctAnswers.remove(event.getItemId());
    }

    public void onEvent(SubtestEndedEvent event) {
        validateItemSetId(event.getItemSetId());

        // TODO: This calculation could be moved into the Event
        channel.send(new SubtestNumberCorrectEvent(event,
        		numberOfItems,
        		correctAnswers.size(),
        		incorrectAnswers.size(),
				attemptedAnswers.size(),
				unattemptedAnswers.size(),
                ScorerHelper.calculatePercentage(
                        correctAnswers.size(), numberOfItems)));
    }
}