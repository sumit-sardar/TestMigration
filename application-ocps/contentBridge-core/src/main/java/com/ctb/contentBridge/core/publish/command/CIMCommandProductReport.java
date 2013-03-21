package com.ctb.contentBridge.core.publish.command;

import java.io.File;

public class CIMCommandProductReport extends AbstractCIMCommand {

    private final File frameworkFile;
    private final File objectivesFile;
    private final File mappingFile;
    private final String objectivesFileFormat;

    public String getCommandName() {
        return PRODUCT_REPORT;
    }

    public CIMCommandProductReport(
        File frameworkFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat,
        String environment) {
        super(environment);
        this.frameworkFile = frameworkFile;
        this.objectivesFile = objectivesFile;
        this.mappingFile = mappingFile;
        this.objectivesFileFormat = fileFormat;
    }

    public File getFrameworkFile() {
        return frameworkFile;
    }

    public File getMappingFile() {
        return mappingFile;
    }

    public File getObjectivesFile() {
        return objectivesFile;
    }

    public String getObjectivesFileFormat() {
        return objectivesFileFormat;
    }

}
