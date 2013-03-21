/*
 * Created on Dec 3, 2003
 *
 */
package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ItemImportAndMapReportFormatter implements Formatter {

    private static Logger logger =
        Logger.getLogger(ItemImportAndMapReportFormatter.class);

    private ItemImportAndMapReport report;

    public ItemImportAndMapReportFormatter(ItemImportAndMapReport report) {
        this.report = report;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        if (!isSubReport) {
            logger.info("Printing Import-and-Map Report");
            writer.println(
                "\nItem imported and mapped at ["
                    + report.getStartTime()
                    + "] on Thread ["
                    + report.getThreadId()
                    + "]");
            if (report.isSuccess())
                writer.println("All Item(s) processed successfully.");
            else {
                writer.println("Some Items failed.");
            }
        }

        writer.println(
            StringUtils.leftPad("Item [" + report.getID() + "]", 17) + ":");
        Iterator itemReports = report.getItemProcessorReports();
        while (itemReports.hasNext()) {
            FormatterFactory.create((Report) itemReports.next(), false).print(
                writer,
                true);
        }
    }

}
