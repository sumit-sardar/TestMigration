package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="SCORABLE_ITEM"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class ScorableItemRecord {

    private ScorableItemCompositeId id = new ScorableItemCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public ScorableItemCompositeId getId() {
        return id;
    }
    public void setId(ScorableItemCompositeId id) {
        this.id = id;
    }

}
