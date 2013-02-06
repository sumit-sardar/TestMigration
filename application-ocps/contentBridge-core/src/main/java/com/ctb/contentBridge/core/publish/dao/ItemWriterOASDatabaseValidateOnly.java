package com.ctb.contentBridge.core.publish.dao;

import java.sql.Connection;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.item.Item;


public class ItemWriterOASDatabaseValidateOnly
    extends AbstractItemWriterOASDatabase {

    public ItemWriterOASDatabaseValidateOnly(Connection trueConnection) {
        super(trueConnection);
    }

    public String write(Item item) {
        try {
			setItemIdFromDatabase(item);
            this.setDatabaseValidator(item);
            getDatabaseValidator().validateItemreadyForInsert();
            return item.getId();
        } catch (Exception e) {
            throw new SystemException(
                "Could not validate Item Insert: " + e.getMessage());
        }
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        
    }
}
