package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.Collection;

import com.ctb.lexington.db.mapper.ItemMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.AssessmentStartedEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestStartedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;

public class SubtestItemCollectionCalculator extends Calculator {
    private Integer productId;

    /**
     * @param channel
     * @param scorer
     */
    public SubtestItemCollectionCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, AssessmentStartedEvent.class);
        channel.subscribe(this, SubtestStartedEvent.class);
        mustPrecede(AssessmentStartedEvent.class, SubtestStartedEvent.class);
    }

    public void onEvent(AssessmentStartedEvent event) {
        productId = event.getProductId();
    }

    public void onEvent(SubtestStartedEvent event) {
        Collection items;
        Connection oasConnection = null;
        try {
            oasConnection = scorer.getOASConnection();
            items = new ItemMapper(oasConnection).findItemByItemSetId(DatabaseHelper.asLong(event
                    .getItemSetId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, oasConnection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(items != null && items.size() > 0) {
	        SubtestItemCollectionEvent subtestItemsEvent = new SubtestItemCollectionEvent(event
	                .getTestRosterId(), event.getItemSetId(), event.getItemSetName(), items);
	        subtestItemsEvent.setProductId(productId);
	        channel.send(subtestItemsEvent);
        }
    }
}