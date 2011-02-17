package com.ctb.hibernate.persist;

/**
 * @hibernate.class table="SCORE_LOOKUP_ITEM_SET"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ScoreLookupItemSetRecord {

    private ScoreLookupItemSetCompositeId id = new ScoreLookupItemSetCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ScoreLookupItemSetCompositeId getId() {
        return id;
    }
    public void setId(ScoreLookupItemSetCompositeId id) {
        this.id = id;
    }
    public Long getItemSetId() {
        return id.getItemSetId();
    }

    public String getScoreLookupId() {
        return id.getScoreLookupId();
    }

    public void setItemSetId(Long itemSetId) {
        id.setItemSetId(itemSetId);
    }

    public void setScoreLookupId(String scoreLookupId) {
        id.setScoreLookupId(scoreLookupId);
    }

}
