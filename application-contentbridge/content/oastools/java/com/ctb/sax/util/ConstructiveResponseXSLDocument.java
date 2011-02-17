package com.ctb.sax.util;


import java.io.*;

import com.ctb.common.tools.*;
import com.ctb.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class ConstructiveResponseXSLDocument extends XSLDocType {

    private static ConstructiveResponseXSLDocument instance = null;
    public static final String KEY_NAME = "CONSTRUCTIVE";

    private ConstructiveResponseXSLDocument(byte[] bytes) {
        super(KEY_NAME, bytes);
    }

    public static ConstructiveResponseXSLDocument instance() {

        if (instance != null) {
            return instance;
        }
        loadInstance();
        return instance;
    }

    private static synchronized void loadInstance() {
        try {
            File xslFile = new File(ParseFilterProperties.instance().getXSLforCRToPDF());
            byte[] bytes = IOUtils.loadBytes(xslFile);

            instance = new ConstructiveResponseXSLDocument(bytes);
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
            throw new SystemException("Could not load XSL-T for Constructive Response Items",
                    e);
        }
    }
}
