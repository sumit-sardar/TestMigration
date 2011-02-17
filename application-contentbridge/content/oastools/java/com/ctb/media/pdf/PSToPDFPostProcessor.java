package com.ctb.media.pdf;


import com.ctb.media.ghostscript.GSWrapper;
import com.ctb.media.pdf.PostProcessor;

import java.io.InputStream;
import java.io.OutputStream;

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
