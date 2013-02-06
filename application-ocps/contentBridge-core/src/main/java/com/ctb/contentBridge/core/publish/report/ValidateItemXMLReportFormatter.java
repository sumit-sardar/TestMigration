package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;
import java.util.List;

/**
 * @author Sreenivas  Ananthakrishna sreeni@thoughtworks.com
 */
public class ValidateItemXMLReportFormatter implements Formatter {
    private ValidateItemXMLReport report;
    private static final String TAB = "                ";
    private static final String header = "Item Id" + TAB + "Validation Error \n";
    private static String LINE_BREAK = "\n";

    public ValidateItemXMLReportFormatter(ValidateItemXMLReport report) {
        this.report = report;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        writeExceptions(writer);
        writer.write(header);
        writeValidationFailures(writer);
    }

    private void writeExceptions(PrintWriter writer) {
        List exceptions = report.getExceptions();
        writer.write(LINE_BREAK);
        writer.write("General exceptions" + LINE_BREAK);

        if (exceptions.isEmpty()) {
            writer.write("no exceptions" + LINE_BREAK + LINE_BREAK);
        }
        for (int i = 0; i < exceptions.size(); i++) {
            Exception o = (Exception) exceptions.get(i);
            writer.write(o.getMessage() + LINE_BREAK);
        }
    }

    private void writeValidationFailures(PrintWriter writer) {
        List validationFailures = report.getValidationFailures();
        for (int i = 0; i < validationFailures.size(); i++) {
            ValidateItemXMLReport.ItemValidationFailure itemValidationFailure = (ValidateItemXMLReport.ItemValidationFailure) validationFailures.get(i);
            writer.write(itemValidationFailure.getItemId() + TAB + itemValidationFailure.getMessage() + LINE_BREAK);
        }
    }
}
