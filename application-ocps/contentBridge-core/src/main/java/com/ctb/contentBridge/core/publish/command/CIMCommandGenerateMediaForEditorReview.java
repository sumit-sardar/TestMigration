package com.ctb.contentBridge.core.publish.command;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Mar 28, 2004
 * Time: 6:29:08 PM
 * To change this template use Options | File Templates.
 */
public class CIMCommandGenerateMediaForEditorReview implements CIMCommand {

    private File inFile;
    private String rootDirectory;
    private String directory;
    private String pdffilename;
    private String flashfilename;
    /** Creates a new instance of CIMCommandCreatePdfThroughFile */
    public CIMCommandGenerateMediaForEditorReview(
                                    File inFile,
                                    String rootDirectory,
                                    String directory,
                                    String pdfOutFile,
                                    String flashOutFile )
    {
        this.inFile = inFile;
        this.rootDirectory = rootDirectory;
        this.directory = directory;
        this.pdffilename = pdfOutFile;
        this.flashfilename = flashOutFile;
    }

    public String getCommandName() {
        return GENERATE_MEDIA_FOR_EDITOR_REVIEW;
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

    public String getFlashOutfile() {
        return flashfilename;
    }
}

