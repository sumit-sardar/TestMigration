package com.ctb.common.tools.itemxml;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;





/**
 * attempt to perform default JDOM Document building
 * on all item XML CLOBs.  print report to file of all errors.
 * this is effectively a report on which test items will
 * cause the 'hanging test' bug.
 */
public class OASJdomParseReport extends StringItemProcessor {

    private PrintWriter writer;

    /**
     * Constructor
     * @param logDir directory in which log file will be written
     */
    public OASJdomParseReport(String logDir) {
        if (logDir == null || logDir.trim().length() == 0) {
            createWriter(".");
        } else {
            createWriter(logDir);
        }
    }

    /**
     * attempt to build into a Document with DOMBuilder.  this replicates
     * OAS test publicaction activity in the TestXMLMessageBean -- if this
     * fails the error in OAS is currently swallowed -- thus the 'hanging test' bug.
     */
    public String processItemAsString(String itemId, String xmlCLOB) {
        try {
            // oas uses deprecated DOMBuilder so we do too ...
            org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
            saxBuilder.build( new ByteArrayInputStream(xmlCLOB.getBytes()) );
   //         new DOMBuilder().build(new ByteArrayInputStream(xmlCLOB.getBytes()));
        } catch (Exception jdomEx) {
            writer.println(itemId + "\t" + jdomEx.getMessage());
        }
        return xmlCLOB;
    }

    /** @return true always -- process all items */
    public boolean doProcessItemAsString(String itemId, String xmlCLOB) {
        return true;
    }

    /** timestamp log file */
    public void startProcessingItems() {
        writer.println("start processing at "
                + DateFormat.getDateTimeInstance().format(new Date()));
        writer.println("\n\n");
    }

    /** flush log file */
    public void stopProcessingItems(boolean success) {
        try {
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("unable to close log file");
        }
    }

    private void createWriter(String logDir) {
        if (!new File(logDir).exists()) {
            new File(logDir).mkdir();
        }
        try {
            writer = new PrintWriter(new FileWriter(new File(logDir,
                    "OASJdomParseReport-" + System.currentTimeMillis() + ".log")));
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unable to construct print writer for logging");
        }
    }
}
