/*
 * CommandProcessorPdfThroughFile.java
 *
 * Created on March 10, 2004, 11:39 AM
 */

package com.ctb.cprocessor;

import org.jdom.Element;

import org.apache.fop.apps.Driver;
import com.ctb.reporting.*;

import com.ctb.common.tools.SystemException;


import com.ctb.media.pdf.XMLToFOTransformer;
//import com.ctb.media.pdf.XMLToPDFRenderer;

import com.ctb.media.pdf.PSToPDFPostProcessor;

import com.ctb.util.iknowxml.R2XmlOutputter;

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
            pdfToFileReport.setException(new SystemException(e.getMessage(),e));
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
