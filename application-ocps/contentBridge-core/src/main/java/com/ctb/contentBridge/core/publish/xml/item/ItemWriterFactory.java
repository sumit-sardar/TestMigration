package com.ctb.contentBridge.core.publish.xml.item;

import java.sql.Connection;

import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabase;
import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabaseInvisibleNoMedia;
import com.ctb.contentBridge.core.publish.dao.ItemWriterOASDatabaseValidateOnly;

import net.sf.hibernate.Session;

public class ItemWriterFactory {

    private ItemWriterFactory() {
    }

    public static ItemWriter getItemWriterDummy() {
        return new ItemWriterDummy();
    }

    public static ItemWriter getItemWriterOASDatabaseValidateOnly(Connection connection) {
        return new ItemWriterOASDatabaseValidateOnly(connection);
    }

    public static ItemWriter getItemWriterOASDatabaseNoMedia(Connection connection) {
        return new ItemWriterOASDatabaseInvisibleNoMedia(connection);
    }

    public static ItemWriter getItemWriterOASDatabase(Connection connection) {
        return new ItemWriterOASDatabase(connection);
    }
    
	public static ItemWriter getItemWriterOASDatabaseNoMedia(Session session) {
		return new ItemWriterOASDatabaseInvisibleNoMedia(session);
	}
}
