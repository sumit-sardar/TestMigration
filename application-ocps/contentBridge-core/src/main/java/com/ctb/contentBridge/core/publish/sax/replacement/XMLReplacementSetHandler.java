package com.ctb.contentBridge.core.publish.sax.replacement;


import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class XMLReplacementSetHandler extends DefaultHandler {

    private Map map = new HashMap();
    private ErrorHandler eh = null;
    private Locator loc = null;

    public XMLReplacementSetHandler() {
        super();

    }

    public XMLReplacementSetHandler(Map map) {
        super();
        this.map = map;
    }

    public void startElement(String namespaceURI,
            String localName,
            String qName,
            Attributes atts)
        throws SAXException {

        if (!localName.equals("entry")) {
            return;
        }
        addReplacementPair(atts);
    }

    public void addReplacementPair(Attributes atts) throws SAXException {

        String key = null;
        String value = null;

        key = atts.getValue("key");
        value = atts.getValue("value");

        if (key != null && value != null) {
            map.put(key, value);
        } else {
            throw new SAXException("<entry> must contain key and value attributes");
        }
    }

    public Map getReplacementMap() {

        return map;

    }
}
