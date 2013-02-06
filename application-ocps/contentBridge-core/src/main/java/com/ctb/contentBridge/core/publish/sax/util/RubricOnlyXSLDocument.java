package com.ctb.contentBridge.core.publish.sax.util;


import java.io.*;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.tools.IOUtils;




/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class RubricOnlyXSLDocument extends XSLDocType {

    private static RubricOnlyXSLDocument instance = null;
    public static final String KEY_NAME = "RUBRIC_ONLY";

    private RubricOnlyXSLDocument(byte[] bytes) {
        super(KEY_NAME, bytes);
    }

    public static RubricOnlyXSLDocument instance() {

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
            File xslFile = new File(RUBRIC_ONLY);
            byte[] bytes = IOUtils.loadBytes(xslFile);

            instance = new RubricOnlyXSLDocument(bytes);
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use Options | File Templates.
            throw new BusinessException("Could not load XSL-T for Rubric");
        }
    }
}
