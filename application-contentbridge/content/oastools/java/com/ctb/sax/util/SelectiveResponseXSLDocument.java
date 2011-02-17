package com.ctb.sax.util;


import java.io.*;

import com.ctb.common.tools.*;
import com.ctb.util.*;
import org.apache.log4j.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class SelectiveResponseXSLDocument extends XSLDocType {
	
	private static Logger logger = Logger.getLogger(SelectiveResponseXSLDocument.class);
    private static SelectiveResponseXSLDocument instance = null;
    public static final String KEY_NAME = "SELECTIVE";

    private SelectiveResponseXSLDocument(byte[] bytes) {
        super(KEY_NAME, bytes);
    }

    public static SelectiveResponseXSLDocument instance() {

        if (instance != null) {
            return instance;
        }
        loadInstance();
        return instance;
    }

    private static synchronized void loadInstance() {
        try {
            File xslFile = new File(PDF_MEDIA);

            logger.debug(ParseFilterProperties.instance().getXSLforSRToPDF());
            byte[] bytes = IOUtils.loadBytes(xslFile);

            instance = new SelectiveResponseXSLDocument(bytes);
        } catch (IOException e) {
            logger.error("Could not load XSL-T for Selective Response Items", e);
            throw new SystemException("Could not load XSL-T for questions on all Items",
                    e);
        }
    }
}
