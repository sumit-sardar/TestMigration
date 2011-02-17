package com.ctb.media.ghostscript;

import java.io.File;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 10:12:25 AM
 * 
 *
 */
public interface GSExecutor {

    public void execute(File psFile, File pdfFile);
}
