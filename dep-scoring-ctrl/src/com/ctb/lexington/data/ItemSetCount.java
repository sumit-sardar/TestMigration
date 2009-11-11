package com.ctb.lexington.data;

import com.ctb.lexington.db.record.Persistent;


public class ItemSetCount implements Persistent {
    private Integer itemSetId;
    private Integer numItems;
    
    public ItemSetCount() { }

    public ItemSetCount(final Integer itemSetId, final Integer numItems) {
        this.itemSetId = itemSetId;
        this.numItems = numItems;
    }
    
    public Integer getNumItems() {
        return numItems;
    }
    public void setNumItems(Integer count) {
        this.numItems = count;
    }
    public Integer getItemSetId() {
        return itemSetId;
    }
    public void setItemSetId(Integer itemSetId) {
        this.itemSetId = itemSetId;
    }
}
