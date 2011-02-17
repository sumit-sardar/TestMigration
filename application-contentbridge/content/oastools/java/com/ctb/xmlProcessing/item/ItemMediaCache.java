/*
 * Created on Nov 3, 2003
 */
package com.ctb.xmlProcessing.item;

import java.util.*;

import com.ctb.common.tools.SystemException;
import com.ctb.common.tools.media.Media;
import com.ctb.util.iknowxml.R2XmlOutputter;

/**
 * @author wmli
 */
public class ItemMediaCache implements ItemMediaGenerator {
    private Map mediaMap;

    public ItemMediaCache() {
        mediaMap = new HashMap();
    }

    public void addMedia(String itemId, Media media) {
        mediaMap.put(itemId, media);
    }

    public Media generateMedia(Item mappedItem) {
        // get the media using the original item id
        Media media = (Media) mediaMap.get(mappedItem.getHistory());

        if (media == null) {
            media = handleMediaNotInCache(mappedItem);
        }
       
		Media result = new Media();
		result.setXml(new R2XmlOutputter().outputString(mappedItem.getItemRootElement()).toCharArray());
		result.setPdf(media.getPdf());
		result.setAkSwf(media.getAkSwf());
		result.setAkPdf(media.getAkPdf());
        postLookup(mappedItem);
        
        return result;
    }

	protected void postLookup(Item mappedItem) {
        mediaMap.remove(mappedItem.getHistory());
    }

    protected Media handleMediaNotInCache(Item mappedItem) {
        throw new SystemException(
            "Cannot find media for item ["
                + mappedItem.getId()
                + "]");
    }

    public void purgeCache() {
		mediaMap = new HashMap();
    }

}
