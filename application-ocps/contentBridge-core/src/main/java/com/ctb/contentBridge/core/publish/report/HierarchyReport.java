package com.ctb.contentBridge.core.publish.report;


import java.util.*;

import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;

public class HierarchyReport extends AbstractReport {
	private static ThreadLocal _current = new ThreadLocal();
    
	public static HierarchyReport getCurrentReport() {
		return (HierarchyReport) _current.get();
	}
	public static void setCurrentReport(HierarchyReport report) {
		_current.set(report);
	}
	
    List subReports = new ArrayList();

    private Objectives objectives;
    private ItemMap itemMap;

    public HierarchyReport() {
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives objectives) {
        this.objectives = objectives;
    }

    public ItemMap getItemMap() {
        return itemMap;
    }

    public void setItemMap(ItemMap itemMap) {
        this.itemMap = itemMap;
    }

    public HierarchyReport(Objectives objectives, ItemMap itemMap) {
        this.objectives = objectives;
        this.itemMap = itemMap;
    }

    public void addSubReport(Report report) {
        subReports.add(report);
    }

    public List getSubReports() {
        return subReports;
    }
}
