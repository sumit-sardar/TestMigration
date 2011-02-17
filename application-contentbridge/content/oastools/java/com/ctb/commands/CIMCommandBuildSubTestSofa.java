package com.ctb.commands;

import java.io.File;

class CIMCommandBuildSubTestSofa
    extends AbstractCIMCommandContentFromFile implements InterfaceGeneratesMedia{

    private final String localImageArea;
    private final String imageArea;
    private final String objectivesFileFormat;
    private final String mappingDir;

    public String getCommandName() {
        return BUILD_TEST;
    }

    CIMCommandBuildSubTestSofa(
        String environment,
        File inputFile,
        String mappingDir,
        String imageArea,
        String localImageArea,
        String fileFormat) {
        super(environment, inputFile);
        this.objectivesFileFormat = fileFormat;
        this.imageArea = imageArea;
        this.localImageArea = localImageArea;
        this.mappingDir = mappingDir;
    }


    String getObjectivesFileFormat() {
        return objectivesFileFormat;
    }

    public String getImageArea() {
        return imageArea;
    }

    public String getLocalImageArea() {
        return localImageArea;
    }

    public String getMappingDir() {
        return mappingDir;
    }

}
