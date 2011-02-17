/*
 * Created on Oct 30, 2003
 *
 */
package com.ctb.xmlProcessing.item;

import java.sql.Connection;

import com.ctb.common.tools.DBConnection;
import com.ctb.common.tools.SystemException;
import com.ctb.common.tools.media.MediaWriter;

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
                "Could not commit Item to Database: " + e.getMessage(),
                e);
        }
    }
}
