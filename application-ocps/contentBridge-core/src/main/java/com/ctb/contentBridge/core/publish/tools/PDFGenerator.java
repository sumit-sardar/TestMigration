package com.ctb.contentBridge.core.publish.tools;


import java.io.*;

import org.jdom.*;

import com.ctb.contentBridge.core.publish.sax.util.XSLDocType;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 11:24:21 AM
 * To change this template use Options | File Templates.
 */
public interface PDFGenerator {

    public byte[] generatePDF(InputStream xmlIs);
    public byte[] generatePDF(Element element);
    public byte[] generatePDF(Element element,XSLDocType docType);
    public byte[] generatePDF(InputStream xmlIs,XSLDocType docType);

}
