package com.ctb.lexington.domain.score.event;

import com.ctb.lexington.domain.score.event.common.Event;

public abstract class SubtestEvent extends Event {
    private final Integer itemSetId;
    protected String itemSetName;
    protected String contentArea; // Added for TN via Acuity

    public SubtestEvent(final Long testRosterId, final Integer itemSetId) {
        super(testRosterId);
        if (null == itemSetId) {
            throw new NullPointerException("Null itemSetId");
        }

        this.itemSetId = itemSetId;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    /**
     * Checks of the given <var>o </var> is equals to this <code>SubtestEvent</code> based on
     * equality of the item set ids.
     * 
     * @param o the other object
     */
    public boolean equals(final Object o) {
        if (null == o)
            return false;

        if (! (o instanceof SubtestEvent))
            return false;

        final SubtestEvent that = (SubtestEvent) o;

        if (null == this.getItemSetId())
            return null == that.getItemSetId();
        return (getItemSetId().equals(that.getItemSetId()));
    }

    public int hashCode() {
        return getItemSetId().intValue();
    }

    public String getItemSetName() {
        return itemSetName;
    }

    public void setItemSetName(final String itemSetName) {
        this.itemSetName = itemSetName;
    }

	public String getContentArea() {
		return contentArea;
	}

	public void setContentArea(String contentArea) {
		this.contentArea = contentArea;
	}
}