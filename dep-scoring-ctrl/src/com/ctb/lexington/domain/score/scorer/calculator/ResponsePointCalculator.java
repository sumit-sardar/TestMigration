package com.ctb.lexington.domain.score.scorer.calculator;

import com.ctb.lexington.data.ItemVO;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.ResponseReceivedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

public class ResponsePointCalculator extends AbstractResponseCalculator {
    /**
     * @param channel
     * @param scorer
     */
    public ResponsePointCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
    }

    public void onEvent(ResponseReceivedEvent event) {
        validateItemSetId(event.getItemSetId());

        if (sicEvent.isAttempted(event)) {
            final Integer attempted = sicEvent.getMaxPoints(event.getItemId());
            final Integer obtained = computePointsObtained(event);

            channel.send(new PointEvent(event.getTestRosterId(), event.getItemId(), event
                    .getItemSetId(), attempted, obtained));
        } else { //if (!(ItemLocal.ITEM_TYPE_CR.equals(sicEvent.getType(event.getItemId())) && sicEvent.isOnlineCr(event.getItemId()))) {
        	channel.send(new PointEvent(event.getTestRosterId(), event.getItemId(), event
                    .getItemSetId(), new Integer(0), new Integer(0)));
        }
    }

    private Integer computePointsObtained(ResponseReceivedEvent event) {
        Integer pointsObtained = event.getPointsObtained();
        if (pointsObtained == null) {
            // For SR items, event.pointsObtained can be null, if so - get it from the item's
            // max/min points. It need not be null, if the proctor sets a value.
            String itemType = sicEvent.getType(event.getItemId());
            if (ItemVO.ITEM_TYPE_SR.equals(itemType)) {
                if (sicEvent.isCorrectResponse(event.getItemId(), event.getResponse())) {
                    pointsObtained = sicEvent.getMaxPoints(event.getItemId());
                } else {
                    pointsObtained = sicEvent.getMinPoints(event.getItemId());
                }
            } else if (ItemVO.ITEM_TYPE_CR.equals(itemType)) {
                if (sicEvent.isAttempted(event))
                    pointsObtained = sicEvent.getMinPoints(event.getItemId());
            } else {
                throw new IllegalArgumentException("Item is not of recognized type: Item id - "
                        + event.getItemId() + ", Item type - " + itemType);
            }
        }

        return pointsObtained;
    }
}