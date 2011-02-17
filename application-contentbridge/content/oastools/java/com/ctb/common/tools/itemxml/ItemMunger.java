package com.ctb.common.tools.itemxml;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * an <code>ItemProcessor</code> that performs text replacements on the item's xml
 */
public class ItemMunger extends StringItemProcessor {

    private static final String REMOVE_SEQUENCE = "@@remove@@";

    private Map substitutions = new HashMap();
    private PrintWriter writer;

    /**
     * Constructor
     * @param logDir the directory to which a log containing a list of processed items will be written
     */
    public ItemMunger(String logDir) {
        if (logDir == null || logDir.trim().length() == 0) {
            createWriter(".");
        } else {
            createWriter(logDir);
        }
    }

    /**
     * add a text substitution
     * @param originalText the search string
     * @param newText the replacement string
     */
    public void addSubstitution(String originalText, String newText) {
        substitutions.put(originalText, newText);
    }

    public void setSubstitutions(Map substitutions) {
        this.substitutions = substitutions;
    }

    /**
     * perform all defined substitutions, write itemId to log file
     */
    public String processItemAsString(String itemId, String xmlCLOB) {
        String processedCLOB = xmlCLOB;

        for (Iterator iterator = substitutions.keySet().iterator(); iterator.hasNext();) {
            String oldText = (String) iterator.next();
            String newText = (String) substitutions.get(oldText);

            if (newText.equals(REMOVE_SEQUENCE)) {
                newText = "";
            }
            processedCLOB = StringUtils.replace(processedCLOB, oldText, newText);
        }
        writer.println(itemId);
        writer.flush();
        return processedCLOB;
    }

    /**
     * @return true if xmlCLOB contains any text defined in my substitutions
     */
    public boolean doProcessItemAsString(String itemId, String xmlCLOB) {
        for (Iterator iterator = substitutions.keySet().iterator(); iterator.hasNext();) {
            String oldText = (String) iterator.next();

            if (xmlCLOB.indexOf(oldText) > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * write start time and substition defs to log file
     */
    public void startProcessingItems() {
        writer.println("start processing at "
                + DateFormat.getDateTimeInstance().format(new Date()));
        writer.println("substitutions:  " + substitutions);
        writer.println("\n\n");
    }

    /**
     * flush log file
     */
    public void stopProcessingItems(boolean success) {
        writer.flush();
        writer.close();
    }

    private void createWriter(String logDir) {
        if (!new File(logDir).exists()) {
            new File(logDir).mkdir();
        }
        try {
            writer = new PrintWriter(new FileWriter(new File(logDir,
                    "ItemMunger-" + System.currentTimeMillis() + ".log")));
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unable to construct print writer for logging");
        }
    }
}
