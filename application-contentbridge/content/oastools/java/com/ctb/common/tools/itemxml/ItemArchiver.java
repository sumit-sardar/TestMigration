package com.ctb.common.tools.itemxml;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ctb.util.Pipe;


/**
 * an <code>ItemProcessor</item> that writes the contents
 * of all item CLOBs to a zip archive.  this is a good means
 * of grabbing a set of item xml as files for unit testing.
 */
public class ItemArchiver extends StringItemProcessor {

    private ZipOutputStream zipOut;

    /**
     * Constructor
     * @param zipDir directory in which archive will be written
     */
    public ItemArchiver(String zipDir) {
        if (zipDir == null || zipDir.trim().length() == 0) {
            createZipOut(".");
        } else {
            createZipOut(zipDir);
        }
    }

    /**
     * write the xml clob to the zip archive
     * @return xmlCLOB untouched
     */
    public String processItemAsString(String itemId, String xmlCLOB) {
        try {
            String fileName = itemId + ".xml";

            zipOut.putNextEntry(new ZipEntry(fileName));
            InputStream xmlIn = new ByteArrayInputStream(xmlCLOB.getBytes());

            new Pipe(xmlIn, zipOut).run();
            xmlIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("unexpected error writing item " + itemId
                    + " to zip file");
        }
        return xmlCLOB;
    }

    /**
     * @return true always -- archive all items.
     */
    public boolean doProcessItemAsString(String itemId, String xmlCLOB) {
        return true;
    }

    /**
     *  no-op
     */
    public void startProcessingItems() {}

    public void stopProcessingItems(boolean success) {
        if (zipOut != null) {
            try {
                zipOut.flush();
                zipOut.close();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
                throw new RuntimeException("unexpected error closing zip file");
            }
        }
    }

    /**
     * create the <code>ZipOutputStream</code> to which
     * the archive will be written.
     * @param zipDir the directory in which the archive will be created.
     */
    private void createZipOut(String zipDir) {
        if (!new File(zipDir).exists()) {
            new File(zipDir).mkdir();
        }
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(new File(zipDir,
                    "ArchiveItems-" + System.currentTimeMillis() + ".zip")));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("unexpected error creating zip file for export");
        }
    }
}
