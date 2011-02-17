package com.ctb.reporting;

import org.apache.log4j.Logger;

import java.io.PrintWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class CommandReportFormatter implements Formatter {
    private static Logger logger =
            Logger.getLogger(CommandReportFormatter.class);
    private CommandReport report;
    private boolean fullReport = false;


    public CommandReportFormatter(CommandReport report) {
        this(report, false);
    }

    public CommandReportFormatter(CommandReport report, boolean fullReport) {
        this.report = report;
        this.fullReport = fullReport;
    }

    public void print(PrintWriter writer, boolean isSubReport) {
        logger.info("Printing Report");
        writer.println("\nCommand '"
                + report.getCommandName()
                + "' at ["
                + report.getStartTime()
                + "] on Thread ["
                + report.getThreadId()
                + "] "
                + ((report.isSuccess()) ? "successful" : "failed"));
        if (!report.isSuccess()) {
            writer.println("ERROR: " + getErrorMessage(report));
        }
        if (report.getFileName() != null)
            writer.println("Input File [" + report.getFileName() + "]");
        Iterator subReports = report.getSubReports();
//        System.out.println("subreports " + subReports.hasNext());
        while (subReports.hasNext()) {
            FormatterFactory.create((Report) subReports.next(),
                    this.fullReport).print(writer,
                            true);
        }
    }

    public void print(boolean isSubReport){
        String fileName = report.getFileName();
        logger.info("writing to file "+ fileName);
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
            print(printWriter, true);
            printWriter.flush();
            printWriter.close();
        }
        catch (IOException e) {
            logger.error("cannot write to file", e); //TODO handle better?
        }
    }

    private String getErrorMessage(CommandReport r) {
        String s = "No specific error message";
        Exception e = r.getException();
        if (e != null) {
            if (e.getMessage().length() > 0)
                s = e.getMessage();
            else
                s = e.toString();
        }
        return s;
    }
}
