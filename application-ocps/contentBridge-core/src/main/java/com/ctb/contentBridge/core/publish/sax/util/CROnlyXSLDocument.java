package com.ctb.contentBridge.core.publish.sax.util;


import java.io.*;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.tools.IOUtils;




/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class CROnlyXSLDocument extends XSLDocType {

    private static CROnlyXSLDocument instance = null;
    public static final String KEY_NAME = "CR_ONLY";

    private CROnlyXSLDocument(byte[] bytes) {
        super(KEY_NAME, bytes);
    }

    public static CROnlyXSLDocument instance() {

        if (instance != null) {
            return instance;
        }
        try {
			loadInstance();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return instance;
    }

    private static synchronized void loadInstance() throws SystemException {
        try {
            File xslFile = new File(CR_ONLY);
            byte[] bytes = IOUtils.loadBytes(xslFile);

            instance = new CROnlyXSLDocument(bytes);
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
            throw new SystemException("Could not load XSL-T for CR Items only");
        }
    }
}
