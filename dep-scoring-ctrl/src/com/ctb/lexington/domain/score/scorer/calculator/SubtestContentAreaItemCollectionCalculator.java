package com.ctb.lexington.domain.score.scorer.calculator;

import java.sql.Connection;
import java.util.List;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.db.mapper.ItemMapper;
import com.ctb.lexington.db.utils.DatabaseHelper;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestItemCollectionEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.util.IndexMap;

public class SubtestContentAreaItemCollectionCalculator extends Calculator {
    /**
     * Constructor for SubtestContentAreaItemCollectionCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public SubtestContentAreaItemCollectionCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);
        channel.subscribe(this, SubtestItemCollectionEvent.class);
    }

    public void onEvent(final SubtestItemCollectionEvent event) {
        final List itemsByContentAreaQueryResults = getItemsByContentArea(event.getItemSetId(),
                event.getProductId());

        final IndexMap itemsByContentArea = new IndexMap(String.class, ItemContentArea.class,
                new IndexMap.Mapper() {
                    public Object getKeyFor(final Object value) {
                        return (((ItemContentArea) value).getItemId()+ ((ItemContentArea) value).getContentAreaName());
                    }
                });

        itemsByContentArea.addAll(itemsByContentAreaQueryResults);

        channel.send(new SubtestContentAreaItemCollectionEvent(event.getTestRosterId(),
                DatabaseHelper.asLong(event.getItemSetId()), itemsByContentArea));
    }

    protected List getItemsByContentArea(Integer itemSetId, Integer productId) {
        Connection conn = null;
        try {
            conn = scorer.getOASConnection();
            ItemMapper mapper = new ItemMapper(conn);
            return mapper.findItemGroupByContentAreaForItemSetAndProduct(DatabaseHelper
                    .asLong(itemSetId), DatabaseHelper.asLong(productId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                scorer.close(true, conn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}