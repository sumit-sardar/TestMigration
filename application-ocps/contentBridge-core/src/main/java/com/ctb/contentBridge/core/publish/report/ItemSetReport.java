package com.ctb.contentBridge.core.publish.report;


public class ItemSetReport extends AbstractXMLElementReport {
    private static ThreadLocal _current = new ThreadLocal();
    public static ItemSetReport getCurrentReport() {
        return (ItemSetReport) _current.get();
    }
    public static void setCurrentReport(ItemSetReport report) {
        _current.set(report);
    }

    public int getNumItems() {
        return this.subReports.size();
    }
}
