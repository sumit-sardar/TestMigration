package com.ctb.xmlProcessing.item;


public class ItemWriterDummy implements ItemWriter {
    public String write(Item item) {
        return (item.getId() == null) ? "" : item.getId();
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        
    }
}
