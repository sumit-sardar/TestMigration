/*
 * Created on Dec 3, 2003
 *
 */
package com.ctb.contentBridge.core.publish.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemImportAndMapReport extends AbstractItemReport {

    private static ThreadLocal _current = new ThreadLocal();

    public static ItemImportAndMapReport getCurrentReport() {
        return (ItemImportAndMapReport) _current.get();
    }
    public static void setCurrentReport(ItemImportAndMapReport report) {
        _current.set(report);
    }

	private List itemReports = new ArrayList();

	public Iterator getItemProcessorReports() {
		return itemReports.iterator();
	}

	public void addItemProcessorReport(AbstractItemReport report) {
		if (!report.isSuccess())
			setSuccess(false);
		itemReports.add(report);
        ((ItemProcessorReport)report).setShowHierarchy(true);
		this.setID(report.getID());
	}

	public int getNumItems() {
		return this.itemReports.size();
	}

}
