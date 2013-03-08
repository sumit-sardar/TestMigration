package com.ctb.contentBridge.core.publish.media.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.sax.util.XSLDocType;

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

    public void process(InputStream is, OutputStream os) throws SystemException {
        Result result = new StreamResult(os);
        process(is,result);
    }
    public void process(InputStream xmlIs, InputStream xslIs, OutputStream os) throws SystemException {
        Result result = new StreamResult(os);
        process(xmlIs,xslIs,result);
    }
    public void process(InputStream xmlIs, Result result) throws SystemException {
        process(xmlIs,getDocumentStream(),result);
    }
    public void process(InputStream xmlIs, InputStream xslIs, Result result) throws SystemException {
        try {
            TransformerFactory tFactory =
                              javax.xml.transform.TransformerFactory.newInstance();

            Transformer transformer = tFactory.newTransformer
                            (new javax.xml.transform.stream.StreamSource(xslIs));
            StreamSource xmlSource = new StreamSource(xmlIs);
            xmlSource.setSystemId(systemID);
            transformer.transform(xmlSource,result);
        } catch (TransformerFactoryConfigurationError e) {
            throw new SystemException(e.getMessage());
        } catch (TransformerException e) {
            throw new SystemException(e.getMessage());
        }
    }

    private InputStream getDocumentStream() throws SystemException {
        if (docType != null)
            return docType.getDocumentStream();
        try {
            return new FileInputStream(xslFile);
        } catch (FileNotFoundException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
