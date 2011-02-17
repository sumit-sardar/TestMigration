package com.ctb.common.tools;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class MappingConfig extends Config {

    private String mappingDir;
    private String[] frameworkCodes;

    public MappingConfig() throws IOException {
        this(userPropertiesFile);
    }

    public MappingConfig(File file) {
        load(file);
    }

    public MappingConfig(String envFileName) {
        load(new File(envFileName));
    }

    protected void readProperties() {
        mappingDir = getRequiredProperty(properties, "mapping.dir", file);
        this.frameworkCodes =
            removeDuplicates(
                StringUtils.split(
                    getRequiredProperty(properties, "mapping.list", file),
                    ","));
    }

    public static String[] removeDuplicates(String[] list) {
        Set fwkCodes = new HashSet();
        for (int i = 0; i < list.length; i++) {
            fwkCodes.add(list[i]); //removes duplicates
        }
        return list = (String[])fwkCodes.toArray(new String[fwkCodes.size()]);
    }

    public String getMappingDir() {
        return this.mappingDir;
    }

    public String[] getFrameworkCodes() {
        return frameworkCodes;
    }
}
