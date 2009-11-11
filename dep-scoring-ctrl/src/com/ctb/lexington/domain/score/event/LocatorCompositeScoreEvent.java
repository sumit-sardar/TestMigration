package com.ctb.lexington.domain.score.event;

import java.math.BigDecimal;

import com.ctb.lexington.domain.score.event.common.Event;

public class LocatorCompositeScoreEvent extends Event {
    private final String type;
    private final Long pointsAttempted;
    private final Long pointsObtained;
    private final Long recommendedLevelId;

    public LocatorCompositeScoreEvent(final Long testRosterId, final String type, final Long pointsAttempted, final Long pointsObtained, final Long recommendedLevelId) {
        super(testRosterId);
        this.type = type;
        this.recommendedLevelId = recommendedLevelId;
        this.pointsAttempted = pointsAttempted;
        this.pointsObtained = pointsObtained;
    }

    public String getType() {
        return type;
    }

    public Long getRecommendedLevelId() {
        return recommendedLevelId;
    }
    
    public Long getPointsAttempted() {
        return pointsAttempted;
    }
    
    public Long getPointsObtained() {
        return pointsObtained;
    }
}