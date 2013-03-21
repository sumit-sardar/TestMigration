/*
 * CommandProcessorPdfThroughFile.java
 *
 * Created on March 10, 2004, 11:39 AM
 */

package com.ctb.contentBridge.core.publish.cprocessor;

import org.jdom.Element;

import org.apache.fop.apps.Driver;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlOutputter;
import com.ctb.contentBridge.core.publish.media.pdf.XMLToFOTransformer;
import com.ctb.contentBridge.core.publish.report.PdfToFileReport;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.report.ReportFactory;

import java.io.*;
/**
 *
 * @author  Wen-Jin_Chang
 */
public class CommandProcessorPdfThroughFile implements CommandProcessor {
    
    private Element rootElement;
    private File xslFile;
    private String outFileName;
    private String pdfType;
    private String processStep;

    /** Creates a new instance of CommandProcessorPdfThroughFile */
    public CommandProcessorPdfThroughFile( Element rootElement, File xslFile,
                                                String outFileName,
                                                String pdfType, String processStep)
    {
        this.rootElement = rootElement;
        this.xslFile = xslFile;
        this.outFileName = outFileName;
        this.pdfType = pdfType;
        this.processStep = processStep;
    }
    
    public Report process() 
    {
        PdfToFileReport pdfToFileReport = ReportFactory.createPdfToFileReport();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        R2XmlOutputter outputter = new R2XmlOutputter();
        try {
            outputter.output(rootElement,bos);
            InputStream is = new ByteArrayInputStream(bos.toByteArray());
            if (processStep.equals("fo"))
                createFOOutput(is);
            if (processStep.equals("ps"))
                createPSOutput(is);
            if (processStep.equals("full"))
                createPDFOutput(is);
        } catch (IOException e) {
            pdfToFileReport.setException(new SystemException(e.getMessage()));
            pdfToFileReport.setSuccess(false);
        }
        return pdfToFileReport;
    }

    private void createPDFOutput(InputStream is) throws IOException {
        OutputStream os = new FileOutputStream(new File(outFileName));
 /*       XMLToPDFRenderer renderer = new XMLToPDFRenderer(new XMLToFOTransformer(xslFile),Driver.RENDER_PS, new PSToPDFPostProcessor());
        renderer.process(is,os);*/
        os.close();
    }


    private void createPSOutput(InputStream is) throws IOException {
        OutputStream os = new FileOutputStream(new File(outFileName));
       /* XMLToPDFRenderer renderer = new XMLToPDFRenderer(new XMLToFOTransformer(xslFile),Driver.RENDER_PS);
        renderer.process(is,os); */
        os.close();
    }

    private void createFOOutput(InputStream is) throws IOException {
        OutputStream os = new FileOutputStream(new File(outFileName));
        XMLToFOTransformer xml2fo = new XMLToFOTransformer(xslFile);
        xml2fo.process(is,os);
        os.close();
    }


}
