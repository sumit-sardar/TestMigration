package com.ctb.contentBridge.core.publish.tools;


import java.io.*;

import org.apache.fop.image.FopImageFactory;
import org.jdom.Element;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.iknowxml.R2XmlOutputter;
import com.ctb.contentBridge.core.publish.sax.util.XSLDocType;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 11:25:10 AM
 * To change this template use Options | File Templates.
 */

public class DefaultPDFGenerator implements PDFGenerator {

    XSLDocType docType = XSLDocType.TRANSPARENT_TRANSFORM;


    public DefaultPDFGenerator() {
        FopImageFactory.resetCache();
    }

    public DefaultPDFGenerator(XSLDocType docType) {
        FopImageFactory.resetCache();
        this.docType = docType;
    }

    public byte[] generatePDF(InputStream is) {
        return generatePDF(is,this.docType);
    }

    public byte[] generatePDF(InputStream xmlIs, XSLDocType docType) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
  /*          XMLToPDFRenderer renderer =
                    new XMLToPDFRenderer(new XMLToFOTransformer(
                            docType),Driver.RENDER_PS,
                            new PSToPDFPostProcessor());
            renderer.process(xmlIs,bos);*/
            bos.close();
        } catch (IOException e) {
            throw new SystemException("Could not read input stream ");
        }
        return bos.toByteArray();
    }

    public byte[] generatePDF(Element element, XSLDocType docType) {
        byte[] bytes = xmlToByteArray(element);

        return generatePDF(new ByteArrayInputStream(bytes),docType);
    }

    public byte[] generatePDF(Element element) {
        byte[] bytes = xmlToByteArray(element);

        return generatePDF(new ByteArrayInputStream(bytes));
    }


    private byte[] xmlToByteArray(Element ele) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            Writer writer = new OutputStreamWriter(os, "UTF-8");
            R2XmlOutputter outputter = new R2XmlOutputter();

            outputter.output(ele, writer);
            writer.close();
        } catch (IOException e) {
            throw new SystemException("Could not write document from DocumentHolder to OutputStream: "
                    + e.getMessage());
        }
        return os.toByteArray();
    }
}
