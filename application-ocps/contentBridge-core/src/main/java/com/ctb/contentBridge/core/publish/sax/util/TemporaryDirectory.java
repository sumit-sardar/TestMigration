package com.ctb.contentBridge.core.publish.sax.util;

import java.io.File;
import java.io.IOException;

import com.ctb.contentBridge.core.exception.SystemException;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 8:52:40 AM
 * 
 *
 */
public class TemporaryDirectory {

    public static File createTempDir(File parent, String prefix) throws SystemException {

        if (!parent.exists()) {
            parent.mkdir();
        }
        try {
            File tempFilePath = File.createTempFile(prefix, "", parent);

            String dirName = tempFilePath.getAbsolutePath();

            tempFilePath.delete();
            File tempDir = new File(dirName);

            if (!tempDir.mkdirs()) {
                throw new SystemException("Failed to create temp directory:"
                        + dirName);
            }
            return tempFilePath;
        } catch (IOException e) {
            throw new SystemException("Failed to create temp directory:"
                    + e.getMessage());
        }
    }

    public static void delete(File dir) {
        if (dir != null) {
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            dir.delete();
        }
    }
}
