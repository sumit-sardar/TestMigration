/*
 * Created on Sep 1, 2004
 *
 */
package com.ctb.oas.normsdata.parser;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @author arathore
 */
public class GenericNormDataReader {

    protected LineNumberReader reader;
    protected File inputFile;

    protected String line() {
        try {
            return reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
