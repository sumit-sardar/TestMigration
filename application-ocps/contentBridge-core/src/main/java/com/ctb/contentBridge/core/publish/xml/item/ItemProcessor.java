package com.ctb.contentBridge.core.publish.xml.item;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;
import com.ctb.contentBridge.core.publish.xml.XMLElementProcessor;


public interface ItemProcessor extends XMLElementProcessor {
    public AbstractXMLElementReport process(Element itemElement);
}