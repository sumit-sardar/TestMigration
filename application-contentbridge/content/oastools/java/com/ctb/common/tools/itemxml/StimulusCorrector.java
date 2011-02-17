package com.ctb.common.tools.itemxml;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import oracle.sql.CLOB;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ctb.common.tools.DBConnection;
import com.ctb.util.Pipe;
import com.ctb.util.iknowxml.R2XmlOutputter;


/**
 * an <code>ItemProcessor</code> that performs text replacements on the item's xml
 */
public class StimulusCorrector implements ItemProcessor {


    private PrintWriter writer;
    private String correctStimulus;
    private String correctTitle;
    private SAXBuilder builder = new SAXBuilder();
    private Element rootItemElement;
    private Connection connection;

    public String processItem(String itemId, CLOB xmlMedia) {
        rootItemElement.getChild("Stimulus").setAttribute("ID",correctStimulus);
        XMLOutputter outputter = new R2XmlOutputter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(rootItemElement,out);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        Object[] arguments = {correctStimulus,correctTitle,itemId};
        new DBConnection(connection).executeUpdate(
            "UPDATE ITEM SET "
                + "EXT_STIMULUS_ID = '{0}', "
                + "EXT_STIMULUS_TITLE = '{1}' "
                + "WHERE ITEM_ID = '{2}'",
            arguments);
        try {
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }
    /**
     * returns true if the item has a Stimulus ID different from the correctStimulus
     * @param itemId
     * @param xmlMedia
     * @return true when Stimulus ID != correctStimulus
     */
    public boolean doProcessItem(String itemId, CLOB xmlMedia) {
        boolean processItem = false;
        try {
            getElementFromCLOB(xmlMedia);
            if (!rootItemElement.getChild("Stimulus").getAttributeValue("ID").equals(correctStimulus))
                processItem = true;
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return processItem;
    }

    private void getElementFromCLOB(CLOB xmlMedia) throws SQLException, JDOMException, IOException {
        rootItemElement = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Pipe(xmlMedia.binaryStreamValue(), out).run();
        rootItemElement = builder.build(
                new InputStreamReader(new ByteArrayInputStream(out.toByteArray())),"UTF-8").getRootElement();
    }


    /**
     * Constructor
     * @param correctStimulus
     */
    public StimulusCorrector(String correctStimulus, Connection connection) {
        this.correctStimulus = correctStimulus;
        this.correctTitle = correctStimulus;
        this.connection = connection;
        createWriter(".");
    }


    /**
     * write start time and substition defs to log file
     */
    public void startProcessingItems() {
        writer.println("start processing at "
                + DateFormat.getDateTimeInstance().format(new Date()));
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
                    "StimulusCorrector-" + System.currentTimeMillis() + ".log")));
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException("unable to construct print writer for logging");
        }
    }
}
