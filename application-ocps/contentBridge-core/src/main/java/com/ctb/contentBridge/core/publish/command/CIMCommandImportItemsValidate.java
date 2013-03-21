package com.ctb.contentBridge.core.publish.command;

import java.io.File;

class CIMCommandImportItemsValidate extends AbstractCIMCommandItemsFromFile {

    public String getCommandName() {
        return IMPORT_ITEMS_VALIDATE;
    }

    CIMCommandImportItemsValidate(
        String environment,
        File inputFile,
        int validationMode) {
        super(environment, inputFile, validationMode);
    }

}
