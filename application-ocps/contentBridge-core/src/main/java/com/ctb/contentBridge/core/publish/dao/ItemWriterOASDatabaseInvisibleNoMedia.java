package com.ctb.contentBridge.core.publish.dao;

import java.sql.Connection;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.xml.item.Item;

import net.sf.hibernate.Session;

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
            	this.getDatabaseValidator().validateItemForAllObjectveForInsert();
            this.getItemGateway().writeItemIntoDatabase(item);
            this.getDatabaseValidator().validateItemInDB();
            return item.getId();
        } catch (Exception e) {
        	System.out.println("Exception inside write:" + e.getMessage());
            throw new SystemException(
                "Could not write Item: " + e.getMessage());
        }
    }
    
}
