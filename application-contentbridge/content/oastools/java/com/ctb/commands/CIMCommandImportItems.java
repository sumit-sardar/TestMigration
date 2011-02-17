package com.ctb.commands;

import java.io.File;

public class CIMCommandImportItems
    extends AbstractCIMCommandItemsFromFile
    implements InterfaceGeneratesMedia {

    private final String imageArea;
    private final String localImageArea;

    public String getCommandName() {
        return IMPORT_ITEMS;
    }

    public CIMCommandImportItems(
        String environment,
        File inputFile,
        int validationMode,
        String imageArea,
        String localImageArea) {
        super(environment, inputFile, validationMode);
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
