package com.ctb.sax.replacement;


import java.io.File;
import java.io.InputStream;

import com.ctb.common.tools.XMLResource;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Aug 29, 2003
 * Time: 11:40:01 AM
 * To change this template use Options | File Templates.
 */
public class FrameworkReplacementConfig extends XMLResource {

    public static String FRAMEWORK_CONFIG_XML = "conf/badFrameworks.xml";
    static FrameworkReplacementConfig instance = null;

    /**
     * Preferred method of getting the config instance uses the singleton
     * @return
     */
    static public FrameworkReplacementConfig instance() {
        if (instance != null) {
            return instance;
        }
        instance = new FrameworkReplacementConfig();
        return instance;
    }

    public FrameworkReplacementConfig() {
        synchronized (this) {
            load(FRAMEWORK_CONFIG_XML);
        }
    }

    public FrameworkReplacementConfig(String fileName) {
        load(fileName);
    }

    public FrameworkReplacementConfig(InputStream is) {
        load(is);
    }

    public FrameworkReplacementConfig(File file) {
        load(file);
    }

}
