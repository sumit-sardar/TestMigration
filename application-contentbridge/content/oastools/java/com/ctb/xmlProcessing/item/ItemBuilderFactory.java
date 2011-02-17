package com.ctb.xmlProcessing.item;

import com.ctb.roundtrip.MediaReader;

public class ItemBuilderFactory {

    private ItemBuilderFactory() {
    }

    public static ItemBuilder getItemBuilderDummy() {
        return new ItemBuilderDummy();
    }

    public static ItemBuilder getItemBuilder(int validationMode) {
        return new ItemAssembler(validationMode);
    }

    public static ItemBuilder getItemBuilderRoundTrip(
        int validationMode,
        MediaReader mediaReader,
        ItemMediaCache itemMediaCache) {
        return new ItemBuilderRoundTrip(
            validationMode,
            mediaReader,
            itemMediaCache);
    }

}
