package com.ctb.common.tools.itemxml;


import oracle.sql.CLOB;


/**
 * marks a class as capable of procssing a set of
 * items, one by one, as xml CLOBs.  implementations
 * are used by <code>ItemsProcessor</code>
 */
public interface ItemProcessor {

    /**
     * process/munge an item xml CLOB
     * @param itemId the id for the item being processed
     * @param xmlMedia the item's xml media
     * @return the processed/munged xml CLOB
     */
    public String processItem(String itemId, CLOB xmlMedia);

    /**
     * return whether to go ahead with item processing.  this is split out
     * from processItem method in order to avoid database updates when
     * an item is not touched.
     * @param itemId the id for the item about to be processed
     * @param xmlMedia the item's xml media
     * @return whether to go ahead and process the item
     */
    public boolean doProcessItem(String itemId, CLOB xmlMedia);

    /**
     * invoked by <code>ItemsProcessor</code> prior
     * to processing any items.
     */
    public void startProcessingItems();

    /**
     * invoked by <code>ItemsProcessor</code> after
     * completion of item processing
     * @param success indicator of whether <code>ItemsProcessor</code>
     * completed item processing without encountering any unexpected exceptions.
     */
    public void stopProcessingItems(boolean success);
}
