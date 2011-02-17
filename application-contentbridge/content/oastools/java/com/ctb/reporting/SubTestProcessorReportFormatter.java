package com.ctb.reporting;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.log4j.*;

public class SubTestProcessorReportFormatter implements Formatter {
    private static Logger logger =
        Logger.getLogger(SubTestProcessorReportFormatter.class);

    private SubTestProcessorReport report;

    public SubTestProcessorReportFormatter(SubTestProcessorReport report) {
        this.report = report;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        if (!isSubReport) {
            logger.info("Printing Schedulable Unit Processor Report");
            writer.println(
                "\nSchedulable Unit processing at ["
                    + report.getStartTime()
                    + "] on Thread ["
                    + report.getThreadId()
                    + "]");
        }
        if (report.isSuccess())
            writer.println("Schedulable Unit processed successfully.");
        else {
            writer.print("ERROR: ");
            Exception e = report.getException();
            if (e == null)
                writer.println("No error message");
            else
                writer.println(e.getMessage());
        }

        if (report.getId() != null)
            writer.println("Schedulable Unit ID [" + report.getId() + "]");

        Iterator subReports = report.getSubReports();
        while (subReports.hasNext()) {
            Report subReport = (Report) subReports.next();
            FormatterFactory.create(subReport).print(writer, true);
        }
    }
}
