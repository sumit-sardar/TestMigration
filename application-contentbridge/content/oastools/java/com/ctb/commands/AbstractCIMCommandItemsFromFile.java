package com.ctb.commands;

import java.io.File;

abstract class AbstractCIMCommandItemsFromFile
    extends AbstractCIMCommandContentFromFile {

    private final int validationMode;

    AbstractCIMCommandItemsFromFile(
        String targetEnvironment,
        File inputFile,
        int validationMode) {
        super(targetEnvironment, inputFile);
        this.validationMode = validationMode;
    }

    int getValidationMode() {
        return validationMode;
    }
}
