package com.ctb.common.tools;


import java.io.*;




import org.apache.fop.apps.*;
import org.apache.fop.image.FopImageFactory;
import org.jdom.*;

import com.ctb.sax.util.*;
import com.ctb.util.iknowxml.*;
//import com.ctb.media.pdf.XMLToPDFRenderer;
import com.ctb.media.pdf.XMLToFOTransformer;
import com.ctb.media.pdf.PSToPDFPostProcessor;


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
            throw new SystemException("Could not read input stream ", e);
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
                    + e.getMessage(),
                    e);
        }
        return os.toByteArray();
    }
}
