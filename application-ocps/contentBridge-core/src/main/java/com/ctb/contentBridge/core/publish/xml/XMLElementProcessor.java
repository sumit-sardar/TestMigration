/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.contentBridge.core.publish.xml;

import org.jdom.Element;

import com.ctb.contentBridge.core.publish.report.AbstractXMLElementReport;

public interface XMLElementProcessor {

    AbstractXMLElementReport process(Element element);
}
