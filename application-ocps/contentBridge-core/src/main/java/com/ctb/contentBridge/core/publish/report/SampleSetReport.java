package com.ctb.contentBridge.core.publish.report;


/**
 * @author wmli
 */
public class SampleSetReport extends AbstractXMLElementReport {
    private static ThreadLocal _current = new ThreadLocal();
    public static SampleSetReport getCurrentReport() {
        return (SampleSetReport) _current.get();
    }
    public static void setCurrentReport(SampleSetReport report) {
        _current.set(report);
    }
}
