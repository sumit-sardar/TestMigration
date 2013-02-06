package com.ctb.contentBridge.core.publish.sax.util;


import java.io.*;
import java.util.*;

import org.jdom.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.tools.IOUtils;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:16:25 PM
 * To change this template use Options | File Templates.
 */
public class XSLDocType {


    public static final String CR_ONLY = "etc/FOP_Interface_CRIBPDF.xsl";
    public static final String RUBRIC_ONLY = "etc/FOP_Interface_CRAKPDF.xsl";
    public static final String ANSWERKEY_STYLESHEET = "etc/FOP_Interface_AK.xsl";
    public static final String PDF_MEDIA = "etc/FOP_Interface.xsl";

    private static final ArrayList docList = new ArrayList();
    public static final XSLDocType SELECTIVE_RESPONSE_TRANSFORM = SelectiveResponseXSLDocument.instance();
    public static final XSLDocType ANSWERKEY_TRANSFORM = AnswerKeyXSLDocument.instance();
    public static final XSLDocType RUBRIC_ONLY_TRANSFORM = RubricOnlyXSLDocument.instance();
    public static final XSLDocType CR_ONLY_TRANSFORM = CROnlyXSLDocument.instance();
    public static final XSLDocType TRANSPARENT_TRANSFORM = TransparentXSLDocument.instance();

    static {
        docList.add(SELECTIVE_RESPONSE_TRANSFORM);
        docList.add(ANSWERKEY_TRANSFORM);
        docList.add(TRANSPARENT_TRANSFORM);
        docList.add(RUBRIC_ONLY_TRANSFORM);
        docList.add(CR_ONLY_TRANSFORM);
    }

    private String keyName;
    private byte[] xslDocumentBytes = null;


    protected XSLDocType(String keyName, byte[] xslDocumentBytes) {
        this.keyName = keyName;
        this.xslDocumentBytes = xslDocumentBytes;
    }

    public String toString() {
        return keyName;
    }

    public InputStream getDocumentStream() {
        return new ByteArrayInputStream(xslDocumentBytes);
    }

    public InputStreamReader getDocumentStreamReader() {
        return new InputStreamReader(new ByteArrayInputStream(xslDocumentBytes));
    }

    public byte[] getDocumentBytes() {
        return xslDocumentBytes;
    }

    public Document getDocument() throws BusinessException {
        try {
            return IOUtils.loadXmlDocument(getDocumentStream(), false);
        } catch (JDOMException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
            throw new BusinessException("Could not load XSL document with JDOM for "
                    + this);
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
            throw new BusinessException("Could not load XSL document with JDOM for "
                    + this);
        }
    }

    public static XSLDocType[] getDocTypes() {
        return (XSLDocType[]) docList.toArray(new XSLDocType[docList.size()]);
    }

}
