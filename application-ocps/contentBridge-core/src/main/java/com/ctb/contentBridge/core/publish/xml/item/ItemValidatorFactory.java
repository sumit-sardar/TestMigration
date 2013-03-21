package com.ctb.contentBridge.core.publish.xml.item;


public class ItemValidatorFactory {

    private ItemValidatorFactory() {
    }

    public static ItemValidator getItemValidatorDummy() {
        return new ItemValidatorDummy();
    }

    public static ItemValidator getItemValidator(int validationMode) {
        return new ItemAssembler(validationMode);
    }

}
