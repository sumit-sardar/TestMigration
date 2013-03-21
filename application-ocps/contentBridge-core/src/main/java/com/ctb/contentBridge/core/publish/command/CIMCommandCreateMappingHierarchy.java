package com.ctb.contentBridge.core.publish.command;

import java.io.File;

public class CIMCommandCreateMappingHierarchy implements CIMCommand {

    private final File frameworkFile;
    private final File objectivesFile;
    private final File mappingFile;
    private final String objectivesFileFormat;

    public String getCommandName() {
        return MAPPING_HIERARCHY;
    }

    public CIMCommandCreateMappingHierarchy(
        File frameworkFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat) {
        this.frameworkFile = frameworkFile;
        this.objectivesFile = objectivesFile;
        this.mappingFile = mappingFile;
        this.objectivesFileFormat = fileFormat;
    }

    public String getTargetEnvironment() {
        return null;
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
