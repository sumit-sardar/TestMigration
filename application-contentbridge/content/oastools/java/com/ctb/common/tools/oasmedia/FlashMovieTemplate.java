package com.ctb.common.tools.oasmedia;

/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class FlashMovieTemplate extends MediaTemplate {
	

    private static FlashMovieTemplate instance = null;
    public static final String KEY_NAME = "FlashMovieTemplate";
    public static final String TEMPLATE_FILE_REFERENCE = "etc/cab_ib.swt";

    private FlashMovieTemplate(byte[] bytes) {
        super(KEY_NAME, bytes);
    }
    private FlashMovieTemplate(String fileNameReference) {
        super(KEY_NAME, fileNameReference);
    }

    public static synchronized MediaTemplate instance() {
        if (instance != null) {
            return instance;
        }
        instance = new FlashMovieTemplate(TEMPLATE_FILE_REFERENCE);
        return instance;
    }
}
