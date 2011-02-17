/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.reporting;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ctb.xmlProcessing.XMLConstants;

public class AssessmentProcessorReportFormatter
    extends AbstractXMLElementReportFormatter {

    public AssessmentProcessorReportFormatter(AssessmentProcessorReport report) {
        super(report, XMLConstants.ELEMENT_NAME_ASSESSMENT);
    }

    protected void printSubReports(PrintWriter writer) {
        writer.println("Assessment ID [" + ((AssessmentProcessorReport)report).getId() + "]");
        if (((AssessmentProcessorReport)report).getProductId() != null)
            writer.println("Product ID [" + ((AssessmentProcessorReport)report).getProductId() + "]");
        writer.println(StringUtils.repeat("-", 100));
        for (Iterator iter = report.getSubReports(); iter.hasNext();) {
            FormatterFactory.create((Report) iter.next()).print(writer, true);
            writer.println(StringUtils.repeat("-", 100));
        }
    }

}
