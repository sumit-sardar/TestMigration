package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.media.Media;

public interface ItemMediaGenerator {
    Media generateMedia(Item mappedItem);
}
