package com.ctb.contentBridge.core.publish.xml.item;


public class ItemWriterDummy implements ItemWriter {
    public String write(Item item) {
        return (item.getId() == null) ? "" : item.getId();
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        
    }
}
