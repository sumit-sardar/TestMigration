package com.ctb.contentBridge.core.publish.media.pdf;

import java.io.OutputStream;
import java.io.InputStream;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 8:23:52 AM
 * 
 *
 */
public interface PostProcessor {

    void process(InputStream is,OutputStream os);
}
