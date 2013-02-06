package com.ctb.contentBridge.core.publish.command;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Mar 30, 2004
 * Time: 1:48:02 PM
 * To change this template use Options | File Templates.
 */
public class CIMCommandCreateFlattenedMappingHierarchy implements CIMCommand{

    private final File frameworkFile;
    private final File objectivesFile;
    private final File mappingFile;
    private final String objectivesFileFormat;

    public String getCommandName() {
        return FLATTENED_MAPPING_HIERARCHY;
    }

    public CIMCommandCreateFlattenedMappingHierarchy(
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
