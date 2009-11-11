package com.ctb.lexington.db.mapper;

import com.ctb.lexington.db.record.Persistent;

public class ConditionCodeIdItemId implements Persistent {
    private Integer conditionCodeId;
    private String itemId;
    private Integer itemSetId;

    public Integer getConditionCodeId() {
        return conditionCodeId;
    }

    public void setConditionCodeId(Integer conditionCodeId) {
        this.conditionCodeId = conditionCodeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }
}