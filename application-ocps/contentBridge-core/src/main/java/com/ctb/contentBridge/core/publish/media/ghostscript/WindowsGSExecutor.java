package com.ctb.contentBridge.core.publish.media.ghostscript;

import java.io.File;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.executable.AbstractExecutor;
import com.ctb.contentBridge.core.publish.executable.ExecutionInfo;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 10:10:55 AM
 * 
 *
 */
public class WindowsGSExecutor extends AbstractExecutor implements GSExecutor {

    public static String PS2PDF_WIN = "gswin32c.exe -q -dNOPAUSE -sDEVICE=pdfwrite -dBATCH -sOutputFile=";

    public void execute(File psFile, File pdfFile) {
        exec(PS2PDF_WIN + pdfFile.getPath() + " " + psFile.getPath());
    }

    protected void checkForErrors(ExecutionInfo info) {
        super.checkForErrors(info);
        if ((!info.getErrors().equals("")) || (!info.getOutput().equals("")))
            throw new SystemException(info.toString());
    }
}
