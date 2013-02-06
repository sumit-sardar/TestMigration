/*
 * Created on Dec 2, 2003
 *
 */
package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.media.Media;


public class ItemMediaGeneratorCaching extends ItemMediaCache {

    private final ItemMediaGenerator mediaGenerator;

    public ItemMediaGeneratorCaching() {
        super();
        mediaGenerator = ItemMediaGeneratorFactory.getItemMediaGenerator();
    }

    protected Media handleMediaNotInCache(Item mappedItem) {
        Media generatedMedia = mediaGenerator.generateMedia(mappedItem);
        String itemId =
            ((mappedItem.getHistory() != null)
                && (mappedItem.getHistory().length() != 0))
                ? mappedItem.getHistory()
                : mappedItem.getId();
        addMedia(itemId, generatedMedia);
        return generatedMedia;
    }
    
	protected void postLookup(Item mappedItem) {
	}
}
