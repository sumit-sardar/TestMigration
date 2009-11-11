package com.ctb.lexington.domain.score.scorer.calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ctb.lexington.data.ItemContentArea;
import com.ctb.lexington.domain.score.event.ContentAreaRawScoreEvent;
import com.ctb.lexington.domain.score.event.PointEvent;
import com.ctb.lexington.domain.score.event.SubtestContentAreaItemCollectionEvent;
import com.ctb.lexington.domain.score.event.SubtestEndedEvent;
import com.ctb.lexington.domain.score.event.common.Channel;
import com.ctb.lexington.domain.score.scorer.Scorer;
import com.ctb.lexington.domain.score.scorer.ScorerHelper;
import com.ctb.lexington.util.SafeHashMap;

/**
 * @todo The calculator depends on the order of events -- it holds an SCAIC event over as a private
 *       member
 */
public class ContentAreaRawScoreCalculator extends Calculator {
    private final Map contentAreas = new SafeHashMap(String.class, Map.class);
    private final Map possibleMap = new SafeHashMap(String.class, Integer.class);
    private SubtestContentAreaItemCollectionEvent subtestContentArea = null;

    /**
     * Constructor for ContentAreaRawScoreCalculator.
     * 
     * @param channel
     * @param scorer
     */
    public ContentAreaRawScoreCalculator(Channel channel, Scorer scorer) {
        super(channel, scorer);

        channel.subscribe(this, SubtestContentAreaItemCollectionEvent.class);
        channel.subscribe(this, PointEvent.class);
        channel.subscribe(this, SubtestEndedEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, PointEvent.class);
        mustPrecede(SubtestContentAreaItemCollectionEvent.class, SubtestEndedEvent.class);
    }

    public void onEvent(SubtestContentAreaItemCollectionEvent event) {
        subtestContentArea = event;

        Iterator iter = event.getContentAreaNames().iterator();
        while (iter.hasNext()) {
            String contentAreaName = (String) iter.next();
            contentAreas.put(contentAreaName, new HashMap()); // nulls permitted as values
            Map itemContentAreaMap = event.getItemContentAreasByItemId();
            Iterator itemContentAreaIter = itemContentAreaMap.values().iterator();
            int contentAreaPointsPossible = 0;
            while (itemContentAreaIter.hasNext()) {
                ItemContentArea ica = (ItemContentArea) itemContentAreaIter.next();
                if (ica.getContentAreaName().equals(contentAreaName)) {
                    contentAreaPointsPossible += ica.getMaxPoints().intValue();
                }
            }
            possibleMap.put(contentAreaName, new Integer(contentAreaPointsPossible));
        }
    }

    public void onEvent(PointEvent event) {
        setItemPointsForContentArea(contentAreas, getItemContentAreaFor(event), event);
    }

    public void onEvent(SubtestEndedEvent event) {
        calculateAndSendRawScoreEvent(event.getTestRosterId(), contentAreas);
    }

    private void setItemPointsForContentArea(final Map map, final String contentArea,
            final PointEvent event) {
        final Map itemPointsForContentArea = (Map) map.get(contentArea);

        itemPointsForContentArea.put(event.getItemId(), new ItemScore(event.getPointsObtained(),
                event.getPointsAttempted()));
    }

    private void calculateAndSendRawScoreEvent(final Long testRosterId, final Map contentAreaMap) {
        for (final Iterator it = contentAreaMap.entrySet().iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String contentArea = (String) entry.getKey();
            final Map itemMap = (Map) entry.getValue();

            int pointsObtained = 0;
            int pointsAttempted = 0;

            for (final Iterator scoreIt = itemMap.values().iterator(); scoreIt.hasNext();) {
                final ItemScore score = (ItemScore) scoreIt.next();

                if (score != null && score.getPointsObtained() != null)
                    pointsObtained += score.getPointsObtained().intValue();
                if (score != null && score.getPointsAttempted() != null)
                    pointsAttempted += score.getPointsAttempted().intValue();
            }

            final int pointsPossible = getPointsPossibleFor(contentArea);

            channel.send(new ContentAreaRawScoreEvent(testRosterId, contentArea, pointsObtained,
                    pointsAttempted, ScorerHelper.calculatePercentage(pointsObtained,
                            pointsPossible), pointsPossible));
        }

        contentAreas.clear();
        possibleMap.clear();
    }

    private String getItemContentAreaFor(final PointEvent event) {
        return ((ItemContentArea) subtestContentArea.getItemContentAreasByItemId().get(
                event.getItemId())).getContentAreaName();
    }

    private int getPointsPossibleFor(final String contentArea) {
        return ((Integer) possibleMap.get(contentArea)).intValue();
    }

    private class ItemScore {
        private Integer pointsObtained;
        private Integer pointsAttempted;

        public ItemScore(Integer pointsObtained, Integer pointsAttempted) {
            this.pointsObtained = pointsObtained;
            this.pointsAttempted = pointsAttempted;
        }

        public Integer getPointsAttempted() {
            return pointsAttempted;
        }

        public Integer getPointsObtained() {
            return pointsObtained;
        }
    }
}