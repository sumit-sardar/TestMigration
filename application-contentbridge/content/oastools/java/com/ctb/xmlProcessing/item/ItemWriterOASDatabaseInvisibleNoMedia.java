package com.ctb.xmlProcessing.item;

import java.sql.Connection;

import net.sf.hibernate.Session;

import com.ctb.common.tools.SystemException;

public class ItemWriterOASDatabaseInvisibleNoMedia
    extends AbstractItemWriterOASDatabase {

    public ItemWriterOASDatabaseInvisibleNoMedia(Connection trueConnection) {
        super(trueConnection);
    }
    
	public ItemWriterOASDatabaseInvisibleNoMedia(Session session) {
		super(session);
	}

    public String write(Item item) {
        try {
        	item.setInvisible(true);
            setItemIdFromDatabase(item);
            this.setDatabaseValidator(item);
            if (!"NI".equals(item.getType()))
            	this.getDatabaseValidator().validateItemreadyForInsert();
            this.getItemGateway().writeItemIntoDatabase(item);
            this.getDatabaseValidator().validateItemInDB();
            return item.getId();
        } catch (Exception e) {
            throw new SystemException(
                "Could not write Item: " + e.getMessage(),
                e);
        }
    }
    
}
