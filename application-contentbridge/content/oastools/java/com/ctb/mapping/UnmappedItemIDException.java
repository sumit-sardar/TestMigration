package com.ctb.mapping;

import com.ctb.common.tools.SystemException;

public class UnmappedItemIDException extends SystemException {
    public UnmappedItemIDException(String itemId) {
        super("Item ID '" + itemId + "' not found in mapping file");
    }
}
