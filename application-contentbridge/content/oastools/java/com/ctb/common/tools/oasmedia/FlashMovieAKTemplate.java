package com.ctb.common.tools.oasmedia;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 3:29:18 PM
 * To change this template use Options | File Templates.
 */
public class FlashMovieAKTemplate extends MediaTemplate {
	

    private static FlashMovieAKTemplate instance = null;
    public static final String KEY_NAME = "FlashMovieAKTemplate";
    public static final String TEMPLATE_FILE_REFERENCE = "etc/cab_ak.swt";

    private FlashMovieAKTemplate(byte[] bytes) {
        super(KEY_NAME, bytes);
    }
    private FlashMovieAKTemplate(String fileNameReference) {
        super(KEY_NAME, fileNameReference);
    }

    public static synchronized MediaTemplate instance() {
        if (instance != null) {
            return instance;
        }
        instance = new FlashMovieAKTemplate(TEMPLATE_FILE_REFERENCE);
        return instance;
    }
}
