package com.ctb.contentBridge.core.publish.cprocessor;

import java.io.File;

import com.ctb.contentBridge.core.publish.report.PdfToFileReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;

/**
 * Created by IntelliJ IDEA. User: JEMarley Date: Apr 30, 2004
 */
public class CommandProcessorGeneratePDF implements CommandProcessor {

	String directoryName;
	String pdfName;
	File xmlDoc;
	File directory;
	File rootDirectory;

	public CommandProcessorGeneratePDF(File xmlDoc, String rootDirectory,
			String directoryName, String pdfName) {
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
