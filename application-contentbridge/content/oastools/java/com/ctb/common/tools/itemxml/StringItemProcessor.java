package com.ctb.common.tools.itemxml;


import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import oracle.sql.CLOB;

import com.ctb.common.tools.SystemException;
import com.ctb.util.Pipe;


public abstract class StringItemProcessor implements com.ctb.common.tools.itemxml.ItemProcessor {

    /**
     * process/munge an item xml CLOB
     * @param itemId the id for the item being processed
     * @param xmlMedia the item's xml media
     * @return the processed/munged xml CLOB
     */
    public String processItem(String itemId, CLOB xmlMedia) {
        return processItemAsString(itemId, clobToString(xmlMedia));
    }

    public abstract String processItemAsString(String itemId, String xmlMedia);

    /**
     * return whether to go ahead with item processing.  this is split out
     * from processItem method in order to avoid database updates when
     * an item is not touched.
     * @param itemId the id for the item about to be processed
     * @param xmlMedia the item's xml media
     * @return whether to go ahead and process the item
     */
    public boolean doProcessItem(String itemId, CLOB xmlMedia) {
        return doProcessItemAsString(itemId, clobToString(xmlMedia));
    }

    private String clobToString(CLOB clob) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            new Pipe(clob.binaryStreamValue(), out).run();
            return out.toString();
        } catch (SQLException e) {
            throw new SystemException("unexpected error reading CLOB", e);
        }
    }

    public abstract boolean doProcessItemAsString(String itemId, String xmlMedia);
}
