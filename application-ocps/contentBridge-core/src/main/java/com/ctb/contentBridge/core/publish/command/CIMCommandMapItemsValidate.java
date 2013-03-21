package com.ctb.contentBridge.core.publish.command;

import java.io.File;

class CIMCommandMapItemsValidate extends AbstractCIMCommandItemsFromFile {

    private final File frameworkFile;
    private final File objectivesFile;
    private final File mappingFile;
    private final String objectivesFileFormat;
    
    public String getCommandName() {
		return MAP_ITEMS_VALIDATE;
	}

    CIMCommandMapItemsValidate(
        String environment,
        File inputFile,
        int validationMode,
        File frameworkFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat) {
        super(environment, inputFile, validationMode);
        this.frameworkFile = frameworkFile;
        this.objectivesFile = objectivesFile;
        this.mappingFile = mappingFile;
        this.objectivesFileFormat = fileFormat;
    }

    File getFrameworkFile() {
        return frameworkFile;
    }

    File getMappingFile() {
        return mappingFile;
    }

    File getObjectivesFile() {
        return objectivesFile;
    }

    String getObjectivesFileFormat() {
        return objectivesFileFormat;
    }

}
