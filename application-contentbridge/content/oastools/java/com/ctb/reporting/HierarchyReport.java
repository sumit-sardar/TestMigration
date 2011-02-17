package com.ctb.reporting;

import com.ctb.mapping.Objectives;
import com.ctb.mapping.ItemMap;

import java.util.*;

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
