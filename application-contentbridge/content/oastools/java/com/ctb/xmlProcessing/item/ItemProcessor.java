package com.ctb.xmlProcessing.item;

import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.xmlProcessing.XMLElementProcessor;

public interface ItemProcessor extends XMLElementProcessor {
    public AbstractXMLElementReport process(Element itemElement);
}
