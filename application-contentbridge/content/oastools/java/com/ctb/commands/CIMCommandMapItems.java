package com.ctb.commands;

import java.io.File;

class CIMCommandMapItems
    extends CIMCommandMapItemsValidate
    implements InterfaceGeneratesMedia {

    private String localImageArea;
    private String imageArea;

    public String getCommandName() {
        return MAP_ITEMS;
    }

    CIMCommandMapItems(
        String environment,
        File inputFile,
        int validationMode,
        String imageArea,
        String localImageArea,
        File frameworkFile,
        File objectivesFile,
        File mappingFile,
        String fileFormat) {
        super(
            environment,
            inputFile,
            validationMode,
            frameworkFile,
            objectivesFile,
            mappingFile,
            fileFormat);
        this.imageArea = imageArea;
        this.localImageArea = localImageArea;
    }

    public String getImageArea() {
        return imageArea;
    }

    public String getLocalImageArea() {
        return localImageArea;
    }

}
