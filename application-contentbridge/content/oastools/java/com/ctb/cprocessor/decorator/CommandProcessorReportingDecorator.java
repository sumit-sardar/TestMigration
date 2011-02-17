package com.ctb.cprocessor.decorator;

import org.apache.log4j.Logger;

import com.ctb.cprocessor.CommandProcessor;
import com.ctb.reporting.CommandReport;
import com.ctb.reporting.Report;

public class CommandProcessorReportingDecorator implements CommandProcessor {
    private static Logger logger =
        Logger.getLogger(CommandProcessorReportingDecorator.class);

    private final String fileName;
    private final String command;
    private final CommandProcessor processor;

    public CommandProcessorReportingDecorator(
        String command,
        String fileName,
        CommandProcessor processor) {
        this.command = command;
        this.fileName = fileName;
        this.processor = processor;
    }

    public Report process() {
        CommandReport report = new CommandReport(this.command, fileName);
        CommandReport.setCurrentReport(report);
        logger.info("Processing File: " + fileName);
        try {
            Report subReport = this.processor.process();
            report.addSubReport(subReport);
            report.setSuccess(subReport.isSuccess());
            if (!subReport.isSuccess())
                report.setException(new Exception(command + " processing failed"));
        } catch (Exception e) {
            logger.error(command + " command failed", e);
            report.setException(e);
            report.setSuccess(false);
        }
        return report;
    }

}
