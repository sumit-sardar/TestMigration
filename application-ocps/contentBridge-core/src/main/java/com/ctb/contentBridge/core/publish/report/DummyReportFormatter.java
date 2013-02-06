package com.ctb.contentBridge.core.publish.report;

import java.io.PrintWriter;

public class DummyReportFormatter implements Formatter {

    private final DummyReport report;

    public DummyReportFormatter(DummyReport report) {
        this.report = report;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        writer.println(report.getMessage());
    }

}
