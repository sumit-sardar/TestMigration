package com.ctb.contentBridge.core.publish.command;

import java.io.File;

class CIMCommandImportAndMapItems
    extends AbstractCIMCommandContentFromFile
    implements InterfaceGeneratesMedia {

    private final String objectivesFileFormat;
    private final String imageArea;
    private final String localImageArea;
    private final int validationMode;
    private final String mappingDir;
	private final String[] mappingList;

    CIMCommandImportAndMapItems(
        String environment,
        File inputFile,
        String mappingDir,
        String[] mappingList,
        int validationMode,
        String objectivesFileFormat,
        String imageArea,
        String localImageArea) {
        super(environment, inputFile);
        this.imageArea = imageArea;
        this.localImageArea = localImageArea;
        this.objectivesFileFormat = objectivesFileFormat;
        this.validationMode = validationMode;
        this.mappingDir = mappingDir;
        this.mappingList = mappingList;
    }

    public String getCommandName() {
        return IMPORT_AND_MAP_ITEMS;
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

    int getValidationMode() {
        return validationMode;
    }

    String getMappingDir() {
        return mappingDir;
    }

    String[] getMappingList() {
        return mappingList;
    }

}
