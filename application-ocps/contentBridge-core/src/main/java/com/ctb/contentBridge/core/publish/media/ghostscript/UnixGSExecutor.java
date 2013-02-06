package com.ctb.contentBridge.core.publish.media.ghostscript;


import java.io.File;

import com.ctb.contentBridge.core.exception.BusinessException;
import com.ctb.contentBridge.core.publish.executable.AbstractExecutor;
import com.ctb.contentBridge.core.publish.executable.ExecutionInfo;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 10:10:55 AM
 * 
 *
 */
public class UnixGSExecutor extends AbstractExecutor implements GSExecutor {

        public static String PS2PDF_UNIX = "/usr/local/bin/ps2pdf";

    public void execute(File psFile, File pdfFile) throws BusinessException {
        exec(PS2PDF_UNIX + " " + psFile.getPath() + " " + pdfFile.getPath());
    }

    protected void checkForErrors(ExecutionInfo info) {
        super.checkForErrors(info);
        if ((!info.getErrors().equals("")) || (!info.getOutput().equals("")))
            throw new BusinessException(info.toString());
    }
}
