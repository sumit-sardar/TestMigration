package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.mapping.MapperFactory;
import com.ctb.contentBridge.core.publish.tools.IOUtils;

public class MappingWriterMerge implements MappingWriter {
    private File outputFile;
    private File tempFile;
    private BufferedWriter writer;

    public MappingWriterMerge(File outputFile) {
        try {
            this.outputFile = outputFile;

        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }

        try {
            tempFile = File.createTempFile("item_map", ".txt");
            tempFile.deleteOnExit();

            writer = new BufferedWriter(new FileWriter(tempFile));
        } catch (Exception e) {
            throw new SystemException("Cannot create temp file for copy mapping.");
        }

    }

    public MappingWriterMerge(String fileName) {
        this(new File(fileName));
    }

    public void writeLine(String line) {
        try {
            writer.write(line);
            writer.write("\n");
            writer.flush();

        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    public void close() {
        //        try {
        // close the temp file
        try {
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
			throw new SystemException(ioe.getMessage());
        }

		verifyItemMapLoading(tempFile);

        IOUtils.copyFile(tempFile, outputFile);

		verifyItemMapLoading(outputFile);
    }

    private void verifyItemMapLoading(File itemMapFile) {
        try {
            MapperFactory.loadItemMapFromFile(itemMapFile);
        } catch (Exception e) {
            throw new SystemException(
                "Cannot load new mapping from: " + itemMapFile.getPath());
        }
    }
}
