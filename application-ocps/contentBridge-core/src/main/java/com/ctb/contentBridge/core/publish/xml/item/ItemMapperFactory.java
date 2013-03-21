package com.ctb.contentBridge.core.publish.xml.item;

import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;


public class ItemMapperFactory {

    private ItemMapperFactory() {
    }

    public static ItemMapper getItemMapperDummy() {
        return new ItemMapperDummy();
    }

    public static ItemMapper getItemMapperMapOnly(
        int validationMode,
        Objectives objectives,
        ItemMap itemMap) {
        return new ItemMapperMapOnly(validationMode, objectives, itemMap);
    }

    public static ItemMapper getItemMapperAllowsImport(
        int validationMode,
        Objectives obj,
        ItemMap itemMap) {
        return new ItemMapperAllowsImport(validationMode, obj, itemMap);
    }
    
    public static ItemMapper getItemMapperIgnoreSample(
            int validationMode,
            Objectives objectives,
            ItemMap itemMap) {
            return new ItemMapperIgnoreSample(validationMode, objectives, itemMap);
    }
}
