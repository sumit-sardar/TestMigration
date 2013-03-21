package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.ctb.contentBridge.core.exception.SystemException;

public class MappingWriterImpl implements MappingWriter {
    BufferedWriter writer;

    public MappingWriterImpl(File outputFile) {
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }

    }

    public MappingWriterImpl(String fileName) {
        this(new File(fileName));
    }

    public void writeLine(String line) {
        try {
            writer.write(line);
            writer.write("\n");
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }

    }

    public void close() {
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                throw new SystemException(e.getMessage());
            }

        }
    }

}
