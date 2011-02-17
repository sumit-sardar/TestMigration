package com.ctb.xmlProcessing.item;

import java.sql.Connection;

import com.ctb.common.tools.SystemException;

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
                "Could not validate Item Insert: " + e.getMessage(),
                e);
        }
    }
    
    public void writeDatapointConditionCode( Item item )
    {
        
    }
}
