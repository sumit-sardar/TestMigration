package com.ctb.xmlProcessing.item;

import com.ctb.common.tools.media.MediaGenerator;

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
