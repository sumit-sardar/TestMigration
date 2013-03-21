package com.ctb.contentBridge.core.publish.mapping;

import com.ctb.contentBridge.core.exception.SystemException;


public class UnmappedItemIDException extends SystemException {
    public UnmappedItemIDException(String itemId) {
        super("Item ID '" + itemId + "' not found in mapping file");
    }
}
