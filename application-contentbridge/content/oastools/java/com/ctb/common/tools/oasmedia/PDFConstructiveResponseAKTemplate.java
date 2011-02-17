package com.ctb.common.tools.oasmedia;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class PDFConstructiveResponseAKTemplate extends MediaTemplate {
	
    private static PDFConstructiveResponseAKTemplate instance = null;
    public static final String KEY_NAME = "PDFConstructiveResponseAKTemplate";
    public static final String TEMPLATE_FILE_REFERENCE = "etc/FOP_Interface_CRAKPDF.xsl";

    private PDFConstructiveResponseAKTemplate(byte[] bytes) {
        super(KEY_NAME, bytes);
    }
    private PDFConstructiveResponseAKTemplate(String fileNameReference) {
        super(KEY_NAME, fileNameReference);
    }

    public static synchronized MediaTemplate instance() {
        if (instance != null) {
            return instance;
        }
        instance = new PDFConstructiveResponseAKTemplate(TEMPLATE_FILE_REFERENCE);
        return instance;
    }
}
