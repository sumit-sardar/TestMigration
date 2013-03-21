package com.ctb.contentBridge.core.publish.xml.item;

import java.io.File;

import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;


public class ItemMapperAllowsImport extends ItemMapperMapOnly {

    public ItemMapperAllowsImport(
        int validationMode,
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String objectivesFileFormat) {
        super(
            validationMode,
            frameworkDefinitonFile,
            objectivesFile,
            mappingFile,
            objectivesFileFormat);
    }
    public ItemMapperAllowsImport(
        int validationMode,
        Objectives objectives,
        ItemMap itemMap) {
        super(validationMode, objectives, itemMap);
    }

    public Item mapItem(Item originalItem) throws IdentityMappingException {

    /*    if (originalItem.getFrameworkCode().equals(this.getFrameworkCode()))
            return originalItem;
        else */
    	System.out.println("originalItem.getType()="+originalItem.getType());    	
		if ("NI".equals(originalItem.getType()))
			return originalItem;
		else 
			return super.mapItem(originalItem);
    }

}
