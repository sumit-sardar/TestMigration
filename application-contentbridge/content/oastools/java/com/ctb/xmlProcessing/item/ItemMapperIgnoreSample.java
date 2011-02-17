/*
 * Created on Aug 1, 2004 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ctb.xmlProcessing.item;

import java.io.File;

import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;

/**
 * @author wmli TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class ItemMapperIgnoreSample extends ItemMapperAllowsImport {

    public ItemMapperIgnoreSample(int validationMode, File frameworkDefinitonFile,
            File objectivesFile, File mappingFile, String objectivesFileFormat) {
        super(validationMode, frameworkDefinitonFile, objectivesFile, mappingFile,
                objectivesFileFormat);

    }

    public ItemMapperIgnoreSample(int validationMode, Objectives objectives, ItemMap itemMap) {
        super(validationMode, objectives, itemMap);
    }

    public Item mapItem(Item originalItem) throws IdentityMappingException {
/*        if (originalItem.getFrameworkCode().equals(this.getFrameworkCode()))
            return originalItem;
        else */
    	/*if (originalItem.isSample())
        {
            originalItem.setFrameworkCode( this.getFrameworkCode());
            return originalItem;
        }
        else */
            return super.mapItem(originalItem);
    }

}