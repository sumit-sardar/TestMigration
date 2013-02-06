/*
 * CIMCommandCreatePdfThroughFile.java
 *
 * Created on March 10, 2004, 10:27 AM
 */

package com.ctb.contentBridge.core.publish.command;

import java.io.File;


/**
 *
 * @author  Wen-Jin_Chang
 */
public class CIMCommandCreatePdfThroughFile implements CIMCommand
{
    private File inFile;
    private File xslFile;
    private String outFile;
    private String pdfType;
    private String processStep;
    /** Creates a new instance of CIMCommandCreatePdfThroughFile */
    public CIMCommandCreatePdfThroughFile(
                                    File inFile,
                                    File xslFile,
                                    String outFile,
                                    String pdfType,
                                    String processStep)
    {
        this.inFile = inFile;
        this.xslFile = xslFile;
        this.outFile = outFile;
        this.pdfType = pdfType;
        this.processStep = processStep; //either fo, ps, or full
    }

    public String getCommandName() {
        return CREATE_PDF_THROUGH_FILE;
    }

    public String getOutputFile() {
        return outFile;
    }
    public File getInputFile() {
        return inFile;
    }

    public String getPdfType() {
        return pdfType;
    }

    public String getProcessStep() {
        return processStep;
    }

    public File getXslFile() {
        return xslFile;
    }
}
