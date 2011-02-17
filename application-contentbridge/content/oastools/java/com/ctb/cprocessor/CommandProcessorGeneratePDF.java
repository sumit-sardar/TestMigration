package com.ctb.cprocessor;



import com.ctb.reporting.PdfToFileReport;
import com.ctb.reporting.Report;
import com.ctb.reporting.ReportFactory;






import java.io.File;



/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: Apr 30, 2004
 */
public class CommandProcessorGeneratePDF implements CommandProcessor {

    String directoryName;
    String pdfName;
    File xmlDoc;
    File directory;
    File rootDirectory;

    public CommandProcessorGeneratePDF(File xmlDoc, String rootDirectory, String directoryName, String pdfName) {
        this.xmlDoc = xmlDoc;
        this.rootDirectory = new File(rootDirectory);
        this.directory = new File(rootDirectory, directoryName);
        this.pdfName = pdfName;
    }

    public Report process() {
        PdfToFileReport pdfToFileReport = ReportFactory.createPdfToFileReport();
        return pdfToFileReport;
    }


}
