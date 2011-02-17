package com.ctb.hibernate.persist;

import java.io.Serializable;

    public class ScorableItemCompositeId implements Serializable {
        private String itemId;
        private Long itemSetId;
        private String scoreTypeCode;

    /**
     * @hibernate.property
     * column="ITEM_ID"
     * not-null="true"
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @hibernate.property
     * column="ITEM_SET_ID"
     * not-null="true"
     */
    public Long getItemSetId() {
        return itemSetId;
    }

    /**
     * @hibernate.property
     * column="SCORE_TYPE_CODE"
     * not-null="true"
     */
    public String getScoreTypeCode() {
        return scoreTypeCode;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public void setScoreTypeCode(String scoreTypeCode) {
        this.scoreTypeCode = scoreTypeCode;
    }

    public int hashCode() {
        // TODO: Generated by HibernateGen.rb
        return toString().hashCode();
    }

    public boolean equals(Object object) {
        // TODO: Generated by HibernateGen.rb
        return toString().equals(object.toString());
    }

    public String toString() {
        // TODO: Generated by HibernateGen.rb
        return "" + scoreTypeCode.toString() + "|" + itemSetId.toString() + "|" + itemId.toString();
    }
}
