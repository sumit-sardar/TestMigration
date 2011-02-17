/*
 * Created on Nov 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ctb.reporting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wen-jin_chang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeliverableUnitReport extends AbstractXMLElementReport
{
    private static ThreadLocal _current = new ThreadLocal();
    private List itemReports = new ArrayList();
    private List itemSetIds = new ArrayList();
    private String id;
    private String subTestLevel;
    private String subTestForm;
    private String subTestName; 
    
    public static DeliverableUnitReport getCurrentReport() {
        return (DeliverableUnitReport) _current.get();
    }
    public static void setCurrentReport(DeliverableUnitReport report) {
        _current.set(report);
    }
    
    public DeliverableUnitReport() 
    {
        super();
    }

    public Iterator getItemProcessorReports() {
		return itemReports.iterator();
	}

	public void addItemProcessorReport(AbstractItemReport report) 
	{
		if (!report.isSuccess())
			setSuccess(false);
		itemReports.add(report);
        ((ItemProcessorReport)report).setShowHierarchy(true);
	}
	
	public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
	
	/**
     * @return Returns the subTestLevel.
     */
    public String getSubTestLevel() {
        return subTestLevel;
    }
    /**
     * @param subTestLevel The subTestLevel to set.
     */
    public void setSubTestLevel(String subTestLevel) {
        this.subTestLevel = subTestLevel;
    }
    
    /**
     * @return Returns the subTestForm.
     */
    public String getSubTestForm() {
        return subTestForm;
    }
    /**
     * @param subTestLevel The subTestLevel to set.
     */
    public void setSubTestForm(String subTestForm) {
        this.subTestForm = subTestForm;
    }
    /**
     * @return Returns the subtestName.
     */
    public String getSubTestName() {
        return subTestName;
    }
    /**
     * @param subtestName The subtestName to set.
     */
    public void setSubTestName(String subTestName) {
        this.subTestName = subTestName;
    }

	public int getNumItems() {
		return this.itemReports.size();
	}
	
	public Long getItemSetId() {
        return (Long) itemSetIds.get(0);
    }

    public List getItemSetIds() {
        return itemSetIds;
    }

    public void setItemSetId(Long itemSetId) {
        this.itemSetIds.add(itemSetId);
    }

    public void setItemSetIds(List itemSetIds) {
        this.itemSetIds.addAll(itemSetIds);
    }
}
