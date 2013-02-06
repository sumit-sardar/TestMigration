/*
 * Created on Oct 30, 2003
 *
 */
package com.ctb.contentBridge.core.publish.dao;

import java.sql.Connection;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.media.MediaWriter;
import com.ctb.contentBridge.core.publish.xml.item.Item;


public class ItemWriterOASDatabase extends AbstractItemWriterOASDatabase {

    public ItemWriterOASDatabase(Connection trueConnection) {
        super(trueConnection);
    }

    public String write(Item item) {
        try {
			setItemIdFromDatabase(item);
            this.setDatabaseValidator(item);
            this.getDatabaseValidator().validateItemreadyForInsert();
            this.getItemGateway().writeItemIntoDatabase(item);
            MediaWriter mediaWriter =
                new MediaWriter(item.getId(), new DBConnection(getSession().connection()));
            if ( item.getMedia() != null )
                mediaWriter.writeMedia(item.getMedia());
            this.getDatabaseValidator().validateItemInDB();
            return item.getId();
        } catch (Exception e) {
            throw new SystemException(
                "Could not commit Item to Database: " + e.getMessage());
        }
    }
}
