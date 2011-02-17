package com.ctb.cprocessor;








import com.ctb.reporting.PdfToFileReport;
import com.ctb.reporting.ReportFactory;
import com.ctb.reporting.Report;



import com.ctb.cprocessor.CommandProcessor;

import java.io.*;



/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Mar 25, 2004
 * Time: 11:01:47 AM
 * To change this template use Options | File Templates.
 */
public class CommandProcessorGenerateMediaForEditorReview implements CommandProcessor{

    String directoryName;
    String pdfName;
    String flashName;
    File xmlDoc;
    File directory;
    File rootDirectory;

    public CommandProcessorGenerateMediaForEditorReview(File xmlDoc, String rootDirectory, String directoryName, String pdfName, String flashName) {
        this.xmlDoc = xmlDoc;
        this.rootDirectory = new File(rootDirectory);
        this.directory = new File(rootDirectory, directoryName);
        this.pdfName = pdfName;
        this.flashName = flashName;
    }

    public Report process() {
        PdfToFileReport pdfToFileReport = ReportFactory.createPdfToFileReport();
        return pdfToFileReport;
    }

 }








