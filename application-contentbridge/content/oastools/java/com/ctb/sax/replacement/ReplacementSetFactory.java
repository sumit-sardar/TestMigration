package com.ctb.sax.replacement;


import java.io.*;
import java.util.*;


public class ReplacementSetFactory {

    static public ReplacementSet create(File file) {

        String key = file.getName();
        InputStream is = null;

        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException fnf) {

            throw new MissingResourceException(fnf.getMessage(), null, null);
        }

        String extension = key.substring(key.lastIndexOf(".") + 1);

        return new XMLReplacementSet(is);

    }

    static public ReplacementSet create(String key) {
        return new XMLReplacementSet(key);
    }

    static public ReplacementSet create(InputStream is) {
        return new XMLReplacementSet(is);
    }

}
