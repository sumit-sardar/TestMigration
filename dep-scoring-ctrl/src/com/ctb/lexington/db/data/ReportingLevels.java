package com.ctb.lexington.db.data;

import com.ctb.lexington.db.record.Persistent;

public class ReportingLevels implements Persistent {

    private Integer contentAreaLevel;
    private Integer scoringItemSetLevel;
    private Integer secondaryScoringItemSetLevel;

    public ReportingLevels() {
    }

    public Integer getContentAreaLevel() {
        return contentAreaLevel;
    }

    public void setContentAreaLevel(Integer contentAreaLevel) {
        this.contentAreaLevel = contentAreaLevel;
    }

    public Integer getScoringItemSetLevel() {
        return scoringItemSetLevel;
    }

    public void setScoringItemSetLevel(Integer scoringItemSetLevel) {
        this.scoringItemSetLevel = scoringItemSetLevel;
    }

    public Integer getSecondaryScoringItemSetLevel() {
        return secondaryScoringItemSetLevel;
    }

    public void setSecondaryScoringItemSetLevel(Integer secondaryScoringItemSetLevel) {
        this.secondaryScoringItemSetLevel = secondaryScoringItemSetLevel;
    }
}
