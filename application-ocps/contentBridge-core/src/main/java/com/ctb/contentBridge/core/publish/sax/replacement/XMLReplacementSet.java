package com.ctb.contentBridge.core.publish.sax.replacement;


import java.io.*;
import java.util.*;

import org.apache.xerces.parsers.*;
import org.xml.sax.*;

import com.ctb.contentBridge.core.exception.SystemException;



public class XMLReplacementSet implements ReplacementSet {

    private String file = null;
    private Map map = null;

    public XMLReplacementSet(String xmlFile) throws SystemException {
        file = xmlFile;
        InputStream is = null;

        try {
            is = new FileInputStream(new File("xmlFile"));
        } catch (FileNotFoundException e) {
            throw new SystemException("XML replacement file not found: "
                    + e.getMessage());
        }
        initialize(is);
    }

    public XMLReplacementSet(InputStream is) {
        initialize(is);
    }

    public String get(String key) {
        // System. out. println("looking for:" + key);
        return (String) map.get(key);
    }

    public Map getEntryMap() {
        return map;
    }

    private synchronized void initialize(InputStream is) {

        try {

            XMLReader myReader = new SAXParser();
            XMLReplacementSetHandler rsch = new XMLReplacementSetHandler();

            myReader.setContentHandler(rsch);
            myReader.parse(new InputSource(is));
            map = rsch.getReplacementMap();

        } catch (SAXException se) {
            throw new IllegalArgumentException("XML Stream could not be parsed: "
                    + se.getMessage());
        } catch (IOException ioe) {
            throw new MissingResourceException("XML Stream could not be parsed: "
                    + ioe.getMessage(),
                    null,
                    null);
        }

    }

}
