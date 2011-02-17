package com.ctb.common.tools.itemxml;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import oracle.sql.CLOB;

import com.ctb.common.tools.SystemException;
import com.ctb.util.Pipe;


public class JobBuilder implements com.ctb.common.tools.itemxml.ItemProcessor {

    private String jobId;
    private OutputStream jobOutStream;
    private PrintWriter log;

    public JobBuilder(String jobId) {
        this.jobId = jobId;
        try {
            log = new PrintWriter(new FileWriter(jobId + ".log"));
            jobOutStream = new FileOutputStream(jobId + ".xml");
        } catch (IOException ioEx) {
            throw new SystemException("unable to create output stream", ioEx);
        }
    }

    public String processItem(String itemId, CLOB xmlCLOB) {
        log.println(itemId);
        try {
            new Pipe(xmlCLOB.binaryStreamValue(), jobOutStream).run();
        } catch (SQLException sqlEx) {
            throw new SystemException("unexpected error reading CLOB", sqlEx);
        }
        return "";
    }

    public boolean doProcessItem(String itemId, CLOB xmlCLOB) {
        return true;
    }

    /**
     * invoked by <code>ItemsProcessor</code> prior
     * to processing any items.
     */
    public void startProcessingItems() {
        try {
            log.write("items included in " + jobId + ".xml\n");
            jobOutStream.write(("<ItemSet ID=\"" + jobId + "\">").getBytes());
        } catch (IOException ioEx) {
            throw new SystemException("unexpected error closing job file");
        }
    }

    /**
     * invoked by <code>ItemsProcessor</code> after
     * completion of item processing
     * @param success indicator of whether <code>ItemsProcessor</code>
     * completed item processing without encountering any unexpected exceptions.
     */
    public void stopProcessingItems(boolean success) {
        try {
            log.flush();
            log.close();
            jobOutStream.write("</ItemSet>".getBytes());
            jobOutStream.flush();
            jobOutStream.close();
        } catch (IOException ioEx) {
            throw new SystemException("unexpected error closing job file");
        }
    }
}
