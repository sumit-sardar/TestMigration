package com.ctb.contentBridge.core.publish.command;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Apr 30, 2004
 */
public class CIMCommandGeneratePDF implements CIMCommand {

    private File inFile;
    private String rootDirectory;
    private String directory;
    private String pdffilename;

    /** Creates a new instance of CIMCommandCreatePdfThroughFile */
    public CIMCommandGeneratePDF( File inFile,
                                  String rootDirectory,
                                  String directory,
                                  String pdfOutFile)
    {
        this.inFile = inFile;
        this.rootDirectory = rootDirectory;
        this.directory = directory;
        this.pdffilename = pdfOutFile;
    }

    public String getCommandName() {
        return GENERATE_PDF;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getDirectory() {
        return directory;
    }

    public String getPdfOutputFile() {
        return pdffilename;
    }
    public File getInputFile() {
        return inFile;
    }
}
