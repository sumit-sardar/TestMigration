package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.media.MediaGenerator;


public class ItemMediaGeneratorFactory {
    private ItemMediaGeneratorFactory() {
    }

    public static ItemMediaGenerator getItemMediaGeneratorDummy() {
        return new ItemMediaGeneratorDummy();
    }

    public static ItemMediaGenerator getItemMediaGenerator() {
        return new MediaGenerator();
    }
    
    public static ItemMediaGenerator getItemMediaCache() {
    	return new ItemMediaCache();
    }

    public static ItemMediaGeneratorCaching getItemMediaGeneratorCaching() {
        return new ItemMediaGeneratorCaching();
    }

}
