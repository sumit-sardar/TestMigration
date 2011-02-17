/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.xmlProcessing;

import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;

public interface XMLElementProcessor {

    AbstractXMLElementReport process(Element element);
}
