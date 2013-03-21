package com.ctb.contentBridge.core.publish.hibernate.persist;

/**
 * @hibernate.class table="DATAPOINT_CONDITION_CODE"
 * persister="com.ctb.hibernate.CTBEntityPersister"
 * dynamic-update="true"
 * dynamic-insert="true"
 */
public class DatapointConditionCodeRecord {

    private DatapointConditionCodeCompositeId id = new DatapointConditionCodeCompositeId();

    /** @hibernate.id generator-class="assigned" */
    public DatapointConditionCodeCompositeId getId() {
        return id;
    }
    public void setId(DatapointConditionCodeCompositeId id) {
        this.id = id;
    }

}
