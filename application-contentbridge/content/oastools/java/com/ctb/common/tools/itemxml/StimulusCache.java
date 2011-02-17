/*
 * Created on Dec 9, 2003
 *
 */
package com.ctb.common.tools.itemxml;

import java.io.File;
import java.io.IOException;

import com.ctb.common.tools.DBConfig;
import com.ctb.common.tools.IOUtils;

public class StimulusCache {

    private File baseDir = new File("StimuliCache");

    public StimulusCache(String environment) {
        if (!(baseDir.exists() && (baseDir.isDirectory()))) {
            baseDir.mkdir();
        }
        this.baseDir =
            new File(
                this.baseDir.getPath()
                    + File.separator
                    + new DBConfig(new File(environment)).getSid());
        if (!(baseDir.exists() && (baseDir.isDirectory()))) {
            baseDir.mkdir();
        }
    }

    public char[] get(String key) {
        try {
            File stimulusFile = new File(baseDir, getHashCode(key));
            return IOUtils.loadChars(stimulusFile);
        } catch (Exception e) {
            return null;
        }
    }

    public void put(String key, char[] value) {
        File stimulusFile = new File(baseDir, getHashCode(key));
        try {
            IOUtils.writeFile(stimulusFile, value);
        } catch (IOException e) {
        }
    }

    private static String getHashCode(String string) {
        return ("S" + string.hashCode());
    }

}
