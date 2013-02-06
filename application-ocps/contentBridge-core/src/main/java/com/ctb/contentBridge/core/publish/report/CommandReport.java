package com.ctb.contentBridge.core.publish.report;

import java.util.*;

public class CommandReport extends AbstractReport {
    private static ThreadLocal _current = new ThreadLocal();
    public static CommandReport getCurrentReport() {
        return (CommandReport) _current.get();
    }
    public static void setCurrentReport(CommandReport report) {
        _current.set(report);
    }

    private List subReports = new ArrayList();
    private final String fileName;
    private final String command;

    public CommandReport(String command, String fileName) {
        super();
        this.fileName = fileName;
        this.command = command;
    }

    public String getFileName() {
        return fileName;
    }
    
    public String getCommandName() {
    	return command;
    }

    public Iterator getSubReports() {
        return subReports.iterator();
    }

    public void addSubReport(Report subReport) {
        this.subReports.add(subReport);
    }

}
