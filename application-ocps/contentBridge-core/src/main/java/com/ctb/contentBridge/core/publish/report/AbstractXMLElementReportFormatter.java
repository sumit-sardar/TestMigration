/*
 * Created on Jan 20, 2004
 *
 */
package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.log4j.Logger;

public abstract class AbstractXMLElementReportFormatter implements Formatter {

    private static Logger logger =
        Logger.getLogger(AbstractXMLElementReportFormatter.class);

    protected final AbstractXMLElementReport report;
    private final String elementName;

    public AbstractXMLElementReportFormatter(
        AbstractXMLElementReport report,
        String elementName) {
        this.report = report;
        this.elementName = elementName;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        if (!isSubReport) {
            String className = this.getClass().getName();
            logger.info(
                "Printing "
                    + className.substring(
                        className.lastIndexOf(".") + 1,
                        className.length()));
            writer.println(
                "\nProcessed "
                    + elementName
                    + " at ["
                    + report.getStartTime()
                    + "] on Thread ["
                    + report.getThreadId()
                    + "]");
        }
        if (report.isSuccess())
            writer.println(elementName + " processed successfully.");
        else {
            writer.print(elementName + " processing failed. ERROR: ");
            Exception e = report.getException();
            if (e == null)
                writer.println("No error message");
            else
                writer.println(e.getMessage());
        }
        printSubReports(writer);

    }

    protected void printSubReports(PrintWriter writer) {

        for (Iterator iter = report.getSubReports(); iter.hasNext();) {
            FormatterFactory.create((Report) iter.next()).print(writer, true);
        }
    }

}
