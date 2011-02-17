package com.ctb.common.tools.oasmedia;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class PDFAnswerKeyTemplate extends MediaTemplate {

    private static PDFAnswerKeyTemplate instance = null;
    public static final String KEY_NAME = "PDFAnswerKeyTemplate";
    public static final String TEMPLATE_FILE_REFERENCE = "etc/FOP_Interface_AK.xsl";

    private PDFAnswerKeyTemplate(byte[] bytes) {
        super(KEY_NAME, bytes);
    }
    private PDFAnswerKeyTemplate(String fileNameReference) {
        super(KEY_NAME, fileNameReference);
    }

    public static synchronized MediaTemplate instance() {
        if (instance != null) {
            return instance;
        }
        instance = new PDFAnswerKeyTemplate(TEMPLATE_FILE_REFERENCE);
        return instance;
    }
}
