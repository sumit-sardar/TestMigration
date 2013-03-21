package com.ctb.contentBridge.core.publish.report;

public class ProductReport extends AbstractReport {
    private static ThreadLocal _current = new ThreadLocal();

    public static ProductReport getCurrentReport() {
        return (ProductReport) _current.get();
    }
    
    public static void setCurrentReport(ProductReport report) {
        _current.set(report);
    }
    
	private int inactiveCount;
	private int noAnswerCount;
	private int stateSpecificCount;
	private int invisibleCount;
	private int totalCount;
	private long potentialDBItems;

	private HierarchyReport hierarchyReportForDBItems;
	private Report potentialDBItemsReport;
    
    
    public HierarchyReport getHierarchyReportForDBItems() {
        return hierarchyReportForDBItems;
    }

    public int getInactiveCount() {
        return inactiveCount;
    }

    public int getInvisibleCount() {
        return invisibleCount;
    }

    public int getNoAnswerCount() {
        return noAnswerCount;
    }

    public Report getPotentialDBItemsReport() {
        return potentialDBItemsReport;
    }

    public int getStateSpecificCount() {
        return stateSpecificCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

	public long getPotentialDBItems() {
		return potentialDBItems;
	}

    public void setHierarchyReportForDBItems(HierarchyReport report) {
        hierarchyReportForDBItems = report;
    }

    public void setInactiveCount(int inactiveCount) {
        this.inactiveCount = inactiveCount;
    }

    public void setInvisibleCount(int invisibleCount) {
        this.invisibleCount = invisibleCount;
    }

    public void setNoAnswerCount(int noAnswerCount) {
        this.noAnswerCount = noAnswerCount;
    }

    public void setPotentialDBItemsReport(Report report) {
        potentialDBItemsReport = report;
    }

    public void setStateSpecificCount(int stateSpecificCount) {
        this.stateSpecificCount = stateSpecificCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPotentialDBItems(long numPotentialDBItems) {
        this.potentialDBItems = numPotentialDBItems;
    }

}
