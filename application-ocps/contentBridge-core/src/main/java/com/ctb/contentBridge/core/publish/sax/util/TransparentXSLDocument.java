package com.ctb.contentBridge.core.publish.sax.util;


import java.io.*;

import com.ctb.contentBridge.core.exception.BusinessException;



/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class TransparentXSLDocument extends XSLDocType {

    private static TransparentXSLDocument instance = null;
    public static final String KEY_NAME = "TRANSPARENT";
    private static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String XSL_STYLESHEET = "<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>";
    private static String XSL_STYLESHEET_CLOSE = "</xsl:stylesheet>";
    private static String XSL_STRING = XML_HEADER + XSL_STYLESHEET
            + XSL_STYLESHEET_CLOSE;

    private TransparentXSLDocument(byte[] bytes) {

        super(KEY_NAME, bytes);
    }

    public static TransparentXSLDocument instance() {
        if (instance != null) {
            return instance;
        }
        try {
			loadInstance();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return instance;
    }

    private static synchronized void loadInstance() throws BusinessException {
        try {
            instance = new TransparentXSLDocument(XSL_STRING.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("Could not support UTF-8 encoding");
        }
    }
}
