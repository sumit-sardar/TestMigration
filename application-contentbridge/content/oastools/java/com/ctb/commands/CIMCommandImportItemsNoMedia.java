package com.ctb.commands;

import java.io.File;

class CIMCommandImportItemsNoMedia extends AbstractCIMCommandItemsFromFile {

    public String getCommandName() {
		return IMPORT_ITEMS_NO_MEDIA;
	}
     
    CIMCommandImportItemsNoMedia(int validationMode, String environment, File inputFile) {
        super(environment, inputFile, validationMode);
    }

}
