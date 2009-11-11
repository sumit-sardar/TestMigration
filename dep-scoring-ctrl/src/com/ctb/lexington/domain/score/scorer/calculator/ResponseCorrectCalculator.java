package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.CorrectResponseEvent;
import com.ctb.lexington.domain.score.event.IncorrectResponseEvent;
import com.ctb.lexington.domain.score.event.NoResponseEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

/**
 * This class counts the number of correct answers for a subtest for specific roster instance.
 */
public class ResponseCorrectCalculator extends AbstractResponseCalculator {
    public ResponseCorrectCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
    }

    /**
     * Sends a <code>CorrectResponseEvent</code> event if the answer is correct and a
     * <code>IncorrectResponseEvent</code> if the answer is incorrect. NOTE: A condition code
     * constitutes neither a correct nor incorrect answer! An item is considered incorrect only if
     * an incorrect choice is marked and no other choice is marked.
     * 
     * @param event
     */
    public void onEvent(ResponseReceivedEvent event) {
        validateItemSetId(event.getItemSetId());

        final String itemId = event.getItemId();
        if (ItemVO.ITEM_TYPE_SR.equals(sicEvent.getType(itemId))) {
            final String response = event.getResponse();
            final boolean isConditionCode = sicEvent.isConditionCode(itemId, response);
            if (isConditionCode || response == null) {
            	if (sicEvent.isMarked(event)) {
                	channel.send(new IncorrectResponseEvent(event));
                } else {
                	channel.send(new NoResponseEvent(event));
                }
            } else {
                if (sicEvent.isCorrectResponse(itemId, response)) {
                    channel.send(new CorrectResponseEvent(event));
                } else {
                    channel.send(new IncorrectResponseEvent(event));
                }
            }
        }
        // TODO: how do we handle CR item responses?
    }
}