package com.ctb.contentBridge.core.publish.sax.element;


import org.xml.sax.*;


public interface FieldOperation {

    public String process(String input) throws SAXException;

}
