package com.ctb.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.ctb.common.tools.SystemException;

abstract public class Loader {

    protected void load(File file) {
        FileReader in;
        try {
            in = new FileReader(file);
            load(in);
            in.close();
        } catch (IOException e) {
            throw new SystemException(
                "Could not read file [" + file.getPath() + "]:" + e.toString(),
                e);
        }
    }

    protected void load(Reader in) {
        String line;
        BufferedReader lines = new BufferedReader(in);

        try {
            while ((line = lines.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                }
                addLine(line);
            }
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    abstract protected void addLine(String line);

}
