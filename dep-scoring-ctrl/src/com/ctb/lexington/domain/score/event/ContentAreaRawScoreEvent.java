package com.ctb.lexington.domain.score.event;

import java.util.Map;

import com.ctb.lexington.domain.score.event.common.Event;

public class ContentAreaRawScoreEvent extends Event {
    private final int pointsObtained;
    private final int pointsAttempted;
    private final int percentObtained;
    private final int pointsPossible;
    private final String contentAreaName;
    private final Map contentAreaMap; // For Laslink Scoring
    private final int itemSetId; // For Laslink Scoring

   
 // For Laslink Scoring
	public ContentAreaRawScoreEvent(final Long testRosterId, final String contentAreaName, final int pointsObtained,
            final int pointsAttempted, final int percentObtained, final int pointsPossible, final Map contentAreaMap, 
            final int itemSetId) {
        super(testRosterId);
        this.pointsObtained = pointsObtained;
        this.pointsAttempted = pointsAttempted;
        this.contentAreaName = contentAreaName;
        this.percentObtained = percentObtained;
        this.pointsPossible = pointsPossible;
        this.contentAreaMap = contentAreaMap; // For Laslink Scoring
        this.itemSetId = itemSetId; // For Laslink Scoring
        
    }

    /**
     * @return Returns the pointsAttempted.
     */
    public int getPointsAttempted() {
        return pointsAttempted;
    }

    /**
     * @return Returns the pointsObtained.
     */
    public int getPointsObtained() {
        return pointsObtained;
    }

    /**
     * @return Returns the reportingLevel.
     */
    public String getContentAreaName() {
        return contentAreaName;
    }

    public int getPercentObtained() {
        return percentObtained;
    }

    public int getPointsPossible() {
        return pointsPossible;
    }
    

	/**
	 * @return the contentAreaMap
	 */
	public Map getContentAreaMap() {
		return contentAreaMap;
	}

	/**
	 * @return the itemSetId
	 */
	public int getItemSetId() {
		return itemSetId;
	}

}