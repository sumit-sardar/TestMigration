package com.ctb.media.pdf;

import com.ctb.sax.util.XSLDocType;
import com.ctb.common.tools.SystemException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 7:18:32 AM
 * 
 *
 */
public class XMLToFOTransformer {

    XSLDocType docType;
    String systemID = ".";
    File xslFile;


    public XMLToFOTransformer(File xslFile) {
        this.xslFile = xslFile;
    }
    public XMLToFOTransformer(XSLDocType docType, String systemID) {
        this.docType = docType;
        this.systemID = systemID;
    }

    public XMLToFOTransformer(XSLDocType docType) {
        this.docType = docType;
    }

    public void process(InputStream is, OutputStream os) {
        Result result = new StreamResult(os);
        process(is,result);
    }
    public void process(InputStream xmlIs, InputStream xslIs, OutputStream os) {
        Result result = new StreamResult(os);
        process(xmlIs,xslIs,result);
    }
    public void process(InputStream xmlIs, Result result) {
        process(xmlIs,getDocumentStream(),result);
    }
    public void process(InputStream xmlIs, InputStream xslIs, Result result) {
        try {
            TransformerFactory tFactory =
                              javax.xml.transform.TransformerFactory.newInstance();

            Transformer transformer = tFactory.newTransformer
                            (new javax.xml.transform.stream.StreamSource(xslIs));
            StreamSource xmlSource = new StreamSource(xmlIs);
            xmlSource.setSystemId(systemID);
            transformer.transform(xmlSource,result);
        } catch (TransformerFactoryConfigurationError e) {
            throw new SystemException(e.getMessage(),e);
        } catch (TransformerException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }

    private InputStream getDocumentStream() {
        if (docType != null)
            return docType.getDocumentStream();
        try {
            return new FileInputStream(xslFile);
        } catch (FileNotFoundException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
}
