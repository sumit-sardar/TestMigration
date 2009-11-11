package com.ctb.lexington.db.record;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ScorableItemRecord implements Persistent {
    public static final String TABLE_NAME = "SCORABLE_ITEM";

    public static final String SCORE_TYPE_CODE = "SCORE_TYPE_CODE";
    public static final String ITEM_SET_ID = "ITEM_SET_ID";
    public static final String ITEM_ID = "ITEM_ID";

    private String scoreTypeCode;
    private Long itemSetId;
    private String itemId;
    
    public ScorableItemRecord() { /* boring */ }
    
    public ScorableItemRecord(final String scoreTypeCode, final Long itemSetId, final String itemId) {
        this.scoreTypeCode = scoreTypeCode;
        this.itemSetId = itemSetId;
        this.itemId = itemId;
    }

    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    public void setScoreTypeCode(String scoreTypeCode) {
        this.scoreTypeCode = scoreTypeCode;
    }

    public Long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    // Object
    
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
    
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}