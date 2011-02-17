/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.xmlProcessing.itemset;

import java.util.List;

import org.jdom.Element;

import com.ctb.reporting.AbstractXMLElementReport;
import com.ctb.reporting.ReportFactory;
import com.ctb.xmlProcessing.AbstractXMLElementProcessor;

public class ItemSetProcessor extends AbstractXMLElementProcessor {

    protected AbstractXMLElementReport validateElement(Element element) {
        return ReportFactory.createItemSetReport();
    }

    protected AbstractXMLElementReport processCurrentElement(
        Element element,
        AbstractXMLElementReport report,
        List subReports) {

        report.addSubReport(subReports);
        return report;
    }
}
