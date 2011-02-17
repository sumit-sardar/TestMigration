package com.ctb.reporting;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class ProductReportFormatter implements Formatter {

    private static Logger logger =
        Logger.getLogger(ItemSetReportFormatter.class);

    private ProductReport report;

    public ProductReportFormatter(ProductReport report) {
        this.report = report;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        if (!isSubReport) {
            logger.info("Printing Product Report");
            writer.println(
                "\nExecuted at ["
                    + report.getStartTime()
                    + "] on Thread ["
                    + report.getThreadId()
                    + "]");
        }
        if (report.isSuccess())
            writer.println("Product analyzed successfully.");
        else {
            writer.println("Product analysis failed.");
        }
        writer.println("TOTAL in DB: " + report.getTotalCount());
        writer.println("Total not active: " + report.getInactiveCount());
        writer.println("Total invisible: " + report.getInactiveCount());
        writer.println("Total CR: " + report.getNoAnswerCount());
        writer.println(
            "Total specific to state: " + report.getStateSpecificCount());
        writer.println(
            "Not yet Mapped or Imported: " + report.getPotentialDBItems());
        FormatterFactory
            .create(report.getHierarchyReportForDBItems(), false, false)
            .print(writer, true);
        FormatterFactory.create(report.getPotentialDBItemsReport()).print(
            writer,
            true);

    }

}
