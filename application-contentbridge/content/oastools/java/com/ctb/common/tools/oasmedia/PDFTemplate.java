package com.ctb.common.tools.oasmedia;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class PDFTemplate extends MediaTemplate {
	

    private static PDFTemplate instance = null;
    public static final String KEY_NAME = "PDFTemplate";
    public static final String TEMPLATE_FILE_REFERENCE = "etc/FOP_Interface.xsl";

    private PDFTemplate(byte[] bytes) {
        super(KEY_NAME, bytes);
    }
    private PDFTemplate(String fileNameReference) {
        super(KEY_NAME, fileNameReference);
    }

    public static synchronized MediaTemplate instance() {
        if (instance != null) {
            return instance;
        }
        instance = new PDFTemplate(TEMPLATE_FILE_REFERENCE);
        return instance;
    }
}
