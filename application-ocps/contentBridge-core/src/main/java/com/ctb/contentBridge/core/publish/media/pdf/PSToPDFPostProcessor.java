package com.ctb.contentBridge.core.publish.media.pdf;



import java.io.InputStream;
import java.io.OutputStream;

import com.ctb.contentBridge.core.publish.media.ghostscript.GSWrapper;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 8:44:01 AM
 * 
 *
 */
public class PSToPDFPostProcessor implements PostProcessor {

    public void process(InputStream is, OutputStream os) {
        GSWrapper wrapper = new GSWrapper();
        wrapper.psToPdf(is,os);
    }

}
